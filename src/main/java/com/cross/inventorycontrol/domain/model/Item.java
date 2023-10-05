package com.cross.inventorycontrol.domain.model;

import lombok.Data;

@Data
public class Item {
    private Integer id;
    private Integer category;
    private String itemName;
    private Boolean expiration;
    private Integer lowerLimit;
    private Integer stock;
}
