package com.fosu.edu.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosu.edu.common.JsonData;
import com.fosu.edu.dto.DeptLevelDto;
import com.fosu.edu.model.SysDept;
import com.fosu.edu.param.DeptParam;
import com.fosu.edu.service.SysDeptService;
import com.fosu.edu.service.SysTreeService;
import com.fosu.edu.util.JsonMapper;
import com.google.common.base.Preconditions;


import lombok.extern.slf4j.Slf4j;

@RequestMapping("/sys/dept")
@Controller
@Slf4j
public class SysDeptController {
	
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired	
	private SysTreeService sysTreeService;
	
	//保存
	//http://localhost:8080/sys/dept/save.json?id=13&parentId=0&seq=1&remark=nothing&name=%E4%BD%9C%E6%88%98%E9%83%A8
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData saveDept(DeptParam param) {
		String message = JsonMapper.Obj2String(param);
		log.info("控制器" + message);
		boolean rs = sysDeptService.save(param);
		if(rs) {
			return JsonData.success();
		}else {
			return JsonData.fail("系统故障，请联系管理员");
		}
	}
	
	
	@RequestMapping("/getTree.json")
	@ResponseBody
	public JsonData toTree() {
		//获得所有部门树
		List<DeptLevelDto> dtoList = sysTreeService.deptTree();
		return JsonData.success(dtoList);
		
	}
	//更新，当一个结构进行插入时，其他同级结构也要更新
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData update(DeptParam param) {
		log.info("更新");
		sysDeptService.update(param);
		return JsonData.success();
		
	}
	@RequestMapping("/getlevel.json")
	@ResponseBody
	public JsonData getByLevel() {
		List<SysDept> list = sysDeptService.getByLevel();
		String message = JsonMapper.Obj2String(list);
		log.info(message);
		return JsonData.success();
		
	}
	
	
	@RequestMapping("/delete.json")
	@ResponseBody
	public JsonData deleteDept(Integer id) {
		sysDeptService.deleteById(id);
		return JsonData.success();
	}
	
	//部门管理页面访问
	@RequestMapping("/dept.page")
	public String getDeptPage() {
		return "dept";
	}
	
}
