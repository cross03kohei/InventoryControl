package com.cross.inventorycontrol.domain.service;

import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.repository.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemDao dao;
    public boolean insert(Item item){
        Integer stock = 0;
        if(item.getStock() != null){
            stock = item.getStock();
        }
        int rowNumber = dao.insertOne(item);
        Integer id = dao.lastInsertId();    //insertしたIDを取得
        rowNumber = dao.insertInventory(id,stock);
        return rowNumber > 0;
    }
    public boolean updateItem(Item item){
        int rowNumber = dao.updateItem(item);
        return rowNumber > 0;
    }
    public List<Item> selectMany(){
        return dao.selectMany();
    }
    public Item selectOne(Integer id){
        return dao.selectOne(id);
    }
    public Integer findByInventoryId(Integer id){ return dao.findInventoryId(id);}
    public Integer findByItemId(Integer id) { return dao.findItemId(id);}
}
