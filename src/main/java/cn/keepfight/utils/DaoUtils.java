package cn.keepfight.utils;

import java.io.*;

/**
 * 工具类
 * Created by tom on 2017/9/25.
 */
public abstract class DaoUtils {


    /**
     * 克隆对象数据.
     * 深克隆，要求对象可序列化
     */
    @SuppressWarnings("unchecked")
    public static<T> T cloneTo(T src) throws RuntimeException {
        ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(memoryBuffer)){
            out.writeObject(src);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(memoryBuffer.toByteArray()))) {
            return (T) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
