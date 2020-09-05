package com.fosu.edu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysRoleAclMapper;
import com.fosu.edu.model.SysRoleAcl;
import com.fosu.edu.util.IpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
public class SysRoleAclService {
	@Autowired
	private SysRoleAclMapper sysRoleAclMapper;
	
	@Autowired
	private SysLogService sysLogService;
	
	public void changeRoleAcls(Integer roleId,List<Integer>aclIdList) {
		//先取出原来的权限列表
		List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
		
		//如果没有变化，则放弃更新
		if(originAclIdList.size() == aclIdList.size()) {
			Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
			Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
			originAclIdSet.remove(aclIdSet);
			if(originAclIdList.isEmpty()) {
				return;
			}
		}
		//需要更新
		//删除旧的权限，增加新的权限（事务操作）
		updateRoleAcls(roleId, aclIdList);
		sysLogService.saveRoleAclLog(roleId, originAclIdList, aclIdList);
		
	}
	
	@Transactional
	public void updateRoleAcls(int  roleId,List<Integer> aclIdList) {
		sysRoleAclMapper.deleteByRoleId(roleId);
		//要新增的为空
		if(CollectionUtils.isEmpty(aclIdList)) {
			return;
		}
		
		List<SysRoleAcl> roleAclList =  Lists.newArrayList();
		
		for(Integer aclId:aclIdList) {
			SysRoleAcl roleAcl= SysRoleAcl.builder()
					.roleId(roleId)
					.aclId(aclId)
					.operator(RequestHolder.getCurrentUser().getUsername())
					.operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
					.operateTime(new Date()).build();
			roleAclList.add(roleAcl);
		}
		sysRoleAclMapper.batchInsert(roleAclList);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
