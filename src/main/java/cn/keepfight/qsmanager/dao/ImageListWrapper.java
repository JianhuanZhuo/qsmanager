package cn.keepfight.qsmanager.dao;

import cn.keepfight.utils.FXReflectUtils;
import javafx.beans.property.*;

import java.sql.Timestamp;

public class ImageListWrapper implements DaoWrapper<ImageListDao> {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty category = new SimpleStringProperty();
    private ObjectProperty<Timestamp> cdate = new SimpleObjectProperty<>();
    private StringProperty pname = new SimpleStringProperty();
    private StringProperty note = new SimpleStringProperty();

    public ImageListWrapper(){}
    public ImageListWrapper(ImageListDao data){
        wrap(data);
    }

    @Override
    public void wrap(ImageListDao data) {
        FXReflectUtils.attrsCopy(data, this);
    }

    @Override
    public ImageListDao get() {
        return FXReflectUtils.attrsCopyAndReturn(this, new ImageListDao());
    }


    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public Timestamp getCdate() {
        return cdate.get();
    }

    public ObjectProperty<Timestamp> cdateProperty() {
        return cdate;
    }

    public void setCdate(Timestamp cdate) {
        this.cdate.set(cdate);
    }

    public String getPname() {
        return pname.get();
    }

    public StringProperty pnameProperty() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname.set(pname);
    }

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }
}
