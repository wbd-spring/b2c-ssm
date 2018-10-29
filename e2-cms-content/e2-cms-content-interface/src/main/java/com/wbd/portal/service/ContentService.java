package com.wbd.portal.service;
/**
 * 内容管理服务
 * @author zhuguanghe
 *
 */

import java.util.List;

import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbContent;

public interface ContentService {
	/**
	 * 添加内容
	 * 
	 * @param content
	 * @return
	 */
	WBDResult addContent(TbContent content);

	/**
	 * 根据分类id获取内容列表
	 * 
	 * @param cid
	 * @return
	 */
	List<TbContent> getContentListByCategoryId(long cid);
}
