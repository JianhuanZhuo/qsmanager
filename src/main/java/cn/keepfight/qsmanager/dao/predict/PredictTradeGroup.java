package cn.keepfight.qsmanager.dao.predict;

import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分组数据访问对象
 * Created by 卓建欢 on 2017/10/29.
 */
public class PredictTradeGroup {
    private Long sid;
    private String name;
    private List<PredictTradeItemDao> lefts;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PredictTradeItemDao> getLefts() {
        return lefts;
    }

    public void setLefts(List<PredictTradeItemDao> lefts) {
        this.lefts = lefts;
    }
}
