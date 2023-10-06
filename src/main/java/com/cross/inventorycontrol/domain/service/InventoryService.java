package com.cross.inventorycontrol.domain.service;

import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.repository.InventoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    @Autowired
    InventoryDao dao;

    @Transactional
    public boolean receive(Inventory inventory){
        int rowNumber = dao.insertReceive(inventory);
        rowNumber += dao.plusInventory(inventory);
        return rowNumber > 1;
    }
}
