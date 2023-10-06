package com.cross.inventorycontrol.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Inventory {
    private Integer inventoryId;
    private Integer quantity;
    private Integer price;
    private String date;
    private Boolean isIssue; //出庫か入庫を真偽型で判断
}
