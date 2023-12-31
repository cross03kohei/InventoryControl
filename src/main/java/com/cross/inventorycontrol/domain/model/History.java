package com.cross.inventorycontrol.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class History {
    private Integer id;
    private String date;
    private String status;
    private String number;
    private Integer quantity;
    private Integer stock;
}
