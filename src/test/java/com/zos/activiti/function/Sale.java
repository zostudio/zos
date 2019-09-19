package com.zos.activiti.function;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    private String identity;
    
    private int amount;
    
    public double doDiscount(double discount) {
        return this.amount * discount;
    }
    
}
