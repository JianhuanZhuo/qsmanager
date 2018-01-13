package cn.keepfight.qsmanager.Mapper;

import cn.keepfight.qsmanager.dao.ImageDao;
import cn.keepfight.qsmanager.dao.ImageListDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageMapper {

    List<ImageListDao> selectByCate(String cate) throws Exception;

    String getImage(Long id) throws Exception;

    void updateName(@Param("id") Long id, @Param("pname") String pname) throws Exception;

    void updateNote(@Param("id") Long id, @Param("note") String note) throws Exception;

    void del(Long id) throws Exception;

    void insert(ImageDao dao) throws Exception;
}
