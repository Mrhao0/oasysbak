package cn.gson.oasys.model.dao.filedao;

import java.util.List;

import cn.gson.oasys.model.entity.file.DirManagement;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import cn.gson.oasys.model.entity.file.FileList;
import cn.gson.oasys.model.entity.file.FilePath;
import cn.gson.oasys.model.entity.user.User;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FileListdao extends PagingAndSortingRepository<FileList, Long>{
	List<FileList> findByFpath(FilePath filepath);
	
	List<FileList> findByFpathAndFileIstrash(FilePath filepath,Long istrash);
	
	FileList findByFileNameAndFpath(String filename,FilePath filepath);
	
	List<FileList> findByUserAndContentTypeLikeAndFileIstrash(User user,String contenttype,Long istrash);
	
	List<FileList> findByUserAndFileIstrash(User user,Long istrash);
	
	@Query("from FileList f where f.user=?1 and f.fileIstrash=0 and f.contentType NOT LIKE 'image/%' and f.contentType NOT LIKE 'application/x%' and f.contentType NOT LIKE 'video/%' and f.contentType NOT LIKE 'audio/%'")
	List<FileList> finddocument(User user);
	
	@Query("from FileList f where f.user=?1 and f.fileIstrash=0 and f.fileName LIKE ?2 and f.contentType NOT LIKE 'image/%' and f.contentType NOT LIKE 'application/x%' and f.contentType NOT LIKE 'video/%' and f.contentType NOT LIKE 'audio/%'")
	List<FileList> finddocumentlike(User user ,String likefilename);
	
	List<FileList> findByUserAndFileIstrashAndContentTypeLikeAndFileNameLike(User user,Long istrash,String contenttype,String likefilename);
	
	List<FileList> findByFileIsshareAndFileIstrash(Long isshare,Long istrash);
	
	List<FileList> findByFileIsshareAndFileNameLike(Long isshare,String likefile);
	
	List<FileList> findByUserAndFileIsshareAndFileIstrash(User user,Long isshare,Long istrash);
	
	List<FileList> findByUserAndFileIstrashAndFileNameLike(User user,Long istrash,String likefile);

	@Query("update FileList fa set fa.id_dir_management=?2 where fa.fileId=?1")
	@Modifying
	void updateSubmitpathById(Long fileid, DirManagement submitpath);

	@Query("update FileSplit s set s.deleteflag=1 where s.splitid=?1")
	@Modifying
	void deletesplitfile(Long splitid);

	@Query("update FileSplit s set s.remark=?2 where s.splitid=?1")
	@Modifying
	void updatesplitfile(Long splitid, String remark);

	@Query("select l from FileList l where l.user.userId=?1 and l.fileShuffix='pdf' ")
	List<FileList> findfile(Long userid);

	@Modifying
	@Transactional
	@Query("update FileList set fileName=?1 where filePath = ?2")
	void updateFileNameByFilePath(String filename,String filePath);

	@Query("from FileList f where f.id_dir_management=?1")
	List<FileList> findByDirId(DirManagement one);
}
