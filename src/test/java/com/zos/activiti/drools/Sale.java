package com.zos.activiti.drools;

import java.util.Date;

import lombok.Data;

@Data
public class Sale {
	
	private Date date;
	
	private int price;
	
	private int amount;
	
	private String saleCode;
}
