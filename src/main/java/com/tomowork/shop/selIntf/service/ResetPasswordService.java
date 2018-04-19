package com.tomowork.shop.selIntf.service;

import com.tomowork.shop.selIntf.exception.ViolationException;

/**
 * 重置用户密码Service
 * @author kuangxiang
 */
public interface ResetPasswordService {

	/**
	 * 验证验证码
	 * @param uid UID
	 * @param mobile 手机号码
	 * @param code 验证码
	 * @throws ViolationException 输入手机号码不正确、uid不正确、验证码不正确
	 * @throws NullPointerException 输入手机号码、验证码、uid为null
	 * @throws IllegalArgumentException 输入手机号码、验证码、uid为空
	 */
	boolean verifyMobileVerificationCode(String uid, String mobile, String code) throws ViolationException, NullPointerException, IllegalArgumentException;

	/**
	 * 用户重置密码
	 * @param mobile 手机号码
	 * @param password 初始密码
	 * @throws ViolationException 输入手机号码不正确
	 * @throws NullPointerException 输入密码为空或手机号码为空
	 * @throws IllegalArgumentException 输入密码为空或手机号码为空
	 */
	void resetPassword(String mobile, String password) throws ViolationException, NullPointerException, IllegalArgumentException;
}
