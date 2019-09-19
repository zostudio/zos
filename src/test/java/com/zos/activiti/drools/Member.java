package com.zos.activiti.drools;

import java.io.Serializable;

import lombok.Data;

@Data
public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6045263128897560042L;

	private String identity;
	
	private Integer age;
	
	private Integer amount;
	
	private Integer afterDiscount;
	
	private double result;
	
	public void setResult(double discount) {
		this.result = this.amount * discount;
	}
}
