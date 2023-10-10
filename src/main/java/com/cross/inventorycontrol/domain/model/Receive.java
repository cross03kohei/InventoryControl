package com.cross.inventorycontrol.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Receive {
    private Integer id;
    private String date;
    private Integer quantity;
    private Integer price;
    private Integer inventoryId;
}
