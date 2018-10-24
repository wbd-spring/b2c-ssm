package com.wbd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wbd.common.pojo.EasyUITreeNode;
import com.wbd.mapper.TbItemCatMapper;
import com.wbd.pojo.TbItemCat;
import com.wbd.pojo.TbItemCatExample;
import com.wbd.pojo.TbItemCatExample.Criteria;
import com.wbd.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		// 1.构建查询对象

		TbItemCatExample tbItemCatExample = new TbItemCatExample();

		Criteria createCriteria = tbItemCatExample.createCriteria();
        
		//2.设置查询条件
		createCriteria.andParentIdEqualTo(parentId);

		List<TbItemCat> selectList = itemCatMapper.selectByExample(tbItemCatExample);

		//3.将selectList的值转换为List<EasyUITreeNode>所需要的值
		List<EasyUITreeNode> list = new ArrayList<EasyUITreeNode>();

		for (TbItemCat tc : selectList) {
			
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tc.getId());
			node.setText(tc.getName());
			node.setState(tc.getIsParent()?"closed":"open");
			list.add(node);

		}
		return list;
	}

}
