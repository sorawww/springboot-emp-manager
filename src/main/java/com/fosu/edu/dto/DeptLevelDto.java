package com.fosu.edu.dto;



import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fosu.edu.model.SysDept;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {
	//自己包含自己 树形结构
	private List<DeptLevelDto> deptList = Lists.newArrayList();
	
	//把传入的对象copy为本地的dto
	public static DeptLevelDto adapt(SysDept dept) {
		DeptLevelDto dto = new DeptLevelDto();
		BeanUtils.copyProperties(dept, dto);
		return dto;
	}	
}
