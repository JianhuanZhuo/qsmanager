package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.OrderItemModel;
import cn.keepfight.utils.FXUtils;
import cn.keepfight.utils.ImageLoadUtil;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 购物单条
 * Created by tom on 2017/6/18.
 */
public class ShopItemController implements ContentController, Initializable {
    @FXML
    private HBox root;
    @FXML
    private ImageView pic;
    @FXML
    private Label head;
    @FXML
    private Label detail;
    @FXML
    private Label price;
    @FXML
    private ChoiceBox<Long> pack;
    @FXML
    private TextField num;
    @FXML
    private Label unit;
    @FXML
    private Label total;
    // 控制器
    private ObjectProperty<OrderItemModel> itemProperty = new SimpleObjectProperty<>();

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void loaded() {

    }

    @Override
    public void showed() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXUtils.limitNum(num, 10, 5, true);

        total.textProperty().bind(new StringBinding() {
            {
                bind(itemProperty, pack.getSelectionModel().selectedItemProperty(), num.textProperty());
            }
            @Override
            protected String computeValue() {
                try {
                    return itemProperty.get().getPrice()
                            .multiply(new BigDecimal(pack.getSelectionModel().getSelectedItem()))
                            .multiply(new BigDecimal(num.getText())).toString();
                }catch (Exception e){
                    return "0";
                }
            }
        });

        pack.setConverter(new StringConverter<Long>() {
            @Override
            public String toString(Long object) {
                if (object.equals(1L)) {
                    return "散装";
                } else {
                    return "整装(" + object + ")";
                }
            }
            @Override
            public Long fromString(String string) {
                return null;
            }
        });

        itemProperty.addListener(((observable, oldValue, newValue) -> {
            if (oldValue!=null){
                oldValue.numProperty().unbind();
                oldValue.packProperty().unbind();
            }

            if (newValue==null){
                return;
            }

            // 填充数据
            ImageLoadUtil.bindImageDirectly(pic, newValue.getPicurl());
            head.setText(newValue.getSerial() + "-" + newValue.getName());
            detail.setText("包装明细：" + newValue.getDetail());
            price.setText("" + newValue.getPrice());

            // 装数填充
            List<Long> ls = new ArrayList<>(2);
            if (newValue.getPackDefault() != -1L) ls.add(newValue.getPackDefault());
            ls.add(1L);
            pack.getItems().setAll(ls);
                if (newValue.packProperty().getValue()==-1L){
                pack.getSelectionModel().selectFirst();
            }else {
                pack.getSelectionModel().select(newValue.packProperty().getValue());
            }
            // 绑定
            newValue.packProperty().bind(pack.getSelectionModel().selectedItemProperty());


            // 订购数填充
            num.setText("" + newValue.getNum());
            newValue.numProperty().bind(new ObjectBinding<BigDecimal>() {
                {
                    bind(num.textProperty());
                }
                @Override
                protected BigDecimal computeValue() {
                    try {
                        return new BigDecimal(num.getText());
                    }catch (Exception e){
                        return new BigDecimal(0);
                    }
                }
            });
        }));

        pack.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> unit.setText((newValue==null || newValue.equals(1L)) ? "个" : itemProperty.get().getUnit()));
    }


    public void fill(OrderItemModel item) {
        itemProperty.set(item);
    }
}
