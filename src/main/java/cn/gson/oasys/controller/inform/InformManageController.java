package cn.gson.oasys.controller.inform;

import cn.gson.oasys.common.formValid.BindingResultVOUtil;
import cn.gson.oasys.common.formValid.MapToList;
import cn.gson.oasys.common.formValid.ResultEnum;
import cn.gson.oasys.common.formValid.ResultVO;
import cn.gson.oasys.mappers.NoticeMapper;
import cn.gson.oasys.model.dao.informdao.InformDao;
import cn.gson.oasys.model.dao.informdao.InformRelationDao;
import cn.gson.oasys.model.dao.system.StatusDao;
import cn.gson.oasys.model.dao.system.TypeDao;
import cn.gson.oasys.model.dao.user.DeptDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.file.FileList;
import cn.gson.oasys.model.entity.notice.NoticeUserRelation;
import cn.gson.oasys.model.entity.notice.NoticesList;
import cn.gson.oasys.model.entity.system.SystemStatusList;
import cn.gson.oasys.model.entity.system.SystemTypeList;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.services.file.DirManagementService;
import cn.gson.oasys.services.file.FileServices;
import cn.gson.oasys.services.inform.InformRelationService;
import cn.gson.oasys.services.inform.InformService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/")
public class InformManageController {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private StatusDao statusDao;

	@Autowired
	private TypeDao typeDao;

	@Autowired
	private InformDao informDao;

	@Autowired
	private InformService informService;

	@Autowired
	private UserDao uDao;

	@Autowired
	private DeptDao deptDao;

	@Autowired
	private InformRelationDao informrelationDao;

	@Autowired
	private InformRelationService informrelationservice;

	@Autowired
	DirManagementService dirManagementService;

	@Autowired
	private NoticeMapper nm;
	@Autowired
	private FileServices fs;
	/**
	 * 通知管理面板
	 * 
	 * @return
	 */
	@RequestMapping("infrommanage")
	public String inform(@RequestParam(value = "page", defaultValue = "0") int page,@SessionAttribute("userId") Long userId,Model model) {
		Page<NoticesList> page2 = informService.pageThis(page,userId);
		List<NoticesList> noticeList=page2.getContent();
		List<Map<String, Object>> list=informService.fengZhuang(noticeList);
		model.addAttribute("list", list);
		model.addAttribute("page", page2);
		//设置变量，需要load的url；
		model.addAttribute("url", "infrommanagepaging");
		return "inform/informmanage";
	}
	
	@RequestMapping("forwardother")
	public String forwardOther(@SessionAttribute("userId")Long userId,@RequestParam(value="noticeId")Long noticeId){
		List<User> users=uDao.findByFatherId(userId);
		NoticesList nl=informDao.findOne(noticeId);
		List<NoticeUserRelation> nurs=new  ArrayList<>();
		for (User user : users) {
			nurs.add(new NoticeUserRelation(nl, user, false));
		}
		informrelationservice.saves(nurs);
		return "redirect:/infromlist";
	}

	/**
	 * 通知管理删除
	 */
	@RequestMapping("infromdelete")
	public String infromDelete(HttpSession session, HttpServletRequest req) {
		Long noticeId = Long.parseLong(req.getParameter("id"));
		Long userId = Long.parseLong(session.getAttribute("userId") + "");
		NoticesList notice = informDao.findOne(noticeId);
		if (!Objects.equals(userId, notice.getUserId())) {
			System.out.println("权限不匹配，不能删除");
			return "redirect:/notlimit";
		}
		System.out.println(noticeId);
		informService.deleteOne(noticeId);
		return "redirect:/infrommanage";

	}

	/**
	 * 通知列表删除
	 */
	@RequestMapping("informlistdelete")
	public String informListDelete(HttpServletRequest req, HttpSession session) {
		Long userId = Long.parseLong(session.getAttribute("userId") + "");
		Long noticeId = Long.parseLong(req.getParameter("id"));
		NoticeUserRelation relation = informrelationDao.findByUserIdAndNoticeId(uDao.findOne(userId),
				informDao.findOne(noticeId));
		if (Objects.isNull(relation)) {
			System.out.println("权限不匹配，不能删除");
			return "redirect:/notlimit";
		}
		informrelationservice.deleteOne(relation);
		return "forward:/infromlist";
	}

	/**
	 * 通知列表
	 * 
	 * @return
	 */
	@RequestMapping("infromlist")
	public String infromList(HttpSession session, HttpServletRequest req, Model model,
							 @RequestParam(value="pageNum",defaultValue="1") int page) {
		Long userId = Long.parseLong(session.getAttribute("userId") + "");
		PageHelper.startPage(page, 10);
		List<Map<String, Object>> list = nm.findMyNotice(userId);
		PageInfo<Map<String, Object>> pageinfo=new PageInfo<Map<String, Object>>(list);
		List<Map<String, Object>> list2=informrelationservice.setList(list);
		for (Map<String, Object> map : list2) {
			System.out.println(map);
		}
		model.addAttribute("url", "informlistpaging");
		model.addAttribute("list", list2);
		model.addAttribute("page", pageinfo);
		System.out.println(pageinfo);
		return "inform/informlist";
	}

	/**
	 * 编辑通知界面
	 */
	@RequestMapping("informedit")
	public String infromEdit(HttpServletRequest req, HttpSession session, Model model) {
		session.removeAttribute("noticeId");
		List<SystemTypeList> typeList = typeDao.findByTypeModel("inform");
		List<SystemStatusList> statusList = statusDao.findByStatusModel("inform");
		if (!StringUtils.isEmpty(req.getAttribute("errormess"))) {
			req.setAttribute("errormess", req.getAttribute("errormess"));
		}
		if (!StringUtils.isEmpty(req.getAttribute("success"))) {
			req.setAttribute("success", "数据保存成功");
		}
		req.setAttribute("typeList", typeList);
		req.setAttribute("statusList", statusList);
		if (!StringUtils.isEmpty(req.getParameter("id"))) {
			Long noticeId = Long.parseLong(req.getParameter("id"));
			NoticesList noticeList = informDao.findOne(noticeId);
			model.addAttribute("noticeList", noticeList);
			model.addAttribute("typeName", typeDao.findOne(noticeList.getTypeId()).getTypeName());
			model.addAttribute("statusName", statusDao.findOne(noticeList.getStatusId()).getStatusName());
			session.setAttribute("noticeId", noticeId);
		}

		return "inform/informedit";
	}

	/**
	 * 详细通知显示
	 */
	@RequestMapping("informshow")
	public String informShow(HttpServletRequest req, Model model) {
		Long noticeId = Long.parseLong(req.getParameter("id"));
		if (!StringUtils.isEmpty(req.getParameter("read"))) {
			if (("0").equals(req.getParameter("read"))) {
				Long relationId = Long.parseLong(req.getParameter("relationid"));
				NoticeUserRelation relation = informrelationDao.findOne(relationId);
				relation.setRead(true);
				informrelationservice.save(relation);
			}
		}
		NoticesList notice = informDao.findOne(noticeId);
		User user = uDao.findOne(notice.getUserId());
		model.addAttribute("notice", notice);
		model.addAttribute("userName", user.getUserName());
		return "inform/informshow";
	}

	/**
	 * 系统管理表单验证
	 * 
	 * @param req
	 * @param menu
	 * @param br
	 *            后台校验表单数据，不通过则回填数据，显示错误信息；通过则直接执行业务，例如新增、编辑等；
	 * @return
	 */
	@RequestMapping("informcheck")
	public String testMess(HttpServletRequest req, @Valid NoticesList menu, BindingResult br) {
		HttpSession session = req.getSession();
		Long menuId = null;
		req.setAttribute("menuObj", menu);
		Long userId = Long.parseLong(session.getAttribute("userId") + "");
		menu.setUserId(userId);

		// 这里返回ResultVO对象，如果校验通过，ResultEnum.SUCCESS.getCode()返回的值为200；否则就是没有通过；
		ResultVO res = BindingResultVOUtil.hasErrors(br);
		// 校验失败
		if (!ResultEnum.SUCCESS.getCode().equals(res.getCode())) {
			List<Object> list = new MapToList<>().mapToList(res.getData());
			req.setAttribute("errormess", list.get(0).toString());
			// 代码调试阶段，下面是错误的相关信息；
			System.out.println("list错误的实体类信息：" + menu);
			System.out.println("list错误详情:" + list);
			System.out.println("list错误第一条:" + list.get(0));
			System.out.println("啊啊啊错误的信息——：" + list.get(0).toString());
			// 下面的info信息是打印出详细的信息
			log.info("getData:{}", res.getData());
			log.info("getCode:{}", res.getCode());
			log.info("getMsg:{}", res.getMsg());
		}
		// 校验通过，下面写自己的逻辑业务
		else {
			// 判断是否从编辑界面进来的，前面有"session.setAttribute("getId",getId);",在这里获取，并remove掉；
			if (!StringUtils.isEmpty(session.getAttribute("noticeId"))) {
				menuId = (Long) session.getAttribute("noticeId"); // 获取进入编辑界面的menuID值
				NoticesList inform = informDao.findOne(menuId);
				menu.setNoticeTime(inform.getNoticeTime());
				menu.setNoticeId(menuId);
				session.removeAttribute("noticeId");
				informService.save(menu);
			} else {
				menu.setNoticeTime(new Date());
				menu.setUserId(userId);
				NoticesList noticeList = informService.save(menu);
				List<User> userList = uDao.findByFatherId(userId);
				for (User user : userList) {
					informrelationservice.save(new NoticeUserRelation(noticeList, user, false));
				}
			}
			// 执行业务代码
			System.out.println("此操作是正确的");
			req.setAttribute("success", "后台验证成功");
		}
		System.out.println("是否进入最后的实体类信息：" + menu);
		return "forward:/informedit";
	}


	@GetMapping("updateInformStatus/{fileId}")
	public String updateInformStatus(@PathVariable Long fileId){
//		informDao.updatestatus(24L,fileId);
	    return "forward:/infromlist";
	}
	/**
	 * @description 审查页
	 * @date 2022-10-19 11:57
	 */
	@GetMapping("tocheck")
	public String tocheck(String noticesListId,Long fileid,HttpServletRequest req, Model model){
		FileList one = fs.findone(fileid);
		String path = one.getId_dir_management().getPath();
		model.addAttribute("submitpath", path);
		model.addAttribute("noticesListId", noticesListId);
		model.addAttribute("fileid", fileid);

		return "inform/check";
	}
	/**
	 * @description 查看页
	 * @date 2022-10-19 11:57
	 */
	@GetMapping("newpdf")
	public String newpdf(String fileid, Model model){
		model.addAttribute("fileid", fileid);
		return "inform/newpdf";
	}
	/**
	 * @description 拒绝
	 * @date 2022-10-19 17:25
	 */
	@GetMapping("refuse")
	public String refuse(Long noticesListId,String remark,HttpSession session){
		Long userid = Long.parseLong(session.getAttribute("userId") + "");
		informService.deleteAndBackSend(noticesListId,remark,userid);
		return "forward:/infromlist";
	}

	/**
	 * @description 同意
	 * @date 2022-10-19 17:25
	 */
	@GetMapping("agree")
	public String agree(Long noticesListId,Long fileid,HttpSession session){
		Long userid = Long.parseLong(session.getAttribute("userId") + "");
		informService.agreeAndSubmit(noticesListId,fileid,userid);
		return "forward:/infromlist";
	}

}
