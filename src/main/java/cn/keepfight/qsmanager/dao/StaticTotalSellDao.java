package cn.keepfight.qsmanager.dao;

import cn.keepfight.utils.FXUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StaticTotalSellDao {
    private String uname;
    private Long uid;
    private Map<String, BigDecimal> unitToSumMap = new HashMap<>();

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Map<String, BigDecimal> getUnitToSumMap() {
        return unitToSumMap;
    }

    public void setUnitToSumMap(Map<String, BigDecimal> unitToSumMap) {
        this.unitToSumMap = unitToSumMap;
    }

    public String getSumOrDefaultZero(String key) {
        return FXUtils.deciToMoney(unitToSumMap.getOrDefault(key, new BigDecimal(0)));
    }

    public BigDecimal getOrDefaultZero(String key) {
        return unitToSumMap.getOrDefault(key, new BigDecimal(0));
    }

}
