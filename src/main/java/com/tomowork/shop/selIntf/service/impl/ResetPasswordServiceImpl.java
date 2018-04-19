package com.tomowork.shop.selIntf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.core.tools.Md5Encrypt;
import com.tomowork.shop.foundation.domain.MobileVerifyCode;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.MobileVerifyCodeService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.ResetPasswordService;

/**
 * @author kuangxiang
 */
@Service
@Transactional
public class ResetPasswordServiceImpl implements ResetPasswordService {

	@Autowired
	private UserService userService;

	@Autowired
	private MobileVerifyCodeService mobileVerifyCodeService;

	@Override
	public boolean verifyMobileVerificationCode(String uid, String mobile, String code) throws ViolationException, NullPointerException, IllegalArgumentException {
		if (uid == null) {
			throw new NullPointerException();
		}

		if (uid.isEmpty()) {
			throw new IllegalArgumentException();
		}

		if (code == null) {
			throw new NullPointerException();
		}

		if (code.isEmpty()) {
			throw new IllegalArgumentException();
		}

		if (mobile == null) {
			throw new NullPointerException();
		}

		if (mobile.isEmpty()) {
			throw new IllegalArgumentException();
		}

		MobileVerifyCode mvc = this.mobileVerifyCodeService.getObjById(CommUtil.null2Long(uid));
		if (mvc == null) {
			throw new ViolationException("请输入正确的验证码");
		}

		if (!mvc.getMobile().equals(mobile)) {
			throw new ViolationException("请输入正确的手机号码");
		}

		Map<String, Object> params = new HashMap<>();
		params.put("mobile", mobile);
		List<User> users = this.userService.query("select obj from User obj where obj.mobile=:mobile", params, -1, -1);
		if (users.size() == 0) {
			throw new ViolationException("请输入正确的手机号码");
		}

		boolean verifyResult = false;
		if (!mvc.getCode().equals(code)) {
			int count = mvc.getCount();
			++count;
			if (count > 5) {
				this.mobileVerifyCodeService.delete(mvc.getId());
				return verifyResult;
			} else {
				mvc.setCount(count);
				this.mobileVerifyCodeService.update(mvc);
			}
		} else {
			this.mobileVerifyCodeService.delete(mvc.getId());
			verifyResult = true;
		}
		return verifyResult;
	}

	@Override
	public void resetPassword(String mobile, String password) throws ViolationException, NullPointerException, IllegalArgumentException {
		if (password == null) {
			throw new NullPointerException();
		}
		if (password.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (mobile == null) {
			throw new NullPointerException();
		}
		if (mobile.isEmpty()) {
			throw new IllegalArgumentException();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("mobile", mobile);
		List<User> users = this.userService.query("select obj from User obj where obj.mobile=:mobile", params, -1, -1);
		users.get(0).setPassword(Md5Encrypt.md5(password).toLowerCase());
		this.userService.update(users.get(0));
	}
}
