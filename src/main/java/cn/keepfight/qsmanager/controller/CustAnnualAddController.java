package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.CustAnnualMonModel;
import cn.keepfight.qsmanager.model.CustAnnualMonModel;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

/**
 * 客户年对对账表
 * Created by tom on 2017/6/22.
 */
public class CustAnnualAddController implements DialogContent<CustAnnualMonModel>, Initializable {

    @FXML
    private VBox root;
    @FXML
    private TextField mon;
    @FXML
    private TextField total;
    @FXML
    private TextField billunit;
    @FXML
    private DatePicker billdate;
    @FXML
    private TextField billtotal;
    @FXML
    private TextField rate;
    @FXML
    private TextField ratetotal;
    @FXML
    private TextField remitunit;
    @FXML
    private TextField pattern;
    @FXML
    private DatePicker remitdate;
    @FXML
    private TextField paytotal;
    @FXML
    private TextArea note;

    private CustAnnualMonModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billdate.getEditor().setDisable(true);
        remitdate.getEditor().setDisable(true);

        FXUtils.limitNum(rate, 3, 5, true);
        FXUtils.limitNum(paytotal, 10, 5, true);
        FXUtils.limitNum(billtotal, 10, 5, true);

        InvalidationListener handler = (x) -> {
            try {
                ratetotal.setText(new BigDecimal(rate.getText()).multiply(new BigDecimal(billtotal.getText())).toString());
            } catch (Exception e) {
                ratetotal.setText("0");
            }
        };

        rate.focusedProperty().addListener(handler);
        billtotal.focusedProperty().addListener(handler);
    }

    @Override
    public void init() {

        mon.setText("");
        total.setText("0");
        billunit.setText("");
        billdate.setValue(FXUtils.stampToLocalDate(null));
        billtotal.setText("0");
        rate.setText("0");

        remitunit.setText("");
        pattern.setText("");
        remitdate.setValue(FXUtils.stampToLocalDate(null));
        paytotal.setText("0");
        note.setText("");


        // 加载默认记忆选项并添加默认下拉
        FXWidgetUtil.defaultList(
                new Pair<>(billunit, "prefer.supan.billunit"),
                new Pair<>(rate, "prefer.supan.rate"),
                new Pair<>(remitunit, "prefer.supan.remitunit"),
                new Pair<>(pattern, "prefer.supan.pattern")
        );
    }

    @Override
    public void fill(CustAnnualMonModel model) {
        this.model = model;
        mon.setText(model.getMon().toString() + "月");
        total.setText(FXUtils.decimalStr(model.getTotal()));
        billunit.setText(model.getBillunit());
        billdate.setValue(FXUtils.stampToLocalDate(model.getBilldate()));
        billtotal.setText(FXUtils.decimalStr(model.getBilltotal()));
        rate.setText(FXUtils.decimalStr(model.getRate()));

        remitunit.setText(model.getRemitunit());
        pattern.setText(model.getPattern());
        remitdate.setValue(FXUtils.stampToLocalDate(model.getRemitdate()));

        paytotal.setText(FXUtils.decimalStr(model.getPaytotal()));
        note.setText(model.getNote());
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        BooleanProperty res = new SimpleBooleanProperty();
        res.bind(billtotal.textProperty().isNotEmpty()
                .and(rate.textProperty().isNotEmpty())
                .and(paytotal.textProperty().isNotEmpty()));
        return res;
    }

    @Override
    public CustAnnualMonModel pack() {
        CustAnnualMonModel model = this.model;
        if (model == null) {
            model = new CustAnnualMonModel();
        }
        model.setMon(Long.valueOf(mon.getText().replaceAll("[^\\d]+", "")));
        model.setTotal(new BigDecimal(total.getText()));
        model.setBillunit(billunit.getText());
        model.setBilldate(Date.valueOf(billdate.getValue()).getTime());
        model.setBilltotal(new BigDecimal(billtotal.getText()));
        model.setRate(new BigDecimal(rate.getText()));
        model.setRemitunit(remitunit.getText());
        model.setPattern(pattern.getText());
        model.setRemitdate(Date.valueOf(remitdate.getValue()).getTime());
        model.setPaytotal(new BigDecimal(paytotal.getText()));
        model.setNote(note.getText());

        FXWidgetUtil.addDefaultList(
                new Pair<>("prefer.supan.billunit", billunit.getText()),
                new Pair<>("prefer.supan.rate", rate.getText()),
                new Pair<>("prefer.supan.remitunit", remitunit.getText()),
                new Pair<>("prefer.supan.pattern", pattern.getText())
        );
        return model;
    }
}
