package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.ReceiptModel;
import cn.keepfight.qsmanager.model.ReceiptModelFull;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.qsmanager.print.PrintSelection;
import cn.keepfight.qsmanager.print.PrintSource;
import cn.keepfight.qsmanager.print.QSPrintType;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * 供应商送货列表控制器
 * Created by tom on 2017/9/4.
 */
public class ReceiptListController implements ContentCtrl, Initializable {

    @FXML
    private VBox root;
    // 送货列表部分
    @FXML
    private ChoiceBox<SupplyModel> rec_sup_sel;
    @FXML
    private ChoiceBox<Long> rec_year_sel;
    @FXML
    private ChoiceBox<Long> rec_mon_sel;
    @FXML
    private Button rec_add;
    @FXML
    private Button rec_print_mon;

    @FXML
    private ListView<ReceiptModelFull> receiptFullList;

    private ReceiptAddController receiptAddController = ViewPathUtil.loadViewForController("receipt_add.fxml");

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receiptFullList.setCellFactory(param -> new ListCell<ReceiptModelFull>() {
            @Override
            protected void updateItem(ReceiptModelFull item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ReceiptController controller = ViewPathUtil.loadViewForController("receipt.fxml");
                    controller.fill(item, ReceiptListController.this);
                    setGraphic(controller.getRoot());
                }
            }
        });

        // 设置供应商下拉转换器、年转换器
        rec_sup_sel.setConverter(FXUtils.converter(x -> x.getSerial() + "-" + x.getName(), "全部供应商"));
        rec_year_sel.setConverter(FXUtils.converter(x -> x + "年", "全部年份"));

        rec_mon_sel.setItems(FXCollections.observableList(LongStream.range(1, 13).boxed().collect(Collectors.toList())));
        rec_mon_sel.getItems().add(null);
        rec_mon_sel.setConverter(FXUtils.converter(x -> x + "月", "全部月份"));

        // 打印按钮使能
        rec_print_mon.disableProperty().bind(rec_mon_sel.getSelectionModel().selectedItemProperty().isNotNull()
                .and(rec_sup_sel.getSelectionModel().selectedItemProperty().isNotNull())
                .and(rec_year_sel.getSelectionModel().selectedItemProperty().isNotNull())
                .not()
        );


        rec_add.setOnAction(event -> {
            Optional<ReceiptModelFull> op = CustomDialog.gen().build(receiptAddController);
            op.ifPresent(model -> {
                try {
                    QSApp.service.getReceiptService().insert(model);
                    loadReceipt();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("新增供应送货失败", "新增供应送货失败，请检查网络是否通畅");
                }
            });
        });

        // 监听选择以加载列表、年度对账表表格
        rec_sup_sel.setOnAction(event -> loadReceipt());
        rec_year_sel.setOnAction(event -> loadReceipt());
        rec_mon_sel.setOnAction(event -> loadReceipt());


        // 打印支持
        rec_print_mon.setOnAction(event -> {
            SupplyModel sup = rec_sup_sel.getSelectionModel().getSelectedItem();
            Long sid = sup == null ? null : sup.getId();
            Long year = rec_year_sel.getSelectionModel().getSelectedItem();
            Long month = rec_mon_sel.getSelectionModel().getSelectedItem();

            PrintSource source = new PrintSource();
            source.setSup(sid);
            source.setYear(year);
            source.setMonth(month);
            QSApp.service.getPrintService().build(new PrintSelection(QSPrintType.MON_SUP, source));
        });
    }

    @Override
    public void loaded() {
        loadSelection();
    }

    @Override
    public void showed(Properties params) {

    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("供应列表");
    }


    /**
     * 所谓事件处理器，加载供应送货数据
     */
    private void loadReceipt() {
        if (rec_sup_sel.getSelectionModel().isEmpty() ||
                rec_year_sel.getSelectionModel().isEmpty() ||
                rec_mon_sel.getSelectionModel().isEmpty()) {
            return;
        }
        Long sid = rec_sup_sel.getSelectionModel().getSelectedItem().getId();
        Long year = rec_year_sel.getSelectionModel().getSelectedItem();
        Long mon = rec_mon_sel.getSelectionModel().getSelectedItem();

        Platform.runLater(() -> {
            try {
                receiptFullList.getItems().setAll(FXCollections.observableList(QSApp.service.getReceiptService().selectAll(sid, year, mon)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * 加载供应商和年份下拉选择的选项
     */
    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                List<Long> years = QSApp.service.getReceiptService().selectYear();
                rec_sup_sel.getItems().setAll(QSApp.service.getSupplyService().selectAll());
                rec_year_sel.getItems().setAll(years);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 从供应送货列表中删除指定的对象
     */
    void deleteSelected(ReceiptModelFull modelFull) {
        Platform.runLater(() -> {
            try {
                QSApp.service.getReceiptService().delete(new ReceiptModel(modelFull));
                receiptFullList.getItems().remove(modelFull);
            } catch (Exception e) {
                WarningBuilder.build("删除失败，请检查网络是否通畅！");
            }
        });
    }

    void updateReceipt(ReceiptModelFull modelFull) {
        Optional<ReceiptModelFull> op = CustomDialog.gen().build(receiptAddController, modelFull);
        op.ifPresent(model -> {
            try {
                model.setId(modelFull.getId());
                QSApp.service.getReceiptService().update(model);
                loadReceipt();
            } catch (Exception e1) {
                e1.printStackTrace();
                WarningBuilder.build("新增供应送货失败", "新增供应送货失败，请检查网络是否通畅");
            }
        });
    }
}
