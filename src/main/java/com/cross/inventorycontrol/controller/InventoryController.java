package com.cross.inventorycontrol.controller;

import com.cross.inventorycontrol.ItemCategory;
import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.model.Issue;
import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.model.Receive;
import com.cross.inventorycontrol.domain.service.InventoryService;
import com.cross.inventorycontrol.domain.service.ItemService;
import com.cross.inventorycontrol.form.IssueForm;
import com.cross.inventorycontrol.form.ReceiveForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
@RequestMapping(value = "inventory")
public class InventoryController {
    @Autowired
    ItemService itemService;
    @Autowired
    InventoryService service;

    /**
     *入庫の登録画面の表示
     */
    @GetMapping("/receive/{id}")
    public String getReceive(@ModelAttribute ReceiveForm form,
                             Model model, @PathVariable("id")Integer id){
        Item item = itemService.selectOne(id);
        Integer inventoryId = itemService.findByInventoryId(id); //inventoryのIdを取得
        if(form.getItemId() == null ){
            form.setInventoryId(inventoryId);
            form.setItemId(id);
        }

        model.addAttribute("item",item);
        model.addAttribute("category", ItemCategory.item);
        return "receive";
    }

    /**
     *出庫の登録の際のマッピング
     */
    @PostMapping("/receive")
    public String postReceive(@ModelAttribute @Validated ReceiveForm form,
                              BindingResult bindingResult,Model model){
        if(bindingResult.hasErrors()){
            return getReceive(form,model,form.getItemId());
        }
        boolean result = service.receive(form);
        String id = String.valueOf(form.getItemId());
        return "redirect:/item/" + id;
    }

    /**
     *出庫登録画面の表示を行うマッピング
     */
    @GetMapping("/issue/{id}")
    public String getIssue(@ModelAttribute IssueForm form,@PathVariable("id")Integer itemId,
                           Model model){
        Item item = itemService.selectOne(itemId);
        model.addAttribute("category",ItemCategory.item);
        model.addAttribute("item",item);
        if(form.getInventoryId() == null){
            Integer inventoryId = itemService.findByInventoryId(itemId);
            form.setInventoryId(inventoryId);
            form.setItemId(itemId);
        }
        return "issue";
    }

    /**
     *出庫の処理 在庫数を上回ると出庫画面に戻る
     */
    @PostMapping("/issue")
    public String postIssue(@ModelAttribute @Validated IssueForm form,
                            BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return getIssue(form,form.getItemId(),model);
        }
        Item item = itemService.selectOne(form.getItemId());
        Integer stock = item.getStock();
        if(form.getIssueQuantity() > stock){
            model.addAttribute("result","在庫数より出庫数が多いです");
            return  getIssue(form, form.getItemId(),model);
        }
        String itemId = String.valueOf(form.getItemId());
        boolean result = service.issue(form);
        return "redirect:/item/" + itemId;
    }

    /**
     *
     * @param id ＋だったら入庫 ーの場合は出庫になる
     */
    @GetMapping("/edit/{id}")
    public String editInventory(@PathVariable("id")Integer id, Model model){
        model.addAttribute("category",ItemCategory.item);
        if(id > 0){
            Receive receive = service.findReceive(id);
            Integer itemId = itemService.findByItemId(receive.getInventoryId());  //inventoryIdからitemIdを取得
            Item item = itemService.selectOne(itemId);
            ReceiveForm form = setReceive(receive, itemId);
            model.addAttribute("item",item);
            model.addAttribute("receiveForm",form);
            model.addAttribute("receiveId",id);
            return "receive_edit";
        }else{
            Integer issueId = id * -1; //ーを＋にする
            Issue issue = service.findIssue(issueId);
            Integer itemId = itemService.findByItemId(issue.getInventoryId());
            Item item = itemService.selectOne(itemId);
            IssueForm form = setIssue(issue, itemId);
            model.addAttribute("item",item);
            model.addAttribute("issueForm",form);
            return "issue_edit";
        }
    }

    /**
     *入庫の更新処理　在庫数がゼロになると編集画面に戻る
     * 在庫数は計算して更新処理を行う
     */
    @PostMapping("receive/edit")
    public String postEditReceive(Model model, @ModelAttribute @Validated ReceiveForm form,
                                  BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return editReceive(model, form);
        }
        Integer stock = service.findStock(form.getInventoryId());
        Receive receive = service.findReceive(form.getReceiveId()); //formとの値の差を計算する　更新にも使う
        Integer difference = receive.getQuantity() - form.getReceiveQuantity(); //もとの出庫数 -　編集した出庫数
        if(stock < difference) {
            model.addAttribute("result","在庫数が0になります");
            return editReceive(model, form);
        }
        receive.setQuantity(form.getReceiveQuantity());
        receive.setPrice(form.getPrice());
        int stockEdit;      //計算する在庫数　＋の場合は引く　ーの場合は足す
        if(difference > 0) {
            stockEdit = stock - difference;

        }else{
            stockEdit = stock + difference * -1;
        }
        boolean result = service.updateReceive(receive, form.getInventoryId(), stockEdit);
        return "redirect:/item/" + form.getItemId();
    }
    /**
     * 入庫の更新の際にバリデーションに引っかかった際こちらで受け取る
     */
    public String editReceive(Model model, @ModelAttribute ReceiveForm form){
        Item item = itemService.selectOne(form.getItemId());
        model.addAttribute("category", ItemCategory.item);
        model.addAttribute("receiveForm",form);
        model.addAttribute("item",item);
        model.addAttribute("receiveId",form.getReceiveId());
        return "receive_edit";
    }

    /**
     *出庫の更新処理
     */
    @PostMapping("/issue/edit")
    public String postEditIssue(Model model, @ModelAttribute @Validated IssueForm form,
                                BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return editIssue(model, form);
        }
        Issue issue = service.findIssue(form.getIssueId());     //編集用
        Integer stock = service.findStock(form.getInventoryId());
        int difference = issue.getQuantity() - form.getIssueQuantity(); //もとの出庫数 -　編集した出庫数
        //差数が在庫数を上回るとメッセージと共に編集画面へ
        if(difference < 0 && stock < difference * -1 ){
            model.addAttribute("result","在庫数が0になります");
            return editIssue(model, form);
        }
        issue.setQuantity(form.getIssueQuantity());
        int stockEdit = stock + difference;     //更新する在庫数
        boolean result = service.updateIssue(issue, form.getInventoryId(), stockEdit);
        return "redirect:/item/" + form.getItemId();
    }

    /**
     * 出庫の更新の際にバリデーションに引っかかった際こちらで受け取る
     */
    public String editIssue(Model model, @ModelAttribute IssueForm form){
        Item item = itemService.selectOne(form.getItemId());
        model.addAttribute("category",ItemCategory.item);
        model.addAttribute("item",item);
        String name;

        return "issue_edit";
    }
    @PostMapping("/receive/delete")
    public String deleteReceive(@RequestParam("id") Integer receiveId){
        //item.idが欲しい　receive.id -> inventory.id -> item.idの順で取ってくる
        Integer inventoryId = service.findInventoryIdByReceiveId(receiveId);
        Integer itemId = itemService.findByItemId(inventoryId);
        return "redirect:/item/" + itemId;
    }

    private ReceiveForm setReceive(Receive r, Integer itemId) {
        ReceiveForm form = new ReceiveForm();
        form.setReceiveId(r.getId());
        form.setInventoryId(r.getInventoryId());
        form.setPrice(r.getPrice());
        form.setReceiveQuantity(r.getQuantity());
        form.setItemId(itemId);
        return form;
    }


    /**
     *Formに値を詰なおす 編集用
     */
    private IssueForm setIssue(Issue i, Integer itemId) {
        IssueForm form = new IssueForm();   //返却用
        form.setIssueId(i.getId());
        form.setInventoryId(i.getInventoryId());
        form.setIssueQuantity(i.getQuantity());
        form.setItemId(itemId);
        return form;
    }

    /**
     *formの中身をmodelに詰めなおす
     */
    private Receive createReceive(ReceiveForm form) {
        Receive r = new Receive();
        r.setId(form.getReceiveId());
        r.setPrice(form.getPrice());
        r.setQuantity(form.getReceiveQuantity());
        return r;
    }

}
