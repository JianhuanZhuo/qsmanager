package cn.keepfight.qsmanager.controller;

import cn.keepfight.qsmanager.model.SupAnnualMonModel;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 送货单表格打印控制器
 * Created by tom on 2017/6/23.
 */
public class PrintMonSupController extends PrintTemplate implements Initializable {

    public VBox root;
    public TableView<SupAnnualMonModel> table;
    public TableColumn<SupAnnualMonModel, Number> id;

    private int currentPage = 0;

    private static final int SIZE_PER_PAGE = 12;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.fixedCellSizeProperty().bind(table.heightProperty().subtract(27).divide(Bindings.size(table.getItems())));
        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1+currentPage*SIZE_PER_PAGE));

        table.getItems().setAll(IntStream.range(1, 13)
                .mapToObj(x -> new SupAnnualMonModel())
                .collect(Collectors.toList())
        );
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public IntegerBinding pageNum() {
        return new IntegerBinding() {
            {
                bind(table.getItems());
            }
            @Override
            protected int computeValue() {
//                return table.getItems().size()/SIZE_PER_PAGE+1;
                return 4;
            }
        };
    }

    @Override
    public void selectPage(int i) {
        if (i>=4){
            new RuntimeException("max is 4, select "+i+" is illegal!").printStackTrace();
        }
        table.getItems().setAll(IntStream.range(i*SIZE_PER_PAGE+1, (i+1)*SIZE_PER_PAGE+1)
                .mapToObj(x -> new SupAnnualMonModel())
                .collect(Collectors.toList())
        );
        currentPage = i;
        id.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getTableView().getItems().indexOf(param.getValue()) + 1+currentPage*SIZE_PER_PAGE));
    }
}
