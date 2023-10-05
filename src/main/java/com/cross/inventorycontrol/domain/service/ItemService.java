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
        int rowNumber = dao.insertOne(item);
        Integer id = dao.lastInsertId();    //insertしたIDを取得
        rowNumber = dao.insertInventory(id);
        return rowNumber > 0;
    }
    public List<Item> selectMany(){
        return dao.selectMany();
    }
    public Item selectOne(String id){
        return dao.selectOne(id);
    }
}
