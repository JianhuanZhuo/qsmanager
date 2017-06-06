package cn.keepfight.qsmanager.network.controller;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Created by tom on 2017/6/6.
 */
public class Supply implements ContentController {


    public HBox root;
    public ImageView addSup;
    public ImageView delSup;
    public ListView supList;
    public ImageView saveInfo;
    public ImageView delMat;
    public TableView matTable;
    public ImageView addMat;
    public Label info_attach;
    public TextArea info_note;
    public TextField info_addr;
    public TextField info_pbacc;
    public TextField info_pvacc;
    public TextField info_fax;
    public TextField info_phone;
    public TextField info_serial;
    public TextField info_name;

    @Override
    public Node getRoot() {
        return root;
    }
}
