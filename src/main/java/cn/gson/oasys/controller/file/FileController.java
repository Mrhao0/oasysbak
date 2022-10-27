package cn.gson.oasys.controller.file;

import cn.gson.oasys.common.formValid.BindingResultVOUtil;
import cn.gson.oasys.common.formValid.ResultVO;
import cn.gson.oasys.mappers.FileMapper;
import cn.gson.oasys.model.dao.filedao.FileListdao;
import cn.gson.oasys.model.dao.filedao.FilePathdao;
import cn.gson.oasys.model.dao.user.DeptDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.file.DirManagement;
import cn.gson.oasys.model.entity.file.FileList;
import cn.gson.oasys.model.entity.file.FilePath;
import cn.gson.oasys.model.entity.file.FileSplit;
import cn.gson.oasys.model.entity.user.Dept;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.services.file.DirManagementService;
import cn.gson.oasys.services.file.FileServices;
import cn.gson.oasys.services.file.FileTransactionalHandlerService;
import cn.gson.oasys.services.inform.InformService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("/")
public class FileController {
	@Autowired
	private FileServices fs;
	@Autowired
	private FilePathdao fpdao;
	@Autowired
	private FileListdao fldao;
	@Autowired
	private UserDao udao;
	@Autowired
	private FileTransactionalHandlerService fileTransactionalHandlerService;
	@Autowired
	private InformService informService;
	@Autowired
	private FileMapper fileMapper;

	@Autowired
	private DeptDao deptDao;
	@Autowired
	private DirManagementService dirManagementService;
	/**
	 * 第一次进入
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("filemanage")
	public String usermanage(@SessionAttribute("userId")Long userid,Model model) {
		System.out.println(userid);
		User user = udao.findOne(userid);

		FilePath filepath = fpdao.findByPathName(user.getUserName());
		
		System.out.println(filepath);
		
		if(filepath == null){
			FilePath filepath1 = new FilePath();
			filepath1.setParentId(1L);
			filepath1.setPathName(user.getUserName());
			filepath1.setPathUserId(user.getUserId());
			filepath = fpdao.save(filepath1);
		}
		model.addAttribute("dirs", dirManagementService.findAll());
		model.addAttribute("nowpath", filepath);
		model.addAttribute("paths", fs.findpathByparent(filepath.getId()));
		model.addAttribute("files", fs.findfileBypath(filepath));
		
		model.addAttribute("userrootpath",filepath);
		model.addAttribute("mcpaths",fs.findpathByparent(filepath.getId()));
		return "file/filemanage";
	}

	/**
	 * 进入指定文件夹 的controller方法
	 * 
	 * @param pathid
	 * @param model
	 * @return
	 */
	@RequestMapping("filetest")
	public String text(@SessionAttribute("userId")Long userid,@RequestParam("pathid") Long pathid, Model model) {
		User user = udao.findOne(userid);
		FilePath userrootpath = fpdao.findByPathName(user.getUserName());
		
		// 查询当前目录
		FilePath filepath = fpdao.findOne(pathid);

		// 查询当前目录的所有父级目录
		List<FilePath> allparentpaths = new ArrayList<>();
		fs.findAllParent(filepath, allparentpaths);
		Collections.reverse(allparentpaths);



		model.addAttribute("allparentpaths", allparentpaths);
		model.addAttribute("nowpath", filepath);
		model.addAttribute("paths", fs.findpathByparent(filepath.getId()));
		model.addAttribute("files", fs.findfileBypath(filepath));
		//复制移动显示 目录
		model.addAttribute("userrootpath",userrootpath);
		model.addAttribute("mcpaths",fs.findpathByparent(userrootpath.getId()));
		return "file/filemanage";
	}
	/**
	 * @description 素材管理
	 * @date 2022-10-21 12:30
	 */
	@RequestMapping("materialManagement")
	public String materialManagement(@SessionAttribute("userId")Long userid,Model model) {
		System.out.println(userid);
		User user = udao.findOne(userid);

		FilePath filepath = fpdao.findByPathName(user.getUserName());

		System.out.println(filepath);

		if(filepath == null){
			FilePath filepath1 = new FilePath();
			filepath1.setParentId(1L);
			filepath1.setPathName(user.getUserName());
			filepath1.setPathUserId(user.getUserId());
			filepath = fpdao.save(filepath1);
		}

		model.addAttribute("nowpath", filepath);
		model.addAttribute("paths", fs.findpathByparent(filepath.getId()));
		model.addAttribute("files", fs.findfileBypath(filepath));

		model.addAttribute("userrootpath",filepath);
		model.addAttribute("mcpaths",fs.findpathByparent(filepath.getId()));
		return "file/material";
	}

	/**
	 * 文件上传 controller方法
	 * 
	 * @param file
	 * @param pathid
	 * @param session
	 * @param model
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("fileupload")
	public String uploadfile(@RequestParam("file") MultipartFile file, @RequestParam("pathid") Long pathid,
			HttpSession session, Model model) throws IllegalStateException, IOException {
		Long userid = Long.parseLong(session.getAttribute("userId") + "");
		User user = udao.findOne(userid);
		FilePath nowpath = fpdao.findOne(pathid);
		// true 表示从文件使用上传
		FileList uploadfile = (FileList) fs.savefile(file, user, nowpath, true);
		System.out.println(uploadfile);
		
		model.addAttribute("pathid", pathid);
		return "forward:/filetest";
	}

	/**
	 * @description 图纸合成
	 * @date 2022-10-26 18:30
	 */
	@RequestMapping("mergeblueprint")
	public String tomergeblueprint(@SessionAttribute("userId")Long userid,Model model) {
		List<DirManagement> dirManagementByType = dirManagementService.findDirManagementByType(1);
		User user = udao.findOne(userid);

		FilePath filepath = fpdao.findByPathName(user.getUserName());

		model.addAttribute("materialLib",dirManagementByType);
		model.addAttribute("pathid",filepath.getId());
		return "file/mergeblueprint";
	}

	@RequestMapping("getMaterialList")
	@ResponseBody
	public String getMaterialList(@RequestBody Map m,Model model) {
		List<FileList> fileLists = fs.getMaterialListById((Long) m.get("id"));
		return JSON.toJSONString(fileLists);
	}

	/**
	 * 文件分享
	 * @param pathid
	 * @param checkfileids
	 * @param model
	 * @return
	 */
	@RequestMapping("doshare")
	public String doshare(@RequestParam("pathid") Long pathid,
			@RequestParam("checkfileids") List<Long> checkfileids, 
			Model model
			){
		if (!checkfileids.isEmpty()) {
			System.out.println(checkfileids);
			fs.doshare(checkfileids);
		}
		model.addAttribute("pathid", pathid);
		model.addAttribute("message","分享成功");
		return "forward:/filetest";
	}
	
	/**
	 * 删除前台选择的文件以及文件夹
	 * 
	 * @param pathid
	 * @param checkpathids
	 * @param checkfileids
	 * @param model
	 * @return
	 */
	@RequestMapping("deletefile")
	public String deletefile(@SessionAttribute("userId") Long userid,
			@RequestParam("pathid") Long pathid,
			@RequestParam("checkpathids") List<Long> checkpathids,
			@RequestParam("checkfileids") List<Long> checkfileids, Model model) {
		System.out.println(checkfileids);
		System.out.println(checkpathids);

		if (!checkfileids.isEmpty()) {
			// 删除文件
			//fs.deleteFile(checkfileids);
			//文件放入回收战
			fileTransactionalHandlerService.trashfile(checkfileids, 1L,userid);
		}
		if (!checkpathids.isEmpty()) {
			// 删除文件夹
			//fs.deletePath(checkpathids);
			fs.trashpath(checkpathids, 1L, true);
			//fs.trashPath(checkpathids);
		}

		model.addAttribute("pathid", pathid);
		return "forward:/filetest";
	}
	
	/**
	 * 重命名
	 * @param name
	 * @param renamefp
	 * @param pathid
	 * @param model
	 * @return
	 */

	@RequestMapping("rename")
	public String rename(@RequestParam("name") String name,
			@RequestParam("renamefp") Long renamefp,
			@RequestParam("pathid") Long pathid,
			@RequestParam("isfile") boolean isfile,
			Model model){

		//这里调用重命名方法
		fs.rename(name, renamefp, pathid, isfile);
		
		model.addAttribute("pathid", pathid);
		return "forward:/filetest";
		
	}

	/**
	 * @description 审核模板管理页
	 * @date 2022-10-25 15:59
	 */
	@RequestMapping("temeplate")
	public String temeplate(Model model){
		List<Map<String,String>> l=new ArrayList<>();
		Map<String,String> thepathmap=new HashMap<>();
		thepathmap.put("id","1");
		thepathmap.put("name","陕西省-西安市-周至县-建设银行审核模板");
		thepathmap.put("path","D:\\陕西省\\西安市\\周至县\\建设银行");
		thepathmap.put("remark","建设银行图纸库");
		thepathmap.put("checktemplate","1");
		l.add(thepathmap);
		Map<String,String> thepathmap2=new HashMap<>();
		thepathmap2.put("id","2");
		thepathmap2.put("name","陕西省-西安市-周至县-招商银行审核模板");
		thepathmap2.put("path","D:\\陕西省\\西安市\\周至县\\招商银行");
		thepathmap2.put("remark","招商银行图纸库");
		thepathmap2.put("checktemplate","1");
		l.add(thepathmap2);
		Map<String,String> thepathmap3=new HashMap<>();
		thepathmap3.put("id","3");
		thepathmap3.put("name","陕西省-铜川市-新兴镇-华夏银行审核模板");
		thepathmap3.put("path","D:\\铜川市\\新兴镇\\周至县\\华夏银行");
		thepathmap3.put("remark","华夏银行图纸库");
		thepathmap3.put("checktemplate","0");
		l.add(thepathmap3);
		model.addAttribute("temeplateList", l);
		return "file/temeplate";
	}

	/**
	 * @description 审核模板管理页-新增修改
	 * @date 2022-10-25 16:00
	 */
	@RequestMapping("addthepath2")
	public String addthepath2(String id,Model model){
		Map<String,String> thepathmap2=new HashMap<>();

		model.addAttribute("pathMap", thepathmap2);
		return "file/addthepath2";
	}




	/**
	 * 移动和复制
	 * @param mctoid
	 * @param model
	 * @return
	 */
	@RequestMapping("mcto")
	public String mcto(@SessionAttribute("userId") Long userid,
			@RequestParam("morc") boolean morc,
			@RequestParam("mctoid") Long mctoid,
			@RequestParam("pathid") Long pathid,
			@RequestParam("mcfileids")List<Long> mcfileids,
			@RequestParam("mcpathids")List<Long> mcpathids,
			Model model){
		System.out.println("--------------------");
		System.out.println("mcfileids"+mcfileids);
		System.out.println("mcpathids"+mcpathids);
	
		if(morc){
			System.out.println("这里是移动！~~");
			fs.moveAndcopy(mcfileids,mcpathids,mctoid,true,userid);
		}else{
			System.out.println("这里是复制！~~");
			fs.moveAndcopy(mcfileids,mcpathids,mctoid,false,userid);
		}
		
		model.addAttribute("pathid", pathid);
		return "forward:/filetest";
	}

	/**
	 * 新建文件夹
	 * 
	 * @param pathid
	 * @param pathname
	 * @param model
	 * @return
	 */
	@RequestMapping("createpath")
	public String createpath(@SessionAttribute("userId") Long userid, @RequestParam("pathid") Long pathid, @RequestParam("pathname") String pathname,
			Model model) {
		System.out.println(pathid + "aaaaaa" + pathname);
		FilePath filepath = fpdao.findOne(pathid);
		String newname = fs.onlyname(pathname, filepath, null, 1, false);

		FilePath newfilepath = new FilePath(pathid, newname);
		newfilepath.setPathUserId(userid);
		
		System.out.println(newname);
		System.out.println(newfilepath);
		fpdao.save(newfilepath);

		model.addAttribute("pathid", pathid);
		return "forward:/filetest";
	}
	
	/**
	 * 图片预览
	 * @param response
	 * @param fileid
	 */
	@RequestMapping("imgshow")
	public void imgshow(HttpServletResponse response, @RequestParam("fileid") Long fileid) {
		FileList filelist = fldao.findOne(fileid);
		File file = fs.getFile(filelist.getFilePath());
		showfile(response, file);
	}
	
	/**
	 * 下载文件
	 * @param response
	 * @param fileid
	 */
	@RequestMapping("downfile")
	public void downFile(HttpServletResponse response, @RequestParam("fileid") Long fileid) {
		try {
			FileList filelist = fldao.findOne(fileid);
			File file = fs.getFile(filelist.getFilePath());
			response.setContentLength(filelist.getSize().intValue());
			response.setContentType(filelist.getContentType());
			response.setHeader("Content-Disposition","attachment;filename=" + new String(filelist.getFileName().getBytes("UTF-8"), "ISO8859-1"));
			writefile(response, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写文件 方法
	 * 
	 * @param response
	 * @param file
	 * @throws IOException 
	 */
	public void writefile(HttpServletResponse response, File file) {
		ServletOutputStream sos = null;
		FileInputStream aa = null;
		try {
			response.reset();
			response.setContentType("application/pdf;charset=UTF-8");
			response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
			//创建一个文件读取流
			aa = new FileInputStream(file);
			//获取浏览器的输入流
			sos = response.getOutputStream();

			// 读取文件问字节码
			byte[] data = new byte[(int) file.length()];

			IOUtils.readFully(aa, data);
			// 将文件流输出到浏览器
			IOUtils.write(data, sos);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				sos.close();
				aa.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写文件 方法
	 *
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	public void showfile(HttpServletResponse response, File file) {
		ServletOutputStream sos = null;
		FileInputStream aa = null;
		try {
			response.reset();
			//创建一个文件读取流
			aa = new FileInputStream(file);
			//获取浏览器的输入流
			sos = response.getOutputStream();

			// 读取文件问字节码
			byte[] data = new byte[(int) file.length()];

			IOUtils.readFully(aa, data);
			// 将文件流输出到浏览器
			IOUtils.write(data, sos);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				sos.close();
				aa.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 提交文件
	 * @param session
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("submitfile")
	@ResponseBody
	public ResultVO submitfile(@RequestParam("id_submitpath") Long id_submitpath,@RequestParam("fileid") Long fileid,HttpSession session) throws IOException {
		Long userid = Long.parseLong(session.getAttribute("userId") + "");

//		boolean pathflag = fs.checkPathValid(submitpath, "windows");
//		Map<String,String> msg=new HashMap<>();
//		if(!pathflag){
//			return BindingResultVOUtil.error(1, "路径异常");
//		}else{
//
//		}
		fs.updateSubmitpathById(fileid,id_submitpath);
		informService.addInfrom(userid,fileid,null,null,23L,17L);
		return BindingResultVOUtil.success();
	}




	/**
	 * 分割文件新增
	 * @param type
	 * @param fileid
	 * @param remark
	 */
	@RequestMapping("splitfileadd")
	@ResponseBody
	public void splitfileadd(Integer type,Long fileid,String remark){
		FileList list = fldao.findOne(fileid);
		String[] split = list.getFilePath().split("/");
		String path="";
		for (int i = 0; i < split.length; i++) {
			if (i != 0) {
				path+="/"+split[i];
				if (i == 3) {
					path=path+"/"+type;
				}
			}
		}
		System.out.println("path = " + path);

		FileSplit fileSplit = new FileSplit();
		fileSplit.setType(type);
		fileSplit.setDeleteflag(0);
		fileSplit.setPath(path);
		fileSplit.setFileid(fileid);
		fileSplit.setRemark(remark);
		fileMapper.addfilesplit(fileSplit);
	}

	/**
	 * 查看当前用户的PDF文件
	 * @param userid
	 * @return
	 */
	@RequestMapping("findfile")
	@ResponseBody
	public List<FileList> findfile(@SessionAttribute("userId") Long userid){
		List<FileList> findfile = fldao.findfile(userid);
		for (FileList fileList : findfile) {
			fileList.setUser(null);
		}
		return findfile;
	}

	/**
	 * 查看当前用户分割的PDF文件
	 * @param type
	 * @param userid
	 * @return
	 */
	@RequestMapping("findsplitfile")
	@ResponseBody
	public List<FileSplit> findsplitfile(Integer type,@SessionAttribute("userId") Long userid){
		List<FileSplit> findsplitfile = fileMapper.findsplitfile(type, userid);
		System.out.println("findsplitfile = " + findsplitfile);
		return  findsplitfile;
	}

	/**
	 * 删除分割文件（修改删除标记）
	 * @param splitid
	 */
	@RequestMapping("deletesplitfile")
	@ResponseBody
	public void deletesplitfile(Long splitid){
		fldao.deletesplitfile(splitid);
	}

	/**
	 * 修改备注
	 * @param remark
	 */
	@RequestMapping("updatesplitfile")
	@ResponseBody
	public void updatesplitfile(Long splitid,String remark){
		fldao.updatesplitfile(splitid,remark);
	}
}
