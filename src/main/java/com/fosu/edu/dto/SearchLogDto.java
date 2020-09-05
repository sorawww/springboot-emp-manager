package com.fosu.edu.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchLogDto {
	
	private Integer type;
	
	private String beforeSeq;
	
	private String afterSeq;
	
	private String operator;
	
	private Date fromTime;
	
	private Date toTime;
}
