package com.fosu.edu.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.bean.PageResult;
import com.fosu.edu.common.JsonData;
import com.fosu.edu.dao.SysRoleAclMapper;
import com.fosu.edu.model.SysAcl;
import com.fosu.edu.model.SysRole;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.param.AclParam;
import com.fosu.edu.service.AclService;
import com.fosu.edu.service.SysRoleAclService;
import com.fosu.edu.service.SysRoleService;
import com.fosu.edu.service.SysRoleUserService;
import com.fosu.edu.service.SysUserService;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class AclController {
	
	@Autowired
	private AclService aclService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysRoleUserService sysRoleUserService;
	
	
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData saveAcl(AclParam param) {
		aclService.save(param);
		return JsonData.success();
	}
	
	
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData update(AclParam param) {
		log.info("===============update====================");
		aclService.update(param);
		
		return JsonData.success();
	}
	
	@RequestMapping("/page.json")
	@ResponseBody
	public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId,PageQuery pageQuery) {
		PageResult<SysAcl> rs  =aclService.findAclsByModuleId(aclModuleId,pageQuery);
		return JsonData.success(rs);
	}
	
	@RequestMapping("acls.json")
	@ResponseBody
	public JsonData acls(@RequestParam("aclId") int aclId) {
		Map<String, Object> map = Maps.newHashMap();
		List<SysRole> roleList = sysRoleService.getRolesByAclId(aclId) ;
		List<SysUser> userList = sysRoleService.getRolesByRoleList(roleList);
		map.put("roles",roleList);
		map.put("users", userList);
		
		return JsonData.success();
	}
	
}
