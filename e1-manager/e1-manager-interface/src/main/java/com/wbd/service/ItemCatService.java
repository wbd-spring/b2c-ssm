package com.wbd.service;

import java.util.List;

import com.wbd.common.pojo.EasyUITreeNode;

/**
 * 商品分类
* <p>Title: TbItemCatService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月24日
 */
public interface ItemCatService {
	
	/**
	 * 根据parentid查询子节点列表
	 * <p>Title: getItemCatList</p>  
	 * <p>Description: </p>  
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getItemCatList(long parentId);

}
