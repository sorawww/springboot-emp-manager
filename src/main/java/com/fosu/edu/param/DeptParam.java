package com.fosu.edu.param;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeptParam {
	private Integer id;
	
	@NotBlank(message = "部门名称不能为空")
	@Length(max = 15,min =2,message = "部门名称长度不符合要求")
	private String name;
	
	
	private Integer parentId = 0;
	
	@NotNull(message = "顺序不能为空")
	private Integer seq;
	
	@Length(max = 150,message = "备注长度150字以内")
	private String remark;
}
