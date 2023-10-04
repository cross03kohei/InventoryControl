package com.cross.inventorycontrol.controller;

import com.cross.inventorycontrol.ItemCategory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/item")
public class ItemController {
    @GetMapping("/add")
    public String getItem(Model model){
        model.addAttribute("category", ItemCategory.item);
        return "item_add";
    }
    @PostMapping("/add")
    public String postItem(Model model){
        return "redirect:/";
    }
}
