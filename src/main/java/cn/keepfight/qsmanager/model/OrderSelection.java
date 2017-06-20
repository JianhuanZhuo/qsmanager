package cn.keepfight.qsmanager.model;

/**
 * 订单选择器
 * Created by tom on 2017/6/20.
 */
public class OrderSelection {
    private Long cid; // 客户ID
    private String year;
    private String month;
    private String s; // 订单状态选择

    public OrderSelection(){}
    public OrderSelection(Long cid, Long year, Long month, State s){
        setCid(cid);
        setYear(year);
        setMonth(month);
        setS(s);
    }


    public enum State{
        READY("r", "未发货"), // 未发货
        DOING("i", "已发货"), // 已发货
        DONE("d", "已完成");  // 已完成，全部发完

        private String s;
        private String displayText;
        State(String c, String displayText){
            s = c;
            this.displayText = displayText;
        }
        public String getS(){
            return s;
        }

        public String getDisplayText() {
            return displayText;
        }
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(Long year) {
        this.year = year==null?null:""+year;
    }

    public void setMonth(Long month) {
        if (month==null){
            this.month=null;
            return;
        }
        if (month < 10) {
            this.month = "0" + month;
        } else {
            this.month = month.toString();
        }
    }

    public String getS() {
        return s;
    }

    public void setS(State s) {
        if (s==null){
            this.s = null;
        }else {
            this.s = s.getS();
        }
    }
}
