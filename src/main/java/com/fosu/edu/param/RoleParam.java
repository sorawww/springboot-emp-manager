package com.fosu.edu.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import ch.qos.logback.core.status.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleParam {
	
	private Integer id;
	@NotBlank(message = "必须指定角色名称")
    @Length(min = 2,max = 64)
	private String name;
	
	@NotNull(message = "必须指定角色类型！")
	@Min(value = 1 ,message = "角色不合法！")
	@Max(value = 2,message = "角色不合法！")
	private Integer type =1;
	
	@NotNull(message = "状态不可用")
	@Max(value = 1,message = "角色状态不合法")
	@Min(value = 0,message = "角色状态不合法")
	private Integer status;
	
	@Length(min = 0,max = 200,message = "备注需要在256个字符以内")
	private String remark;

	
}
