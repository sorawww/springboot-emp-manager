package com.fosu.edu.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysRoleUserMapper;
import com.fosu.edu.dao.SysUserMapper;
import com.fosu.edu.model.SysRoleUser;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.util.IpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
public class SysRoleUserService {
	
	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysLogService sysLogService;
	
	public List<SysUser> getListByRoleId(Integer roleId){
		List<Integer> idList = sysRoleUserMapper.getUserListByRoleId(roleId);
		if(CollectionUtils.isEmpty(idList)) {
			return Lists.newArrayList();
		}
		
		return sysUserMapper.getUsersByIdLists(idList);
		
	}
	
	
	//权限调整
	public void changeRoleUsers(int roleId,List<Integer> userIdList) {
		//判断是否和之前一致
		List<Integer> originUserIdList = sysRoleUserMapper.getUserListByRoleId(roleId);
		//如果没有变化，则放弃更新
				if(originUserIdList.size() == userIdList.size()) {
					Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
					Set<Integer> usersIdSet = Sets.newHashSet(userIdList);
					originUserIdSet.remove(usersIdSet);
					if(originUserIdList.isEmpty()) {
						return;
					}
				}
		updateRoleUsers(roleId,userIdList);
		sysLogService.saveRoleUSerLog(roleId,originUserIdList,userIdList);
 	}

	@Transactional
	private void updateRoleUsers(int roleId, List<Integer> userIdList) {
		sysRoleUserMapper.deleteUserByRoleId(roleId);
		if(CollectionUtils.isEmpty(userIdList)) {
			return;
		}
		List<SysRoleUser> roleUsersList = Lists.newArrayList();
		for(Integer userId : userIdList) {
			SysRoleUser sysRoleUser = SysRoleUser.builder()
										.roleId(roleId)
										.userId(userId)
										.operator(RequestHolder.getCurrentUser().getUsername())
										.operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
										.operateTime(new Date()).build();
		roleUsersList.add(sysRoleUser);
		}
		sysRoleUserMapper.batchinsert(roleUsersList);
	}
}
