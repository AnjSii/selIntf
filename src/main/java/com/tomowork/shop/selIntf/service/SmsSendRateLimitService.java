package com.tomowork.shop.selIntf.service;

/**
 * 验证该手机号码是否是在一分钟内多次请求发送验证码Service
 * @author kuangxiang
 */
public interface SmsSendRateLimitService {

	/**
	 * 验证该手机号码是否是在一分钟内多次请求发送验证码
	 * @param mobile 手机号码
	 * @return 该手机号码是否一分钟内多次请求发送验证码
	 */
	boolean checkRateLimit(String mobile);
}
