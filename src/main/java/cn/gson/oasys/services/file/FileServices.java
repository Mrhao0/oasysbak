package cn.gson.oasys.services.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.file.DirManagement;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import cn.gson.oasys.model.dao.filedao.FileListdao;
import cn.gson.oasys.model.dao.filedao.FilePathdao;
import cn.gson.oasys.model.dao.notedao.AttachService;
import cn.gson.oasys.model.dao.notedao.AttachmentDao;
import cn.gson.oasys.model.entity.file.FileList;
import cn.gson.oasys.model.entity.file.FilePath;
import cn.gson.oasys.model.entity.note.Attachment;
import cn.gson.oasys.model.entity.user.User;
import sun.awt.OSInfo;

import javax.annotation.PostConstruct;

@Service
@Transactional
public class FileServices {
	private static final Pattern linux_path_pattern = Pattern.compile("(/([a-zA-Z0-9][a-zA-Z0-9_\\-]{0,255}/)*([a-zA-Z0-9][a-zA-Z0-9_\\-]{0,255})|/)");
	private static final Pattern windows_path_pattern = Pattern.compile("(^[A-Z]:((\\\\|/)([a-zA-Z0-9\\-_ ]){1,255}){1,255}|([A-Z]:(\\\\|/)))");

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private FileListdao fldao;
	@Autowired
	private FilePathdao fpdao;
	@Autowired
	private AttachmentDao AttDao;
	@Autowired
	private AttachService AttachService;
	@Autowired
	private FileTransactionalHandlerService fileTransactionalHandlerService;
	@Autowired
	private UserDao udao;
	@Autowired
	private DirManagementService dirManagementService;

	@Value("${file.root.path}")
	private String rootPath;

	/**
	 * ?????????	ID ?????? ????????? ??????
	 *
	 * @param parentId
	 * @return
	 */
	public List<FilePath> findpathByparent(Long parentId) {
		return fpdao.findByParentIdAndPathIstrash(parentId, 0L);
	}

	/**
	 * ?????????????????? ????????????
	 *
	 * @param filePath
	 * @return
	 */
	public List<FileList> findfileBypath(FilePath filePath) {
		return fldao.findByFpathAndFileIstrash(filePath, 0L);
	}

	/**
	 * ?????????????????? ????????????????????????
	 *
	 * @param filePath
	 * @param parentpaths
	 */
	public void findAllParent(FilePath filePath, List<FilePath> parentpaths) {
		if (filePath.getParentId() != 1L) {
			FilePath filepath1 = fpdao.findOne(filePath.getParentId());
			parentpaths.add(filepath1);
			findAllParent(filepath1, parentpaths);
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param file
	 * @param user
	 * @param nowpath
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public Object savefile(MultipartFile file, User user, FilePath nowpath, boolean isfile) throws IllegalStateException, IOException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
		File root = new File(this.rootPath, simpleDateFormat.format(new Date()));

		File savepath = new File(root, user.getUserName());
		//System.out.println(savePath.getPath());

		if (!savepath.exists()) {
			savepath.mkdirs();
		}

		String shuffix = FilenameUtils.getExtension(file.getOriginalFilename());
		log.info("shuffix:{}", shuffix);
		String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + shuffix;
		File targetFile = new File(savepath, newFileName);
		file.transferTo(targetFile);

		if (isfile) {
			FileList filelist = new FileList();
			String filename = file.getOriginalFilename();
			filename = onlyname(filename, nowpath, shuffix, 1, true);
			filelist.setFileName(filename);
			filelist.setFilePath(targetFile.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""));
			filelist.setFileShuffix(shuffix);
			filelist.setSize(file.getSize());
			filelist.setUploadTime(new Date());
			filelist.setFpath(nowpath);
			filelist.setContentType(file.getContentType());
			filelist.setUser(user);
			fldao.save(filelist);
			return filelist;
		} else {
			Attachment attachment = new Attachment();
			attachment.setAttachmentName(file.getOriginalFilename());
			attachment.setAttachmentPath(targetFile.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""));
			attachment.setAttachmentShuffix(shuffix);
			attachment.setAttachmentSize(file.getSize());
			attachment.setAttachmentType(file.getContentType());
			attachment.setUploadTime(new Date());
			attachment.setUserId(user.getUserId() + "");
			attachment.setModel("note");
			AttDao.save(attachment);
			return attachment;
		}
	}

	//????????????
	public Integer updateatt(MultipartFile file, User user, FilePath nowpath, long attid) throws IllegalStateException, IOException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
		File root = new File(this.rootPath, simpleDateFormat.format(new Date()));

		File savepath = new File(root, user.getUserName());
		//System.out.println(savePath.getPath());

		if (!savepath.exists()) {
			savepath.mkdirs();
		}
		if (!file.isEmpty()) {
			String shuffix = FilenameUtils.getExtension(file.getOriginalFilename());
			log.info("shuffix:{}", shuffix);
			String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + shuffix;
			File targetFile = new File(savepath, newFileName);
			file.transferTo(targetFile);


			return AttachService.updateatt(file.getOriginalFilename(),
					targetFile.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""), shuffix, file.getSize(),
					file.getContentType(), new Date(), attid);
		}
		return 0;
	}

	@Transactional
	public void doshare(List<Long> fileids) {
		for (Long fileid : fileids) {

			FileList filelist = fldao.findOne(fileid);

			filelist.setFileIsshare(1L);
			fldao.save(filelist);
		}

	}


	/**
	 * ????????????id ?????? ????????????  ???????????? ???????????????????????????
	 *
	 * @param fileids
	 */
	@Transactional
	public void deleteFile(List<Long> fileids) {
		for (Long fileid : fileids) {
			FileList filelist = fldao.findOne(fileid);

			File file = new File(this.rootPath, filelist.getFilePath());
			//System.out.println(fileid+":"+file.exists());
			if (file.exists() && file.isFile()) {
				System.out.println("????????????" + filelist.getFileName() + "???????????????>>>>>>>>>");
				fldao.delete(fileid);
				System.out.println("????????????" + filelist.getFileName() + "????????????    >>>>>>>>>");
				file.delete();
			}
		}
	}

	/**
	 * ???????????????id ???????????? ?????????    ???????????????????????????????????????????????????
	 *
	 * @param pathids
	 */
	@Transactional
	public void deletePath(List<Long> pathids) {
		for (Long pathid : pathids) {
			FilePath filepath = fpdao.findOne(pathid);
//			System.out.println("?????????????????????"+filepath);

			//????????????????????????????????????
			List<FileList> files = fldao.findByFpath(filepath);
			if (!files.isEmpty()) {
//				System.out.println("????????????????????????????????????????????????~~~");
//				System.out.println(files);
				List<Long> fileids = new ArrayList<>();
				for (FileList filelist : files) {
					fileids.add(filelist.getFileId());
				}
				deleteFile(fileids);
			}

			//???????????????????????????????????????
			List<FilePath> filepaths = fpdao.findByParentId(pathid);
			if (!filepaths.isEmpty()) {
				List<Long> pathids2 = new ArrayList<>();
				for (FilePath filePath : filepaths) {
					pathids2.add(filePath.getId());
				}
				deletePath(pathids2);
			}

			fpdao.delete(filepath);
		}
	}

//	/**
//	 * ????????????id ????????????????????????
//	 * @param fileids
//	 */
//
//	@Transactional
//	public void trashfile(List<Long> fileids,Long setistrashhowmany,Long userid){
//		for (Long fileid : fileids) {
//			FileList fileList = fldao.findOne(fileid);
//			fileList.setFileIstrash(setistrashhowmany);
//			if(userid != null){
//				fileList.setFpath(null);
//			}
//
//			fldao.save(fileList);
//		}
//
//	}

//	public void trashPath(List<Long> pathids){
//		for (Long pathid : pathids) {
//			FilePath filepath = fpdao.findOne(pathid);
//
//			filepath.setPathIstrash(1L);
//
//			fpdao.save(filepath);
//		}
//
//	}

	/**
	 * ???????????????id ?????????????????????
	 *
	 * @param pathids
	 */
	public void trashpath(List<Long> pathids, Long setistrashhaomany, boolean isfirst) {
		for (Long pathid : pathids) {
			FilePath filepath = fpdao.findOne(pathid);
			//System.out.println("?????????????????????"+filepath);

			//????????????????????????????????????????????????
			List<FileList> files = fldao.findByFpath(filepath);
			if (!files.isEmpty()) {
				//	System.out.println("????????????????????????????????????????????????~~~");
				//System.out.println(files);
				List<Long> fileids = new ArrayList<>();
				for (FileList filelist : files) {
					fileids.add(filelist.getFileId());
				}
				fileTransactionalHandlerService.trashfile(fileids, 2L, null);
			}
//			System.out.println("????????????????????????????????????");
			//???????????????????????????????????????????????????
			List<FilePath> filepaths = fpdao.findByParentId(pathid);
			if (!filepaths.isEmpty()) {
//				System.out.println("??????????????????????????????");
				List<Long> pathids2 = new ArrayList<>();
				for (FilePath filePath : filepaths) {
					pathids2.add(filePath.getId());
				}
//				System.out.println("pathids2"+pathids2);
//				System.out.println("???????????????????????????");
				trashpath(pathids2, 2L, false);
			}
//			System.out.println("??????????????????????????????");
			if (isfirst) {
				filepath.setParentId(0L);
			}
			filepath.setPathIstrash(setistrashhaomany);
			fpdao.save(filepath);
		}
	}

	/**
	 * ????????????
	 *
	 * @param checkfileids
	 */
	@Transactional
	public void filereturnback(List<Long> checkfileids, Long userid) {
		FilePath fpath = fpdao.findByParentIdAndPathUserId(1L, userid);
		for (Long checkfileid : checkfileids) {
			FileList fileList = fldao.findOne(checkfileid);

			if (userid != null) {
				String name = onlyname(fileList.getFileName(), fpath, fileList.getFileShuffix(), 1, true);
				fileList.setFpath(fpath);
				fileList.setFileName(name);
			}
			fileList.setFileIstrash(0L);
			fldao.save(fileList);
		}

	}

	/**
	 * ???????????????
	 */
	public void pathreturnback(List<Long> pathids, Long userid) {
		for (Long pathid : pathids) {
			FilePath filepath = fpdao.findOne(pathid);
			System.out.println("?????????????????????" + filepath);

			//???????????????????????????????????????
			List<FileList> files = fldao.findByFpath(filepath);
			if (!files.isEmpty()) {
				System.out.println("????????????????????????????????????????????????~~~");
				System.out.println(files);
				List<Long> fileids = new ArrayList<>();
				for (FileList filelist : files) {
					fileids.add(filelist.getFileId());
				}
				filereturnback(fileids, null);
			}
			System.out.println("????????????????????????????????????");
			System.out.println("??????????????????????????????????????????");
			//??????????????????????????????????????????
			List<FilePath> filepaths = fpdao.findByParentId(pathid);
			if (!filepaths.isEmpty()) {
				System.out.println("??????????????????????????????");
				List<Long> pathids2 = new ArrayList<>();
				for (FilePath filePath : filepaths) {
					pathids2.add(filePath.getId());
				}
				System.out.println("pathids2" + pathids2);
				System.out.println("???????????????????????????");
				pathreturnback(pathids2, null);
			}
			System.out.println("??????????????????????????????");
			if (userid != null) {
				System.out.println("userid????????????????????????????????????????????? ????????????");
				FilePath fpath = fpdao.findByParentIdAndPathUserId(1L, userid);
				String name = onlyname(filepath.getPathName(), fpath, null, 1, false);
				filepath.setPathName(name);
				filepath.setParentId(fpath.getId());
			}
			System.out.println("????????????");

			filepath.setPathIstrash(0L);
			fpdao.save(filepath);
		}
	}


	/**
	 * ???????????????
	 *
	 * @param fromwhere 1?????????  2 ?????????
	 */
	@Transactional
	public void moveAndcopy(List<Long> mcfileids, List<Long> mcpathids, Long topathid, boolean fromwhere, Long userid) {
		FilePath topath = fpdao.findOne(topathid);
		if (fromwhere) {
			System.out.println("?????????????????????~~");
			if (!mcfileids.isEmpty()) {
				System.out.println("fileid is not null");
				for (Long mcfileid : mcfileids) {
					FileList filelist = fldao.findOne(mcfileid);
					String filename = onlyname(filelist.getFileName(), topath, filelist.getFileShuffix(), 1, true);
					filelist.setFpath(topath);
					filelist.setFileName(filename);
					fldao.save(filelist);
				}
			}
			if (!mcpathids.isEmpty()) {
				System.out.println("pathid is not null");
				for (Long mcpathid : mcpathids) {
					FilePath filepath = fpdao.findOne(mcpathid);
					String name = onlyname(filepath.getPathName(), topath, null, 1, false);
					filepath.setParentId(topathid);
					filepath.setPathName(name);
					fpdao.save(filepath);
				}
			}
		} else {
			System.out.println("?????????????????????~~");
			if (!mcfileids.isEmpty()) {
				System.out.println("fileid is not null");
				for (Long mcfileid : mcfileids) {
					FileList filelist = fldao.findOne(mcfileid);
					copyfile(filelist, topath, true);
				}
			}
			if (!mcpathids.isEmpty()) {
				System.out.println("pathid is not null");
				for (Long mcpathid : mcpathids) {
					copypath(mcpathid, topathid, true, userid);
				}
			}
		}
	}

	public void copypath(Long mcpathid, Long topathid, boolean isfirst, Long userid) {
		FilePath filepath = fpdao.findOne(mcpathid);

		//???????????????????????????
		FilePath copypath = new FilePath();
		copypath.setParentId(topathid);
		String copypathname = filepath.getPathName();
		if (isfirst) {
			copypathname = "?????? " + filepath.getPathName().replace("?????? ", "");
		}
		copypath.setPathName(copypathname);
		copypath.setPathUserId(userid);
		copypath = fpdao.save(copypath);

		//???????????????????????????????????????
		List<FileList> filelists = fldao.findByFpathAndFileIstrash(filepath, 0L);
		for (FileList fileList : filelists) {
			copyfile(fileList, copypath, false);
		}

		List<FilePath> filepathsons = fpdao.findByParentIdAndPathIstrash(filepath.getId(), 0L);

		if (!filepathsons.isEmpty()) {
			for (FilePath filepathson : filepathsons) {
				copypath(filepathson.getId(), copypath.getId(), false, userid);
			}
		}

	}

	/**
	 * ????????????
	 *
	 * @param filelist
	 */
	public void copyfile(FileList filelist, FilePath topath, boolean isfilein) {
		File s = getFile(filelist.getFilePath());
		User user = filelist.getUser();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
		File root = new File(this.rootPath, simpleDateFormat.format(new Date()));
		File savepath = new File(root, user.getUserName());

		if (!savepath.exists()) {
			savepath.mkdirs();
		}

		String shuffix = filelist.getFileShuffix();
		log.info("shuffix:{}", shuffix);
		String newFileName = UUID.randomUUID().toString().toLowerCase() + "." + shuffix;
		File t = new File(savepath, newFileName);

		copyfileio(s, t);

		FileList filelist1 = new FileList();
		String filename = "";
		if (isfilein) {
			filename = "?????? " + filelist.getFileName().replace("?????? ", "");
		} else {
			filename = filelist.getFileName();
		}
		filename = onlyname(filename, topath, shuffix, 1, true);
		filelist1.setFileName(filename);
		filelist1.setFilePath(t.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""));
		filelist1.setFileShuffix(shuffix);
		filelist1.setSize(filelist.getSize());
		filelist1.setUploadTime(new Date());
		filelist1.setFpath(topath);
		filelist1.setContentType(filelist.getContentType());
		filelist1.setUser(user);
		fldao.save(filelist1);

	}

	/**
	 * ??????????????????
	 *
	 * @param s
	 * @param t
	 */
	public void copyfileio(File s, File t) {
		InputStream fis = null;
		OutputStream fos = null;

		try {
			fis = new BufferedInputStream(new FileInputStream(s));
			fos = new BufferedOutputStream(new FileOutputStream(t));
			byte[] buf = new byte[2048];
			int i;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ????????????????????? ????????????
	 *
	 * @param mctoid
	 * @param mcpathids
	 * @return
	 */
	public List<FilePath> mcpathload(Long mctoid, List<Long> mcpathids) {
		List<FilePath> showsonpath = new ArrayList<>();
		List<FilePath> sonpaths = fpdao.findByParentIdAndPathIstrash(mctoid, 0L);

		for (FilePath sonpath : sonpaths) {
			boolean nosame = true;
			for (Long mcpathid : mcpathids) {
				if (sonpath.getId().equals(mcpathid)) {
					nosame = false;
					break;
				}
			}
			if (nosame) {
				showsonpath.add(sonpath);
			}
		}
		return showsonpath;
	}

	/**
	 * ?????????????????????
	 *
	 * @param name
	 * @param renamefp
	 * @param nowpathid
	 * @param isfile
	 */
	public void rename(String name, Long renamefp, Long nowpathid, boolean isfile) {
		if (isfile) {
			//???????????????
			FileList fl = fldao.findOne(renamefp);
			String newname = onlyname(name, fl.getFpath(), fl.getFileShuffix(), 1, isfile);
			fl.setFileName(newname);
			fldao.save(fl);
		} else {
			//??????????????????
			FilePath fp = fpdao.findOne(renamefp);
			FilePath filepath = fpdao.findOne(nowpathid);
			String newname = onlyname(name, filepath, null, 1, false);
			fp.setPathName(newname);
			fpdao.save(fp);
		}

	}


	/**
	 * ?????????????????????????????????
	 *
	 * @param name
	 * @param filepath
	 * @param shuffix
	 * @param num
	 * @return
	 */
	public String onlyname(String name, FilePath filepath, String shuffix, int num, boolean isfile) {
		Object f = null;
		if (isfile) {
			f = fldao.findByFileNameAndFpath(name, filepath);
		} else {
			f = fpdao.findByPathNameAndParentId(name, filepath.getId());
		}
		if (f != null) {
			int num2 = num - 1;
			if (shuffix == null) {
				name = name.replace("(" + num2 + ")", "") + "(" + num + ")";
			} else {
				name = name.replace("." + shuffix, "").replace("(" + num2 + ")", "") + "(" + num + ")" + "." + shuffix;
			}
			num += 1;
			return onlyname(name, filepath, shuffix, num, isfile);
		}
		return name;
	}

	/**
	 * ????????????
	 *
	 * @param filepath
	 * @return
	 */
	public File getFile(String filepath) {
		return new File(this.rootPath, filepath);
	}

	/**
	 * ?????????????????? ??? ???????????????
	 *
	 * @param nowpath
	 * @param parentpaths
	 * @return
	 */
	public String savepath(FilePath nowpath, List<FilePath> parentpaths) {
		findAllParent(nowpath, parentpaths);
		Collections.reverse(parentpaths);
		String savepath = "";
		for (FilePath filePath : parentpaths) {
			savepath += filePath.getPathName() + "/";
		}
		savepath = savepath.substring(0, savepath.length() - 1);
		return savepath;
	}

	/**
	 * ???????????????  ??????????????????id ?????? ???????????????
	 * @param parentid
	 * @return
	 */
//	public List<FilePath> moveandcopy(Long parentid){
//		List<FilePath> filePaths= fpdao.findByParentId(parentid);
//		return filePaths;
//	}
//	

	/**
	 * ????????????
	 *
	 * @param att
	 * @return
	 */
	public File get(Attachment att) {
		return new File(this.rootPath + att.getAttachmentPath());
	}

	public Attachment get(String filePath) {
		return AttDao.findByAttachmentPath(filePath);
	}


	public void updateSubmitpathById(Long fileid, Long id_submitpath) {
		DirManagement one = dirManagementService.findOne(id_submitpath);
		fldao.updateSubmitpathById(fileid, one);
	}

	/**
	 * check path is valid in windows and linux
	 *
	 * @param path     path to be validate
	 * @param platform valid value: linux,windows
	 * @return whether the path is valid
	 **/
	public boolean checkPathValid(String path, String platform) {
		if (platform.toLowerCase().equals(OSInfo.OSType.LINUX.name().toLowerCase()))
			return checkPatternMatch(linux_path_pattern, path);
		if (platform.toLowerCase().equals(OSInfo.OSType.WINDOWS.name().toLowerCase()))
			return checkPatternMatch(windows_path_pattern, path);
		return false;
	}

	private boolean checkPatternMatch(Pattern pattern, String target) {
		return pattern.matcher(target).matches();
	}

	public FileList findone(Long fileid) {
		 return fldao.findOne(fileid);
	}

	public List<FileList> findListByType(Long id) {
		return null;
	}

	public FileList createTempDir(Long pathid, Long userid) {
		User user = udao.findOne(userid);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM");
		File root = new File(this.rootPath, simpleDateFormat.format(new Date()));
		File savepath = new File(root, user.getUserName());

		FilePath nowpath = fpdao.findOne(pathid);
		FileList filelist = new FileList();
		filelist.setFileName("????????????????????????");
		filelist.setFilePath(savepath.getAbsolutePath().replace("\\", "/").replace(this.rootPath, ""));
		filelist.setUploadTime(new Date());
		filelist.setFpath(nowpath);
		filelist.setContentType("");
		filelist.setFileShuffix("temp");
		filelist.setUser(user);
		FileList save = fldao.save(filelist);
		savepath = new File(savepath, String.valueOf(save.getFileId()));
		if (!savepath.exists()) {
			savepath.mkdirs();
		}
		return save;
	}

	public Map<Long,List<FileList>> getMapWithDir() {
		Iterable<FileList> all = fldao.findAll();
		Iterator<FileList> iterator = all.iterator();
		Map<Long,List<FileList>> map=new HashMap<>();
		while (iterator.hasNext()){
			FileList fileList = iterator.next();
			if(fileList.getId_dir_management()==null){
				continue;
			}
			List<FileList> fileLists = map.get(fileList.getId_dir_management().getId());
			if(fileLists==null){
				fileLists=new ArrayList<>();
			}
			fileLists.add(fileList);
			map.put(fileList.getId_dir_management().getId(),fileLists);
		}
		return map;
	}

	public List<FileList> getMaterialListById(Long id) {
		DirManagement one = dirManagementService.findOne(id);
		return fldao.findByDirId(one);
	}
}
