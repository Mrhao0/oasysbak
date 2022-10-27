package cn.gson.oasys.services.audit;

import cn.gson.oasys.model.dao.audit.AuditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditDao auditDao;
}
