package cn.gson.oasys.model.dao.audit;

import cn.gson.oasys.model.entity.audit.Auditlogging;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AuditloggingDao extends PagingAndSortingRepository<Auditlogging, Long>{

    @Query("update Auditlogging l set l.status=?2,l.remark=?3 where l.auditloggingid=?1")
    @Modifying
    @Transactional
    void updateAuditlogging(Long auditloggingid, Integer status, String remark);
}
