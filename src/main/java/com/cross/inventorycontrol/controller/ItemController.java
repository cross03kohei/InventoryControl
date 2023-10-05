package com.cross.inventorycontrol.controller;

import com.cross.inventorycontrol.ItemCategory;
import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.service.ItemService;
import com.cross.inventorycontrol.form.ItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/item")
public class ItemController {
    @Autowired
    ItemService itemService;
    @GetMapping("/add")
    public String getItem(@ModelAttribute ItemForm form, Model model){
        model.addAttribute("category", ItemCategory.item);
        return "item_add";
    }
    @PostMapping("/add")
    public String postItem(@ModelAttribute @Validated ItemForm form,
                           BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return getItem(form,model);
        }
        Item item = createItem(form);
        boolean result = itemService.insert(item);
        if(result){
            System.out.println("成功");
        }else{
            System.out.println("失敗");
        }
        return "redirect:/";
    }
    @GetMapping("/{id}")
    public String getItemDetail(Model model, @PathVariable("id")String id){
        if(id != null && id.length() > 0){
            Item item = itemService.selectOne(id);
            model.addAttribute("item",item);
        }
        return "item_detail";
    }
    private Item createItem(ItemForm form){
        Item item = new Item();
        item.setId(form.getId());
        item.setCategory(form.getCategory());
        item.setItemName(form.getItemName());
        item.setExpiration(form.getIsExpiration());
        item.setLowerLimit(form.getLowerLimit());
        return item;
    }
}
