package com.tomowork.shop.selIntf.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomowork.shop.api.ApiErrorVO;
import com.tomowork.shop.selIntf.service.SmsSendRateLimitService;
import com.tomowork.shop.selIntf.service.VerificationService;
import com.tomowork.shop.selIntf.util.validator.Mobile;

/**
 * @author kuangxiang
 */
@RestController
@Validated
public class VerifyCodeController {

	@Autowired
	private VerificationService verificationService;

	@Autowired
	private SmsSendRateLimitService smsSendRateLimitService;

	/**
	 * 根据注册用户的手机号码发送验证码
	 * @param mobile 手机号码
	 * @return 短信验证码
	 * @throws URISyntaxException URI不正确
	 */
	@RequestMapping(value = "/verification/sms", method = RequestMethod.POST)
	public ResponseEntity<Object> getVerifyCode(@RequestParam(value = "mobile", required = true) @Mobile String mobile) throws URISyntaxException {
		boolean exists = this.verificationService.checkMobileRegistration(mobile);
		if (exists) {
			return new ResponseEntity<Object>(new ApiErrorVO(422, "该手机号码已被注册", "该手机号码已被注册"), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		boolean smsSendRateLimit = this.smsSendRateLimitService.checkRateLimit(mobile);
		if (smsSendRateLimit) {
			return ResponseEntity
					.status(HttpStatus.TOO_MANY_REQUESTS)
					.header("Retry-After", "60")
					.body(new ApiErrorVO(429, "请勿重复获取手机验证码", "请勿重复获取手机验证码"));
		}

		this.verificationService.sendRegistrationSms(mobile);
		return ResponseEntity.created(new URI("/verification/sms/" + mobile)).build();
	}

	/**
	 * 根据用户绑定的手机号码发送验证码
	 * @param mobile 手机号码
	 * @return 短信验证码
	 * @throws URISyntaxException URI语法错误
	 */
	@RequestMapping(value = "/verification/sms/resetPassword", method = RequestMethod.POST)
	public ResponseEntity<Object> getResetPasswordVerifyCode(@RequestParam(value = "mobile", required = true) @Mobile String mobile) throws URISyntaxException {
		boolean exists = this.verificationService.checkMobileRegistration(mobile);
		if (!exists) {
			return new ResponseEntity<Object>(new ApiErrorVO(422, "输入的手机号码不正确", "输入的手机号码不正确"), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		boolean smsSendRateLimit = this.smsSendRateLimitService.checkRateLimit(mobile);
		if (smsSendRateLimit) {
			return ResponseEntity
					.status(HttpStatus.TOO_MANY_REQUESTS)
					.header("Retry-After", "60")
					.body(new ApiErrorVO(429, "请勿重复获取手机验证码", "请勿重复获取手机验证码"));
		}

		Long id = this.verificationService.sendResetPasswordSms(mobile);
		return ResponseEntity.created(new URI("/verification/sms/" + id)).build();
	}
}
