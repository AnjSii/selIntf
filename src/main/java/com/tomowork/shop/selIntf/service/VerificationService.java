package com.tomowork.shop.selIntf.service;

import com.tomowork.shop.selIntf.exception.ViolationException;

/**
 * 发送验证码、验证该手机号码是否被注册过、验证手机号码与验证码是否匹配Service
 * @author kuangxiang
 */
public interface VerificationService {

	/**
	 * 验证手机号码是否被注册
	 * @param mobile 手机号码
	 * @return 手机号码的注册状态
	 */
	boolean checkMobileRegistration(String mobile);

	/**
	 * 发送验证码
	 * @param recipient 收信人的手机号码
	 */
	void sendRegistrationSms(String recipient);

	/**
	 * 验证手机号码与验证码是否匹配
	 * @param mobile 手机号码
	 * @param verifyCode 验证码
	 * @return 手机号码与验证码是否匹配
	 * @throws ViolationException 输入的手机号码不正确
	 * @throws NullPointerException mobile为空或verifyCode为空
	 * @throws IllegalArgumentException mobile为空或verifyCode为空
	 */
	boolean verifyMobilewithCode(String mobile, String verifyCode) throws ViolationException, NullPointerException, IllegalArgumentException;

	/**
	 * 发送重置密码的验证码
	 * @param recipient 收信人的手机号码
	 * @return 验证码的id
	 */
	long sendResetPasswordSms(String recipient);
}
