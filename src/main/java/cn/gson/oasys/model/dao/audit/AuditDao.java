package cn.gson.oasys.model.dao.audit;

import cn.gson.oasys.model.entity.audit.Audittemplate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AuditDao extends PagingAndSortingRepository<Audittemplate, Long> {

    @Query("update Audittemplate a set a.checkpoint=?2,a.iddept=?3 where a.audittemplateid=?1")
    @Modifying
    @Transactional
    void updateAudittemplateList(Long audittemplateid, String checkpoint, Long iddept);

}
