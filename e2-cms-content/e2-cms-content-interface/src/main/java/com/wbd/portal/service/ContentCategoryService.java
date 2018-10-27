package com.wbd.portal.service;

import java.util.List;

import com.wbd.common.pojo.EasyUITreeNode;
import com.wbd.common.utils.WBDResult;

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
	
	
	/**
	 * 添加子节点，返回节点的id
	 * 对ContentCategory.xml文件的insert方法， 进行了修改， 插入数据之后返回id
	 * select last_insert_id()
	 * <p>Title: addContentCategory</p>  
	 * <p>Description: </p>  
	 * @param parentId
	 * @param name
	 * @return
	 */
	public WBDResult addContentCategory(long parentId,String name);
}
