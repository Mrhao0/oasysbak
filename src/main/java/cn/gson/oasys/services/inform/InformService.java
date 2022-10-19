package cn.gson.oasys.services.inform;

import cn.gson.oasys.model.dao.filedao.FileListdao;
import cn.gson.oasys.model.dao.informdao.InformDao;
import cn.gson.oasys.model.dao.informdao.InformRelationDao;
import cn.gson.oasys.model.dao.system.StatusDao;
import cn.gson.oasys.model.dao.system.TypeDao;
import cn.gson.oasys.model.dao.user.UserDao;
import cn.gson.oasys.model.entity.file.FileList;
import cn.gson.oasys.model.entity.notice.NoticeUserRelation;
import cn.gson.oasys.model.entity.notice.NoticesList;
import cn.gson.oasys.model.entity.user.Dept;
import cn.gson.oasys.model.entity.user.User;
import cn.gson.oasys.services.file.FileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Service
@Transactional
public class InformService {
	@Autowired
	private InformRelationDao informRelationdao;

	@Autowired
	private InformDao informDao;

	@Autowired
	private InformRelationDao informrelationDao;

	@Autowired
	private TypeDao tydao;

	@Autowired
	private StatusDao sdao;

	@Autowired
	private UserDao udao;
	@Autowired
	private FileListdao fldao;
	@Autowired
	FileServices fileServices;



	// 保存通知
	public NoticesList save(NoticesList noticelist) {
		return informDao.save(noticelist);
	}

	// 删除通知
	public void deleteOne(Long noticeId) {
		NoticesList notice = informDao.findOne(noticeId);
		List<NoticeUserRelation> relationList = informrelationDao.findByNoticeId(notice);
		informrelationDao.delete(relationList);
		informDao.delete(noticeId);
		System.out.println("通知删除成功！");

	}

	// 封装
	public List<Map<String, Object>> fengZhuang(List<NoticesList> noticelist) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < noticelist.size(); i++) {
			Map<String, Object> result = new HashMap<>();
			result.put("noticeId", noticelist.get(i).getNoticeId());
			result.put("typename", tydao.findname(noticelist.get(i).getTypeId()));
			result.put("statusname", sdao.findname(noticelist.get(i).getStatusId()));
			result.put("statuscolor", sdao.findcolor(noticelist.get(i).getStatusId()));
			result.put("title", noticelist.get(i).getTitle());
			result.put("noticeTime", noticelist.get(i).getNoticeTime());
			result.put("top", noticelist.get(i).getTop());
			result.put("url", noticelist.get(i).getUrl());
			result.put("username", udao.findOne(noticelist.get(i).getUserId()).getUserName());
			result.put("deptname", udao.findOne(noticelist.get(i).getUserId()).getDept().getDeptName());
			list.add(result);
		}
		return list;

	}
	public Page<NoticesList> pageThis(int page,Long userId){
		int size=10;
		Sort sort = getSort();
		Pageable pa = new PageRequest(page, size, sort);
		return informDao.findByUserId(userId, pa);
	}

	private Sort getSort() {
		List<Order> orders = new ArrayList<>();
		orders.addAll(Arrays.asList(new Order(Direction.DESC, "top"), new Order(Direction.DESC, "modifyTime")));
		Sort sort = new Sort(orders);
		return sort;
	}
	
	public Page<NoticesList> pageThis(int page, Long userId,String baseKey,Object type,Object status,Object time) {
		int size=10;
		List<Order> orders = new ArrayList<>();
		Pageable pa=null;
		//根据类型排序
		if(!StringUtils.isEmpty(type)){
			if("1".equals(type)){
				orders.add(new Order(Direction.DESC, "typeId"));
			}
			else{
				orders.add(new Order(Direction.ASC, "typeId"));
			}
		}
		//根据状态排序
		else if(!StringUtils.isEmpty(status)){
			if("1".equals(status)){
				orders.add(new Order(Direction.DESC, "statusId"));
			}
			else{
				orders.add(new Order(Direction.ASC, "statusId"));
			}
		}
		//根据时间排序
		else if(!StringUtils.isEmpty(time)){
			if("1".equals(time)){
				orders.add(new Order(Direction.DESC, "modifyTime"));
			}
			else{
				orders.add(new Order(Direction.ASC, "modifyTime"));
			}
		}
		else if (!StringUtils.isEmpty(baseKey)) {
			String key="%"+baseKey+"%";
			Sort sort = getSort();
			pa=new PageRequest(page, size, sort);
			
			return informDao.findByBaseKey(userId, key,pa);
		}
		System.out.println("orders:"+orders);
		if(orders.size()>0){
			Sort sort = new Sort(orders);
			 pa= new PageRequest(page, size, sort);
		}else{
			pa=new PageRequest(page, size);
		}
		return informDao.findByUserId(userId, pa);
	}

    public void addInfrom(Long userid, Long fileid,String remark,Long receiveUserId) {
	    String fileName="通知";
		if(fileid!=null){
			FileList one = fldao.findOne(fileid);
			fileName = one.getFileName();
		}

	    NoticesList noticesList = new NoticesList();
		noticesList.setTitle(fileName);
		noticesList.setNoticeTime(new Date());
		noticesList.setStatusId(14L);
		noticesList.setTypeId(10L);
		noticesList.setUserId(userid);
	    noticesList.setFile_id(fileid);
	    noticesList.setContent(remark);

	    NoticesList save = informDao.save(noticesList);

	    if(receiveUserId!=null){
		    User one1 = udao.findOne(receiveUserId);
		    NoticeUserRelation noticeUserRelation=new NoticeUserRelation();
		    noticeUserRelation.setNoticeId(save);
		    noticeUserRelation.setUserId(one1);
		    noticeUserRelation.setRead(false);
		    informRelationdao.save(noticeUserRelation);
	    }else{

		    Dept dept=new Dept();
		    dept.setDeptId(1L);
		    List<User> byDept = udao.findByDept(dept);
		    for(User u:byDept){
			    NoticeUserRelation noticeUserRelation=new NoticeUserRelation();
			    noticeUserRelation.setNoticeId(save);
			    noticeUserRelation.setUserId(u);
			    noticeUserRelation.setRead(false);
			    informRelationdao.save(noticeUserRelation);
		    }
	    }

	}


	public void updatestatus(Long noticeId) {
		Long statusid=25L;
		informDao.updatestatus(statusid,noticeId);
	}


    public FileList auditfind(Long noticeId) {
		NoticesList list=informDao.findOne(noticeId);
		String title=list.getTitle().replaceAll("\n","");
		FileList lists=informDao.findByFilename(title);
		return null;
	}

	public void deleteaudit(Long noticeId) {
		informDao.delete(noticeId);
	}

	public void deleteAndBackSend(Long noticesListId, String remark, Long userid) {
		NoticesList one = informDao.findOne(noticesListId);
		Long userId2 = one.getUserId();
		deleteOne(noticesListId);
		addInfrom(userid,null,remark,userId2);
	}

	public void agreeAndSubmit(Long noticesListId, Long fileid) {
		NoticesList one = informDao.findOne(noticesListId);
		one.setStatusId(25L);
		one.setTypeId(11L);
		FileList one1 = fldao.findOne(fileid);
		String submit_path = one1.getSubmit_path();
		String filePath = one1.getFilePath();
		File s = fileServices.getFile(filePath);
		File t = new File(submit_path);
		fileServices.copyfileio(s,t);
	}
}
