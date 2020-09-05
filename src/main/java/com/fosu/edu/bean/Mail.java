package com.fosu.edu.bean;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mail {
	//主题
	private String subject;
	
	//内容
	private String message;
	
	private Set<String> Receivers; 
}
