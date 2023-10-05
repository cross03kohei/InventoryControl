package com.cross.inventorycontrol.controller;

import com.cross.inventorycontrol.ItemCategory;
import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    ItemService itemService;
    @GetMapping("/")
    public String Home(Model model){
        List<Item> items = itemService.selectMany();
        model.addAttribute("items",items);
        model.addAttribute("category", ItemCategory.item);
        return "index";
    }
}
