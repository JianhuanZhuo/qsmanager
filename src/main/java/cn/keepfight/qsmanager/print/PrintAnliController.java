package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.QSUtil;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 安利送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintAnliController extends PrintTemplate<OrderModelFull> implements Initializable {
    @FXML
    private TextField serial;
    @FXML
    private TextField mdate;
    @FXML
    private TextField anli_serial;
    @FXML
    private VBox root;
    @FXML
    private TextField item_detail1;
    @FXML
    private TextField item_detail2;
    @FXML
    private TextField item_detail3;
    @FXML
    private TextField item_serial1;
    @FXML
    private ChoiceBox<String> item_name1;
    @FXML
    private TextField item_pack1;
    @FXML
    private TextField item_unit1;
    @FXML
    private TextField item_num1;
    @FXML
    private TextField item_serial2;
    @FXML
    private ChoiceBox<String> item_name2;
    @FXML
    private TextField item_pack2;
    @FXML
    private TextField item_unit2;
    @FXML
    private TextField item_num2;
    @FXML
    private TextField item_serial3;
    @FXML
    private ChoiceBox<String> item_name3;
    @FXML
    private TextField item_pack3;
    @FXML
    private TextField item_unit3;
    @FXML
    private TextField item_num3;

    private List<BigDecimal> prices = Arrays.asList(new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));

    @FXML
    private Label total_all;

    private OrderModelFull datas;
    private ObservableList<OrderItemModel> deliveryItemModels = FXCollections.observableList(new ArrayList<>());

    private List<TextField> serials;
    private List<ChoiceBox<String>> names;
    private List<TextField> packs;
    private List<TextField> units;
    private List<TextField> nums;
    private List<TextField> details;
    private List<ObjectProperty<QSUtil.AnliPack>> anliPacks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serials = Arrays.asList(item_serial1, item_serial2, item_serial3);
        names = Arrays.asList(item_name1, item_name2, item_name3);
        packs = Arrays.asList(item_pack1, item_pack2, item_pack3);
        units = Arrays.asList(item_unit1, item_unit2, item_unit3);
        nums = Arrays.asList(item_num1, item_num2, item_num3);
        details = Arrays.asList(item_detail1, item_detail2, item_detail3);
        anliPacks = Arrays.asList(QSUtil.setAnliPack(item_detail1), QSUtil.setAnliPack(item_detail2), QSUtil.setAnliPack(item_detail3));

        names.forEach(x -> x.setItems(FXCollections.observableArrayList("不锈钢软丝刷", null)));
        for (int i = 0; i < names.size(); i++) {
            final int j = i;
            names.get(i).setOnAction(event -> {
                if (names.get(j).getSelectionModel().getSelectedItem() == null) {
                    serials.get(j).setText("");
                    packs.get(j).setText("");
                    units.get(j).setText("");
                    nums.get(j).setText("");
                    details.get(j).setText("");
                } else if (names.get(j).getSelectionModel().getSelectedItem().equals("不锈钢软丝刷")) {
                    serials.get(j).setText("112563CH1*OP10");
                    packs.get(j).setText("4个/盒");
                    units.get(j).setText("盒");
                    nums.get(j).setText("1960盒/板");
                    details.get(j).setText("70盒/箱×28箱/板×13板");
                }
            });
        }

        // 加载默认记忆选项并添加默认下拉
        FXWidgetUtil.defaultList(
                new Pair<>(anli_serial, "prefer.delivery.anli.serial"),
                new Pair<>(item_detail1, "prefer.delivery.anli.detail"),
                new Pair<>(item_detail2, "prefer.delivery.anli.detail"),
                new Pair<>(item_detail3, "prefer.delivery.anli.detail"),
                new Pair<>(item_serial1, "prefer.delivery.anli.item"),
                new Pair<>(item_serial2, "prefer.delivery.anli.item"),
                new Pair<>(item_serial3, "prefer.delivery.anli.item"),
                new Pair<>(item_num1, "prefer.delivery.anli.num"),
                new Pair<>(item_num2, "prefer.delivery.anli.num"),
                new Pair<>(item_num3, "prefer.delivery.anli.num")
        );
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void fill(OrderModelFull datas) {
        clear();
        this.datas = datas;
        deliveryItemModels.setAll(datas.getOrderItemModels());

        serial.setText(datas.getSerial());
        mdate.setText(FXUtils.stampToDate(datas.getOrderdate()));

        // 加载默认记忆选项并添加默认下拉
        FXWidgetUtil.defaultList(
                new Pair<>(anli_serial, "prefer.delivery.anli.serial"),
                new Pair<>(item_detail1, "prefer.delivery.anli.detail"),
                new Pair<>(item_detail2, "prefer.delivery.anli.detail"),
                new Pair<>(item_detail3, "prefer.delivery.anli.detail"),
                new Pair<>(item_serial1, "prefer.delivery.anli.item"),
                new Pair<>(item_serial2, "prefer.delivery.anli.item"),
                new Pair<>(item_serial3, "prefer.delivery.anli.item"),
                new Pair<>(item_num1, "prefer.delivery.anli.num"),
                new Pair<>(item_num2, "prefer.delivery.anli.num"),
                new Pair<>(item_num3, "prefer.delivery.anli.num")
        );

        List<OrderItemModel> os = deliveryItemModels
                .stream()
                .filter(x -> x.getName().contains("不锈钢软丝刷"))
                .limit(3)
                .collect(Collectors.toList());
        for (int i = 0; i < os.size(); i++) {
            OrderItemModel x = os.get(i);
            names.get(i).getSelectionModel().select(x.getName());
            final int j = i;
            Platform.runLater(() -> {
                serials.get(j).setText(x.getSerial());
                units.get(j).setText(x.getUnit());
                nums.get(j).setText("1960盒/板");
                packs.get(j).setText("4个/盒");
                details.get(j).setText(x.getDetail());
            });
            prices.set(i, x.getPrice());
        }
    }

    @Override
    public void cancel() {
    }

    @Override
    public void printBefore() {
        if (!datas.isDeli()) {
            System.out.println("!datas.isDeli()");
            try {
                QSApp.service.getOrderService().deliOrder(datas.getId());
                datas.setDeli(true);
            } catch (Exception e) {
                // @TODO 这里的话，nothing to do
                e.printStackTrace();
            }
        }
    }

    @Override
    public void printAfter() {
        // 填充
        List<OrderItemModel> is = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            if (names.get(i).getSelectionModel().getSelectedItem() != null) {
                OrderItemModel model = new OrderItemModel();
                model.setName(names.get(i).getValue());
                model.setUnit(units.get(i).getText());
                model.setDetail(details.get(i).getText());
                model.setPrice(new BigDecimal(0));
                model.setNum(new BigDecimal(anliPacks.get(i).get().count()));
                model.setPack(1);
                model.setPackDefault(1);
                model.setSerial(serials.get(i).getText());
                model.setPrice(prices.get(i));
                is.add(model);
            }
        }
        datas.setOrderdate(
                Date.valueOf(FXUtils.getDate("yyyy-M-d", mdate.getText())).getTime()
        );
        datas.setOrderItemModels(is);
        try {
            // 修改为 false
            QSApp.service.getOrderService().update(datas);
        } catch (Exception e) {
            e.printStackTrace();
            // @TODO 如果这里挂了该怎么破？
        }
        // 记忆序列号
        FXWidgetUtil.addDefaultList(
                new Pair<>("prefer.delivery.anli.serial", anli_serial.getText()),
                new Pair<>("prefer.delivery.anli.detail", item_detail1.getText()),
                new Pair<>("prefer.delivery.anli.detail", item_detail2.getText()),
                new Pair<>("prefer.delivery.anli.detail", item_detail3.getText()),
                new Pair<>("prefer.delivery.anli.item", item_serial1.getText()),
                new Pair<>("prefer.delivery.anli.item", item_serial2.getText()),
                new Pair<>("prefer.delivery.anli.item", item_serial3.getText()),
                new Pair<>("prefer.delivery.anli.num", item_num1.getText()),
                new Pair<>("prefer.delivery.anli.num", item_num2.getText()),
                new Pair<>("prefer.delivery.anli.num", item_num3.getText())
        );
    }

    @Override
    public ReadOnlyBooleanProperty autoComputable() {
        return new SimpleBooleanProperty(true);
    }

    @Override
    public void autoCalculate() {
        int total = 0;
        for (int i = 0; i < 3; i++) {
            try {
                total += anliPacks.get(i).get().count();
            } catch (Exception e) {
                // ignore
            }
        }
        total_all.setText("" + total);
    }

    private void clear() {
        for (int i = 0; i < 3; i++) {
            names.get(i).getSelectionModel().select(null);
            serials.get(i).setText("");
            packs.get(i).setText("");
            units.get(i).setText("");
            nums.get(i).setText("");
            details.get(i).setText("");
        }
    }
}
