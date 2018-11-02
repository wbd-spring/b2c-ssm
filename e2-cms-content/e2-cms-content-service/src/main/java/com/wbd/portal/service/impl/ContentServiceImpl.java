package com.wbd.portal.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wbd.common.jedis.JedisClient;
import com.wbd.common.utils.JsonUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.mapper.TbContentMapper;
import com.wbd.pojo.TbContent;
import com.wbd.pojo.TbContentExample;
import com.wbd.pojo.TbContentExample.Criteria;
import com.wbd.portal.service.ContentService;
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper  contentMapper;
	
	@Autowired
	private JedisClient  jedisClient;
	
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Override
	public WBDResult addContent(TbContent content) {
		Date date = new Date();
		content.setCreated(date);
		content.setUpdated(date);
		contentMapper.insert(content);
		/**		
		 * 一般在 add，update，delelte 方法中需要实现缓存同步，
	     * 即在这些方法中 删除之前的缓存，
		 */
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return WBDResult.ok();
	}

	
	/**
	 * 一般在 add，update，delelte 方法中需要实现缓存同步，
	 * 即在这些方法中 删除之前的缓存，
	 * 
	 * 在该方法中 添加缓存的业务逻辑
	 * 
	 * 1.先去缓存中获取，取缓存中如果有异常，不能导致下面的业务也失败， 所以把对 缓存的操作
	 * 放在try catch中
	 * 2.如果缓存中没有，查询数据库，然后存入redis缓存中 ,采用hashset， hashet的结构为
	 * 
	 * <key,Map<filed,value>>
	 * 
	 * Hash：key-fields-values（做缓存）
相当于一个key对于一个map，map中还有key-value
使用hash对key进行归类。
Hset：向hash中添加内容
Hget：从hash中取内容

	   存入缓存中如果有异常，不能导致下面的业务也失败， 所以把对 缓存的操作
	 * 放在try catch中
	 * 
	 * 根据分类id获取内容列表 
	 * @param cid
	 * @return
	 */
	@Override
	public List<TbContent> getContentListByCategoryId(long cid) {
		//1.先去缓存中进行查询， 把改代码放入try-catch中
		
				try {
					String redisJson = jedisClient.hget(CONTENT_LIST, cid+"");
					if(StringUtils.isNotBlank(redisJson)) {
						//将json转换为 list对象
					List<TbContent> jsonToList = JsonUtils.jsonToList(redisJson, TbContent.class);
					return jsonToList;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				//创建查询条件对象
				TbContentExample tbContentExample = new TbContentExample();
				
				Criteria cc = tbContentExample.createCriteria();
				//设置分类id等于cid
				cc.andCategoryIdEqualTo(cid);
				
				List<TbContent> list = contentMapper.selectByExampleWithBLOBs(tbContentExample);
				
				//2.存入缓存
				try {
					jedisClient.hset(CONTENT_LIST, cid+"", JsonUtils.objectToJson(list));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
	

}
