package com.wbd.manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.common.pojo.EasyUITreeNode;
import com.wbd.portal.service.ContentCategoryService;

@Controller
public class CmsCategoryController {

	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getCategoryList(@RequestParam(name="id",defaultValue="0") long parentId){
		return contentCategoryService.getContentCategoryList(parentId);
	}
	
}
