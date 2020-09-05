package com.fosu.edu.service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fosu.edu.bean.CacheKeyConstants;
import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysAclMapper;
import com.fosu.edu.dao.SysRoleAclMapper;
import com.fosu.edu.dao.SysRoleUserMapper;
import com.fosu.edu.model.SysAcl;
import com.fosu.edu.util.JsonMapper;
import com.fosu.edu.util.StringUtil;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysCoreService {
	@Autowired
    private SysAclMapper sysAclMapper;
	
	@Autowired
	private SysRoleUserMapper sysRoleUserMapper;
	
	@Autowired
	private SysRoleAclMapper sysRoleAclMapper;
	
	
	@Autowired
	private SysCacheService sysCacheService;
	
	public List<SysAcl> getCurrentUserAclList(){
		//获取userid
		int userId = RequestHolder.getCurrentUser().getId();
		return getUserAclList(userId);
		
	}
	
	
	public List<SysAcl> getRoleAclList(int roleId){
		List<Integer> list =  Lists.newArrayList(roleId);
		list =  sysRoleAclMapper.getAclIdListByRoleIdList(list);
		if(CollectionUtils.isEmpty(list)) {
			return Lists.newArrayList();
		}
		return sysAclMapper.getByIdList(list);
	}
	
	//查询一个用户的权限点列表
	public List<SysAcl> getUserAclList(int userId){
		if(isSuperAdmin()) {
			return sysAclMapper.getAll();
		}
		 List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
	        if (CollectionUtils.isEmpty(userRoleIdList)) {
	            return Lists.newArrayList();
	        }
	        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
	        if (CollectionUtils.isEmpty(userAclIdList)) {
	            return Lists.newArrayList();
	        }
	        return sysAclMapper.getByIdList(userAclIdList);
		
		
		
	}
	
	
	//超级管理员
	public boolean isSuperAdmin() {
		//TODO
		
		return false;
	}
	
	public boolean hasUrlAcl(String path) {
		if(isSuperAdmin()) {
			return true;
		}
		List<SysAcl> aclList = sysAclMapper.getByUrl(path);
		
		if(CollectionUtils.isEmpty(aclList)) {
			return true;
		}
		List<SysAcl> userAclList = getCurrentUserAclList();
		Set<Integer> userAclIdsSet = userAclList.stream().map(acl ->acl.getId()).collect(Collectors.toSet());
		
		//只要有一个权限点有权限，就认为有访问权限
		boolean hasValidAcl = true;
		
		for(SysAcl acl :aclList) {
			if(acl == null || acl.getStatus() != 1) {
				continue;
			}
			hasValidAcl = true;
			if(userAclIdsSet.contains(acl.getId())) {
				return true;
			}
			
			
		}
		if(!hasValidAcl) {
			return true;
		}
		return false;
	}
	
	public List<SysAcl> getCurrentUserAclListFromCache(){
		int userId = RequestHolder.getCurrentUser().getId();
		String cacheKeyValue =sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS,String.valueOf(userId));
		if(StringUtils.isBlank(cacheKeyValue)) {
			//无法从redis取出，从mysql取出
			List<SysAcl> aclList = getCurrentUserAclList();
			sysCacheService.saveCache(JsonMapper.Obj2String(aclList), 600, CacheKeyConstants.USER_ACLS,String.valueOf(userId));
			return aclList;
		}
		return JsonMapper.String2Obj(cacheKeyValue, new TypeReference<List<SysAcl>>(){
		}) ;
		
	}
}
