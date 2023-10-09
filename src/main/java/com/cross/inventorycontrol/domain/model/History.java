package com.cross.inventorycontrol.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class History {
    private String date;
    private String number;
    private Integer stock;
}
