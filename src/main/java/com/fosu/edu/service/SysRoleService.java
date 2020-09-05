package com.fosu.edu.service;


import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fosu.edu.common.JsonData;
import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysRoleAclMapper;
import com.fosu.edu.dao.SysRoleMapper;
import com.fosu.edu.dao.SysRoleUserMapper;
import com.fosu.edu.dao.SysUserMapper;
import com.fosu.edu.exception.ParamException;
import com.fosu.edu.model.SysRole;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.param.RoleParam;
import com.fosu.edu.util.BeanValidator;
import com.fosu.edu.util.IpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

@Service
public class SysRoleService {
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;
	
	@Autowired
	private SysRoleAclMapper sysRoleAclMapper;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	protected SysLogService sysLogService;
	
	//暂时决定角色名称不可重复
	public void save(RoleParam param) {
		BeanValidator.check(param);
		if(checkExits(param.getName(), param.getId())) {
			throw new ParamException();
		};
		SysRole role = SysRole.builder()
				.name(param.getName())
				.type(param.getType())
				.remark(param.getRemark())
				.status(param.getStatus())
				.build();
		role.setOperator(RequestHolder.getCurrentUser().getUsername());
		role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		role.setOperateTime(new Date());
		sysRoleMapper.insertSelective(role);
		sysLogService.saveRoleLog(null, role);
		
		
	}	
	public void update(RoleParam param) {
		BeanValidator.check(param);
		SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
		Preconditions.checkNotNull(before,"没有之前的对象！");
		
		
		SysRole role = SysRole.builder()
				.name(param.getName())
				.type(param.getType())
				.remark(param.getRemark())
				.id(param.getId())
				.status(param.getStatus())
				.build();	
		role.setOperator(RequestHolder.getCurrentUser().getUsername());
		role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		role.setOperateTime(new Date());
		sysRoleMapper.updateByPrimaryKey(role);
		sysLogService.saveRoleLog(before, role);
	}
	
	public List<SysRole> getAll(){
		return sysRoleMapper.getAll();
	}
	
	
	private boolean checkExits(String name, Integer id) {
		return sysRoleMapper.countByName(name, id)>0;
	}
	
	public List<SysRole> getRoleListByUserId(int userId){
		List<Integer> idList = sysRoleUserMapper.getRoleIdListByUserId(userId);
		if(CollectionUtils.isEmpty(idList)) {
			return Lists.newArrayList();
		}
		return sysRoleMapper.getRoleByIdList(idList);
	}
	
	
	public List<SysRole> getRolesByAclId(int aclId){
		List<Integer> rolesIdList = sysRoleAclMapper.getRolesIdListByaclId(aclId);
		if(CollectionUtils.isEmpty(rolesIdList)) {
			return Lists.newArrayList();
		}
		List<SysRole> rolesList = sysRoleMapper.getRoleByIdList(rolesIdList);
		return rolesList;
	}
	
	public List<SysUser> getRolesByRoleList(List<SysRole> roleList){
		if(CollectionUtils.isEmpty(roleList)) {
			return Lists.newArrayList();
		}
		List<Integer> roleIdList = roleList.stream().map(role->role.getId()).collect(Collectors.toList());
		List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
		if(CollectionUtils.isEmpty(userIdList)) {
			return Lists.newArrayList();
		}
		List<SysUser> userList = sysUserMapper.getUsersByIdLists(userIdList);
		return userList;
		
	}
	
}
