package com.cross.inventorycontrol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 入庫テーブル
 */
@Entity
@Table(name = "receive")
@Getter
@Setter
public class ReceiveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "receive_quantity")
    private Integer receiveQuantity;
    @Column(name = "date_receive", length = 20)
    private String dateReceive;
    @Column(name = "price")
    private Integer price; //一個あたりの値段
    @ManyToOne
    @JoinColumn(name ="inventory_id")
    private Inventory inventory;
}
