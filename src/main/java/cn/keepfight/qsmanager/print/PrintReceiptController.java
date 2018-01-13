package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.ConfigUtil;
import cn.keepfight.utils.FXReflectUtils;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 收据单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintReceiptController extends PrintTemplate<OrderModelFull> implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private TextField serial;
    @FXML
    private TextField cust;
    @FXML
    private TextField addr;
    @FXML
    private TextField phone;
    @FXML
    private TextField maker;
    @FXML
    private TextField mdate;

    @FXML
    private TableView<Item> table;
    @FXML
    private TableColumn<Item, Number> id;
    @FXML
    private TableColumn<Item, String> name;
    @FXML
    private TableColumn<Item, String> detail;
    @FXML
    private TableColumn<Item, String> unit;
    @FXML
    private TableColumn<Item, BigDecimal> num;
    @FXML
    private TableColumn<Item, BigDecimal> price;
    @FXML
    private TableColumn<Item, Integer> yuan_u4;
    @FXML
    private TableColumn<Item, Integer> yuan_u3;
    @FXML
    private TableColumn<Item, Integer> yuan_u2;
    @FXML
    private TableColumn<Item, Integer> yuan_u1;
    @FXML
    private TableColumn<Item, Integer> yuan;
    @FXML
    private TableColumn<Item, Integer> yuan_d1;
    @FXML
    private TableColumn<Item, Integer> yuan_d2;
    @FXML
    private TextField all_total;

    private static final int SIZE_PER_PAGE = 8;
    private Map<String, ProductModel> productList;
    private OrderModelFull datas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(51).divide(SIZE_PER_PAGE));

        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1));

        table.setEditable(true);
        table.getColumns().forEach(x -> x.setSortable(false));
        FXWidgetUtil.cellStr(detail, unit, name);
        FXWidgetUtil.cellDecimal(num);
        FXWidgetUtil.cellMoney(price);
        FXWidgetUtil.cellInteger(yuan_u4, yuan_u3, yuan_u2, yuan_u1, yuan, yuan_d1, yuan_d2);

        name.setCellValueFactory(x -> x.getValue().nameItem);
        FXWidgetUtil.connect(detail, Item::detailProperty);
        FXWidgetUtil.connect(unit, Item::unitProperty);
        num.setCellValueFactory(x -> x.getValue().numProperty());
        price.setCellValueFactory(x -> x.getValue().priceProperty());
        yuan_u4.setCellValueFactory(x -> x.getValue().yuan_u4.asObject());
        yuan_u3.setCellValueFactory(x -> x.getValue().yuan_u3.asObject());
        yuan_u2.setCellValueFactory(x -> x.getValue().yuan_u2.asObject());
        yuan_u1.setCellValueFactory(x -> x.getValue().yuan_u1.asObject());
        yuan.setCellValueFactory(x -> x.getValue().yuan.asObject());
        yuan_d1.setCellValueFactory(x -> x.getValue().yuan_d1.asObject());
        yuan_d2.setCellValueFactory(x -> x.getValue().yuan_d2.asObject());

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

            // 加载默认记忆选项并添加默认下拉
            FXWidgetUtil.defaultList(
                    new Pair<>(addr, "custom.info.addr."+c.getSerial())
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 填写其他信息
        maker.setText(ConfigUtil.load("fxapp.properties").getProperty("print.maker"));
        mdate.setText(FXUtils.stampToDate(System.currentTimeMillis()));

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
                    new Pair<>("custom.info.addr." + c.getSerial(), addr.getText())
            );
        }catch (Exception e){
            // to do nothing
        }
    }

    @Override
    public ReadOnlyBooleanProperty autoComputable() {
        return new SimpleBooleanProperty(true);
    }

    @Override
    public void autoCalculate() {
        Optional<BigDecimal> t = table.getItems().stream()
                .peek(x -> {
                    BigDecimal d = x.getTakeTotal();
                    //@TODO 搞完回来弄
                    x.yuan_u4.set(FXUtils.getNumAt(d, 5, true));
                    x.yuan_u3.set(FXUtils.getNumAt(d, 4, true));
                    x.yuan_u2.set(FXUtils.getNumAt(d, 3, true));
                    x.yuan_u1.set(FXUtils.getNumAt(d, 2, true));
                    x.yuan.set(FXUtils.getNumAt(d, 1, true));
                    x.yuan_d1.set(FXUtils.getNumAt(d, 1, false));
                    x.yuan_d2.set(FXUtils.getNumAt(d, 2, false));
                })
                .map(OrderItemModel::getTakeTotal)
                .reduce(BigDecimal::add);
        String text = "0";
        if (t.isPresent()) {
            text = FXUtils.deciToMoney(t.get());
        }
        all_total.setText(text);
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
                    "note", "detail", "id", "name", "num", "pack", "price", "serial", "unit");
            nameItem.set(getSerial() + "-" + getName());
        }

        IntegerProperty yuan_u4 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_u3 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_u2 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_u1 = new SimpleIntegerProperty(0);
        IntegerProperty yuan = new SimpleIntegerProperty(0);
        IntegerProperty yuan_d1 = new SimpleIntegerProperty(0);
        IntegerProperty yuan_d2 = new SimpleIntegerProperty(0);
        StringProperty nameItem = new SimpleStringProperty();
    }
}
