package cn.keepfight.widget;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 月份拾取器
 * Created by tom on 2017/10/13.
 */
public class MonthPicker implements Initializable, Widget<Pair<Integer, Integer>>{
    public HBox root;
    public Label label_year;
    public Label label_month;
    public Button year_uu;
    public Button year_u;
    public Button year_c;
    public Button year_d;
    public Button year_dd;
    public Button month_uu;
    public Button month_u;
    public Button month_c;
    public Button month_d;
    public Button month_dd;
    public Pane floatPane;
    public HBox floatBox;
    public VBox year_list;
    public VBox month_list;
    private Popup popup = new Popup();

    private Pair<Integer, Integer> data = new Pair<>(2017, 7);


    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.getChildren().remove(floatPane);
        popup.setAutoHide( true );
        popup.setHideOnEscape( true );
        popup.setAutoFix( true );
        popup.getContent().add(floatPane);
        floatPane.setVisible(true);
        label_year.setOnMouseClicked(e-> show());
        label_month.setOnMouseClicked(e-> show());

        paint();

        year_u.setOnAction(e->{
            upYear();
            paint();
        });
        year_uu.setOnAction(e->{
            upYear();
            upYear();
            paint();
        });
        year_dd.setOnAction(e->{
            downYear();
            downYear();
            paint();
        });
        year_d.setOnAction(e->{
            downYear();
            paint();
        });


        month_u.setOnAction(e->{
            upMonth();
            paint();
        });
        month_uu.setOnAction(e->{
            upMonth();
            upMonth();
            paint();
        });
        month_d.setOnAction(e->{
            downMonth();
            paint();
        });
        month_dd.setOnAction(e->{
            downMonth();
            downMonth();
            paint();
        });

        year_list.setOnScroll(event -> {
            if (event.getDeltaY()>0){
                upYear();
                paint();
            }else{
                downYear();
                paint();
            }
        });

        month_list.setOnScroll(event -> {
            if (event.getDeltaY()>0){
                upMonth();
                paint();
            }else{
                downMonth();
                paint();
            }
        });
    }

    private void show(){
        popup.show(root,
                label_year.localToScreen(label_year.getBoundsInLocal()).getMinX(),
                label_year.localToScreen(label_year.getBoundsInLocal()).getMinY());

    }

    private void upYear(){
        data = new Pair<>(data.getKey()+1, data.getValue());
    }

    private void downYear(){
        data = new Pair<>(data.getKey()-1, data.getValue());
    }

    private void upMonth(){
        data = new Pair<>(data.getKey(), getMonth(1));
    }

    private void downMonth(){
        data = new Pair<>(data.getKey(), getMonth(-1));
    }

    private int getMonth(int offset){
        int month = data.getValue();
        return (month+11+offset)%12+1;
    }

    private void paint(){
        int year = data.getKey();
        year_uu.setText(""+(year+2));
        year_u.setText(""+(year+1));
        year_c.setText(""+(year));
        year_d.setText(""+(year-1));
        year_dd.setText(""+(year-2));
        int month = data.getValue();
        month_uu.setText(""+(getMonth(+2)));
        month_u.setText(""+(getMonth(1)));
        month_c.setText(""+(month));
        month_d.setText(""+(getMonth(-1)));
        month_dd.setText(""+(getMonth(-2)));

        label_year.setText(""+year);
        label_month.setText(""+month);
    }
}
