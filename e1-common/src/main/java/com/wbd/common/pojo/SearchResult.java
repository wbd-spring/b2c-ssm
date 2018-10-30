package com.wbd.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 封装solr搜索的结果,
 * 参数为：
 * 1.keyword
 * 2.page(默认为1)
 *
 * 返回值：
 * 1.商品列表(List<SearchItem>)
 * 2.总页数：totalPage
 * 3.总记录数 recourdCount
 * 
* <p>Title: SearchResult.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月30日
 */
public class SearchResult implements Serializable{
	
	/** serialVersionUID*/ 
	private static final long serialVersionUID = 1L;

	private long totalPages; //总页数
	
	private long recourdCount; //总记录数
	
	private List<SearchItem> itemList;

	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public long getRecourdCount() {
		return recourdCount;
	}

	public void setRecourdCount(long recourdCount) {
		this.recourdCount = recourdCount;
	}

	public List<SearchItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}
	
	
	

}
