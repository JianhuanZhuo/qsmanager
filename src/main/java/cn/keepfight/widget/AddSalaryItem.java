package cn.keepfight.widget;

import cn.keepfight.qsmanager.dao.StuffDao;
import cn.keepfight.utils.FXUtils;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 预算表中的小组件
 * Created by 卓建欢 on 2017/9/23.
 */
public class AddSalaryItem implements Initializable, Widget<AddSalaryItem.AddSalaryObj> {
    public HBox root;
    public CheckBox check;
    public TextField text_total;
    public HBox content;
    public Label halt;
    public TextField text_fix;

    private AddSalaryObj obj;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        content.disableProperty().bind(check.selectedProperty().not());
    }

    public Node setStuff(StuffDao data) {
        set(new AddSalaryObj(
                true,
                new BigDecimal(0),
                new BigDecimal(2000),
                data));
        return getRoot();
    }

    @Override
    public void set(AddSalaryObj data) {
        obj = data;
        check.selectedProperty().set(data.getSelect());
        text_total.setText(FXUtils.deciToMoney(data.getTotal()));
        text_fix.setText(FXUtils.deciToMoney(data.getFix()));
        if (!data.getStuff().getOperatorDao().getUserDao().getHalt()) {
            halt.setText("");
            check.setSelected(true);
        } else {
            halt.setText("（该员工已停职）");
            check.setSelected(false);
        }
        check.setText(data.getStuff().getSerial() + "-" + data.getStuff().getName());
    }

    @Override
    public AddSalaryObj get() {
        if (obj==null){
            throw new RuntimeException("why not initialize?");
        }
        obj.setTotal(FXUtils.getDecimal(text_total.getText()));
        obj.setFix(FXUtils.getDecimal(text_fix.getText()));
        obj.setSelect(check.isSelected());
        return obj;
    }

    public void setCheck(boolean c){
        check.setSelected(c);
    }

    public void setTotal(String xx){
        text_total.setText(xx);
    }

    public static class AddSalaryObj {
        private Boolean select = false;
        private BigDecimal total = new BigDecimal(0);
        private BigDecimal fix = new BigDecimal(0);
        private StuffDao stuff;

        public AddSalaryObj(Boolean select, BigDecimal total, BigDecimal basic, StuffDao stuff) {
            this.select = select;
            this.total = total;
            this.fix = basic;
            this.stuff = stuff;
        }

        public Boolean getSelect() {
            return select;
        }

        public AddSalaryObj setSelect(Boolean select) {
            this.select = select;
            return this;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public AddSalaryObj setTotal(BigDecimal total) {
            this.total = total;
            return this;
        }

        public BigDecimal getFix() {
            return fix;
        }

        public void setFix(BigDecimal fix) {
            this.fix = fix;
        }

        public StuffDao getStuff() {
            return stuff;
        }

        public AddSalaryObj setStuff(StuffDao stuff) {
            this.stuff = stuff;
            return this;
        }
    }
}
