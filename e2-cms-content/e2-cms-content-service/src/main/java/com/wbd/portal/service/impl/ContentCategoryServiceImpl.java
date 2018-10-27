package com.wbd.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbd.common.pojo.EasyUITreeNode;
import com.wbd.common.utils.WBDResult;
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

	@Override
	public WBDResult addContentCategory(long parentId, String name) {
		
		//1.创建ContentCategory对象，进行复制
		TbContentCategory contentCategory = new TbContentCategory();
		Date date = new Date();
		//新添加的节点都是叶子节点
		contentCategory.setIsParent(false);
		contentCategory.setParentId(parentId);
		//'状态。可选值:1(正常),2(删除)',
		contentCategory.setStatus(1);
		contentCategory.setCreated(date);
		contentCategory.setName(name);
		contentCategory.setUpdated(date);
		contentCategory.setSortOrder(1);
		
		//2.插入数据库,此时会返回id，赋给contentCategory对象的属性id
		contentCategoryMapper.insert(contentCategory);
		
		//3.根据parentid查询对象，如果是叶子节点就修改为 父节点。
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()) { //如果 是子节点， 就修改为父节点
			parent.setIsParent(true); //设置父节点
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		//此时的contentCategory对象中已经包含了从数据库插入数据的最后id
		return WBDResult.ok(contentCategory);
	}

}
