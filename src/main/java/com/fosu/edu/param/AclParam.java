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
public class AclParam {
	
	    private Integer id;

	   
	    @NotBlank(message = "必须指定权限点名称")
	    @Length(min = 2,max = 64)
	    private String name;

	    @NotNull(message = "必须制定权限模块")
	    private Integer aclModuleId;

	    @Length(min = 5,max = 100,message = "url长度不合法")
	    private String url;

	    @NotNull(message = "必须指定权限点类型！")
	    @Min(value = 0 ,message = "类型不合法！")
	    @Max(value = 2,message = "类型不合法！")
	    private Integer type;

	    @NotNull(message = "必须指定权限点状态！")
	    @Min(value = 0 ,message = "状态不合法！")
	    @Max(value = 2,message = "状态不合法！")
	    private Integer status;

	    @NotNull(message = "必须制定顺序")
	    private Integer seq;
	    @Length(min = 0,max = 200,message = "备注需要在256个字符以内")
	    private String remark;
}
