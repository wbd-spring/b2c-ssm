package com.wbd.common.pojo;

import java.io.Serializable;
/**
 * 加载树所需要的数据pojo
 * 
* <p>Title: EasyUITreeNode.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年10月24日
 */
public class EasyUITreeNode implements Serializable{

	/** serialVersionUID*/ 
	private static final long serialVersionUID = 1L;

	private long id;
	
	private String text;
	
	private String state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
