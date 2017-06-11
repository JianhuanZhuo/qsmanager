package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.*;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.FXUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地服务实现
 * Created by tom on 2017/6/7.
 */
public class LocalServiceImpl implements ServerService {

    private static SqlSessionFactory factory;

    /**
     * 图片内部存储路径
     */
    private static String picPath;

    static {
        try {
            picPath = new File("./pic").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LocalServiceImpl() {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatis.xml");
        } catch (IOException e) {
            // 要是这个也没读出来，那说明没得玩了
            e.printStackTrace();
        }
        factory = new SqlSessionFactoryBuilder().build(reader);
    }

    @Override
    public LoginService getLoginService() {
        return new LoginService() {
            @Override
            public CustomModel login(CustomModel model) throws Exception {
                CustomModel userRes = FXUtils.getMapper(factory, CustomMapper.class, CustomMapper::checkLegality, model);
                if (userRes == null) {
                    throw new PswIllegalException();
                }
                return userRes;
            }

            @Override
            public void checkConnect() throws Exception {

            }
        };
    }

    @Override
    public CustomService getCustomService() {
        return new CustomService() {
            @Override
            public List<CustomModel> selectAll() throws Exception {
                return FXUtils.getMapper(factory, CustomMapper.class, CustomMapper::selectAll);
            }

            @Override
            public void update(CustomModel custom) throws Exception {
                FXUtils.getMapper(factory, CustomMapper.class, CustomMapper::update, custom);
            }

            @Override
            public void insert(CustomModel custom) throws Exception {
                FXUtils.getMapper(factory, CustomMapper.class, CustomMapper::insert, custom);
            }

            @Override
            public void delete(CustomModel custom) throws Exception {
                FXUtils.getMapper(factory, CustomMapper.class, CustomMapper::delete, custom);
            }
        };
    }

    @Override
    public ProductService getProductService() {
        return new ProductService() {
            @Override
            public List<ProductModel> selectAll() throws Exception {
                return FXUtils.getMapper(factory, ProductMapper.class, ProductMapper::selectAll);
            }

            @Override
            public void update(ProductModel model) throws Exception {
                FXUtils.getMapper(factory, ProductMapper.class, ProductMapper::update, model);
            }

            @Override
            public void insert(ProductModel model) throws Exception {
                if (model.getPicurl() != null && !model.getPicurl().trim().equals("") &&
                        !isPrivateUrl(model.getPicurl())) {
                    model.setPicurl(copyAndTransform(model.getPicurl()));
                    System.out.println("图片转存为：" + model.getPicurl());
                }
                FXUtils.getMapper(factory, ProductMapper.class, ProductMapper::insert, model);
            }

            @Override
            public void delete(ProductModel model) throws Exception {
                FXUtils.getMapper(factory, ProductMapper.class, ProductMapper::delete, model);
            }
        };
    }

    @Override
    public SupplyService getSupplyService() {
        return new SupplyService() {
            @Override
            public List<SupplyModel> selectAll() throws Exception {
                return FXUtils.getMapper(factory, SupplyMapper.class, SupplyMapper::selectAll);
            }

            @Override
            public void update(SupplyModel model) throws Exception {
                FXUtils.getMapper(factory, SupplyMapper.class, SupplyMapper::update, model);
            }

            @Override
            public void insert(SupplyModel model) throws Exception {
                FXUtils.getMapper(factory, SupplyMapper.class, SupplyMapper::insert, model);
            }

            @Override
            public void delete(SupplyModel model) throws Exception {
                FXUtils.getMapper(factory, SupplyMapper.class, SupplyMapper::delete, model);
            }
        };
    }

    @Override
    public MaterialService getMaterialService() {
        return new MaterialService() {
            @Override
            public List<MaterialModel> selectAll(Long supply) throws Exception {
                return FXUtils.getMapper(factory, MaterialMapper.class, MaterialMapper::selectAll, supply);
            }

            @Override
            public void update(MaterialModel model) throws Exception {
                FXUtils.getMapper(factory, MaterialMapper.class, MaterialMapper::update, model);
            }

            @Override
            public void insert(MaterialModel model) throws Exception {
                FXUtils.getMapper(factory, MaterialMapper.class, MaterialMapper::insert, model);
            }

            @Override
            public void delete(MaterialModel model) throws Exception {
                FXUtils.getMapper(factory, MaterialMapper.class, MaterialMapper::delete, model);
            }
        };
    }

    @Override
    public ReceiptService getReceiptService() {
        return new ReceiptService() {
            @Override
            public List<ReceiptModelFull> selectAll(Long sid, Long year, Long month) throws Exception {
                ReceiptSelection selection = new ReceiptSelection();
                List<ReceiptModel> receiptModels = FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::selectAll, selection);

                // 按ID查询全部的明细
                List<ReceiptModelFull> fulls = new ArrayList<>();
                if (receiptModels != null) {
                    for (ReceiptModel receiptModel : receiptModels) {
                        ReceiptModelFull modelFull = new ReceiptModelFull(receiptModel);
                        modelFull.setDetailList(FXUtils.getMapper(factory, ReceiptDetailMapper.class, ReceiptDetailMapper::selectAll, receiptModel.getId()));
                        fulls.add(modelFull);
                    }
                }
                return fulls;
            }

            @Override
            public void insert(ReceiptModelFull model) throws Exception {
                ReceiptModel receiptModel = model.getReceiptModel();
                FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::insert, receiptModel);

                // 回设 RID
                model.setId(receiptModel.getId());

                for (ReceiptDetailModel detail : model.getDetailList()) {
                    FXUtils.getMapper(factory, ReceiptDetailMapper.class, ReceiptDetailMapper::insert, detail);
                }
            }

            @Override
            public void delete(ReceiptModel model) throws Exception {
                FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::delete, model);
            }
        };
    }

    private boolean isPrivateUrl(String picUrl) {
        return picUrl != null && picUrl.startsWith(picPath);
    }

    private String copyAndTransform(String picUrl) throws IOException {
        Path source = Paths.get(picUrl);
        File targetFile = new File("./pic/" + source.getFileName()).getCanonicalFile();
        Path target = targetFile.toPath();
        Files.copy(source, target);
        // 添加 file: 本地文件连接
        return "file:" + targetFile.getAbsolutePath();
    }
}
