package cn.keepfight.widget;

import cn.keepfight.qsmanager.dao.ImageDao;
import cn.keepfight.qsmanager.dao.ImageListDao;
import cn.keepfight.qsmanager.dao.ImageListWrapper;
import cn.keepfight.qsmanager.service.ImageServers;
import cn.keepfight.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class ImageManager implements Initializable {
    public VBox root;
    public Button look;
    public Button refresh;
    public Button upload;
    public Button download;
    public Button delete;
    public Button rename;
    public Button tip;

    public TableView<ImageListWrapper> table;
    public TableColumn<ImageListWrapper, String> name;
    public TableColumn<ImageListWrapper, Timestamp> date;
    public TableColumn<ImageListWrapper, String> note;

    private String category;
    private final FileChooser fileChooser = new FileChooser();
    private final FileChooser fileSaver = new FileChooser();

    private final static Map<Long, String> keyToBase64 = new HashMap<>();

    public static void newManager(String category, String title) throws Exception {
        Stage stage = new Stage();
        ImageManager manager = ViewPathUtil.loadWidgetForController("image_manager/image_manager.fxml");
        manager.setCategory(category);
        stage.setTitle(title);
        stage.setScene(new Scene(manager.root));
        manager.load();
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        fileChooser.setTitle("选择需要上传的图片文件");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg"));
        fileSaver.setTitle("选择图片文件保存的位置");
        fileSaver.getExtensionFilters().add(new FileChooser.ExtensionFilter("图片文件", "*.jpg"));

        table.setPlaceholder(new Label("无图片，请刷新重新加载列表或添加图片"));

        FXWidgetUtil.connectStrColumn(name, ImageListWrapper::pnameProperty);
        FXWidgetUtil.connectStrColumn(note, ImageListWrapper::noteProperty);
        FXWidgetUtil.connectTimestampColumn(date, ImageListWrapper::cdateProperty);
        FXWidgetUtil.cellTimestamp(date);

        BooleanBinding isnull = table.getSelectionModel().selectedItemProperty().isNull();
        look.disableProperty().bind(isnull);
        download.disableProperty().bind(isnull);
        delete.disableProperty().bind(isnull);
        rename.disableProperty().bind(isnull);
        tip.disableProperty().bind(isnull);

        refresh.setOnAction(event -> {
            try {
                keyToBase64.clear();
                load();
            } catch (Exception e) {
                e.printStackTrace();
                WarningBuilder.build("刷新失败，请检查网络是否出问题了");
            }
        });

        rename.setOnAction(e -> {
            ImageListWrapper selected = table.getSelectionModel().getSelectedItem();
            String name = selected.getPname();
            alter("文件名", name).ifPresent(s -> {
                try {
                    ImageServers.updateName(selected.getId(), s);
                    load();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("文件名修改失败，请检查网络是否出问题了");
                }
            });
        });

        tip.setOnAction(e -> {
            ImageListWrapper selected = table.getSelectionModel().getSelectedItem();
            String note = selected.getNote();
            alter("备注", note).ifPresent(s -> {
                try {
                    ImageServers.updateNote(selected.getId(), s);
                    load();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("备注修改失败，请检查网络是否出问题了");
                }
            });
        });

        delete.setOnAction(e -> {
            ImageListWrapper selected = table.getSelectionModel().getSelectedItem();
            WarningBuilder.simpleConfirm(() -> {
                try {
                    ImageServers.del(selected.getId());
                    load();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    WarningBuilder.build("删除失败，请检查网络是否出问题了");
                }
            }, "确定删除该图片？");
        });

        upload.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
            if (file != null) {
                String base64;
                try {
                    base64 = FXImgUtils.imgToBase64(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    WarningBuilder.build("上传失败，请检查文件是否存在且文件是否已损坏");
                    return;
                }
                ImageDao dao = new ImageDao();
                dao.setPname(file.getName());
                dao.setContent(base64);
                dao.setCategory(category);
                try {
                    ImageServers.insert(dao);
                    load();
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("上传失败，请检查网络是否通畅");
                }
            }
        });

        download.setOnAction(event -> {
            ImageListWrapper selected = table.getSelectionModel().getSelectedItem();
            File file = fileSaver.showSaveDialog(root.getScene().getWindow());
            if (file != null) {
                String base64 = keyToBase64.get(selected.getId());
                if (base64 == null) {
                    base64 = new WaitDialog<String>(() -> {
                        try {
                            ImageServers.getImage(selected.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).justWait().orElse(null);
                    if (base64 == null) {
                        WarningBuilder.build("下载失败，请重试");
                        return;
                    }
                    keyToBase64.put(selected.getId(), base64);
                }
                // 写入文件中
                try {
                    FXImgUtils.saveTo(base64, file);
                } catch (Exception e) {
                    e.printStackTrace();
                    WarningBuilder.build("文件写入失败，请重试");
                }
            }
        });

        look.setOnAction(event -> {
            ImageListWrapper selected = table.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(selected.getPname());
            alert.setHeaderText("备注：" + (selected.getNote() == null ? "" : selected.getNote()));
            ImageView image = new ImageView(ImageLoadUtil.preloadImage);

            String base64 = keyToBase64.get(selected.getId());
            if (base64 == null) {
                new Thread(() -> {
                    String newBase64 = null;
                    try {
                        newBase64 = ImageServers.getImage(selected.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (newBase64 == null) {
                        WarningBuilder.build("图片下载失败，请重试");
                        return;
                    }
                    keyToBase64.put(selected.getId(), newBase64);

                    Image img = FXImgUtils.baseToImage(newBase64);
                    Platform.runLater(() -> image.setImage(img));
                }).start();
            } else {
                Image img = FXImgUtils.baseToImage(base64);
                Platform.runLater(() -> image.setImage(img));
            }
            Slider slider = new Slider(0.2, 2.0, 1.0);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            image.scaleXProperty().bind(slider.valueProperty());
            image.scaleYProperty().bind(slider.valueProperty());
            image.setFitWidth(300);
            image.setFitHeight(300);
            BorderPane pane = new BorderPane(image);
            pane.setTop(new HBox(new Label("放大缩小："), slider));
            alert.getDialogPane().setContent(pane);
            alert.getDialogPane().setMinHeight(600);
            alert.getDialogPane().setMinWidth(600);
            alert.showAndWait();
        });

        table.setRowFactory( tv -> {
            TableRow<ImageListWrapper> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    look.fire();
                }
            });
            return row ;
        });
    }

    public void setCategory(String c) {
        this.category = c;
    }


    public void load() throws Exception {
        if (category == null) {
            //TODO
            return;
        }
        List<ImageListDao> list = ImageServers.selectByCate(category);
        if (list != null) {
            Platform.runLater(() -> table.getItems().setAll(list.stream().map(ImageListWrapper::new).collect(Collectors.toList())));
        }
    }

    public Optional<String> alter(String label, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setHeaderText(label + "修改");
        dialog.setContentText("输入修改后的" + label + "：");
        return dialog.showAndWait();
    }
}
