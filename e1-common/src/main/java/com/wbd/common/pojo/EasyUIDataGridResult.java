package com.wbd.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页请求的路径： /item/list
 * 请求参数：page=1&rows=30
 * 响应的json数据格式如下：
 * Easyui中datagrid控件要求的数据格式为
 * 
 * {total:'2',rows[{"id":"1","name":"abc"},{"id":"2","name":"cf"}]}
 * 
 * @author zhuguanghe
 *
 */
public class EasyUIDataGridResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long total;
	private List rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	

}
