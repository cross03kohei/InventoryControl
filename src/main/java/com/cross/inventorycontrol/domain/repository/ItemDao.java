package com.cross.inventorycontrol.domain.repository;

import com.cross.inventorycontrol.domain.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDao {
    @Autowired
    JdbcTemplate jdbc;
    public int insertOne(Item item)throws DataAccessException {
        int rowNumber = jdbc.update("INSERT INTO item(category," +
                "item_name,expiration,lower_limit)" +
                "VALUES(?,?,?,?)",
                item.getCategory(),item.getItemName(),item.getExpiration(),item.getLowerLimit());
        return rowNumber;
    }
    public int insertInventory(Integer i){
        String id = String.valueOf(i);
        int rowNumber = jdbc.update("INSERT INTO inventory(stock,item_id)" +
                "VALUES(0,?)",id);
        return rowNumber;
    }

    /**
     *直前にインサートしたIDを所得
     */
    public Integer lastInsertId() throws DataAccessException {
        Integer id = jdbc.queryForObject("SELECT LAST_INSERT_ID();",Integer.class);
        return id;
    }
    public Item selectOne(String id) throws DataAccessException {
        Map<String, Object> map = jdbc.queryForMap("SELECT * FROM item " +
                "JOIN inventory ON item.id = inventory.item_id WHERE item.id = ?",id);
        return createItem(map);
    }
    public List<Item> selectMany()throws DataAccessException {
        List<Map<String, Object>> getList = jdbc.queryForList("SELECT * FROM item " +
                "JOIN inventory ON item.id = inventory.item_id");
        List<Item> items = new ArrayList<>(); //返却用
        for(Map<String,Object> map : getList){
            Item i = createItem(map);
            items.add(i);
        }
        return items;
    }
    private Item createItem(Map<String, Object> map){
        Item i = new Item();
        i.setId((Integer) map.get("id"));
        i.setCategory((Integer) map.get("category"));
        i.setLowerLimit((Integer) map.get("lower_limit"));
        i.setItemName((String) map.get("item_name"));
        i.setExpiration((Boolean) map.get("expiration"));
        i.setStock((Integer) map.get("stock"));
        return i;
    }
}