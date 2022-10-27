package cn.gson.oasys.model.dao.filedao;

import cn.gson.oasys.model.entity.file.DirManagement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DirManagementDao extends JpaRepository<DirManagement, Long> {
	@Query("update DirManagement d set d.name=?2,d.path=?3,d.remark=?4,d.type=?5 where d.id=?1")
	@Modifying
	@Transactional
	void edit(Long id, String name, String path, String remark, Integer type);

	Page<DirManagement> findByType(Integer type, Pageable pa);
}
