package com.cross.inventorycontrol.controller;

import com.cross.inventorycontrol.ItemCategory;
import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.model.Item;
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
    @GetMapping("/receive/{id}")
    public String getReceive(@ModelAttribute ReceiveForm form,
                             Model model, @PathVariable("id")Integer id){
        Item item = itemService.selectOne(id);
        Integer inventoryId = itemService.findByInventoryId(id); //inventoryのIdを取得
        if(form.getItemId() == null ){
            form.setInventoryId(inventoryId);
            form.setItemId(Integer.valueOf(id));
        }

        model.addAttribute("item",item);
        model.addAttribute("category", ItemCategory.item);
        return "receive";
    }

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

}
