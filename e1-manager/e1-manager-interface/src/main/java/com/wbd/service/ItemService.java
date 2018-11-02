package com.wbd.service;

import com.wbd.common.pojo.EasyUIDataGridResult;
import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbItem;
import com.wbd.pojo.TbItemDesc;
/**
 * 商品
* <p>Title: ItemService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月24日
 */
public interface ItemService {

	/**
	 * 根据商品id查询商品信息
	 * <p>Title: getItemById</p>  
	 * <p>Description: </p>  
	 * @param itemId
	 * @return
	 */
	TbItem getItemById(long itemId);
	
	
	/**
	 * 根据商品id查询商品描述信息
	 * <p>Title: getItemDescById</p>  
	 * <p>Description: </p>  
	 * @param itemId
	 * @return
	 */
	TbItemDesc getItemDescById(long itemId);

	/**
	 * 
	 * * 分页请求的路径： 
	 * /item/list 
	 * 请求参数：page=1&rows=30 响应的json数据格式如下：
	 * Easyui中datagrid控件要求的数据格式为
	 * 
	 * {total:'2',rows[{"id":"1","name":"abc"},{"id":"2","name":"cf"}]}
	 * 
	 * @param page  页码
	 * @param rows  每页显示的条数
	 * @return
	 */
	EasyUIDataGridResult getItemList(int page, int rows);
	
	/**
	 * 商品添加
	 * <p>Title: addItem</p>  
	 * <p>Description: </p>  
	 * @param item
	 * @param desc
	 * @return
	 */
	public WBDResult  addItem(TbItem item,String desc);

}
