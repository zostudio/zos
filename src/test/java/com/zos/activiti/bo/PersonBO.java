package com.zos.activiti.bo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonBO {
	
	private Long id;
	
	private String name;
	
	private Date date;
	
}
