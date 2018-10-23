package com.wbd.service;

import com.wbd.common.pojo.EasyUIDataGridResult;
import com.wbd.pojo.TbItem;

public interface ItemService {

	TbItem getItemById(long itemId);

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

}
