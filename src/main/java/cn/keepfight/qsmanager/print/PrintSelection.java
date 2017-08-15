package cn.keepfight.qsmanager.print;

/**
 * 打印选择器
 * Created by tom on 2017/7/7.
 */
public class PrintSelection {
    private QSPrintType type;

    private PrintSource printSource;

    public PrintSelection(QSPrintType type, PrintSource source){
        this.type = type;
        this.printSource = source;
    }

    public QSPrintType getType() {
        return type;
    }

    public PrintSource getPrintSource() {
        return printSource;
    }
}
