package cn.keepfight.qsmanager.dao;

import java.sql.Timestamp;


public class ImageDao {
    private Long id;
    private String category;
    private Timestamp cdate;
    private String pname;
    private String note;
    private Boolean del;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Timestamp getCdate() {
        return cdate;
    }

    public void setCdate(Timestamp cdate) {
        this.cdate = cdate;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getDel() {
        return del;
    }

    public void setDel(Boolean del) {
        this.del = del;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageDao imageDao = (ImageDao) o;

        if (id != null ? !id.equals(imageDao.id) : imageDao.id != null) return false;
        if (category != null ? !category.equals(imageDao.category) : imageDao.category != null) return false;
        if (cdate != null ? !cdate.equals(imageDao.cdate) : imageDao.cdate != null) return false;
        if (pname != null ? !pname.equals(imageDao.pname) : imageDao.pname != null) return false;
        if (note != null ? !note.equals(imageDao.note) : imageDao.note != null) return false;
        if (del != null ? !del.equals(imageDao.del) : imageDao.del != null) return false;
        return content != null ? content.equals(imageDao.content) : imageDao.content == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (cdate != null ? cdate.hashCode() : 0);
        result = 31 * result + (pname != null ? pname.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (del != null ? del.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImageDao{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", cdate=" + cdate +
                ", pname='" + pname + '\'' +
                ", note='" + note + '\'' +
                ", del=" + del +
                ", content='" + content + '\'' +
                '}';
    }
}
