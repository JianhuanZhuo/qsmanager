package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.model.ProductModel;
import cn.keepfight.utils.DialogContent;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.FXWidgetUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by tom on 2017/7/19.
 */
public class OrderFavorAddController implements DialogContent<ProductModel>, Initializable {

    @FXML
    private VBox root;
    @FXML
    private ChoiceBox<ProductModel> ps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ps.setConverter(FXUtils.converter(x->x.getSerial()+"-"+x.getName()));
    }

    @Override
    public Node getContent() {
        return root;
    }

    @Override
    public void init() {
        ps.getSelectionModel().clearSelection();
        try {
            ps.getItems().setAll(FXCollections.observableList(QSApp.service.getProductService().selectAll()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReadOnlyBooleanProperty allValid() {
        BooleanProperty b = new SimpleBooleanProperty();
        b.bind(ps.getSelectionModel().selectedItemProperty().isNotNull());
        return b;
    }

    @Override
    public ProductModel pack() {
        return ps.getSelectionModel().getSelectedItem();
    }

}
