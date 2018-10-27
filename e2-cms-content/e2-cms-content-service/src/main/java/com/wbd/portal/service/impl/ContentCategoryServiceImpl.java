package com.wbd.portal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbd.common.pojo.EasyUITreeNode;
import com.wbd.mapper.TbContentCategoryMapper;
import com.wbd.pojo.TbContentCategory;
import com.wbd.pojo.TbContentCategoryExample;
import com.wbd.pojo.TbContentCategoryExample.Criteria;
import com.wbd.portal.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {

		TbContentCategoryExample e = new TbContentCategoryExample();
		Criteria c = e.createCriteria();
		c.andParentIdEqualTo(parentId);
		List<TbContentCategory> cateGorylist = contentCategoryMapper.selectByExample(e);

		List<EasyUITreeNode> treeList = new ArrayList<EasyUITreeNode>();

		for (TbContentCategory cl : cateGorylist) {
			EasyUITreeNode eun = new EasyUITreeNode();

			eun.setId(cl.getId());
			eun.setText(cl.getName());
			eun.setState(cl.getIsParent() ? "closed" : "open");

			treeList.add(eun);
		}

		return treeList;
	}

}
