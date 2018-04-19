package com.tomowork.shop.selIntf.service;

import com.tomowork.shop.selIntf.exception.ViolationException;

/**
 * 用户注册Service
 * @author kuangxiang
 */
public interface RegisterService {

	/**
	 * 用户注册
	 * @param mobile 手机号码
	 * @param password 初始密码
	 * @throws ViolationException 该手机号已经被注册过
	 * @throws NullPointerException 初始密码为空或手机号码为空
	 * @throws IllegalArgumentException 初始密码为空或手机号码为空
	 */
	void register(String mobile, String password) throws ViolationException, NullPointerException, IllegalArgumentException;

}
