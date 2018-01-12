package cn.keepfight.qsmanager.dao.analysis;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MaterialAnalysisDao {
    private String month;
    private Map<String, BigDecimal> unitMapSum = new HashMap<>();

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Map<String, BigDecimal> getUnitMapSum() {
        return unitMapSum;
    }

    public void setUnitMapSum(Map<String, BigDecimal> unitMapSum) {
        this.unitMapSum = unitMapSum;
    }
}
