package com.wbd.item.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wbd.item.web.pojo.Item;
import com.wbd.pojo.TbItem;
import com.wbd.pojo.TbItemDesc;
import com.wbd.service.ItemService;

/**
 * 显示商品详细页面的控制器
* <p>Title: ItemController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月2日
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService  itemService;
	
	@RequestMapping("/item/{itemId}")
	public String shoItemInfo(@PathVariable Long itemId,Model model) {
		
		TbItem tbitem = itemService.getItemById(itemId);
		TbItemDesc itemDesc = itemService.getItemDescById(itemId);
		Item item = new Item(tbitem);
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		return "item";
		
	}

}
