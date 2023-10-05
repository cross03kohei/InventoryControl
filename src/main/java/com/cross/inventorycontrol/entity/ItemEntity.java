package com.cross.inventorycontrol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "category")
    private Integer category;
    @Column(name = "item_name",length = 20)
    private String itemName;
    //消費期限があるのか
    @Column(name = "expiration")
    private Boolean isExpiration;
    //下限
    @Column(name = "lower_limit")
    private Integer lowerLimit;


}
