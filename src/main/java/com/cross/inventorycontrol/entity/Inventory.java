package com.cross.inventorycontrol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "expiration_date", length = 20)
    private String expirationDate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
