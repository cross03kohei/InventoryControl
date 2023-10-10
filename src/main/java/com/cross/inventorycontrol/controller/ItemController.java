package com.cross.inventorycontrol.controller;

import com.cross.inventorycontrol.ItemCategory;
import com.cross.inventorycontrol.config.CalcByDate;
import com.cross.inventorycontrol.config.SortByDate;
import com.cross.inventorycontrol.domain.model.History;
import com.cross.inventorycontrol.domain.model.Issue;
import com.cross.inventorycontrol.domain.model.Item;
import com.cross.inventorycontrol.domain.model.Receive;
import com.cross.inventorycontrol.domain.service.InventoryService;
import com.cross.inventorycontrol.domain.service.ItemService;
import com.cross.inventorycontrol.form.EditItemForm;
import com.cross.inventorycontrol.form.ItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/item")
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    InventoryService inventoryService;
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
    public String getItemDetail(Model model, @PathVariable("id")Integer id){
        if(id != null && id > 0){
            Item item = itemService.selectOne(id);
            Integer inventoryId = itemService.findByInventoryId(id);
            List<Receive> receives = inventoryService.findAllReceive(inventoryId);  //入庫一覧を取得
            List<Issue> issues = inventoryService.findAllIssue(inventoryId);
            List<History> histories = calc(receives, issues);
            model.addAttribute("histories",histories);
            model.addAttribute("item",item);
            model.addAttribute("category",ItemCategory.item);
        }
        return "item_detail";
    }
    @GetMapping("/edit/{id}")
    public String getEditItem(Model model, @ModelAttribute EditItemForm form,
                              @PathVariable("id")Integer id){
        if(id !=null && id > 0) {
            Item item = itemService.selectOne(id);
            EditItemForm editForm = setForm(item);
            model.addAttribute("editItemForm",editForm);
            model.addAttribute("itemId",id);
        }
        model.addAttribute("itemId",form.getId());
        model.addAttribute("category",ItemCategory.item);
        return "item_edit";
    }
    @PostMapping("/edit")
    public String editItem(Model model,@ModelAttribute @Validated EditItemForm form,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return getEditItem(model,form,null);
        }
        Item item = editItem(form);
        boolean result = itemService.updateItem(item);
        if(result){
            model.addAttribute("result","更新成功");
        }else{
            model.addAttribute("result","更新失敗");
        }
        Integer id = form.getId();
        return getItemDetail(model,id);
    }
    private Item createItem(ItemForm form){
        Item item = new Item();
        item.setId(form.getId());
        item.setCategory(form.getCategory());
        item.setItemName(form.getItemName());
        item.setExpiration(form.getIsExpiration());
        item.setLowerLimit(form.getLowerLimit());
        item.setStock(form.getStock());
        return item;
    }
    private EditItemForm setForm(Item item){
        EditItemForm form = new EditItemForm();
        form.setId(item.getId());
        form.setIsExpiration(item.getExpiration());
        form.setCategory(item.getCategory());
        form.setLowerLimit(item.getLowerLimit());
        form.setItemName(item.getItemName());
        return form;
    }
    private Item editItem(EditItemForm form){
        Item item = new Item();
        item.setId(form.getId());
        item.setItemName(form.getItemName());
        item.setExpiration(form.getIsExpiration());
        item.setCategory(form.getCategory());
        item.setLowerLimit(form.getLowerLimit());
        return item;
    }

    /**
     *在庫数の履歴をだすためのメソッド
     */
    private List<History> calc(List<Receive> r, List<Issue> i) {
        List<History> histories = new ArrayList<>();    //返却用
        for(Receive receive : r){
            History h = new History();
            h.setId(receive.getId());
            h.setDate(receive.getDate());
            h.setQuantity(receive.getQuantity());
            h.setStatus("入庫");
            histories.add(h);
        }
        for (Issue issue : i){
            History h = new History();
            h.setId(-issue.getId());
            h.setDate(issue.getDate());
            h.setQuantity(issue.getQuantity());
            h.setStatus("出庫");
            histories.add(h);
        }
        histories.sort(new CalcByDate());   //在庫数を計算するためにあえて逆に並び変え
        for(int x = 0; x < histories.size(); x++){
            if(x == 0){
                histories.get(x).setStock(histories.get(x).getQuantity());
                String number = "+" + histories.get(x).getQuantity();
                histories.get(x).setNumber(number);
            }else{
                if(histories.get(x).getStatus().equals("入庫")){
                    Integer stock = histories.get(x - 1).getStock();
                    histories.get(x).setStock(stock + histories.get(x).getQuantity());
                    String number = "+" + histories.get(x).getQuantity();
                    histories.get(x).setNumber(number);
                }
                if(histories.get(x).getStatus().equals("出庫")){
                    Integer stock = histories.get(x - 1).getStock();
                    histories.get(x).setStock(stock - histories.get(x).getQuantity());
                    String number = "-" + histories.get(x).getQuantity();
                    histories.get(x).setNumber(number);
                }
            }
        }
        histories.sort(new SortByDate());
        return histories;
    }
}
