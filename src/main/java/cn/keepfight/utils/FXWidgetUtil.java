package cn.keepfight.utils;


import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.controller.MainPaneList;
import cn.keepfight.widget.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberExpressionBase;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PrintQuality;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * 组件强化功能
 * Created by tom on 2017/6/16.
 */
public class FXWidgetUtil {

    /**
     * 属性存储的默认属性文件
     */
    private static final String PROPERTIES_FILE = "fxapp.properties";

    /**
     * 属性数组存储的分割符
     */
    private static final String SPLITER = "~";

    /**
     * 最大属性数组存储尺寸
     */
    private static final int STORE_LIMIT = 5;

    /**
     * 为指定文本框添加默认下拉填充字符串，该功能是基于上下文菜单实现的
     *
     * @param text 需要填充默认字符串的文本框
     * @param list 默认字符串列表
     */
    public static void defaultList(TextField text, List<String> list) {
        if (list == null || text == null || list.isEmpty()) {
            return;
        }

        // 创建上下文菜单
        ContextMenu popup = new ContextMenu();
        for (String s : list) {
            MenuItem item = new MenuItem(s);
            popup.getItems().add(item);
            item.setOnAction(event -> text.setText(s));
        }

        // 监听
        text.setOnMouseClicked(t -> {
            TextField txt = (TextField) t.getSource();
            popup.show(txt, t.getScreenX(), t.getScreenY());
        });
    }

    /**
     * 为指定的文本框匹配相应的默认下拉输入
     *
     * @param textFieldStringPair 指定文本框和与之匹配的下拉属性的数组
     */
    @SafeVarargs
    public static void defaultList(Pair<TextField, String>... textFieldStringPair) {
        Properties ps = ConfigUtil.load(PROPERTIES_FILE);
        for (Pair<TextField, String> pair : textFieldStringPair) {
            try {
                defaultList(pair.getKey(), Arrays.asList(((String) ps.get(pair.getValue())).split(SPLITER)));
            } catch (Exception e) {
                // to do nothing
            }
        }
    }

    public static MonthPicker getMonthPicker() {
        return ViewPathUtil.loadWidgetForController("monthPicker/month_picker.fxml");
    }

    public static YearScrollPicker getYearPicker() {
        return ViewPathUtil.loadWidgetForController("yearPicker/year_picker.fxml");
    }

    public static <T> UnitScrollPicker<T> getUnitPicker() {
        return ViewPathUtil.loadWidgetForController("unitPicker/unit_picker.fxml");
    }

    public static AddSalaryItem getSalaryNewItem() {
        return ViewPathUtil.loadWidgetForController("add_salary/add_salary.fxml");
    }

    public static PredictItem getPredictItem() {
        return ViewPathUtil.loadWidgetForController("predict/predict_item.fxml");
    }

    /**
     * 为指定属性名的数组添加一个属性值，该新添加值将被插在头，且因数组的长度限制而舍弃最旧的值
     *
     * @param pair 数组属性名和新增属性值的 Pair 对数组
     */
    @SafeVarargs
    public static void addDefaultList(Pair<String, String>... pair) {
        new Thread(() -> {
            Properties ps = ConfigUtil.load(PROPERTIES_FILE);
            for (Pair<String, String> p : pair) {
                if (p.getValue() == null || p.getValue().trim().equals("")) {
                    continue;
                }

                List<String> ls = new ArrayList<>(7);
                try {
                    ls.addAll(Arrays.asList(((String) ps.get(p.getKey())).split(SPLITER)));
                } catch (Exception e) {
                    // to do nothing
                }
                ls.add(0, p.getValue());
                ps.setProperty(p.getKey(), ls.stream()
                        .distinct()
                        .limit(STORE_LIMIT)
                        .collect(Collectors.joining(SPLITER)));
            }
            ConfigUtil.store(PROPERTIES_FILE, ps);
        }).start();
    }

    public static <T> void compute(List<T> list, Function<T, BigDecimal> toDecimal,
                                   Consumer<String> consumer) {
        compute(list, toDecimal, consumer, BigDecimal::add);
    }

    public static <T> void compute(List<T> list, Function<T, BigDecimal> toDecimal,
                                   Consumer<String> consumer, BinaryOperator<BigDecimal> accumulator) {
        Optional<BigDecimal> t = list.stream()
                .map((item) -> {
                    try {
                        return toDecimal.apply(item);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .reduce(accumulator);
        if (t.isPresent()) {
            Platform.runLater(() -> consumer.accept(FXUtils.deciToMoney(t.get())));
        } else {
            Platform.runLater(() -> consumer.accept("0"));
        }
    }

    /**
     * 对可变数组进行统计监听
     *
     * @param obserList   需要监听的可变数组
     * @param toDecimal   对可变数组的某一元素转化为数字特征
     * @param consumer    统计结果的消费器
     * @param accumulator 对于数组的所有数字特征进行合计的方法
     * @param <T>         可变数组的泛化类型
     */
    public static <T> void calculate(ObservableList<T> obserList, Function<T, BigDecimal> toDecimal,
                                     Consumer<String> consumer, BinaryOperator<BigDecimal> accumulator) {
        // 默认值
        consumer.accept("0");

        // 添加监听
        obserList.addListener((ListChangeListener<T>) c -> {
                    compute(obserList, toDecimal, consumer, accumulator);
                }
        );
    }

    /**
     * 对可变数组进行统计监听，该重载方法使用了加法作为默认合计方法
     *
     * @param obserList 需要监听的可变数组
     * @param toDecimal 对可变数组的某一元素转化为数字特征
     * @param consumer  统计结果的消费器
     * @param <T>       可变数组的泛化类型
     */
    public static <T> void calculate(ObservableList<T> obserList, Function<T, BigDecimal> toDecimal,
                                     Consumer<String> consumer) {
        calculate(obserList, toDecimal, consumer, BigDecimal::add);
    }

    public static <T> void calculateMoney(ObservableList<T> obserList, Function<T, BigDecimal> toDecimal,
                                          Consumer<String> consumer) {
        calculate(obserList, toDecimal, consumer, BigDecimal::add);
    }

    public static <T> void calculateListForStr(StringProperty sp, List<T> obserList, Function<T, Observable> toObserable, Function<T, BigDecimal> toDecimal) {
        sp.bind(new StringBinding() {
            {
                obserList.forEach(item -> bind(toObserable.apply(item)));
            }

            @Override
            protected String computeValue() {
                BigDecimal x = new BigDecimal(0);
                for (T item : obserList) {
                    x = x.add(toDecimal.apply(item));
                }
                return FXUtils.deciToMoney(x);
            }
        });
    }


    /**
     * 破解提示工具 Tooltip，修改其触发时间为 200 毫秒
     * 参考：https://stackoverflow.com/questions/26854301/how-to-control-the-javafx-tooltips-delay
     *
     * @param tooltip 需要被破解的提示工具
     */
    public static void hackTooltipStartTiming(Tooltip tooltip) {
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(200)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 target=sa*sb 进行绑定
     */
    public static void simpleBiMultiply(TextField target, TextField sa, TextField sb) {
        simpleBiOper(target, BigDecimal::multiply, sa, sb);
    }

    /**
     * 将 target=sa*sb 进行绑定
     */
    public static void rateBiMultiply(TextField target, TextField total, TextField rate) {
        simpleBiOper(target, BigDecimal::multiply,
                total, x -> new BigDecimal(x.replace(",", "")),
                rate, x -> new BigDecimal(x.replace(",", "").replace("%", ""))
                        .movePointLeft(x.contains("%") ? 2 : 0));
    }

    /**
     * 将 target=sa+sb 进行绑定
     */
    public static void simpleBiAdd(TextField target, TextField sa, TextField sb) {
        simpleBiOper(target, BigDecimal::add, sa, sb);
    }

    /**
     * 将 target=sa+sb 进行绑定
     */
    public static void simpleBiSub(TextField target, TextField sa, TextField sb) {
        simpleBiOper(target, BigDecimal::subtract, sa, sb);
    }


    /**
     * 将 target=sa oper sb 进行绑定
     */
    public static void simpleBiOper(TextField target, BiFunction<BigDecimal, BigDecimal, BigDecimal> oper, TextField sa, TextField sb) {
        simpleBiOper(target, oper,
                sa, x -> new BigDecimal(x.replace(",", "")),
                sb, x -> new BigDecimal(x.replace(",", "")));
    }

    /**
     * 将 target=sa oper sb 进行绑定
     * 这里对 sa 和 sb 的抽取方法进行了
     */
    public static void simpleBiOper(TextField target, BiFunction<BigDecimal, BigDecimal, BigDecimal> oper,
                                    TextField sa, Function<String, BigDecimal> getSaDecimal,
                                    TextField sb, Function<String, BigDecimal> getSbDecimal) {
        target.textProperty().bind(new StringBinding() {
            {
                bind(sa.textProperty(), sb.textProperty());
            }

            @Override
            protected String computeValue() {
                try {
                    return oper.apply(
                            getSaDecimal.apply(sa.getText()),
                            getSbDecimal.apply(sb.getText())
                    ).stripTrailingZeros().toPlainString();
                } catch (Exception e) {
                    return "0";
                }
            }
        });
    }

    /**
     * 将 target=sa oper1 sb oper2 sc 进行绑定
     */
    public static void simpleTriOper(TextField target,
                                     BiFunction<BigDecimal, BigDecimal, BigDecimal> oper1,
                                     BiFunction<BigDecimal, BigDecimal, BigDecimal> oper2,
                                     TextField sa, Function<TextField, BigDecimal> getSaDecimal,
                                     TextField sb, Function<TextField, BigDecimal> getSbDecimal,
                                     TextField sc, Function<TextField, BigDecimal> getScDecimal,
                                     Function<BigDecimal, String> setDecimal) {
        target.textProperty().bind(new StringBinding() {
            {
                bind(sa.textProperty(), sb.textProperty(), sc.textProperty());
            }

            @Override
            protected String computeValue() {
                try {
                    return setDecimal.apply(oper2.apply(
                            oper1.apply(getSaDecimal.apply(sa), getSbDecimal.apply(sb)),
                            getScDecimal.apply(sc)
                    ));
                } catch (Exception e) {
                    return "0";
                }
            }
        });
    }

    /**
     * 将 target=sa oper1 sb oper2 sc 进行绑定
     */
    public static void simpleTriOper(TextField target,
                                     BiFunction<BigDecimal, BigDecimal, BigDecimal> oper1,
                                     BiFunction<BigDecimal, BigDecimal, BigDecimal> oper2,
                                     TextField sa, TextField sb, TextField sc) {
        simpleTriOper(target, oper1, oper2,
                sa, x -> new BigDecimal(x.getText().replace(",", "")),
                sb, x -> new BigDecimal(x.getText().replace(",", "")),
                sc, x -> new BigDecimal(x.getText().replace(",", "")),
                FXUtils::deciToMoney
        );
    }

    /**
     * 将 target=sa oper1 sb oper2 sc 进行绑定
     */
    public static void simpleTriOper(TextField target,
                                     BiFunction<BigDecimal, BigDecimal, BigDecimal> oper1,
                                     BiFunction<BigDecimal, BigDecimal, BigDecimal> oper2,
                                     TextField sa, TextField sb, TextField sc,
                                     Function<BigDecimal, String> setDecimal) {
        simpleTriOper(target, oper1, oper2,
                sa, x -> new BigDecimal(x.getText().replace(",", "")),
                sb, x -> new BigDecimal(x.getText().replace(",", "")),
                sc, x -> new BigDecimal(x.getText().replace(",", "")),
                setDecimal
        );
    }


    private static <T> void connectHelper(TableColumn<T, String> tab_col, Function<T, String> getStr, String d) {
        tab_col.setCellValueFactory(param -> {
            String s;
            try {
                s = getStr.apply(param.getValue());
            } catch (Exception e) {
                s = d;
            }
            return new SimpleStringProperty(s);
        });
    }

    private static <T> void connectHelper(TableColumn<T, String> tab_col, ObservableValue<String> s) {
        tab_col.setCellValueFactory(param -> s);
    }

    /**
     * 以指定的转换方式连接表格列
     */
    public static <T> void connect(TableColumn<T, String> tab_col, Function<T, ObservableValue<String>> x) {
        tab_col.setCellValueFactory(param -> x.apply(param.getValue()));
        connectHelper(tab_col, p -> x.apply(p).getValue(), "");
    }

    /**
     * 以指定的转换方式连接表格列
     */
    public static <T> void connectNum(TableColumn<T, String> tab_col, Function<T, NumberExpressionBase> x) {
        connectHelper(tab_col, p -> x.apply(p).asString().get(), "0");
    }

    /**
     * 以字符串的转换方式连接表格列
     */
    public static <T> void connectStrColumn(TableColumn<T, String> tab_col, Function<T, StringProperty> x) {
        tab_col.setCellValueFactory(data -> x.apply(data.getValue()));
    }

    /**
     * 以字符串的转换方式连接表格列
     */
    public static <T> void connectTimestampColumn(TableColumn<T, Timestamp> tab_col, Function<T, ObjectProperty<Timestamp>> x) {
        tab_col.setCellValueFactory(data -> x.apply(data.getValue()));
    }

    /**
     * 以指定的转换方式连接表格列
     */
    public static <T> void connectObj(TableColumn<T, String> tab_col, Function<T, ObjectExpression> x) {
        connectHelper(tab_col, p -> x.apply(p).asString().get(), "");
    }

    /**
     * 以指定的转换方式连接表格列
     */
    public static <T> void connectDecimal(TableColumn<T, String> tab_col, Function<T, BigDecimal> x) {
        connectHelper(tab_col, p -> x.apply(p).stripTrailingZeros().toPlainString(), "0");
    }

    /**
     * 以指定的转换方式连接表格列
     */
    public static <T> void connectDecimalObj(TableColumn<T, String> tab_col, Function<T, ObjectProperty<BigDecimal>> x) {
        tab_col.setCellValueFactory(param -> new SimpleStringProperty(x.apply(param.getValue()).get().stripTrailingZeros().toPlainString()));
    }

    /**
     * 以指定的转换方式连接表格列
     */
    public static <T> void connectDecimalColumn(TableColumn<T, BigDecimal> tab_col, Function<T, ObjectProperty<BigDecimal>> x) {
        tab_col.setCellValueFactory(param -> x.apply(param.getValue()));
    }


    /**
     * 双击编辑
     *
     * @param tab           指定表格视图
     * @param getController 编辑时所需要用的视图控制器
     * @param resHandler    编辑完成返回后使用的消费器，一般为更新状态
     * @param runBefore     在进行编辑界面前需要运行的操作，一般为设置属性等
     * @param <T>           运行过程中所使用的核心数据结构类型
     */
    public static <T> void doubleToEdit(TableView<T> tab,
                                        Supplier<DialogContent<T>> getController,
                                        BiConsumer<T, T> resHandler,
                                        Runnable runBefore) {
        tab.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    T rowData = row.getItem();
                    runBefore.run();
                    Optional<T> op = CustomDialog.gen().build(getController.get(), rowData);
                    op.ifPresent(model -> resHandler.accept(rowData, model));
                }
            });
            return row;
        });
    }


    /**
     * 双击编辑，重载方法，编辑前使用默认行为
     *
     * @param tab           指定表格视图
     * @param getcontroller 编辑时所需要用的视图控制器
     * @param resHandler    编辑完成返回后使用的消费器，一般为更新状态
     * @param <T>           运行过程中所使用的核心数据结构类型
     */
    public static <T> void doubleToEdit(TableView<T> tab,
                                        Supplier<DialogContent<T>> getcontroller,
                                        BiConsumer<T, T> resHandler) {
        doubleToEdit(tab, getcontroller, resHandler, () -> {
        });
    }


    public static boolean printNode(Node node, Printer printer, PageLayout pageLayout) throws Exception {
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        System.out.println("node.getBoundsInParent().getHeight():" + node.getBoundsInParent().getHeight());
        System.out.println("node.getBoundsInParent().getWidth():" + node.getBoundsInParent().getWidth());

        Thread.sleep(100);

        double scaleX
                = (pageLayout.getPrintableWidth() - 10) / node.getBoundsInParent().getWidth();
        double scaleY
                = (pageLayout.getPrintableHeight() - 10) / node.getBoundsInParent().getHeight();

        int loop = 0;
        while (loop++ < 10 && (Math.abs(scaleX - 1.0) > 0.2 || Math.abs(scaleY - 1.0) > 0.2)) {
            System.err.println("need to sleep 1s to wait style rerencering!");
            Thread.sleep(1000);
            scaleX = (pageLayout.getPrintableWidth() - 10) / node.getBoundsInParent().getWidth();
            scaleY = (pageLayout.getPrintableHeight() - 10) / node.getBoundsInParent().getHeight();
            System.out.println("scaleX:" + scaleX);
            System.out.println("scaleY:" + scaleY);
        }

        System.out.println("pageLayout.getPrintableWidth():" + pageLayout.getPrintableWidth());
        System.out.println("pageLayout.getPrintableHeight():" + pageLayout.getPrintableHeight());

        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);

        try {
            job.getJobSettings().setJobName("丹灶晴旭管理软件打印任务");
            if (job.printPage(pageLayout, node)) {
                return job.endJob();
            }
        } finally {
            node.getTransforms().remove(scale);
        }
        return false;
    }


    public static boolean printNodeNew(Node node, Printer printer, PageLayout pageLayout, int folder) throws Exception {
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        System.out.println("node.getBoundsInParent().getHeight():" + node.getBoundsInParent().getHeight());
        System.out.println("node.getBoundsInParent().getWidth():" + node.getBoundsInParent().getWidth());
        System.out.println("pageLayout.getPrintableWidth():" + pageLayout.getPrintableWidth());
        System.out.println("pageLayout.getPrintableHeight():" + pageLayout.getPrintableHeight());

        Thread.sleep(100);

//        Set<PrintResolution> rs = printer.getPrinterAttributes().getSupportedPrintResolutions();
//        PrintResolution best = null;
//        for (PrintResolution r: rs) {
//            System.out.println("r:"+r);
//            if (best ==null){
//                best = r;
//            }else{
//                int oldOne = best.getFeedResolution()+best.getCrossFeedResolution();
//                int newOne = r.getFeedResolution()+r.getCrossFeedResolution();
//                if (oldOne<newOne){
//                    best = r;
//                }
//            }
//        }
//        job.getJobSettings().setPrintResolution(best);
        job.getJobSettings().setPrintQuality(PrintQuality.HIGH);

        double realPrintable = (pageLayout.getPrintableHeight() - (folder - 1) * 2 * pageLayout.getTopMargin()) / folder;


        System.out.println("folder:" + folder + "\n" + "realPrintable:" + realPrintable);

        // 减5防止大一点点，超过打印范围，换页了
        double scaleX = (pageLayout.getPrintableWidth() - 5) / node.getBoundsInParent().getWidth();
        double scaleY = (realPrintable - 5) / node.getBoundsInParent().getHeight();

//        int loop = 0;
//        while (loop++ <10 && (Math.abs(scaleX-1.0)>0.2 || Math.abs(scaleY-1.0)>0.2)){
//            System.err.println("need to sleep 1s to wait style rerencering!");
//            Thread.sleep(1000);
//            scaleX = (pageLayout.getPrintableWidth()-10) / node.getBoundsInParent().getWidth();
//            scaleY = (pageLayout.getPrintableHeight()-10) / node.getBoundsInParent().getHeight();
//            System.out.println("scaleX:"+scaleX);
//            System.out.println("scaleY:"+scaleY);
//        }

        Scale scale = new Scale(scaleX, scaleY);
        node.getTransforms().add(scale);

        try {
            job.getJobSettings().setJobName("丹灶晴旭管理软件打印任务");
            if (job.printPage(pageLayout, node)) {
                return job.endJob();
            }
        } finally {
            node.getTransforms().remove(scale);
        }
        return false;
    }


    /**
     * 分页数绑定
     *
     * @param list    表格所表示的内容数据列表
     * @param getSize 从列表中获取尺寸大小的回调
     * @param perNum  每个分页所表示的数据大小，如每页为20个
     * @param obs     需要监听的可监听值列表
     */
    public static <T> IntegerBinding pageNumBind(Supplier<T> list, Function<T, Integer> getSize, int perNum, Observable... obs) {
        return new IntegerBinding() {
            {
                bind(obs);
            }

            @Override
            protected int computeValue() {
                if (list.get() == null) {
                    return 0;
                } else {
                    return (int) Math.ceil(getSize.apply(list.get()) / (double) perNum);
                }
            }
        };
    }

    @SafeVarargs
    public static <T> void cellStr(TableColumn<T, String>... cs) {
        for (TableColumn<T, String> c : cs) {
            c.setCellFactory(TextFieldTableCell.forTableColumn());
        }
    }

    @SafeVarargs
    public static <T> void cellDecimal(StringConverter<BigDecimal> converter, TableColumn<T, BigDecimal>... cs) {
        for (TableColumn<T, BigDecimal> c : cs) {
            c.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        }
    }

    @SafeVarargs
    public static <T> void cellTimestamp(StringConverter<Timestamp> converter, TableColumn<T, Timestamp>... cs) {
        for (TableColumn<T, Timestamp> c : cs) {
            c.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        }
    }


    public static SimpleDateFormat default_format_date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @SafeVarargs
    public static <T> void cellTimestamp(TableColumn<T, Timestamp>... cs) {
        cellTimestamp(new StringConverter<Timestamp>() {

            @Override
            public String toString(Timestamp object) {
                if (object == null) {
                    return "";
                }
                return default_format_date.format(object);
            }

            @Override
            public Timestamp fromString(String string) {
                try {
                    return Timestamp.valueOf(string);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, cs);
    }

    @SafeVarargs
    public static <T> void cellRate(TableColumn<T, BigDecimal>... cs) {
        for (TableColumn<T, BigDecimal> c : cs) {
            c.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<BigDecimal>() {
                @Override
                public String toString(BigDecimal decimal) {
                    return decimal == null ? "0%" : decimal.movePointRight(2).stripTrailingZeros().toPlainString() + "%";
                }

                @Override
                public BigDecimal fromString(String s) {
                    return s == null ? new BigDecimal(0) : new BigDecimal(s.replace("%", "")).movePointLeft(2);
                }
            }));
        }
    }

    @SafeVarargs
    public static <T> void cellDecimal(TableColumn<T, BigDecimal>... cs) {
        cellDecimal(FXUtils.decimalConverter("0"), cs);
    }

    @SafeVarargs
    public static <T> void cellMoney(TableColumn<T, BigDecimal>... cs) {
        cellDecimal(FXUtils.deciMoneyConverter(""), cs);
    }

    @SafeVarargs
    public static <T> void cellInteger(StringConverter<Integer> converter, TableColumn<T, Integer>... cs) {
        for (TableColumn<T, Integer> c : cs) {
            c.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        }
    }

    @SafeVarargs
    public static <T> void cellInteger(TableColumn<T, Integer>... cs) {

    }

    @SafeVarargs
    public static <T> void cellLong(StringConverter<Long> converter, TableColumn<T, Long>... cs) {
        for (TableColumn<T, Long> c : cs) {
            c.setCellFactory(TextFieldTableCell.forTableColumn(converter));
        }
    }

    @SafeVarargs
    public static <T> void cellLong(TableColumn<T, Long>... cs) {
        cellLong(FXUtils.longConverter("0"), cs);
    }

    public static <T, R> void cellList(TableColumn<T, List<R>> column, Function<R, String> getStr) {
        column.setCellFactory(new Callback<TableColumn<T, List<R>>, TableCell<T, List<R>>>() {
            @Override
            public TableCell<T, List<R>> call(TableColumn<T, List<R>> param) {
                return new TableCell<T, List<R>>() {
                    @Override
                    protected void updateItem(List<R> item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            return;
                        }
                        VBox box = new VBox(5);
                        box.setAlignment(Pos.CENTER);
                        List<Node> ls = item.stream()
                                .map(getStr)
                                .map(Label::new)
                                .collect(ArrayList::new,
                                        (list, e) -> {
                                            list.add(e);
                                            list.add(new Separator());
                                        },
                                        ArrayList::addAll
                                );
                        box.getChildren().addAll(ls);
                        setGraphic(box);
                    }
                };
            }
        });
    }


    public static StringBinding sBinding(String s) {
        return new StringBinding() {
            @Override
            protected String computeValue() {
                return s;
            }
        };
    }

    public static StringBinding spBinding(StringProperty s) {
        return new StringBinding() {
            {
                bind(s);
            }

            @Override
            protected String computeValue() {
                return s.get();
            }
        };
    }

    public static void setConstraints(GridPane gridPane) {
        setRowConstraints(gridPane);
        setColumnConstraints(gridPane);
    }

    public static void setRowConstraints(GridPane gridPane) {
        int maxRow = gridPane.getChildren().stream().mapToInt(GridPane::getRowIndex).filter(Objects::nonNull).max().orElse(0) + 1;
        List<RowConstraints> rows = gridPane.getRowConstraints();
        rows.clear();
        for (int i = 0; i < maxRow; i++) {
            rows.add(new RowConstraints(30));
        }
    }

    public static void setColumnConstraints(GridPane gridPane) {
        setColumnConstraints(gridPane, 80);
    }

    public static void setColumnConstraints(GridPane gridPane, double width) {
        int maxCol = gridPane.getChildren().stream().mapToInt(GridPane::getColumnIndex).filter(Objects::nonNull).max().orElse(0) + 1;
        List<ColumnConstraints> cols = gridPane.getColumnConstraints();
        cols.clear();
        for (int i = 0; i < maxCol; i++) {
            cols.add(new ColumnConstraints(80, width, 130));
        }
    }


    public static void addToGridPaneLink(GridPane gridPane, String nodeStr, String styleStr, int colIndex, int rowIndex, MainPaneList menuList, Properties params) {
        Button button = new Button(nodeStr);
        FXUtils.addStyle("url", button);
        button.setBackground(Background.EMPTY);
        button.setOnAction(event -> QSApp.mainPane.changeTo(menuList, params));
        HBox hBox3 = new HBox(button);
        if (styleStr != null && !styleStr.equals("")) {
            FXUtils.addStyle(styleStr, hBox3);
        }
        gridPane.add(hBox3, colIndex, rowIndex);
    }


    public static void addToGridPane(GridPane gridPane, String nodeStr, String styleStr, int colIndex, int rowIndex) {
        HBox hBox3 = new HBox(new Label(nodeStr));
        if (styleStr != null && !styleStr.equals("")) {
            FXUtils.addStyle(styleStr, hBox3);
        }
        gridPane.add(hBox3, colIndex, rowIndex);
    }

    public static void addToGridPane(GridPane gridPane, String nodeStr, String styleStr, int colIndex, int rowIndex, int colspan, int rowspan) {
        HBox hBox3 = new HBox(new Label(nodeStr));
        if (styleStr != null && !styleStr.equals("")) {
            FXUtils.addStyle(styleStr, hBox3);
        }
        gridPane.add(hBox3, colIndex, rowIndex, colspan, rowspan);
    }
}
