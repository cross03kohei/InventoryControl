package com.cross.inventorycontrol.domain.service;

import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.repository.InventoryDao;
import com.cross.inventorycontrol.domain.repository.ItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        if (stock > 0){ //在庫が０を上回ると入庫登録も行う
            Inventory inventory = new Inventory();
            Integer inventoryId = dao.lastInsertId(); //insertしたinventoryのIDを取得
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            inventory.setDate(now.format(f)); //日付をセット
            inventory.setInventoryId(inventoryId);  //外部キーをセット
            inventory.setQuantity(stock);       //入庫数もセット
            inventory.setPrice(0);   //金額は0に
            rowNumber = inventoryDao.insertReceive(inventory);      //保存
        }

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
