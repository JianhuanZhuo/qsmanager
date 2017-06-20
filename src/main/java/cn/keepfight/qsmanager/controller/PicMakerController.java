package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.ImageLoadUtil;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 图片选择器界面控制器
 * Created by tom on 2017/6/9.
 */
public class PicMakerController implements DialogContent<String>, Initializable {
    @FXML
    private VBox root;
    @FXML
    private TextField picurl;
    @FXML
    private Button pickBtn;
    @FXML
    private HBox preview;
    @FXML
    private ImageView imageLoader;

    @FXML
    private Slider rotate;
    @FXML
    private Slider scaleX;
    @FXML
    private Slider scaleY;
    @FXML
    private Slider translateX;
    @FXML
    private Slider translateY;

    private BooleanProperty change = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pickBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择图片");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(QSApp.primaryStage);
            if (selectedFile != null) {
                setPicurl("file:" + selectedFile.getAbsolutePath());
                change.set(true);
            }
        });

        // 变换控件绑定
        imageLoader.rotateProperty().bindBidirectional(rotate.valueProperty());
        imageLoader.scaleXProperty().bindBidirectional(scaleX.valueProperty());
        imageLoader.scaleYProperty().bindBidirectional(scaleY.valueProperty());
        imageLoader.translateXProperty().bindBidirectional(translateX.valueProperty());
        imageLoader.translateYProperty().bindBidirectional(translateY.valueProperty());

        // 添加已改变的监听
        rotate.valueProperty().addListener(e->change.set(true));
        scaleX.valueProperty().addListener(e->change.set(true));
        scaleY.valueProperty().addListener(e->change.set(true));
        translateX.valueProperty().addListener(e->change.set(true));
        translateY.valueProperty().addListener(e->change.set(true));

        // 剪切溢出边界
        Rectangle clipRect = new Rectangle();
        clipRect.heightProperty().bind(preview.heightProperty());
        clipRect.widthProperty().bind(preview.widthProperty());
        preview.setClip(clipRect);
    }

    @Override
    public void init() {
        picurl.setText("");
        ImageLoadUtil.bindDefault(imageLoader);
        resetImageTransform();
    }

    @Override
    public void fill(String s) {
        setPicurl(s);
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public BooleanProperty allValid() {
        return change;
    }

    @Override
    public String pack() {
        try {
            WritableImage image = preview.snapshot(null, null);
            return "file:" + FXUtils.writeImage(image);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 重置变换控件
     */
    private void resetImageTransform() {
        ImageLoadUtil.bindImageDirectly(imageLoader, "");
        rotate.setValue(0);
        scaleX.setValue(1);
        scaleY.setValue(1);
        translateX.setValue(0);
        translateY.setValue(0);
        change.set(false);
    }

    private void setPicurl(String absolutePath) {
        if (absolutePath != null && !absolutePath.equals("")) {
            picurl.setText(absolutePath);
            resetImageTransform();
            ImageLoadUtil.bindImageDirectly(imageLoader, absolutePath);
        }
    }
}
