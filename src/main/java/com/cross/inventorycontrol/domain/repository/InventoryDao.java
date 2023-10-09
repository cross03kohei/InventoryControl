package com.cross.inventorycontrol.domain.repository;

import com.cross.inventorycontrol.domain.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InventoryDao {
    @Autowired
    JdbcTemplate jdbc;

    /**
     *入庫の登録処理
     */
    public int insertReceive(Inventory inventory) throws DataAccessException {
        return jdbc.update("INSERT INTO receive(receive_quantity, date_receive, " +
                "price, inventory_id)VALUES(?,?,?,?)",
                inventory.getQuantity(),
                inventory.getDate(),
                inventory.getPrice(),
                inventory.getInventoryId());
    }
    public int insertIssue(Inventory inventory) throws DataAccessException {
        return jdbc.update("INSERT INTO issue(issue_quantity, date_issue, inventory_id)" +
                "VALUES(?, ?, ?)",
                inventory.getQuantity(),
                inventory.getDate(),
                inventory.getInventoryId());
    }
    public int plusInventory(Inventory inventory) throws DataAccessException {
        return jdbc.update("UPDATE inventory SET " +
                "stock = stock + ? WHERE inventory.id = ?",
                inventory.getQuantity(),
                inventory.getInventoryId());
    }
    public int minusInventory(Inventory inventory) throws DataAccessException {
        return jdbc.update("UPDATE inventory SET " +
                "stock = stock - ? WHERE inventory.id = ?",
                inventory.getQuantity(),
                inventory.getInventoryId());
    }
}
