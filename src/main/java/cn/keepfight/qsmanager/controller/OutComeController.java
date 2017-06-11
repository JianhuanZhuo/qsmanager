package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.CustomModel;
import cn.keepfight.qsmanager.model.ReceiptModelFull;
import cn.keepfight.qsmanager.model.SupplyModel;
import cn.keepfight.utils.CustomDialog;
import cn.keepfight.utils.ViewPathUtil;
import cn.keepfight.utils.WarningBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import javax.swing.text.View;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * 支出管理控制器
 * Created by tom on 2017/6/6.
 */
public class OutComeController implements ContentController {

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
    private VBox receiptList;

    @FXML
    private ListView<ReceiptModelFull> receiptFullList;

    // 年度对账表部分
    @FXML
    private ChoiceBox an_sup_sel;
    @FXML
    private ChoiceBox an_year_sel;
    @FXML
    private Button an_print;
    @FXML
    private TableView anTable;
    @FXML
    private Label an_count_bf;
    @FXML
    private Label an_total_annu;
    @FXML
    private Label an_total_rate;
    @FXML
    private Label an_total_pay;
    @FXML
    private Label an_total_actu;
    @FXML
    private Label an_attach;

    // 子控制器
    @FXML
    private VBox take;
    @FXML
    private OutComeTakeController takeController;
    @FXML
    private VBox pay;
    @FXML
    private OutComePayController payController;
    @FXML
    private VBox fac;
    @FXML
    private OutComeFacController facController;
    @FXML
    private HBox stat;
    @FXML
    private OutComeStatController statController;

    // 子界面
    private ReceiptAddController addController;

    @FXML
    public void initialize() {
        initUI();
        initAction();
        loadSelection();
    }

    private void initUI() {
        Platform.runLater(() -> {
            try {
                addController = ViewPathUtil.loadViewForController("receipt_add.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        receiptFullList.setCellFactory(new Callback<ListView<ReceiptModelFull>, ListCell<ReceiptModelFull>>() {
            @Override
            public ListCell<ReceiptModelFull> call(ListView<ReceiptModelFull> param) {
                return new ListCell<ReceiptModelFull>() {
                    @Override
                    protected void updateItem(ReceiptModelFull item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty||item==null){
                            setGraphic(null);
                        }else {
                            try {
                                ReceiptController controller = ViewPathUtil.loadViewForController("receipt.fxml");
                                controller.fill(item);
                                setGraphic(controller.getRoot());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
            }
        });

        rec_sup_sel.setConverter(new StringConverter<SupplyModel>() {
            @Override
            public String toString(SupplyModel object) {
                return object.getSerial()+"-"+object.getName();
            }

            @Override
            public SupplyModel fromString(String string) {
                return null;
            }
        });

        rec_year_sel.setConverter(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                return object+" 年";
            }

            @Override
            public Long fromString(String string) {
                return null;
            }
        });

        List<Long> range = LongStream.range(1, 13).boxed().collect(Collectors.toList());
        rec_mon_sel.setItems(FXCollections.observableList(range));
        rec_mon_sel.getItems().add(null);
        rec_mon_sel.setConverter(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                if (object==null){
                    return "全年";
                }
                return object+" 月";
            }

            @Override
            public Long fromString(String string) {
                return null;
            }
        });
    }

    private void initAction() {
        rec_add.setOnAction(event -> {
            Optional<ReceiptModelFull> op = CustomDialog.gen().build(addController);
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

        rec_sup_sel.setOnAction(event -> loadReceipt());
        rec_year_sel.setOnAction(event -> loadReceipt());
        rec_mon_sel.setOnAction(event -> loadReceipt());
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void refresh() {

    }


    private void loadSelection() {
        Platform.runLater(() -> {
            try {
                rec_sup_sel.getItems().setAll(QSApp.service.getSupplyService().selectAll());
                rec_year_sel.getItems().setAll(QSApp.service.getReceiptService().selectYear());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

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
                ObservableList<ReceiptModelFull> receipts = FXCollections.observableList(QSApp.service.getReceiptService().selectAll(sid, year, mon));
                receiptFullList.getItems().setAll(receipts);
                receipts.forEach(r->{
                    System.out.println(r.getRdate());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
