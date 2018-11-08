package com.wbd.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.wbd.common.jedis.JedisClient;
import com.wbd.common.utils.JsonUtils;
import com.wbd.common.utils.WBDResult;
import com.wbd.mapper.TbUserMapper;
import com.wbd.pojo.TbUser;
import com.wbd.pojo.TbUserExample;
import com.wbd.pojo.TbUserExample.Criteria;
import com.wbd.sso.service.UserService;

/**
 * 
 * <p>
 * Title: UserServiceImpl.java
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 朱光和
 * @date 2018年11月7日
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient  jedisClient;

	//token key的前缀
	@Value("${SESSION}")
	private String SESSION;
	
	//token的过期时间
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public WBDResult checkData(String param, int type) {

		// 根据传入不同的值，动态生成sql对数据库进行查询

		TbUserExample te = new TbUserExample();
		// 创建查询条件
		Criteria criteria = te.createCriteria();
		// 进行逻辑判断
		if (type == 1) {
			criteria.andUsernameEqualTo(param);

		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);

		} else if (type == 3) {
			criteria.andEmailEqualTo(param);

		} else {
			return WBDResult.build(400, "输入非法的字段");
		}

		List<TbUser> list = userMapper.selectByExample(te);

		
		if (list == null || list.size() == 0) {
			// 如果没有返回true
			return WBDResult.ok(true);
		}

		return WBDResult.ok(false);
	}

	@Override
	public WBDResult createUser(TbUser user) {

		// 注册用户 ，需要给用户密码加密操作

		if (StringUtils.isBlank(user.getUsername())) {
			return WBDResult.build(400, "用户名为空");
		}

		if (StringUtils.isBlank(user.getPassword())) {
			return WBDResult.build(400, "密码为空");
		}
		
		// 验证数据的合法性

		if (StringUtils.isNoneBlank(user.getUsername())) {

			if (!(boolean) this.checkData(user.getUsername(), 1).getData()) {
				return WBDResult.build(400, "用户名已经被占用");
			}
		}
		if (StringUtils.isNoneBlank(user.getPhone())) {

			if (!(boolean) this.checkData(user.getPhone(), 2).getData()) {
				return WBDResult.build(400, "手机已经被占用");
			}
		}

		if (StringUtils.isNoneBlank(user.getEmail())) {

			if (!(boolean) this.checkData(user.getEmail(), 3).getData()) {
				return WBDResult.build(400, "邮箱已经被占用");
			}
		}

		
		// 补全其他属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		userMapper.insert(user);
		return WBDResult.ok();
	}

	@Override
	public WBDResult login(String username, String password) {
		
		//1.判断用户名和密码是否正确
		
		TbUserExample tue = new TbUserExample();
		Criteria criteria = tue.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(tue);
		if(list==null||list.size()==0) { //如果数据库中不存在，就调整到登录页面
			return WBDResult.build(400, "用户名或者密码错误");
		}
		
			//1.1验证密码
		
		TbUser tbUser = list.get(0);
		if(!tbUser.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			
			return WBDResult.build(400, "用户名或者密码错误");
		}
		
		//2.成功之后，通过uuid生成token，也就是jssionid,唯一性的
		String token = UUID.randomUUID().toString();
		tbUser.setPassword(null);
		
		
		//3.把用户信息保存到redis中，key为token，value为user信息
		// key=SESSION:token
		// value= user
		
		jedisClient.set(SESSION+":"+token, JsonUtils.objectToJson(tbUser));
		
			//3.1设置key的过期时间为半个小时，redis以秒为单位，半个小时：60*30=1800
		
		jedisClient.expire(SESSION+":"+token, SESSION_EXPIRE);
		
		
		//4.返回token(uuid生成的，即也是jessinoid)
		return WBDResult.ok(token);
	}

	@Override
	public WBDResult getUserByToken(String token) {
		
		//1.根据token查询redis中的user信息
		
		String json = jedisClient.get(SESSION+":"+token);
		
		//2.如果不存在，跳转到登录页面
		
		if(StringUtils.isBlank(json)) {
			return WBDResult.build(400, "用户登录已过期，请重新登录");
		}
		
		//3.如果查询到数据， 说明用户已经登录，需要重置key的过期时间
		jedisClient.expire(SESSION+":"+token, SESSION_EXPIRE);
		
		//4.把 json转换为user对象，然后返回到WBDResult对象中
		TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);
		
		return WBDResult.ok(tbUser);
	}

}
