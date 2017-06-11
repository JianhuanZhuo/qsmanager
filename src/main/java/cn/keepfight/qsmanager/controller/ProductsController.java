package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

/**
 * 产品管理界面控制器
 * Created by tom on 2017/6/6.
 */
public class ProductsController implements ContentController {
    @FXML
    private VBox root;
    @FXML
    private TableView<ProductModel> prodTable;
    @FXML
    private Button del;
    @FXML
    private Button add;

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
    private PicMakerController picMakerController;

    private Dialog dialog;

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void refresh() {
        loadProducts();

        // 把初始化放在这里有点奇怪，但这个初始化需要加载后的世界
        initDialog();
    }

    @FXML
    public void initialize() throws IOException {
        Platform.runLater(() -> {
            try {
                addController = ViewPathUtil.loadViewForController("product_add.fxml");
                picMakerController = ViewPathUtil.loadViewForController("pic_viewer.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        add.setOnAction(event -> {
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
        });

        // 删除客户按钮
        del.setOnMouseClicked(event -> {
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
        });

        table_serial.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getSerial()));
        table_name.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getName()));
        table_price.setCellValueFactory(cellFeature ->
                new SimpleStringProperty(cellFeature.getValue().getPrice().toString()));
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
                                picMakerController.setImage(item);
                                dialog.showAndWait();
                            }
                    );
                    setGraphic(btn);
                }
            }
        });

        loadProducts();
    }


    // 加载客户列表
    private void loadProducts() {
        Platform.runLater(() -> {
            try {
                prodTable.getItems().setAll(FXCollections.observableList(QSApp.service.getProductService().selectAll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initDialog() {
        if (dialog != null) {
            return;
        }
        System.out.println("ok");
        dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        dialog.getDialogPane().setContent(picMakerController.getRoot());
    }
}
