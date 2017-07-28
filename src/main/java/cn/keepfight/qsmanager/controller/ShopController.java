package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.qsmanager.model.OrderModelFull;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.ListBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 浏览下单菜单项内容控制器
 * Created by tom on 2017/6/18.
 */
public class ShopController implements ContentCtrl, Initializable {

    @FXML
    private VBox root;
    @FXML
    private Button sumup;
    @FXML
    private Button refreshInfo;

    @FXML
    private Label all_total;
    @FXML
    private CheckBox justAdd;

    @FXML
    private ListView<OrderItemModel> proList;

    private SumupController sumupController;

    // 全部产品列表
    private ObservableList<OrderItemModel> plist = FXCollections.observableArrayList();

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadProducts();
        Platform.runLater(() -> {
            try {
                sumupController = ViewPathUtil.loadViewForController("prod_sumup.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void showed() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        proList.setCellFactory(param -> new ProdListCell());

        // 刷新
        refreshInfo.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("是否要刷新产品列表？");
                    alert.setContentText("由于刷新产品列表会因为产品属性改变而清除已选的全部数据，确定刷新？");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        loadProducts();
                    }
                }
        );

        proList.itemsProperty().bind(new ListBinding<OrderItemModel>() {
            {
                bind(plist, justAdd.selectedProperty());
            }

            @Override
            protected ObservableList<OrderItemModel> computeValue() {
                return plist.filtered(i -> !justAdd.isSelected() || !i.getNum().equals(new BigDecimal(0)));
            }
        });

        sumup.setOnAction(e->{
            ObservableList<OrderItemModel> ls = plist.filtered(i->!i.getNum().equals(new BigDecimal(0)));
            if (ls.size()==0){
                WarningBuilder.build("无法结算订购", "由于您当前未选择任何产品，所以无法结算订购！");
            }else {
                OrderModelFull modelFull = new OrderModelFull();
                modelFull.setOrderItemModels(ls);
                Optional<OrderModelFull> op = CustomDialog.gen().build(sumupController, modelFull);
                op.ifPresent(model -> {
                    try {
                        QSApp.service.getOrderService().insert(model);
                        loadProducts();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        WarningBuilder.build("订购失败", "订购失败，请检查网络是否通畅");
                    }
                });
            }
        });
    }

    // 加载产品列表
    private void loadProducts() {
        Platform.runLater(() -> {
            try {
                all_total.setText("0");
                plist.setAll(QSApp.service.getProductService().selectAll().stream().map(OrderItemModel::new).collect(Collectors.toList()));
                plist.forEach(e->{
                    e.numProperty().addListener(x);
                    e.packProperty().addListener(x);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 产品列表条目
     */
    private static class ProdListCell extends ListCell<OrderItemModel> {
        ShopItemController controller;

        ProdListCell() {
            try {
                controller = ViewPathUtil.loadViewForController("shop_item.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateItem(OrderItemModel item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                setGraphic(controller.getRoot());
                controller.fill(item);
            }
        }
    }

    private final InvalidationListener x = (ob)->{
        Optional t = proList.getItems().stream()
                .map((model)->{
                    try {
                        return model.getPrice()
                                .multiply(model.getNum())
                                .multiply(new BigDecimal(model.getPack()));
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .reduce(BigDecimal::add);
        String text = "0";
        if (t.isPresent()) {
            text = t.get().toString();
        }
       all_total.setText(text);
    };
}
