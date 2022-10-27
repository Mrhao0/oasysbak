package cn.gson.oasys.mappers;

import cn.gson.oasys.model.entity.audit.Auditlogging;
import cn.gson.oasys.model.entity.audit.Audittemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditMapper {

//    List<Audittemplate> findAudittemplateList();

    void addAudittemplate(@Param("audittemplates") Audittemplate audittemplates);


    void addAuditlogging(@Param("auditlogging")Auditlogging auditlogging);

    List<Auditlogging> findAuditloggingList();
}
