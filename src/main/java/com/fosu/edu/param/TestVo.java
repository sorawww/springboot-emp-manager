package com.fosu.edu.param;


import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TestVo {
	@NotBlank
	private String msg;//msg不允许为空
	
	@Max(value = 10,message = "id至少大于10")
	@Min(value = 0,message = "id至少大于等于0")
	@NotNull(message =  "id不可以为空")
	private Integer id;
	
	@NotEmpty
	private List<String> str;
	
	
	
}
