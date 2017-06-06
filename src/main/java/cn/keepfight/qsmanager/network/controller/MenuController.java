package cn.keepfight.qsmanager.network.controller;

import cn.keepfight.qsmanager.MenuList;
import cn.keepfight.utils.ImageLoadUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 菜单项控制器类
 * Created by tom on 2017/6/6.
 */
public class MenuController {

    @FXML
    private HBox root;
    @FXML
    private ImageView icon;
    @FXML
    private Label title;
    @FXML
    private Label status;
    @FXML
    private Label english;

    void setup(MenuList menu) {
        title.setText(menu.getTitle());
        english.setText(menu.getEnglish());
        icon.setImage(ImageLoadUtil.load(menu.getImage()));

        status.getStyleClass().add("hide");
    }

    public HBox getRoot() {
        return root;
    }
}
