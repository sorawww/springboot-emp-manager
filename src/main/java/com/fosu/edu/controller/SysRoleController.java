package com.fosu.edu.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosu.edu.common.JsonData;
import com.fosu.edu.dao.SysUserMapper;
import com.fosu.edu.dto.SysAclModuleLevelDto;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.param.RoleParam;
import com.fosu.edu.service.SysRoleAclService;
import com.fosu.edu.service.SysRoleService;
import com.fosu.edu.service.SysRoleUserService;
import com.fosu.edu.service.SysTreeService;
import com.fosu.edu.service.SysUserService;
import com.fosu.edu.util.JsonMapper;
import com.fosu.edu.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysql.cj.log.Log;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/sys/role")
@Slf4j
public class SysRoleController {
	@Autowired
	SysRoleService sysRoleService;
	
	@Autowired
	private SysTreeService sysTreeService;
	
	@Autowired
	private SysRoleAclService SysRoleAclService;
	
	@Autowired
	private SysRoleUserService sysRoleUserService;
	
	@Autowired
	private SysUserService SysUserService;
	
	@RequestMapping("role.page")
	public String getPage() {
		return "role";
	}
	
	
	
	
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData saveRole(RoleParam param) {
		sysRoleService.save(param);
		return JsonData.success();
	}
	
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData updateRole(RoleParam param) {
		sysRoleService.update(param);
		return JsonData.success();
	}
	
	
	@RequestMapping("/list.json")
	@ResponseBody
	public JsonData list() {
		return JsonData.success(sysRoleService.getAll());
	}
	
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
    	List<SysAclModuleLevelDto> list = sysTreeService.roleTree(roleId);
    	String msg =  JsonMapper.Obj2String(list);
    	log.info(msg);

        return JsonData.success(list);
    }

    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") int roleId,@RequestParam(value = "aclIds",required =false,defaultValue = "") String aclIds) {
    	List<Integer> list=StringUtil.splitToListInt(aclIds);
    	SysRoleAclService.changeRoleAcls(roleId, list);
    	return JsonData.success();
    }
    
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId,@RequestParam(value = "userIds",required =false,defaultValue = "") String userIds) {
    	List<Integer> userIdsList=StringUtil.splitToListInt(userIds);
    	sysRoleUserService.changeRoleUsers(roleId, userIdsList);
    	return JsonData.success();
    }
    
    

    
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData getUsers(@RequestParam("roleId") int roleId) {
    	List<SysUser> selectedUsers = sysRoleUserService.getListByRoleId(roleId);
    	List<SysUser> allUsers = SysUserService.getAll();
    	List<SysUser> unselectedUsers = Lists.newArrayList();
    	Set<Integer> selectedUserIdSet = selectedUsers.stream().map(sysUser-> sysUser.getId()).collect(Collectors.toSet());
    	for(SysUser sysUser : allUsers) {
    		if(sysUser.getStatus() ==1 && ! selectedUsers.contains(sysUser)) {
    			unselectedUsers.add(sysUser);
    		}
    	}
    	/*
    	log.info(JsonMapper.Obj2String(selectedUsers));
    	log.info(JsonMapper.Obj2String(unselectedUsers));*/
    	Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUsers);
        map.put("unselected", unselectedUsers);
    	return JsonData.success(map);
    }
	
}
