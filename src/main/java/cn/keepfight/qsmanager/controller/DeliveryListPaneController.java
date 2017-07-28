package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.DeliveryModelFull;
import cn.keepfight.qsmanager.model.DeliverySelection;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 送货列表面板控制器
 * Created by tom on 2017/7/23.
 */
public class DeliveryListPaneController implements ContentCtrl,Initializable{
    @FXML
    private VBox root;
    @FXML
    private ChoiceBox<CustomModel> cust_sel;
    @FXML
    private ChoiceBox<Long> year_sel;
    @FXML
    private ChoiceBox<Long> mon_sel;
    @FXML
    private TextField order_sel;
    @FXML
    private Button load;
    @FXML
    private Button print;
    @FXML
    private ListView<DeliveryModelFull> deliveryList;

    private DeliveryAddController deliveryAddController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 设置客户下拉转换器、年转换器
        cust_sel.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "全部客户"));
        year_sel.setConverter(FXUtils.converter(x -> x + "年", "全部年份"));
        mon_sel.setConverter(FXUtils.converter(x -> x + "月", "全部月份"));
        mon_sel.setItems(FXCollections.observableList(LongStream.range(1, 13).boxed().collect(Collectors.toList())));
        mon_sel.getItems().add(null);

        FXUtils.limitNum(order_sel, 10, 0, false, null);
        load.setOnAction(e -> loadDeliverys());

        // 设置列表单元构造器
        deliveryList.setCellFactory(x -> new DeliveryListPaneController.DeliveryListCell(DeliveryListPaneController.this));

        print.disableProperty().bind(
                cust_sel.getSelectionModel().selectedItemProperty().isNotNull()
                        .and(year_sel.getSelectionModel().selectedItemProperty().isNotNull())
                        .and(mon_sel.getSelectionModel().selectedItemProperty().isNotNull())
                        .not()
        );

        // 打印支持
        print.setOnAction(event -> {
            PrintSource source = new PrintSource();
            CustomModel selCust = cust_sel.getSelectionModel().getSelectedItem();
            Long cid = selCust == null ? null : selCust.getId();
            Long year = year_sel.getSelectionModel().getSelectedItem();
            Long month = mon_sel.getSelectionModel().getSelectedItem();
            source.setCust(cid);
            source.setYear(year);
            source.setMonth(month);
            QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.MON_CUST, source));
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
        // 加载选择列表
        loadSelection();
        try {
            deliveryAddController = ViewPathUtil.loadViewForController("delivery_add.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showed() {

    }

    public void noPrintMon(){
        FXUtils.addStyle("hide", print);
    }

    /**
     * 搜索指定订单号的全部送货记录
     */
    public void listDelivery(String s){
//        tab_pane.getSelectionModel().selectFirst();

        cust_sel.getSelectionModel().clearSelection();
        year_sel.getSelectionModel().clearSelection();
        mon_sel.getSelectionModel().clearSelection();

        order_sel.setText(s);

        loadDeliverys();
    }

    void updateDelivery(DeliveryModelFull modelFull) {
        Optional<DeliveryModelFull> op = CustomDialog.gen().build(deliveryAddController, modelFull);
        op.ifPresent(model -> {
            try {
                model.setId(modelFull.getId());
                QSApp.service.getDeliveryService().update(model);
                loadDeliverys();
            } catch (Exception e1) {
                e1.printStackTrace();
                WarningBuilder.build("修改送货单失败", "修改送货单失败，请检查网络是否通畅");
            }
        });
    }


    /**
     * 从送货列表中删除指定的对象
     */
    void deleteSelected(DeliveryModelFull modelFull) {
        Platform.runLater(() -> {
            try {
                QSApp.service.getDeliveryService().delete(modelFull.get());
                deliveryList.getItems().remove(modelFull);
            } catch (Exception e) {
                WarningBuilder.build("删除失败，请检查网络是否通畅！");
            }
        });
    }

    /**
     * 所谓事件处理器，加载对账表
     */
    private void loadDeliverys() {
        CustomModel selCust = cust_sel.getSelectionModel().getSelectedItem();
        Long cid = selCust == null ? null : selCust.getId();
        Long year = year_sel.getSelectionModel().getSelectedItem();
        Long month = mon_sel.getSelectionModel().getSelectedItem();
        String orderSerial = (order_sel.getText() == null) || order_sel.getText().trim().equals("") ?
                null : order_sel.getText();

        Platform.runLater(() -> {
            try {
                DeliverySelection s = new DeliverySelection(cid, year, month, orderSerial);
                List<DeliveryModelFull> ls = QSApp.service.getDeliveryService().selectAll(s);
                deliveryList.getItems().setAll(ls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }



    // 加载下拉列表
    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                List<CustomModel> custs = QSApp.service.getCustomService().selectAll();
                List<Long> years = QSApp.service.getOrderService().selectYear();

                custs.add(null);
                cust_sel.getItems().setAll(FXCollections.observableList(custs));
                years.add(null);
                year_sel.getItems().setAll(years);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 发货列表条目
     */
    private static class DeliveryListCell extends ListCell<DeliveryModelFull> {
        DeliveryItemController controller;
        DeliveryListPaneController paneController;

        DeliveryListCell(DeliveryListPaneController paneController) {
            this.paneController = paneController;
            try {
                controller = ViewPathUtil.loadViewForController("delivery.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void updateItem(DeliveryModelFull item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                setGraphic(controller.getRoot());
                controller.fill(item, paneController);
            }
        }
    }
}
