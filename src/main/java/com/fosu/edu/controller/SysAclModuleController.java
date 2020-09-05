package com.fosu.edu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosu.edu.common.JsonData;
import com.fosu.edu.dto.SysAclModuleLevelDto;
import com.fosu.edu.model.SysAclModule;
import com.fosu.edu.param.AclMouleParam;
import com.fosu.edu.service.SysAclModuleService;
import com.fosu.edu.service.SysTreeService;
import com.fosu.edu.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {
	@Autowired
	private SysAclModuleService SysAclModuleService;
	
	@Autowired	
	private SysTreeService sysTreeService;
	
	@RequestMapping("/acl.page")
	public String getAclPage() {
		return "acl";
	}
	
	
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData saveAclModule(AclMouleParam param) {
		SysAclModuleService.save(param);
		return JsonData.success();
	}
	
	
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateAclModule(AclMouleParam param) {
		SysAclModuleService.update(param);
		return JsonData.success();
	}
	
	
	@RequestMapping("/tree.json")
	@ResponseBody
	public JsonData gettree() {
		List<SysAclModuleLevelDto> dtoList = sysTreeService.aclModuleTree();
		return JsonData.success(dtoList);
	}
	
	@RequestMapping("/delete.json")
	@ResponseBody
	public JsonData delete(@RequestParam("id") int id) {
		SysAclModuleService.delete(id);
		return JsonData.success();
	}
}
