package cn.gson.oasys.model.entity.file;

import javax.persistence.*;


@Entity
@Table(name = "aoa_file_split")
public class FileSplit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long splitid;	//id

    private Integer type;	//pdf分割象限

    private Integer deleteflag;	//删除标记(0未删除，1已删除)

    private String path;	//文件路径

    private Long fileid;	//外键关联文件表id

    private String remark;//备注

    public Long getSplitid() {
        return splitid;
    }

    public void setSplitid(Long splitid) {
        this.splitid = splitid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getFileid() {
        return fileid;
    }

    public void setFileid(Long fileid) {
        this.fileid = fileid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "FileSplit{" +
                "splitid=" + splitid +
                ", type=" + type +
                ", deleteflag=" + deleteflag +
                ", path='" + path + '\'' +
                ", fileid=" + fileid +
                ", remark='" + remark + '\'' +
                '}';
    }
}
