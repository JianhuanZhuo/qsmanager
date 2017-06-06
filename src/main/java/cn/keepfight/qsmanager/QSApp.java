package cn.keepfight.qsmanager;

import cn.keepfight.qsmanager.network.controller.MainPane;
import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 晴旭管理软件类
 * Created by tom on 2017/6/5.
 */
public class QSApp extends Application {

    public static MainPane mainPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = ViewPathUtil.getLoader("main.fxml");
        BorderPane borderPane = loader.load();
        mainPane = loader.getController();
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.runLater(() -> {
            try {
                mainPane.logout();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
