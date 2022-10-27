package cn.gson.oasys.controller.audit;

import cn.gson.oasys.mappers.AuditMapper;
import cn.gson.oasys.model.dao.audit.AuditDao;
import cn.gson.oasys.model.dao.audit.AuditloggingDao;
import cn.gson.oasys.model.entity.audit.Auditlogging;
import cn.gson.oasys.model.entity.audit.Audittemplate;
import cn.gson.oasys.services.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditDao auditDao;

    @Autowired
    private AuditMapper auditMapper;

    @Autowired
    private AuditloggingDao aldao;

    /**
     * 审核模板数据新增
     * @param audittemplates
     */
    @RequestMapping("addAudittemplate")
    @ResponseBody
    public void addAudittemplate(Audittemplate audittemplates){
        System.out.println("audittemplates = " + audittemplates);
        auditMapper.addAudittemplate(audittemplates);
    }

    /**
     * 审核模板查询
     * @return
     */
    @RequestMapping("findAudittemplateList")
    @ResponseBody
    public Iterable<Audittemplate> findAudittemplateList(){
//        Iterable<Audittemplate> audittemplateList = auditMapper.findAudittemplateList();
        Iterable<Audittemplate> audittemplateList = auditDao.findAll();
        System.out.println("audittemplateList = " + audittemplateList);
        return audittemplateList;
    }

    /**
     * 审核模板修改
     * @param audittemplateid
     * @param checkpoint
     * @param iddept
     */
    @RequestMapping("updateAudittemplateList")
    @ResponseBody
    public void updateAudittemplateList(Long audittemplateid,String checkpoint,Long iddept){
        auditDao.updateAudittemplateList(audittemplateid,checkpoint,iddept);
    }

    /**
     * 审核模板删除
     * @param audittemplateid
     */
    @RequestMapping("deleteAudittemplateList")
    @ResponseBody
    public void deleteAudittemplateList(Long audittemplateid){
        auditDao.delete(audittemplateid);
    }

    /**
     * 审核记录数据新增
     * @param auditlogging
     */
    @RequestMapping("addAuditlogging")
    @ResponseBody
    public void addAuditlogging(Auditlogging auditlogging){
        auditMapper.addAuditlogging(auditlogging);
    }

    /**
     * 审核记录查询
     * @return
     */
    @RequestMapping("findAuditloggingList")
    @ResponseBody
    public List<Auditlogging> findAuditloggingList(){
        List<Auditlogging> auditloggingList = auditMapper.findAuditloggingList();
        return auditloggingList;
    }

    /**
     * 审核记录修改
     * @param auditloggingid
     * @param status
     * @param remark
     */
    @RequestMapping("updateAuditlogging")
    @ResponseBody
    public void updateAuditlogging(Long auditloggingid,Integer status,String remark){
        aldao.updateAuditlogging(auditloggingid,status,remark);
    }

    /**
     * 审核模板删除
     * @param auditloggingid
     */
    @RequestMapping("deleteAuditlogging")
    @ResponseBody
    public void deleteAuditlogging(Long auditloggingid){
        aldao.delete(auditloggingid);
    }
}
