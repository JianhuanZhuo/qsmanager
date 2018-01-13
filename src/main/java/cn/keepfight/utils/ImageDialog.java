package cn.keepfight.utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ImageDialog {

    public void xxx() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("图片上传工具");
        dialog.setHeaderText("图片附件管理");

        dialog.setGraphic(new ImageView(this.getClass().getResource("graph\\g32\\category.png").toString()));

        dialog.getDialogPane().getButtonTypes().add(new ButtonType("确定", ButtonBar.ButtonData.CANCEL_CLOSE));

        HBox head = new HBox(5.0);

        VBox contain = new VBox(5.0);
        contain.setPadding(new Insets(10));


        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");


//        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> null);
        dialog.showAndWait();
    }
}
