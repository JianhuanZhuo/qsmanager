package cn.keepfight.qsmanager.dao;

/**
 * 客户，销售商数据访问对象
 * Created by tom on 2017/9/25.
 */
public class CustomerDao {

    Long id;

    /**
     * 交易商
     */
    TraderDao traderDao;

    /**
     * 系统操作者
     */
    OperatorDao operatorDao;
}
