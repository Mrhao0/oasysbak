package cn.gson.oasys.services.file;


import cn.gson.oasys.model.dao.filedao.DirManagementDao;
import cn.gson.oasys.model.entity.file.DirManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DirManagementService {
	@Autowired
	DirManagementDao dirManagementDao;

	public void edit(DirManagement dirManagemen) {
		dirManagementDao.edit(dirManagemen.getId(),dirManagemen.getName(),dirManagemen.getPath(),
				dirManagemen.getRemark(),dirManagemen.getType());
	}

	public void save(DirManagement dirManagemen) {
		dirManagementDao.save(dirManagemen);
	}

	public void deleteDirManagement(Long id) {
		dirManagementDao.delete(id);
	}

	public Page<DirManagement> findDirManagementByType(Integer type,Pageable pa) {

		return dirManagementDao.findByType(type,pa);
	}

	public Page<DirManagement> findAll(Pageable pa) {
		 return dirManagementDao.findAll(pa);
	}

	public DirManagement findOne(Long id) {
		return dirManagementDao.findOne(id);
	}
}
