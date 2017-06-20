package cn.keepfight.qsmanager.service;

import cn.keepfight.qsmanager.Mapper.*;
import cn.keepfight.qsmanager.model.*;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.QSUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
                selection.setSid(sid);
                selection.setYear(year);
                selection.setMonth(month);
                System.out.println(selection);
                List<ReceiptModelFull> receiptModels = FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::selectAll, selection);
                // 按ID查询全部的明细
                List<ReceiptModelFull> fulls = new ArrayList<>();
                for (ReceiptModelFull modelFull : receiptModels) {
                    modelFull.setDetailList(FXUtils.getMapper(factory, ReceiptDetailMapper.class, ReceiptDetailMapper::selectAll, modelFull.getId()));
                    fulls.add(modelFull);
                }
                return fulls;
            }

            @Override
            public List<AnnualTotalModel> supAnnualTotal(Long sid, Long year) throws Exception {
                ReceiptSelection selection = new ReceiptSelection();
                selection.setYear(year);
                selection.setSid(sid);
                return FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::supAnnualTotal, selection);
            }

            @Override
            public List<MonStatModel> takeStat(Long year) throws Exception  {
                return FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::takeStat, year.toString());
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
            public void update(ReceiptModelFull model) throws Exception {
                delete(new ReceiptModel(model));
                insert(model);
            }

            @Override
            public void delete(ReceiptModel model) throws Exception {
                FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::delete, model);
            }

            @Override
            public List<Long> selectYear() throws Exception {
                return FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::selectYear);
            }
        };
    }

    @Override
    public SupAnnualService getSupAnnualService() {
        return new SupAnnualService(){
            @Override
            public SupAnnualModelFull selectAnnual(Long sid, Long year) throws Exception {
                SupAnnualModelFull modelFull = new SupAnnualModelFull();
                AnnualSelection selection = new AnnualSelection(sid, year);

                // 查询对账表记录
                SupAnnualModel annualModel = FXUtils.getMapper(factory, SupAnnuMapper.class, SupAnnuMapper::selectAnnual, selection);
                if (annualModel==null){
                    // 若查无此信息则插入
                    annualModel = new SupAnnualModel();
                    annualModel.setSid(sid);
                    annualModel.setYear(year);
                    FXUtils.getMapper(factory, SupAnnuMapper.class, SupAnnuMapper::insertAnnual, annualModel);
                }
                modelFull.set(annualModel);


                // 查询月份的原有记录，并使用 Map 作为存储
                List<SupAnnualMonModel> mons = FXUtils.getMapper(factory, SupAnnuMapper.class, SupAnnuMapper::selectMon, selection);
                Map<Long, SupAnnualMonModel> monMap = mons.stream()
                        .peek(s->s.setValid(true))
                        .collect(Collectors.toMap(SupAnnualMonModel::getMon, e->e));

                // 查询月份的采购总额
                ReceiptSelection receiptSelection = new ReceiptSelection();
                receiptSelection.setSid(sid);
                receiptSelection.setYear(year);
                List<AnnualTotalModel> monTotals = FXUtils.getMapper(factory, ReceiptMapper.class, ReceiptMapper::supAnnualTotal, receiptSelection);
                monTotals.forEach(m-> monMap.compute(m.getMon(), (k, v)->{
                    if (v==null){
                        v = new SupAnnualMonModel();
                    }
                    v.setTotal(m.getTotal());
                    v.setMon(m.getMon());
                    return v;
                }));

                LongStream.range(1,13).forEach(m-> monMap.compute(m, (k, v)->{
                    if (v==null){
                        v = new SupAnnualMonModel();
                        v.setMon(k);
                    }
                    return v;
                }));

                modelFull.addMon(monMap.values());
                return modelFull;
            }

            @Override
            public void updateAnnual(SupAnnualModel model) throws Exception {

            }

            @Override
            public List<MonStatModel> payStat(Long year) throws Exception {
                return FXUtils.getMapper(factory, SupAnnuMapper.class, SupAnnuMapper::payStat, year);
            }

            @Override
            public List<TPStatModel> tpStat(Long year) throws Exception {
                return FXUtils.getMapper(factory, SupAnnuMapper.class, SupAnnuMapper::tpStat, year);
            }

            @Override
            public void updateMon(SupAnnualMonModel model) throws Exception {
                if (model.isValid()) {
                    FXUtils.getMapper(factory, SupAnnuMapper.class, SupAnnuMapper::updateMon, model);
                }else {
                    FXUtils.getMapper(factory, SupAnnuMapper.class, SupAnnuMapper::insertMon, model);
                }
            }
        };
    }

    @Override
    public OrderService getOrderService() {
        return new OrderService(){

            @Override
            public List<OrderModelFull> selectAll(OrderSelection selection) throws Exception {
                List<OrderModelFull> res = FXUtils.getMapper(factory, OrderMapper.class, OrderMapper::selectAll, selection);
                for (OrderModelFull modelFull : res) {
                    modelFull.setOrderItemModels(FXUtils.getMapper(factory, OrderItemMapper.class, OrderItemMapper::selectAllByOid, modelFull.getId()));
                }
                return res;
            }

            @Override
            public List<AnnualTotalModel> supAnnualTotal(Long cid, Long year) throws Exception {
                OrderSelection selection = new OrderSelection();
                selection.setYear(year);
                selection.setCid(cid);
                return FXUtils.getMapper(factory, OrderMapper.class, OrderMapper::supAnnualTotal, selection);
            }

            @Override
            public void insert(OrderModelFull model) throws Exception {
                Long ct = FXUtils.getMapper(factory, OrderMapper.class, OrderMapper::getCt, model.getOrderdate());
                model.setCt(ct);
                model.setSerial(QSUtil.orderSerial(model.getOrderdate(), ct));
                OrderModel orderModel = model.get();
                FXUtils.getMapper(factory, OrderMapper.class, OrderMapper::insert, orderModel);

                // 回设 RID
                model.setId(orderModel.getId());

                for (OrderItemModel item : model.getOrderItemModels()) {
                    FXUtils.getMapper(factory, OrderItemMapper.class, OrderItemMapper::insert, item);
                }
            }

            @Override
            public void update(OrderModelFull model) throws Exception {
            }

            @Override
            public void delete(OrderModel model) throws Exception {
                FXUtils.getMapper(factory, OrderItemMapper.class, OrderItemMapper::deleteByOid, model.getId());
                FXUtils.getMapper(factory, OrderMapper.class, OrderMapper::delete, model);
            }

            @Override
            public List<Long> selectYear() throws Exception {
                return FXUtils.getMapper(factory, OrderMapper.class, OrderMapper::selectYear);
            }
        };
    }
}
