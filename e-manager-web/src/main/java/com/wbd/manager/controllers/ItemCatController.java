package com.wbd.manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.common.pojo.EasyUITreeNode;
import com.wbd.service.ItemCatService;
/**
 * 商品分类控制器
* <p>Title: ItemCatController.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月24日
 */
@Controller
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public  List<EasyUITreeNode> getItemCatList(@RequestParam(name="id",defaultValue="0") Long parentId) {
		//如果前台的参数为id，而我们后台为了方法参数(parentId)见名知意，
		// 此时参数名称不匹配， 可以采用@RequestParam(name="id")这种方式进行绑定，把id绑定到parentId
		return itemCatService.getItemCatList(parentId);
	}
	
	
	
}
