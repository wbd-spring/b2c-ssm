package com.wbd.portal.service;

import java.util.List;

import com.wbd.common.pojo.EasyUITreeNode;

/**
 * cms  内容分类服务接口
* <p>Title: ContentCategoryService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月27日
 */
public interface ContentCategoryService {

	/**
	 * 根据parentid获取分类列表
	 * <p>Title: getContentCategoryList</p>  
	 * <p>Description: </p>  
	 * @param parentId
	 * @return
	 */
	public List<EasyUITreeNode> getContentCategoryList(long parentId);
}
