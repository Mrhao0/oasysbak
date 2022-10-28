package cn.gson.oasys.controller.file;

import antlr.StringUtils;
import cn.gson.oasys.common.formValid.BindingResultVOUtil;
import cn.gson.oasys.common.formValid.ResultVO;
import cn.gson.oasys.model.entity.file.DirManagement;
import cn.gson.oasys.services.file.DirManagementService;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class DirManagementController {
	@Autowired
	DirManagementService dirManagementService;
	/**
	 * @description 跳页-目录管理
	 * @date 2022-10-25 16:00
	 */
	@RequestMapping("thepath")
	public String toThepath(@RequestParam(value="page",defaultValue="0") int page, Model model,
	                        @RequestParam(value="size",defaultValue="10") int size,Integer selectType){
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable pa=new PageRequest(page, size,sort);

		Page<DirManagement> all;
		if(selectType!=null){
			all=dirManagementService.findDirManagementByType(selectType,pa);
		}else{
			all = dirManagementService.findAll(pa);
		}
		model.addAttribute("page", all);
		model.addAttribute("pathList", all.getContent());
		return "file/thepath";
	}

	/**
	 * @description 目录管理-新增修改页
	 * @date 2022-10-25 16:00
	 */
	@RequestMapping("addthepath")
	public String toAddAndEditThepath(Long id,Model model){
		DirManagement dirManagement=null;
		if(id!=null){
			dirManagement=dirManagementService.findOne(id);
		}
		model.addAttribute("pathMap", dirManagement);
		return "file/addthepath";
	}
	@RequestMapping("getSubmitPath")
	@ResponseBody
	public String getSubmitPath(){
		List<DirManagement> dirManagementByType = dirManagementService.findDirManagementByType(0);
		return JSON.toJSONString(dirManagementByType);
	}

	/**
	 * @description 添加或修改
	 * @date 2022-10-25 16:00
	 */
	@RequestMapping("addOrEditDirManagement")
	public String addOrEditDirManagement(DirManagement dirManagemen){
		Long id = dirManagemen.getId();
		dirManagemen.setPath(dirManagemen.getPath().replace("\\","/"));
		if(id!=null){
			dirManagementService.edit(dirManagemen);
		}else {
			dirManagementService.save(dirManagemen);
		}
		return "forward:/thepath";
	}
	/**
	 * @description 删除
	 * @author 郑昊
	 * @date 2022-10-27 1:22
	 */
	@RequestMapping("deleteDirManagement")
	public String deleteDirManagement(Long id){
		dirManagementService.deleteDirManagement(id);
		return "forward:/thepath";
	}

}
