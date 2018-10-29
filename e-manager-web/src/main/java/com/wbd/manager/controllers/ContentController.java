package com.wbd.manager.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbContent;
import com.wbd.portal.service.ContentService;

/**
 * 内容管理控制器
 * @author zhuguanghe
 *
 */
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public WBDResult addContent(TbContent content) {
		return contentService.addContent(content);
	}
}
