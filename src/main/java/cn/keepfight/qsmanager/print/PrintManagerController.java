package cn.keepfight.qsmanager.print;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.ContentCtrl;
import cn.keepfight.qsmanager.controller.ProductAddController;
import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterAttributes;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 打印界面控制器
 * <p>
 * 打印件类型有：
 * <li>
 * <ol>送货单</ol>
 * <ol>收据单</ol>
 * <ol>安利专用送货单</ol>
 * <ol>客户月对账单</ol>
 * <ol>客户年度对账单</ol>
 * <ol>供应商月对账单</ol>
 * <ol>供应商年度对账单</ol>
 * </li>
 * <p>
 * Created by tom on 2017/6/23.
 */
public class PrintManagerController implements ContentCtrl, Initializable {
    @FXML
    private VBox root;

    @FXML
    private ChoiceBox<Printer> print_sel;
    @FXML
    private Label printHit;

    @FXML
    private ChoiceBox<QSPrintType> type_sel;
    @FXML
    private Button action;
    @FXML
    private Button compute;
    public Button save;
    public Button add_product;
    @FXML
    private ScrollPane printScrollPane;

    private ProductAddController addController;

    /**
     * 打印中标志位
     */
    private static AtomicBoolean busy = new AtomicBoolean(false);
    private static BooleanProperty printing = new SimpleBooleanProperty(false);

    private ObjectProperty<PrintTemplate> printController = new SimpleObjectProperty<>();

    private PrintSelection selection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 初始化打印类型下拉列表
        initPrintTypeList();
        initPrinterList();

        // 打印按钮使能绑定
        action.disableProperty().bind(printScrollPane.contentProperty().isNull()
                .or(print_sel.getSelectionModel().selectedItemProperty().isNull())
                .or(type_sel.getSelectionModel().selectedItemProperty().isNull())
                .or(printHit.textProperty().isNotEqualTo(""))
                .or(printing));
        save.disableProperty().bind(action.disableProperty());

        // 打印动作
        action.setOnAction(event -> printAction());
        save.setOnAction(event -> saveActoion());

        add_product.setDisable(true);
        add_product.setOnAction(event -> {
            if (selection == null) {
                return;
            }
            Long cid = selection.getPrintSource().getCust();
            if (cid == null) {
                return;
            }
            Optional<ProductModel> op = CustomDialog.gen().build(addController);
            op.ifPresent(model -> {
                try {
                    QSApp.service.getProductService().insert(model);
                    QSApp.service.getOrderFavorService().insert(cid, model.getId());
                    printController.get().reloadFavorList();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("新增产品失败", "新增产品失败，请检查网络是否通畅");
                }
            });
        });

        Platform.runLater(() -> addController = ViewPathUtil.loadViewForController("product_add.fxml"));
    }

    public void fill(PrintSelection s) {
        this.selection = s;
        add_product.setDisable(true);
        switch (s.getType()) {
            case DELIVERY:
            case DELIVERY_SIMPLE:
            case RECEIPT:
            case DELIVERY_ANLI:
                add_product.setDisable(false);
                type_sel.getItems().setAll(Arrays.asList(
                        QSPrintType.DELIVERY,
                        QSPrintType.DELIVERY_SIMPLE,
                        QSPrintType.RECEIPT,
                        QSPrintType.DELIVERY_ANLI));
                break;
            case MON_CUST_RATE:
            case MON_CUST:
                type_sel.getItems().setAll(Arrays.asList(QSPrintType.MON_CUST_RATE, QSPrintType.MON_CUST));
                break;
            case MON_SUP:
                type_sel.getItems().setAll(Collections.singletonList(QSPrintType.MON_SUP));
                break;
            case YEAR_SUP:
                type_sel.getItems().setAll(Collections.singletonList(QSPrintType.YEAR_SUP));
                break;
            case YEAR_CUST:
                type_sel.getItems().setAll(Collections.singletonList(QSPrintType.YEAR_CUST));
                break;
        }
        type_sel.getSelectionModel().select(s.getType());
        loadPrintPane();
    }

    /**
     * 初始化打印机选择列表
     */
    private void initPrinterList() {
        print_sel.disableProperty().bind(printing);
        print_sel.setConverter(FXUtils.converter(Printer::getName));
        print_sel.getItems().setAll(Printer.getAllPrinters());
        Printer defaultPrinter = Printer.getDefaultPrinter();
        if (defaultPrinter != null) {
            print_sel.getSelectionModel().select(defaultPrinter);
        }
        print_sel.setOnAction(event -> checkSupport());
    }

    /**
     * 初始化打印类型列表
     */
    private void initPrintTypeList() {
        // 必须是打印机已选才可以有打印类型选择
        type_sel.disableProperty().bind(
                print_sel.getSelectionModel().selectedItemProperty().isNull()
                        .or(print_sel.disabledProperty())
                        .or(printing));

        type_sel.setConverter(FXUtils.converter(QSPrintType::getDesc));
        type_sel.getItems().setAll(QSPrintType.values());
        type_sel.setOnAction(event -> {
            if (checkSupport()) {
                // 非空时设置模板
                loadPrintPane();
            } else {
                printController.set(null);
                // 上下页使能
                compute.disableProperty().unbind();
                compute.setOnAction(null);
            }
        });
        compute.disableProperty().unbind();
    }

    private void saveActoion() {
        QSPrintType type = type_sel.getValue();
        type.getController(PrintTemplate::printBefore);
        type.getController(PrintTemplate::printAfter);
    }

    private void printAction() {
        // 防多次重入
        if (busy.getAndSet(true)) {
            return;
        }
        printing.set(true);
        action.setText("打印中");

        // 获得打印数据与参数
        QSPrintType type = type_sel.getValue();
        Printer printer = print_sel.getValue();
        Node rootNode = printController.get().getRoot();

        // 打印前调用
        type.getController(PrintTemplate::printBefore);

        // 转换为打印截图样式
        FXUtils.addStyle("snap", rootNode);
        PageLayout pageLayout = printer.createPageLayout(
                type.getPaper(),
                type.getOrientation(),
                type.getMarginReqire(), type.getMarginReqire(), type.getMarginReqire(), type.getMarginReqire());

        new Thread(() -> {
            try {
                // 使用新线程以保证可以延迟一段时间进行打印，这样节点就有时间可以正确渲染了
                Thread.sleep(1000);
                // 使用Platfor.runLater 是为了保证在打印完成前，不会先去除样式
                Platform.runLater(() -> {
                    try {
//                        FXWidgetUtil.printNode(rootNode, printer, pageLayout);
                        FXWidgetUtil.printNodeNew(rootNode, printer, pageLayout, type.getFolder());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(() -> FXUtils.delStyle("snap", rootNode));
                        printing.set(false);
                        busy.set(false);
                        action.setText("执行打印");

                        // 打印后调用
                        type.getController(PrintTemplate::printAfter);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    /**
     * 检查打印机和所需打印件类型的支持情况，如果不支持那么就该给予提示
     *
     * @return 返回 true 表示全部支持
     */
    private boolean checkSupport() {
        // 初始化为空字符串
        printHit.setText("");
        Printer printer = print_sel.getSelectionModel().getSelectedItem();
        if (printer == null) {
            printHit.setText("请选择打印机！");
            return false;
        }
        QSPrintType printType = type_sel.getSelectionModel().getSelectedItem();
        if (printType == null) {
            printHit.setText("请选择打印件类型");
            return false;
        }
        PrinterAttributes att = printer.getPrinterAttributes();
        if (!att.getSupportedPapers().contains(printType.getPaper())) {
            printHit.setText("该打印机不支持所选打印件类型的纸大小：" + printType.getPaper());
            return false;
        }
        if (!att.getSupportedPageOrientations().contains(printType.getOrientation())) {
            printHit.setText("该打印机不支持所选打印件类型的纸朝向：" + printType.getOrientation());
            return false;
        }

        PageLayout pageLayout = printer.createPageLayout(printType.getPaper(),
                printType.getOrientation(),
                Printer.MarginType.HARDWARE_MINIMUM);
        double maxMargin = Math.max(
                Math.max(pageLayout.getTopMargin(), pageLayout.getBottomMargin()),
                Math.max(pageLayout.getRightMargin(), pageLayout.getLeftMargin()));
        if (maxMargin > printType.getMarginReqire()) {
            printHit.setText("该打印机的最小支持边距(" + maxMargin + ")大于所需边距(" + printType.getMarginReqire() + ")");
            return false;
        }
        return true;
    }

    private void loadPrintPane() {
        // 非空时设置模板
        type_sel.getSelectionModel().getSelectedItem().getController(c -> {
            printScrollPane.setContent(c.getRoot());
            printController.set(c);
            compute.disableProperty().bind(c.autoComputable().not());
            compute.setOnAction(e -> c.autoCalculate());
        });
        Platform.runLater(() -> type_sel.getSelectionModel().getSelectedItem().getController(c -> {
            try {
                c.fill(selection.getType().getLocate().query(selection.getPrintSource()));
                // 自动计算
                if (c.autoComputable().get()) {
                    c.autoCalculate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {
    }

    @Override
    public void showed(Properties params) {
        // 每次进来都初始化一次打印机列表，以更新
        print_sel.getItems().setAll(Printer.getAllPrinters());
        Printer defaultPrinter = Printer.getDefaultPrinter();
        if (defaultPrinter != null) {
            print_sel.getSelectionModel().select(defaultPrinter);
        }
        // 默认检查一次打印支持机制
        checkSupport();

        PrintSelection s = (PrintSelection) params.get("selection");
        System.out.println("s:" + s.getPrintSource().getCust());
        fill(s);
    }

    @Override
    public StringBinding getTitle() {
        return FXWidgetUtil.sBinding("页面打印");
    }

    @Override
    public boolean hide() {
        printController.get().cancel();
        return true;
    }
}
