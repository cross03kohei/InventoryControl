package com.cross.inventorycontrol.controller;

import com.cross.inventorycontrol.ItemCategory;
import com.cross.inventorycontrol.domain.model.Inventory;
import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.service.InventoryService;
import com.cross.inventorycontrol.domain.service.ItemService;
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
        boolean result = service.receive(createInventory(form));
        String id = String.valueOf(form.getItemId());
        return "redirect:/item/" + id;
    }
    @GetMapping("/issue/{id}")
    public String getIssue(){
        return "issue";
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
