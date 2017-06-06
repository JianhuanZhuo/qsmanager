package cn.keepfight.qsmanager.network.controller;

import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Created by tom on 2017/6/6.
 */
public class Products implements ContentController {
    public VBox root;
    public TableView prodTable;
    public ImageView del;
    public ImageView add;

    @Override
    public Node getRoot() {
        return root;
    }
}
