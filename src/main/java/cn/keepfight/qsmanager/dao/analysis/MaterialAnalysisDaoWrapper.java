package cn.keepfight.qsmanager.dao.analysis;


import cn.keepfight.qsmanager.dao.DaoWrapper;
import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MaterialAnalysisDaoWrapper implements DaoWrapper<MaterialAnalysisDao>{
    private StringProperty month = new SimpleStringProperty();
    private Map<String, BigDecimal> unitMapSum = new HashMap<>();

    public MaterialAnalysisDaoWrapper(MaterialAnalysisDao data){
        wrap(data);
    }

    @Override
    public void wrap(MaterialAnalysisDao data) {
        FXReflectUtils.attrsCopy(data,this);
    }

    @Override
    public MaterialAnalysisDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new MaterialAnalysisDao());
    }

    public String getMonth() {
        return month.get();
    }

    public StringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public Map<String, BigDecimal> getUnitMapSum() {
        return unitMapSum;
    }

    public void setUnitMapSum(Map<String, BigDecimal> unitMapSum) {
        this.unitMapSum = unitMapSum;
    }


    public BigDecimal getUnitMapSumByUnit(String unit){
        return unitMapSum.getOrDefault(unit, new BigDecimal(0));
    }

    public BigDecimal getUnitSumCount(){
        return unitMapSum.values().stream().reduce(BigDecimal::add).orElse(new BigDecimal(0));
    }
}
