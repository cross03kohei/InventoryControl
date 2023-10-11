package com.cross.inventorycontrol.domain.repository;

import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.model.Issue;
import com.cross.inventorycontrol.domain.model.Receive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    public List<Receive> findAllReceive(Integer id) throws DataAccessException {
        List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM " +
                "receive WHERE inventory_id = ?", id);
        List<Receive> receives = new ArrayList<>(); //返却用
        for(Map<String, Object> map : getList){
            Receive receive = new Receive();
            receive.setId((Integer) map.get("id"));
            receive.setQuantity((Integer) map.get("receive_quantity"));
            receive.setPrice((Integer) map.get("price"));
            receive.setDate((String) map.get("date_receive"));
            receives.add(receive);
        }
        return receives;
    }
    public List<Issue> findAllIssue(Integer id) throws DataAccessException {
        List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM issue " +
                "WHERE inventory_id = ?",id);
        List<Issue> issues = new ArrayList<>(); //返却用
        for(Map<String, Object> map : getList) {
            Issue issue = new Issue();
            issue.setId((Integer) map.get("id"));
            issue.setDate((String) map.get("date_issue"));
            issue.setQuantity((Integer) map.get("issue_quantity"));
            issues.add(issue);
        }
        return issues;
    }
    public Receive findReceive(Integer id) throws DataAccessException {
        Map<String, Object> map = jdbc.queryForMap("SELECT * FROM receive " +
                "WHERE receive.id = ?", id);
        Receive receive = new Receive();
        receive.setId(id);
        receive.setInventoryId((Integer) map.get("inventory_id"));
        receive.setQuantity((Integer) map.get("receive_quantity"));
        receive.setPrice((Integer) map.get("price"));
        return receive;
    }
    public Issue findIssue(Integer id) throws DataAccessException {
        Map<String, Object> map = jdbc.queryForMap("SELECT * FROM issue " +
                "WHERE issue.id = ?", id);
        Issue issue = new Issue();  //返却用
        issue.setId(id);
        issue.setInventoryId((Integer) map.get("inventory_id"));
        issue.setQuantity((Integer) map.get("issue_quantity"));
        return issue;
    }

    /**
     *inventoryの更新処理
     */
    public int updateInventory(Integer id, Integer stock) throws DataAccessException {
        return jdbc.update("UPDATE inventory SET " +
                "stock = ? WHERE inventory.id = ?",
                stock,
                id);
    }

    /**
     *入庫の更新処理
     */
    public int updateReceive(Receive receive) throws DataAccessException {
        return jdbc.update("UPDATE receive SET " +
                "receive_quantity = ? , price = ? " +
                "WHERE receive.id = ?",
                receive.getQuantity(),
                receive.getPrice(),
                receive.getId());
    }
    /**
     *在庫数を取得
     */
    public Integer findStock(Integer inventoryId) throws DataAccessException {
        Map<String, Object> map = jdbc.queryForMap("SELECT stock FROM inventory " +
                "WHERE inventory.id = ?", inventoryId);
        return (Integer) map.get("stock");
    }
}
