package com.fosu.edu.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserParam {

	private Integer id;
	@NotBlank(message = "用户名不可以为空")	
	@Length(max = 20,min = 2,message = "用户名长度不符合")
	private String username;
	
	@NotBlank
	@Length(max = 13,message = "电话格式不对！")
	private String telephone;

	@NotBlank(message = "邮箱不为空")
	@Length(max = 20 ,message = "邮箱长度过长")
	private String mail;

	@NotNull(message ="必须提供所在的部门！")
	private Integer deptId;
	
	@NotNull
	@Min(value = 0,message = "状态不合法")
	@Max(value = 2,message = "状态不合法")
	private Integer status;

	@Length(max = 200,message = "备注过长！请在200以内！")
	private String remark;
	

}
