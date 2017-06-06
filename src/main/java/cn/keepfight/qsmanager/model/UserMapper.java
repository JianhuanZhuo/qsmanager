package cn.keepfight.qsmanager.model;

/**
 * 用户映射类
 * Created by tom on 2017/6/5.
 */
public interface UserMapper {

    User selectUserByID(int i) throws Exception;

    void insertUser(User user) throws Exception;

    /**
     * 检查用户的合法性
     *
     * @param user 带有账号和密码的用户对象
     * @return 若账号密码合法，则返回 null
     * @throws Exception
     */
    User checkLegality(User user) throws Exception;
}
