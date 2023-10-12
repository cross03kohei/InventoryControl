package com.cross.inventorycontrol.domain.service;

import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.repository.InventoryDao;
import com.cross.inventorycontrol.domain.repository.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    ItemDao dao;
    @Autowired
    InventoryDao inventoryDao;

    /**
     *itemの登録処理　同時に在庫数を初期化 = 0
     */
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

    /**
     *itemの更新処理
     */
    public boolean updateItem(Item item){
        int rowNumber = dao.updateItem(item);
        return rowNumber > 0;
    }

    /**
     *商品を全件取得
     */
    public List<Item> selectMany(){
        return dao.selectMany();
    }

    /**
     *商品を一件取得　引数はitem.id
     */
    public Item selectOne(Integer id){
        return dao.selectOne(id);
    }

    /**
     *item.idから一対一の関係のテーブル　inventory.idを取得
     */
    public Integer findByInventoryId(Integer id){ return dao.findInventoryId(id);}

    /**
     *inventory.idからitem.idを取得
     */
    public Integer findByItemId(Integer id) { return dao.findItemId(id);}

    /**
     *商品に紐づく全てのデータを削除する
     */
    @Transactional
    public boolean deleteItem(Integer itemId) {
        Integer inventoryId = dao.findInventoryId(itemId);
        inventoryDao.deleteIssueByInventory(inventoryId);
        inventoryDao.deleteReceiveByInventory(inventoryId);
        inventoryDao.deleteInventory(inventoryId);
        int rowNumber = dao.deleteItem(itemId);
        return rowNumber > 0;
    }
}
