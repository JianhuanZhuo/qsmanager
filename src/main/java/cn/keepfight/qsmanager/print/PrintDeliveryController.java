package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.*;
import javafx.beans.property.*;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintDeliveryController extends PrintTemplate<OrderModelFull> implements Initializable {

    public TextField serial;
    public TextField cust;
    public TextField addr;
    public TextField phone;
    public TextField contract;
    public TextField maker;
    public TextField ddate;

    public TableView<Item> table;
    public TableColumn<Item, Number> id;
    public TableColumn<Item, String> name;
    public TableColumn<Item, String> detail;
    public TableColumn<Item, String> unit;
    public TableColumn<Item, BigDecimal> num;
    public TableColumn<Item, BigDecimal> price;
    public TableColumn<Item, BigDecimal> total;
    public TableColumn<Item, String> note;
    public VBox root;
    public TextField all_total;

    private static final int SIZE_PER_PAGE = 8;
    private OrderModelFull datas;
    private Map<String, ProductModel> productList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(27).divide(SIZE_PER_PAGE));

        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

        table.setEditable(true);
        table.getColumns().forEach(x->x.setSortable(false));

        name.setCellFactory(ChoiceBoxTableCell.forTableColumn(""));
        name.setOnEditCommit(event -> {
            try {
                if (event.getNewValue()==null){
                    table.getItems().remove(event.getRowValue());
                    table.getItems().add(new Item());
                }else {
                    ProductModel p = productList.get(event.getNewValue());
                    Item item = event.getRowValue();
                    item.nameItem.set(event.getNewValue());
                    FXReflectUtils.attrAssign(p, item, "name", "serial", "detail", "pack", "price", "unit", "picurl");
                    item.setPackDefault(p.getPack());
                }
            }catch (Exception e){
                // to do nothing
            }
        });

        FXWidgetUtil.cellStr(detail, unit, note);
        FXWidgetUtil.cellDecimal(num);
        FXWidgetUtil.cellMoney(price, total);

        price.setCellValueFactory(x -> x.getValue().priceProperty());
        num.setCellValueFactory(x -> x.getValue().numProperty());
        total.setCellValueFactory(x -> x.getValue().totalItem);
        name.setCellValueFactory(x -> x.getValue().nameItem);

        FXWidgetUtil.connect(detail, OrderItemModel::detailProperty);
        FXWidgetUtil.connect(unit, OrderItemModel::unitProperty);
        FXWidgetUtil.connect(note, OrderItemModel::noteProperty);

        table.getItems().setAll(IntStream.range(0, SIZE_PER_PAGE).mapToObj(x->new Item()).collect(Collectors.toList()));
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void fill(OrderModelFull datas) {
        this.datas = datas;

        serial.setText(datas.getSerial());

        // 填充客户信息
        try {
            CustomModel c = QSApp.service.getCustomService().selectAllByID(datas.getCid());
            cust.setText(c.getNamefull());
            addr.setText(c.getAddr());
            phone.setText(c.getPhone());
            contract.setText("");

            // 加载默认记忆选项并添加默认下拉
            FXWidgetUtil.defaultList(
                    new Pair<>(addr, "custom.info.addr."+c.getSerial())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 填写其他信息
        maker.setText(ConfigUtil.load("fxapp.properties").getProperty("print.maker"));

        ddate.setText(FXUtils.stampToDate(datas.getOrderdate()));

        // 添加为表格
        List<Item> items = datas.getOrderItemModels().stream().map(Item::new).collect(Collectors.toList());
        for (int i=0; i<SIZE_PER_PAGE-datas.getOrderItemModels().size(); i++){
            items.add(new Item());
        }
        table.getItems().setAll(items);

        // 添加表格可选的菜单下拉
        try {
            List<ProductModel> plist = QSApp.service.getOrderFavorService().selectAll(datas.getCid());
            productList = plist
                    .stream()
                    .collect(Collectors.toMap(p->p.getSerial()+"-"+p.getName(), p->p));
            List<String> ss = plist.stream().map(p->p.getSerial()+"-"+p.getName()).collect(Collectors.toList());
            ss.add(null);
            name.setCellFactory(ChoiceBoxTableCell.forTableColumn(ss.toArray(new String[ss.size()])));
        } catch (Exception e) {
            //@TODO 这里如果有毛病了，那么该怎么处理？
            e.printStackTrace();
            productList = new HashMap<>();
        }


    }

    @Override
    public void cancel() {
    }

    @Override
    public ReadOnlyBooleanProperty autoComputable() {
        return new SimpleBooleanProperty(true);
    }

    @Override
    public void autoCalculate() {
        Optional<BigDecimal> t = table.getItems().stream()
                .peek(x->x.totalItem.set(x.getTakeTotal()))
                .map(OrderItemModel::getTakeTotal)
                .reduce(BigDecimal::add);
        String text = "0";
        if (t.isPresent()) {
            text = FXUtils.deciToMoney(t.get());
        }
        all_total.setText(text);
    }

    @Override
    public void printBefore() {
        // 保存信息
        try {
            ConfigUtil.alter("fxapp.properties", "print.maker", maker.getText());
            CustomModel c = QSApp.service.getCustomService().selectAllByID(datas.getCid());
            c.setNamefull(cust.getText());
            c.setPhone(phone.getText());
            c.setAddr(addr.getText());
            QSApp.service.getCustomService().update(c);

            FXWidgetUtil.addDefaultList(
                    new Pair<>("custom.info.addr."+c.getSerial(), addr.getText())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!datas.isDeli()){
            try {
                QSApp.service.getOrderService().deliOrder(datas.getId());
                datas.setDeli(true);
            } catch (Exception e) {
                // @TODO 这里的话，nothing to do
            }
        }
    }

    @Override
    public void printAfter() {
        // 填充
        List<OrderItemModel> is = table.getItems()
                .stream()
                .filter(item->item.nameItem.get()!=null && !item.nameItem.get().trim().equals(""))
                .collect(Collectors.toList());
        datas.setOrderItemModels(is);
        try {
            QSApp.service.getOrderService().update(datas);
        } catch (Exception e) {
            e.printStackTrace();
            // @TODO 如果这里挂了该怎么破？
        }
    }

    @Override
    public void reloadFavorList() {
        // 添加表格可选的菜单下拉
        try {
            List<ProductModel> plist = QSApp.service.getOrderFavorService().selectAll(datas.getCid());
            productList = plist
                    .stream()
                    .collect(Collectors.toMap(p->p.getSerial()+"-"+p.getName(), p->p));
            List<String> ss = plist.stream().map(p->p.getSerial()+"-"+p.getName()).collect(Collectors.toList());
            ss.add(null);
            name.setCellFactory(ChoiceBoxTableCell.forTableColumn(ss.toArray(new String[ss.size()])));
        } catch (Exception e) {
            //@TODO 这里如果有毛病了，那么该怎么处理？
            e.printStackTrace();
            productList = new HashMap<>();
        }
    }

    private class Item extends OrderItemModel {
        Item(){}
        Item(OrderItemModel m) {
            FXReflectUtils.attrAssign(m, this,
                    "note", "detail", "oid", "id", "name", "num", "pack", "price", "serial", "unit");
            nameItem.set(getSerial()+"-"+getName());
        }
        ObjectProperty<BigDecimal> totalItem = new SimpleObjectProperty<>(new BigDecimal(0));
        private StringProperty nameItem = new SimpleStringProperty();
    }
}
