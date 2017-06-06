package cn.keepfight.utils;

import java.util.Optional;
import java.util.function.Supplier;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class WaitDialog<V> extends Dialog<Boolean> implements EventHandler<WorkerStateEvent> {

    private static final String waitIcon = "wait.png";

    private long befStp;
    private long aftStp;

    private Supplier<V> action;

    public WaitDialog(Supplier<V> action) {
        setTitle("正在执行");
        setHeaderText("这个操作可能需要花费些时间，请稍等待...");
        setGraphic(new ImageView(ImageLoadUtil.load(waitIcon, ImageLoadUtil.IMG_SIZE_64)));

        ButtonType buttonTypeCancel = new ButtonType("取消", ButtonData.CANCEL_CLOSE);

        getDialogPane().getButtonTypes().addAll(buttonTypeCancel);

        //进程指示器
        ProgressIndicator indic = new ProgressIndicator();
        BorderPane borderPane = new BorderPane(indic);

        getDialogPane().setContent(borderPane);

        Stage stage = (Stage) getDialogPane().getScene().getWindow();

        // Add a custom icon.
        stage.getIcons().add(ImageLoadUtil.load(waitIcon, ImageLoadUtil.IMG_SIZE_16));

        setResultConverter(dialogButton -> false);

        this.action = action;
    }

    /**
     * as it said, just wait.
     * this method will block the thread to wait finishing of task action.
     *
     * @return an optional which indicate whether task finishes or interrupts for exceptions
     */
    public Optional<V> justWait() {
        Task<V> task = new Task<V>() {
            @Override
            protected V call() throws Exception {
                return action.get();
            }
        };
        task.setOnSucceeded(this);

        befStp = System.currentTimeMillis();
        new Thread(task).start();
        Optional<Boolean> result = showAndWait();
        aftStp = System.currentTimeMillis();
        System.out.println("此次等待时间为：" + (aftStp - befStp) + " 毫秒");

        if (result.isPresent() && result.get()) {
            return Optional.ofNullable(task.getValue());
        }

        return Optional.empty();
    }

    /**
     * 设置头部文字信息
     */
    public WaitDialog<V> setDetail(String title, String headerText) {
        setTitle(title);
        setHeaderText(headerText);
        return this;
    }

    @Override
    public void handle(WorkerStateEvent event) {
        setResult(Boolean.TRUE);
        close();
    }

}
