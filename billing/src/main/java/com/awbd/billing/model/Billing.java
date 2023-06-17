package com.awbd.billing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Billing {
    private int amount;
    private boolean debt;
}
