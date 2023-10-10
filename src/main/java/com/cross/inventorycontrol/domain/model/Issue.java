package com.cross.inventorycontrol.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Issue {
    private Integer id;
    private Integer inventoryId;
    private String date;
    private Integer quantity;
}
