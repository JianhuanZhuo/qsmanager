package cn.keepfight.qsmanager.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Created by tom on 2017/6/9.
 */
public class PicMakerController {
    @FXML
    private HBox root;
    @FXML
    private ImageView image;

    private String imageUrl;

    public void setImage(String imageUrl) {
//        if (imageUrl==null)
        Platform.runLater(() -> image.setImage(new Image(imageUrl)));
    }

    public HBox getRoot() {
        return root;
    }
}
