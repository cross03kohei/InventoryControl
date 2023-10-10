package com.cross.inventorycontrol.domain.service;

import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.model.Issue;
import com.cross.inventorycontrol.domain.model.Receive;
import com.cross.inventorycontrol.domain.repository.InventoryDao;
import com.cross.inventorycontrol.form.IssueForm;
import com.cross.inventorycontrol.form.ReceiveForm;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    InventoryDao dao;

    @Transactional
    public boolean receive(ReceiveForm form){
        Inventory inventory = createInventory(form);
        int rowNumber = dao.insertReceive(inventory);
        rowNumber += dao.plusInventory(inventory);
        return rowNumber > 1;
    }
    @Transactional
    public boolean issue(IssueForm form) {
        Inventory inventory = setIssueForm(form);
        int rowNumber = dao.insertIssue(inventory);
        rowNumber += dao.minusInventory(inventory);
        return rowNumber > 1;
    }
    public List<Receive> findAllReceive(Integer id) {
        return dao.findAllReceive(id);
    }
    public List<Issue> findAllIssue(Integer id) {
        return dao.findAllIssue(id);
    }
    public Receive findReceive(Integer id){
        return dao.findReceive(id);
    }
    public Integer findStock(Integer inventoryId){ return dao.findStock(inventoryId);}
    public Issue findIssue(Integer id){ return dao.findIssue(id);}
    private Inventory setIssueForm(IssueForm form){
        Inventory inventory = new Inventory();
        inventory.setInventoryId(form.getInventoryId());
        inventory.setQuantity(form.getIssueQuantity());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        inventory.setDate(now.format(f));
        return inventory;
    }
    private Inventory createInventory(ReceiveForm form){
        Inventory i = new Inventory();
        i.setInventoryId(form.getInventoryId());
        i.setQuantity(form.getReceiveQuantity());
        i.setPrice(form.getPrice());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        i.setDate(now.format(f));
        return i;
    }
}
