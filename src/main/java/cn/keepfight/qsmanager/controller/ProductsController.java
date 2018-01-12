package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.FXWidgetUtil;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

/**
 * 产品管理界面控制器
 * Created by tom on 2017/6/6.
 */
public class ProductsController implements ContentCtrl {
    @FXML
    private VBox root;
    @FXML
    private TableView<ProductModel> prodTable;

    @FXML
    private TableColumn<ProductModel, String> table_serial;
    @FXML
    private TableColumn<ProductModel, String> table_name;
    @FXML
    private TableColumn<ProductModel, String> table_detail;
    @FXML
    private TableColumn<ProductModel, String> table_price;
    @FXML
    private TableColumn<ProductModel, String> table_unit;
    @FXML
    private TableColumn<ProductModel, String> table_pack;
    @FXML
    private TableColumn<ProductModel, String> table_pic;

    private ProductAddController addController;
    private PicViewer picViewer;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        loadProducts();
        Platform.runLater(() -> {
            addController = ViewPathUtil.loadViewForController("product_add.fxml");
            picViewer = ViewPathUtil.loadViewForController("pic_viewer.fxml");
        });
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("产品列表");
    }

    @Override
    public List<BarBtn> getBarBtns(Properties params) {
        return Arrays.asList(new BarBtn() {
                                 @Override
                                 public String getText() {
                                     return "新增";
                                 }

                                 @Override
                                 public String getHit() {
                                     return null;
                                 }

                                 @Override
                                 public String getImage() {
                                     return "item-add.png";
                                 }

                                 @Override
                                 public Runnable getAction() {
                                     return () -> {
                                         Optional<ProductModel> op = CustomDialog.gen().build(addController);
                                         op.ifPresent(model -> {
                                             try {
                                                 QSApp.service.getProductService().insert(model);
                                                 loadProducts();
                                             } catch (Exception e1) {
                                                 e1.printStackTrace();
                                                 WarningBuilder.build("新增产品失败", "新增客户失败，请检查网络是否通畅");
                                             }
                                         });
                                     };
                                 }
                             },
                new BarBtn() {
                    @Override
                    public String getText() {
                        return "删除";
                    }

                    @Override
                    public String getHit() {
                        return null;
                    }

                    @Override
                    public String getImage() {
                        return "item-del.png";
                    }

                    @Override
                    public Runnable getAction() {
                        return () -> {
                            ProductModel model = prodTable.getSelectionModel().getSelectedItem();
                            if (model == null) {
                                WarningBuilder.build("删除产品失败", "请先选中指定产品后再进行删除！");
                            } else {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setHeaderText("是否要删除这个产品？");
                                alert.setContentText("系统为了账目安全，你必须先删除和该产品相关的交易记录！");
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    try {
                                        QSApp.service.getProductService().delete(model);
                                        loadProducts();
                                    } catch (Exception e) {
                                        WarningBuilder.build("删除产品失败", "请先清空该产品相关的交易记录，再进行删除！");
                                    }
                                }
                            }
                        };
                    }
                });
    }

    @FXML
    public void initialize() throws IOException {

        // 双击编辑产品
        prodTable.setRowFactory(tv -> {
            TableRow<ProductModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    ProductModel rowData = row.getItem();
                    Optional<ProductModel> op = CustomDialog.gen().build(addController, rowData);
                    op.ifPresent(model -> {
                        try {
                            model.setId(rowData.getId());
                            QSApp.service.getProductService().update(model);
                            loadProducts();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            WarningBuilder.build("更新产品信息失败", "更新产品信息失败，请检查网络是否通畅！");
                        }
                    });
                }
            });
            return row;
        });

        table_serial.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getSerial()));
        table_name.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getName()));
        table_price.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPrice().stripTrailingZeros().toString()));
        table_detail.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getDetail()));
        table_unit.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getUnit()));
        table_pack.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPack().toString()));
        table_pic.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPicurl()));

        table_pic.setCellFactory(param -> new TableCell<ProductModel, String>() {
            Button btn = new Button("查看");

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                setText(null);
                if (!empty) {
                    btn.setOnAction((ActionEvent event) -> {
                                CustomDialog.gen().build(picViewer, item);
                            }
                    );
                    HBox hBox = new HBox(btn);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });
    }

    // 加载产品列表
    private void loadProducts() {
        Platform.runLater(() -> {
            try {
                prodTable.getItems().setAll(FXCollections.observableList(QSApp.service.getProductService().selectAll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
