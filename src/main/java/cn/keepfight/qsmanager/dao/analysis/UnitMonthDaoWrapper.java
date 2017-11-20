package cn.keepfight.qsmanager.dao.analysis;

import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class UnitMonthDaoWrapper implements DaoWrapper<UnitMonthDao> {
    private LongProperty uid = new SimpleLongProperty();
    private StringProperty uname = new SimpleStringProperty();
    private ObjectProperty<BigDecimal> mon1 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon2 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon3 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon4 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon5 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon6 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon7 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon8 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon9 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon10 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon11 = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> mon12 = new SimpleObjectProperty<>();

    public UnitMonthDaoWrapper(UnitMonthDao data){
        wrap(data);
    }

    @Override
    public void wrap(UnitMonthDao data) {
        FXReflectUtils.attrsCopy(data,this);
    }

    @Override
    public UnitMonthDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new UnitMonthDao());
    }

    public long getUid() {
        return uid.get();
    }

    public LongProperty uidProperty() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid.set(uid);
    }

    public String getUname() {
        return uname.get();
    }

    public StringProperty unameProperty() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname.set(uname);
    }

    public ObjectProperty<BigDecimal> countProperty() {
        SimpleObjectProperty<BigDecimal> count = new SimpleObjectProperty<>();
        count.bind(new ObjectBinding<BigDecimal>() {
            {
                bind(mon1, mon2, mon3, mon4, mon5, mon6, mon7, mon8, mon9, mon10, mon11, mon12);
            }

            @Override
            protected BigDecimal computeValue() {
                BigDecimal res = new BigDecimal(0);
                for (ObjectProperty<BigDecimal> a : Arrays.asList(mon1, mon2, mon3, mon4, mon5, mon6, mon7, mon8, mon9, mon10, mon11, mon12)) {
                    res = res.add(a.get() == null ? new BigDecimal(0) : a.get());
                }
                return res;
            }
        });
        return count;
    }

    public BigDecimal getMon1() {
        return mon1.get();
    }

    public ObjectProperty<BigDecimal> mon1Property() {
        return mon1;
    }

    public void setMon1(BigDecimal mon1) {
        this.mon1.set(mon1);
    }

    public BigDecimal getMon2() {
        return mon2.get();
    }

    public ObjectProperty<BigDecimal> mon2Property() {
        return mon2;
    }

    public void setMon2(BigDecimal mon2) {
        this.mon2.set(mon2);
    }

    public BigDecimal getMon3() {
        return mon3.get();
    }

    public ObjectProperty<BigDecimal> mon3Property() {
        return mon3;
    }

    public void setMon3(BigDecimal mon3) {
        this.mon3.set(mon3);
    }

    public BigDecimal getMon4() {
        return mon4.get();
    }

    public ObjectProperty<BigDecimal> mon4Property() {
        return mon4;
    }

    public void setMon4(BigDecimal mon4) {
        this.mon4.set(mon4);
    }

    public BigDecimal getMon5() {
        return mon5.get();
    }

    public ObjectProperty<BigDecimal> mon5Property() {
        return mon5;
    }

    public void setMon5(BigDecimal mon5) {
        this.mon5.set(mon5);
    }

    public BigDecimal getMon6() {
        return mon6.get();
    }

    public ObjectProperty<BigDecimal> mon6Property() {
        return mon6;
    }

    public void setMon6(BigDecimal mon6) {
        this.mon6.set(mon6);
    }

    public BigDecimal getMon7() {
        return mon7.get();
    }

    public ObjectProperty<BigDecimal> mon7Property() {
        return mon7;
    }

    public void setMon7(BigDecimal mon7) {
        this.mon7.set(mon7);
    }

    public BigDecimal getMon8() {
        return mon8.get();
    }

    public ObjectProperty<BigDecimal> mon8Property() {
        return mon8;
    }

    public void setMon8(BigDecimal mon8) {
        this.mon8.set(mon8);
    }

    public BigDecimal getMon9() {
        return mon9.get();
    }

    public ObjectProperty<BigDecimal> mon9Property() {
        return mon9;
    }

    public void setMon9(BigDecimal mon9) {
        this.mon9.set(mon9);
    }

    public BigDecimal getMon10() {
        return mon10.get();
    }

    public ObjectProperty<BigDecimal> mon10Property() {
        return mon10;
    }

    public void setMon10(BigDecimal mon10) {
        this.mon10.set(mon10);
    }

    public BigDecimal getMon11() {
        return mon11.get();
    }

    public ObjectProperty<BigDecimal> mon11Property() {
        return mon11;
    }

    public void setMon11(BigDecimal mon11) {
        this.mon11.set(mon11);
    }

    public BigDecimal getMon12() {
        return mon12.get();
    }

    public ObjectProperty<BigDecimal> mon12Property() {
        return mon12;
    }

    public void setMon12(BigDecimal mon12) {
        this.mon12.set(mon12);
    }

}
