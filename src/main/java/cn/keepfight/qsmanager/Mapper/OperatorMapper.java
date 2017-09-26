package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.OperatorDao;
import cn.keepfight.qsmanager.dao.OperatorDao;
import cn.keepfight.qsmanager.model.CustomModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统操作者表映射器
 * Created by tom on 2017/6/7.
 */
public interface OperatorMapper extends Mapper {

    /**
     * 选择全部系统操作者
     */
    List<OperatorDao> selectAll() throws Exception;

    /**
     * 根据系统操作者 ID 选择系统操作者
     */
    OperatorDao selectByID(Long id) throws Exception;

    /**
     * 更新指定系统操作者，以ID为准
     */
    void update(OperatorDao custom) throws Exception;

    /**
     * 插入指定系统操作者，插入后，该系统操作者的ID将得到更新
     */
    void insert(OperatorDao custom) throws Exception;

    /**
     * 删除指定系统操作者，以 ID 为准。
     */
    void deleteByID(Long id) throws Exception;

    /**
     * 根据指定口令检查系统操作者的合法性
     *
     * @return 若账号密码合法返回口令对应的系统操作者，否则返回 null
     */
    OperatorDao checkLegality(
            @Param("account") String account,
            @Param("password") String password) throws Exception;
}
