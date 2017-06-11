package cn.keepfight.utils;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;

import java.util.Optional;

/**
 * Created by tom on 2017/6/7.
 */
public class CustomDialog {

    private String title;
    private String head;
    private String graph;

    public static CustomDialog gen() {
        return new CustomDialog();
    }

    public static CustomDialog gen(String head, String graph) {
        CustomDialog res = new CustomDialog();
        res.setGraph(graph);
        res.setHead(head);
        return res;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public <T> Optional<T> build(DialogContent<T> content, T initData) {
        //初始化内容
        content.init();
        content.fill(initData);
        return construct(content);
    }

    public <T> Optional<T> build(DialogContent<T> content) {
        //初始化内容
        content.init();
        return construct(content);
    }

    private <T> Optional<T> construct(DialogContent<T> content) {

        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(head);
        if (graph != null)
            dialog.setGraphic(ImageLoadUtil.bindImage(new ImageView(), graph));

        // 添加按钮
        ButtonType okButtonType = new ButtonType("确定", ButtonBar.ButtonData.OK_DONE);
        ButtonType canButtonType = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, canButtonType);

        dialog.getDialogPane().setContent(content.getContent());

        //有效性验证
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.disableProperty().bind(content.allValid().not());

        // 设置转换器
        dialog.setResultConverter(content::resultConverter);

        return dialog.showAndWait();
    }
}
