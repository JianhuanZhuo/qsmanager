package cn.keepfight.qsmanager;

import cn.keepfight.utils.ViewPathUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tom on 2017/10/13.
 */
public class AppLoaderTest extends Application {
    @Test
    public void main() throws Exception {
        Application.launch(AppLoaderTest.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = ViewPathUtil.getLoader("test_list.fxml");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }
}