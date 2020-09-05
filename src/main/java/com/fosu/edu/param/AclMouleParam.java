package com.fosu.edu.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AclMouleParam {
		
		private  Integer id;
	
		@NotBlank
		@Length(max = 20,min = 2)
	    private String name;

	    private Integer parentId=0;

		@NotNull(message = "顺序不能为空")
	    private Integer seq;
		
		@NotNull(message = "状态不能为空")
		@Min(value = 0,message = "状态不合法")
		@Max(value = 1,message = "状态不合法")
	    private Integer status;

	    @Length(max = 200,message = "长度不能超过200")
	    private String remark;
}
