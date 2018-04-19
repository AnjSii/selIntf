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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tomowork.shop.api.ApiErrorVO;
import com.tomowork.shop.selIntf.service.ResetPasswordService;
import com.tomowork.shop.selIntf.util.validator.Mobile;
import com.tomowork.shop.selIntf.util.validator.Password;

/**
 * @author kuangxiang
 */
@RestController
public class ResetPasswordController {

	@Autowired
	private ResetPasswordService resetPasswordService;

	/**
	 * 用户重置密码
	 * @param resetPasswordVO 用户重置密码类
	 * @return 用户重置密码
	 */
	@RequestMapping(value = "/password/reset", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordVO resetPasswordVO) {
		boolean verifyResult = this.resetPasswordService.verifyMobileVerificationCode(resetPasswordVO.getId(), resetPasswordVO.getMobile(), resetPasswordVO.getVerifyCode());
		if (!verifyResult) {
			return new ResponseEntity<Object>(new ApiErrorVO(422, "请输入正确的验证码", "参数错误"), HttpStatus.UNPROCESSABLE_ENTITY);
		}

		this.resetPasswordService.resetPassword(resetPasswordVO.getMobile(), resetPasswordVO.getPassword());
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

	static class ResetPasswordVO {
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

		@NotNull(message = "请输入uid")
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

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
