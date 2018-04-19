package com.tomowork.shop.selIntf.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tomowork.shop.api.ApiErrorVO;
import com.tomowork.shop.foundation.domain.MobileVerifyCode;
import com.tomowork.shop.foundation.service.MobileVerifyCodeService;
import com.tomowork.shop.selIntf.service.RegisterService;
import com.tomowork.shop.selIntf.service.VerificationService;
import com.tomowork.shop.selIntf.util.validator.Mobile;
import com.tomowork.shop.selIntf.util.validator.Password;

/**
 * @author kuangxiang
 */
@RestController
public class RegisterController {

	@Autowired
	private MobileVerifyCodeService mobileverifycodeService;

	@Autowired
	private RegisterService registerService;

	@Autowired
	private VerificationService verificationService;

	/**
	 * 验证验证码和注册账号
	 * @param registerVO 用户注册类
	 * @return 注册用户
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Object> register(@RequestBody @Valid RegisterVO registerVO) {
		MobileVerifyCode mvc = this.mobileverifycodeService.getObjByProperty(
				"mobile", registerVO.getMobile());
		if (mvc == null) {
			return new ResponseEntity<Object>(new ApiErrorVO(422, "请输入正确的手机号码", "参数错误"), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		boolean verifyResult = this.verificationService.verifyMobilewithCode(registerVO.getMobile(), registerVO.getVerifyCode());
		if (!verifyResult) {
			return new ResponseEntity<Object>(new ApiErrorVO(422, "请输入正确的验证码", "参数错误"), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		this.registerService.register(registerVO.getMobile(), registerVO.getPassword());
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	static class RegisterVO {
		@Mobile
		@NotNull(message = "请输入手机号码")
		private String mobile;

		@NotNull(message = "请输入验证码")
		@Size(min = 1, message = "输入的验证码不能为空")
		private String verifyCode;

		@NotNull(message = "请输入密码")
		@Password
		@Size(min = 6, max = 20, message = "输入的密码为6到20个字符")
		private String password;

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getVerifyCode() {
			return verifyCode;
		}

		public void setVerifyCode(String verifyCode) {
			this.verifyCode = verifyCode;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
