package com.cross.inventorycontrol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 出庫テーブル
 */
@Entity
@Table(name = "issue")
@Getter
@Setter
public class IssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "issue_quantity")
    private Integer issueQuantity;
    @Column(name = "date_issue", length = 20)
    private String dateIssue;

    @ManyToOne
    @JoinColumn(name ="inventory_id")
    private Inventory inventory;
}
