package cn.gson.oasys.model.entity.audit;

import javax.persistence.*;

/**
 * 审核模板
 */
@Entity
@Table(name = "aoa_audittemplate_list")
public class Audittemplate {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long audittemplateid;	//模板id

    @Column(name = "page_no")
    private Integer pageno;		//审核页码

    @Column(name = "check_point")
    private String checkpoint;		//检查项

    @Column(name = "id_path")
    private Long idpath;		//关联目录id

    @Column(name = "id_dept")
    private Long iddept;		//关联部门id



    public Long getAudittemplateid() {
        return audittemplateid;
    }

    public void setAudittemplateid(Long audittemplateid) {
        this.audittemplateid = audittemplateid;
    }

    public Integer getPageno() {
        return pageno;
    }

    public void setPageno(Integer pageno) {
        this.pageno = pageno;
    }

    public String getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(String checkpoint) {
        this.checkpoint = checkpoint;
    }

    public Long getIdpath() {
        return idpath;
    }

    public void setIdpath(Long idpath) {
        this.idpath = idpath;
    }

    public Long getIddept() {
        return iddept;
    }

    public void setIddept(Long iddept) {
        this.iddept = iddept;
    }

    @Override
    public String toString() {
        return "Audittemplate{" +
                "audittemplateid=" + audittemplateid +
                ", pageno=" + pageno +
                ", checkpoint='" + checkpoint + '\'' +
                ", idpath=" + idpath +
                ", iddept=" + iddept +
                '}';
    }
}
