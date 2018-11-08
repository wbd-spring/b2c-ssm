package com.wbd.sso.service;
/**
 * 用户服务接口
* <p>Title: UserService.java</p>  
* <p>Description: </p>  
* @author 朱光和 
* @date 2018年11月7日
 */

import com.wbd.common.utils.WBDResult;
import com.wbd.pojo.TbUser;

public interface UserService {
	
	/**
	 * 检查用户名是否存在。
	 * 
	 * type=1时为用户名
	 * type=2是为手机
	 * type=3时为email
	 * <p>Title: checkData</p>  
	 * <p>Description: </p>  
	 * @param param  值
	 * @param type   类型
	 * @return
	 */
	WBDResult checkData(String param,int type);
	
	/**
	 * 注册用户 
	 * <p>Title: createUser</p>  
	 * <p>Description: </p>  
	 * @param user
	 * @return
	 */
	WBDResult createUser(TbUser user);

	
	/**
	 * 登录接口， 返回值中包含用户的token，
	 * token通过uuid自动生成， uuid是唯一的
	 * <p>Title: login</p>  
	 * <p>Description: </p>  
	 * @param username
	 * @param password
	 * @return
	 */
	WBDResult login(String username,String password);
	
	/**
	 * 根据token获取用户信息， 直接查询在redis存储的token对应的user信息
	 * <p>Title: getUserByToken</p>  
	 * <p>Description: </p>  
	 * @param token
	 * @return
	 */
	WBDResult getUserByToken(String token);
}
