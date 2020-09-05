package com.fosu.edu.dto;

import org.springframework.beans.BeanUtils;

import com.fosu.edu.model.SysAcl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AclDto extends SysAcl{
	private boolean checked = false;
	
	private boolean hasAcl = false;
	
	//adapt 将SysAcl转化为AclDto
	
	public static AclDto adapt(SysAcl acl) {
		AclDto dto = new AclDto();
		BeanUtils.copyProperties(acl, dto);
		return dto;
	}

	
}
