package com.fosu.edu.dto;


import java.util.List;
import org.springframework.beans.BeanUtils;
import com.fosu.edu.model.SysAclModule;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class SysAclModuleLevelDto extends SysAclModule {
	

	//自己包含自己 树形结构
	private List<SysAclModuleLevelDto> aclModuleList = Lists.newArrayList();
	
	
	private List<AclDto> aclList = Lists.newArrayList();
	
	//把传入的对象copy为本地的dto
	public static SysAclModuleLevelDto aclLevelDto(SysAclModule module) {
		SysAclModuleLevelDto dto = new SysAclModuleLevelDto();
		BeanUtils.copyProperties(module, dto);
		return dto;
	}
	
	//挂接权限点
	
}
