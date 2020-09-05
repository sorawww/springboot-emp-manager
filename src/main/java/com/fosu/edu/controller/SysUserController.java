package com.fosu.edu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.bean.PageResult;
import com.fosu.edu.common.JsonData;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.param.UserParam;
import com.fosu.edu.service.SysRoleService;
import com.fosu.edu.service.SysTreeService;
import com.fosu.edu.service.SysUserService;
import com.google.common.collect.Maps;


@Controller
@RequestMapping("sys/user")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysTreeService sysTreeService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	
	
	@RequestMapping("/noAuth.page")
	public String getNoAuthPage() {
		return "noAuth";
	}
	
	
	
	
	
	@RequestMapping("/save.json")
	@ResponseBody
	public JsonData save(UserParam param) {
		sysUserService.save(param);
		return JsonData.success();
	}
	
	
	@RequestMapping("/update.json")
	@ResponseBody
	public JsonData update(UserParam param) {
		sysUserService.update(param);
		return JsonData.success();
	}
	
	
	@RequestMapping("/page.json")
	@ResponseBody
	public JsonData page(@RequestParam("deptId") Integer deptId,PageQuery page) {
		PageResult<SysUser> result = sysUserService.getPageById(deptId, page);
		return JsonData.success(result);
	}
	
	@RequestMapping("/acls.json")
	@ResponseBody
	public JsonData acls(@RequestParam("userId") int userId) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("acls", sysTreeService.userAclTree(userId));
		map.put("roles",sysRoleService.getRoleListByUserId(userId));
		return JsonData.success(map);
	}
}
