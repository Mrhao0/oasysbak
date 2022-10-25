package cn.gson.oasys.controller.file;

import cn.gson.oasys.mappers.FileMapper;
import cn.gson.oasys.model.dao.filedao.FileListdao;
import cn.gson.oasys.model.dao.filedao.FilePathdao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.file.FileList;
import cn.gson.oasys.model.entity.file.FilePath;
import cn.gson.oasys.model.entity.file.FileSplit;
import cn.gson.oasys.model.entity.user.User;
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

	@RequestMapping("temeplate")
	public String temeplate(){

		return "file/temeplate";
	}

	@RequestMapping("thepath")
	public String thepath(){

		return "file/thepath";
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
	public String submitfile(@RequestParam("submitpath") String submitpath,@RequestParam("fileid") Long fileid,HttpSession session) throws IOException {
		Long userid = Long.parseLong(session.getAttribute("userId") + "");
		boolean pathflag = fs.checkPathValid(submitpath, "windows");
		Map<String,String> msg=new HashMap<>();
		if(!pathflag){
			msg.put("status","1");
			msg.put("msg","路径异常");
		}else{
			msg.put("status","0");
			msg.put("msg","提交成功");
			fs.updateSubmitpathById(fileid,submitpath);
			informService.addInfrom(userid,fileid,null,null,23L,17L);
		}
		String s = JSON.toJSONString(msg);
		return s;
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
