package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 新增原料界面控制器
 * Created by tom on 2017/6/7.
 */
public class OrderItemAddController implements DialogContent<OrderItemModel> {

    @FXML
    private VBox root;

    @FXML
    private ChoiceBox<ProductModel> serial_c;

    @FXML
    private TextField tab_serial;
    @FXML
    private TextField tab_name;
    @FXML
    private TextField tab_detail;
    @FXML
    private TextField tab_price;
    @FXML
    private ChoiceBox<Long> tab_pack;
    @FXML
    private TextField tab_packNum;
    @FXML
    private TextField tab_num;
    @FXML
    private Label tab_unit;
    @FXML
    private TextField tab_total;
    @FXML
    private TextField tab_rate;
    @FXML
    private TextField tab_ratetotal;
    @FXML
    private TextField tab_totallWithRate;
    @FXML
    private TextField tab_rebate;
    @FXML
    private TextField tab_allrebate;
    @FXML
    private TextField tab_delifee;
    @FXML
    private TextField tab_actPay;

    private boolean serialChangeEnable = true;

    private long cid;

    @FXML
    public void initialize() {
        // 设置切换恢复默认值
        serial_c.getSelectionModel().selectedIndexProperty().addListener((x, oldOne, newOne) -> {
            if (newOne.intValue() < 0 || !serialChangeEnable) {
                return;
            }

            ProductModel model = serial_c.getItems().get(newOne.intValue());
            if (model == null) {
                tab_serial.setText("");
                tab_name.setText("");
                tab_detail.setText("");
                tab_price.setText("");
                tab_unit.setText("箱");
            } else {
                tab_serial.setText(model.getSerial());
                tab_name.setText(model.getName());
                tab_detail.setText(model.getDetail());
                tab_price.setText("" + model.getPrice());
                tab_unit.setText(model.getUnit());
                tab_pack.getItems().setAll(model.getPack(), 1L, null);
                tab_pack.getSelectionModel().selectFirst();
            }
        });

        // 设置转换器
        serial_c.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "选择模板"));
        tab_pack.setConverter(FXUtils.converter(x -> x.equals(1L) ? "散装" : "整装(" + x + ")", "自定义"));
        tab_packNum.disableProperty().bind(tab_pack.getSelectionModel().selectedItemProperty().isNotNull());
        tab_pack.getSelectionModel().selectedItemProperty().addListener((x, o, n) -> {
            if (n != null) tab_packNum.setText("" + n);
        });

        FXWidgetUtil.simpleTriOper(tab_total,
                BigDecimal::multiply,
                BigDecimal::multiply,
                tab_price, tab_num, tab_packNum);
        FXWidgetUtil.simpleBiMultiply(tab_ratetotal, tab_rate, tab_total);
        FXWidgetUtil.simpleBiAdd(tab_totallWithRate, tab_ratetotal, tab_total);
        FXWidgetUtil.simpleBiMultiply(tab_allrebate, tab_rebate, tab_num);
        FXWidgetUtil.simpleTriOper(tab_actPay,
                BigDecimal::subtract,
                BigDecimal::add,
                tab_totallWithRate, tab_allrebate, tab_delifee);

        FXUtils.limitLength(tab_serial, 30);
        FXUtils.limitLength(tab_name, 30);
        FXUtils.limitLength(tab_detail, 30);
        FXUtils.limitNum(tab_packNum, 9, 4, true);
        FXUtils.limitNum(tab_num, 9, 4, true);
        FXUtils.limitNum(tab_rate, 4, 4, true);
        FXUtils.limitNum(tab_rebate, 4, 4, true);
        FXUtils.limitNum(tab_delifee, 9, 4, true);

    }

    @Override
    public void init() {
        tab_pack.getSelectionModel().clearSelection();
        tab_serial.setText("");
        tab_name.setText("");
        tab_detail.setText("");
        tab_price.setText("0");
        tab_packNum.setText("1");
        tab_num.setText("0");
        tab_rate.setText("0");
        tab_rebate.setText("0");
        tab_delifee.setText("0");

        // 加载默认记忆选项并添加默认下拉
        FXWidgetUtil.defaultList(
                new Pair<>(tab_rate, "prefer.addorder.rate"),
                new Pair<>(tab_rebate, "prefer.addorder.rebate"),
                new Pair<>(tab_delifee, "prefer.addorder.delifee")
        );

        // 加载产品列表
        loadProducts();
    }

    @Override
    public void fill(OrderItemModel model) {
        tab_serial.setText(model.getSerial());
        tab_name.setText(model.getName());
        tab_detail.setText(model.getDetail());
        tab_price.setText("" + model.getPrice());
        tab_unit.setText(model.getUnit());
        tab_pack.getItems().setAll(new HashSet<>(Arrays.asList(model.getPackDefault(), 1L, null)));
        tab_pack.getSelectionModel().selectFirst();
        tab_packNum.setText(""+model.getPack());
        tab_num.setText(""+model.getNum());
        tab_rate.setText("" + model.getRate());
        tab_rebate.setText("" + model.getRebate());
        tab_delifee.setText("" + model.getDelifee());
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(tab_serial.textProperty().isNotEmpty()
                .and(tab_name.textProperty().isNotEmpty())
                .and(tab_price.textProperty().isNotEmpty())
                .and(tab_packNum.textProperty().isNotEmpty())
                .and(tab_num.textProperty().isNotEmpty())
                .and(tab_total.textProperty().isNotEmpty())
                .and(tab_rate.textProperty().isNotEmpty())
                .and(tab_ratetotal.textProperty().isNotEmpty())
                .and(tab_totallWithRate.textProperty().isNotEmpty())
                .and(tab_rebate.textProperty().isNotEmpty())
                .and(tab_ratetotal.textProperty().isNotEmpty())
                .and(tab_allrebate.textProperty().isNotEmpty())
                .and(tab_delifee.textProperty().isNotEmpty())
                .and(tab_actPay.textProperty().isNotEmpty())
        );
        return res;
    }

    @Override
    public OrderItemModel pack() {
        OrderItemModel res = new OrderItemModel();
        res.setName(tab_name.getText());
        res.setSerial(tab_serial.getText());
        res.setDetail(tab_detail.getText());
        res.setPrice(FXUtils.getDecimal(tab_price));
        if (tab_pack.getItems().size()<3){
            res.setPackDefault(tab_pack.getItems().get(0));
        }else{
            res.setPackDefault(FXUtils.getLong(tab_packNum.getText(), 1));
        }
        res.setPack(FXUtils.getLong(tab_packNum.getText(), 1));
            res.setNum(FXUtils.getDecimal(tab_num));
        res.setRate(FXUtils.getDecimal(tab_rate));
        res.setRebate(FXUtils.getDecimal(tab_rebate));
        res.setDelifee(FXUtils.getDecimal(tab_delifee));

        res.setUnit(tab_unit.getText());

        // 添加默认值
        FXWidgetUtil.addDefaultList(
                new Pair<>("prefer.addorder.rate", tab_rate.getText()),
                new Pair<>("prefer.addorder.rebate", tab_rebate.getText()),
                new Pair<>("prefer.addorder.delifee", tab_delifee.getText())
        );

        // 清空选择器
        serial_c.getSelectionModel().clearSelection();
        return res;
    }

    public void setCid(long cid){
        this.cid = cid;
    }

    /**
     * 加载产品列表
     */
    private void loadProducts() {
        Platform.runLater(() -> {
            try {
                serialChangeEnable = false;
//                serial_c.getItems().setAll(QSApp.service.getProductService().selectAll());
                serial_c.getItems().setAll(QSApp.service.getOrderFavorService().selectAll(cid));
                serial_c.getItems().add(null);
                serialChangeEnable = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
