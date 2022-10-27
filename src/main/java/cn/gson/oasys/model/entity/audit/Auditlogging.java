package cn.gson.oasys.model.entity.audit;

import cn.gson.oasys.model.entity.user.User;

import javax.persistence.*;
import java.util.Date;

/**
 * 审核记录
 */
@Entity

@Table(name = "aoa_auditlogging_list")
public class Auditlogging {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long auditloggingid;	//审核记录id

    private Integer status;		//检查项是否有问题(0否，1是)

//    @Column(name = "id_user")
//    private Long iduser;		//用户关联id
//
//    @Column(name = "id_audittemplateid")
//    private Long idaudittemplateid;		//审核模板关联id

    private Date checktime;		//记录时间(年月日)

    private String remark;		//备注

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @OneToOne
    @JoinColumn(name = "id_audittemplateid")
    private Audittemplate audittemplate;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAuditloggingid() {
        return auditloggingid;
    }

    public void setAuditloggingid(Long auditloggingid) {
        this.auditloggingid = auditloggingid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

//    public Long getIduser() {
//        return iduser;
//    }
//
//    public void setIduser(Long iduser) {
//        this.iduser = iduser;
//    }
//
//    public Long getIdaudittemplateid() {
//        return idaudittemplateid;
//    }
//
//    public void setIdaudittemplateid(Long idaudittemplateid) {
//        this.idaudittemplateid = idaudittemplateid;
//    }

    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Audittemplate getAudittemplate() {
        return audittemplate;
    }

    public void setAudittemplate(Audittemplate audittemplate) {
        this.audittemplate = audittemplate;
    }

    @Override
    public String toString() {
        return "Auditlogging{" +
                "auditloggingid=" + auditloggingid +
                ", status=" + status +
                ", checktime=" + checktime +
                ", remark='" + remark + '\'' +
//                ", user=" + user +
                ", audittemplate=" + audittemplate +
                '}';
    }
}
