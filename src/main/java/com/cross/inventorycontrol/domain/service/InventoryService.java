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

    /**
     *入庫管理の処理　入庫を登録すると共に入庫数に応じて在庫数を更新する
     */
    @Transactional
    public boolean receive(ReceiveForm form){
        Inventory inventory = createInventory(form);
        int rowNumber = dao.insertReceive(inventory);
        rowNumber += dao.plusInventory(inventory);
        return rowNumber > 1;
    }

    /**
     *出庫管理の処理　出庫を登録するとともに出庫するに応じて在庫数を更新する
     */
    @Transactional
    public boolean issue(IssueForm form) {
        Inventory inventory = setIssueForm(form);
        int rowNumber = dao.insertIssue(inventory);
        rowNumber += dao.minusInventory(inventory);
        return rowNumber > 1;
    }

    /**
     * 在庫のテーブルに紐づく入庫テーブルを全件取得　引数はinventory.id
     */
    public List<Receive> findAllReceive(Integer id) {
        return dao.findAllReceive(id);
    }

    /**
     *在庫のテーブルに紐づく出庫テーブルを全件取得　引数はinventory.id
     */
    public List<Issue> findAllIssue(Integer id) {
        return dao.findAllIssue(id);
    }

    /**
     *入庫の履歴を一件取得　引数はreceive.id
     */
    public Receive findReceive(Integer id){
        return dao.findReceive(id);
    }

    /**
     *在庫数を取得
     */
    public Integer findStock(Integer inventoryId){ return dao.findStock(inventoryId);}

    /**
     *出庫の履歴を一件取得　引数はissue.id
     */
    public Issue findIssue(Integer id){ return dao.findIssue(id);}

    /**
     *入庫の更新処理　同時に在庫数も更新する
     */
    @Transactional
    public boolean updateReceive(Receive receive, Integer inventoryId, Integer stock) {
        int rowNumber = dao.updateReceive(receive);
        rowNumber += dao.updateInventory(inventoryId, stock);
        return rowNumber > 1;
    }

    /**
     *出庫の更新処理　同時に在庫数も更新
     */
    @Transactional
    public boolean updateIssue(Issue issue, Integer inventoryId, Integer stock) {
        int rowNumber = dao.updateIssue(issue);
        rowNumber += dao.updateInventory(inventoryId, stock);
        return rowNumber > 1;
    }

    /**
     *入庫履歴を一件削除 入庫数の分だけ在庫数を減らす
     */
    @Transactional
    public boolean deleteReceive(Integer receiveId) {
        Receive receive = dao.findReceive(receiveId);
        Integer stock = dao.findStock(receive.getInventoryId());
        Integer quantity = receive.getQuantity();
        int rowNumber = dao.updateInventory(receive.getInventoryId(), stock - quantity);
        rowNumber += dao.deleteReceive(receiveId);
        return rowNumber > 1;
    }

    /**
     *出庫履歴を一件削除し、出庫数の数だけ在庫数を増やす inventory.idを返す
     */
    @Transactional
    public Integer deleteIssue(Integer issueId) {
        Issue issue = dao.findIssue(issueId);
        Integer issueQuantity = issue.getQuantity();
        Integer stock = dao.findStock(issue.getInventoryId());
        int rowNumber = dao.updateInventory(issue.getInventoryId(), stock + issueQuantity);
        rowNumber += dao.deleteIssue(issueId);
        return issue.getInventoryId();
    }

    /**
     *formの記述をmodelに変換　同時に登録日付を登録　秒数まで
     */
    private Inventory setIssueForm(IssueForm form){
        Inventory inventory = new Inventory();
        inventory.setInventoryId(form.getInventoryId());
        inventory.setQuantity(form.getIssueQuantity());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        inventory.setDate(now.format(f));
        return inventory;
    }

    /**
     *formの記述をmodelに変換　同時に登録日付を登録　秒数まで
     */
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
    public Integer findInventoryIdByReceiveId(Integer receiveId) {
        return dao.findInventoryIdByReceiveId(receiveId);
    }
}
