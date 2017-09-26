package cn.keepfight.utils;

import cn.keepfight.qsmanager.dao.DaoWrapper;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    public static <T> T cloneTo(T src) throws RuntimeException {
        ByteArrayOutputStream memoryBuffer = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(memoryBuffer)) {
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

    /**
     * 包装一整个列表的原生数据对象 为 包装类型对象列表
     *
     * @param dList 原生数据对象列表
     * @param newer 包装类型对象生成器
     * @param <D>   原生数据类型
     * @return 包装类型对象列表
     */
    public static <D> List<DaoWrapper<D>> daosWrap(List<D> dList, Supplier<DaoWrapper<D>> newer) {
        return dList.stream()
                .filter(Objects::nonNull)
                .map(d -> {
                    DaoWrapper<D> data = newer.get();
                    data.wrap(d);
                    return data;
                }).collect(Collectors.toList());
    }

    /**
     * 从包装类型对象列表中抽取出原生数据对象列表
     * @param wrappers 包装类型对象列表
     * @param <D> 原生数据类型
     * @return 原生数据对象列表
     */
    public static <D> List<D> daosGet(List<DaoWrapper<D>> wrappers) {
        return wrappers.stream()
                .filter(Objects::nonNull)
                .map(DaoWrapper::get)
                .collect(Collectors.toList());
    }
}
