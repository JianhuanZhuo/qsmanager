package cn.keepfight.qsmanager.dao.analysis;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SellAnalysisDaoWrapper {

    private LongProperty cid = new SimpleLongProperty();
    private StringProperty cname = new SimpleStringProperty();

    private Map<String, BigDecimal> sumMap = new HashMap<>();

    public long getCid() {
        return cid.get();
    }

    public LongProperty cidProperty() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid.set(cid);
    }

    public String getCname() {
        return cname.get();
    }

    public StringProperty cnameProperty() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname.set(cname);
    }

    public Map<String, BigDecimal> getSumMap() {
        return sumMap;
    }

    public void setSumMap(Map<String, BigDecimal> sumMap) {
        this.sumMap = sumMap;
    }

    public ObjectProperty<BigDecimal> sumCountProperty() {
        return sumMap.values().stream()
                .reduce(BigDecimal::add)
                .<ObjectProperty<BigDecimal>>map(SimpleObjectProperty::new)
                .orElseGet(() -> new SimpleObjectProperty<>(new BigDecimal(0)));
    }
}
