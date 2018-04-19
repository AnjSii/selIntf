package com.tomowork.shop.selIntf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.core.tools.VelocityHelper;
import com.tomowork.shop.foundation.domain.MobileVerifyCode;
import com.tomowork.shop.foundation.domain.Template;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.MobileVerifyCodeService;
import com.tomowork.shop.foundation.service.TemplateService;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.manage.admin.tools.MsgTools;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.VerificationService;

/**
 * @author kuangxiang
 */
@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

	@Autowired
	private UserService userService;

	@Autowired
	private MobileVerifyCodeService mobileverifycodeService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private MsgTools msgTools;

	@Override
	public boolean checkMobileRegistration(String mobile) {
		Map<String, Object> params = new HashMap<>();
		params.put("mobile", mobile);
		List<User> user = this.userService.query("select obj from User obj where obj.mobile=:mobile", params, -1, -1);
		return user.size() > 0;
	}

	@Override
	public void sendRegistrationSms(String recipient) {
		MobileVerifyCode mvc = this.mobileverifycodeService
				.getObjByProperty("mobile", recipient);
		Template template = this.templateService
				.getObjByProperty("mark", "sms_toregistrant_register_notify");
		String code = CommUtil.randomInt(6);
		Map<String, Object> context = new HashMap<>();
		context.put("code", code);
		String content = VelocityHelper.merge(template.getContent(), context);
		boolean sent = this.msgTools.sendSMS(recipient, content);
		if (sent) {
			if (mvc == null) {
				mvc = new MobileVerifyCode();
				mvc.setAddTime(new Date());
				mvc.setCode(code);
				mvc.setMobile(recipient);
				this.mobileverifycodeService.save(mvc);
			} else {
				mvc.setAddTime(new Date());
				mvc.setCode(code);
				this.mobileverifycodeService.update(mvc);
			}
		}
	}

	@Override
	public boolean verifyMobilewithCode(String mobile, String verifyCode) throws ViolationException, NullPointerException, IllegalArgumentException {
		if (mobile == null) {
			throw new NullPointerException();
		}
		if (mobile.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (verifyCode == null) {
			throw new NullPointerException();
		}
		if (verifyCode.isEmpty()) {
			throw new IllegalArgumentException();
		}
		MobileVerifyCode mvc = this.mobileverifycodeService.getObjByProperty(
				"mobile", mobile);
		if (mvc == null) {
			throw new ViolationException("请输入正确的验证码");
		}
		boolean verifyResult = false;
		if (!mvc.getCode().equalsIgnoreCase(verifyCode)) {
			int count = mvc.getCount();
			++count;
			if (count > 9) {
				this.mobileverifycodeService.delete(mvc.getId());
				return verifyResult;
			} else {
				mvc.setCount(count);
				this.mobileverifycodeService.update(mvc);
			}
		} else {
			this.mobileverifycodeService.delete(mvc.getId());
			verifyResult = true;
		}
		return verifyResult;
	}

	@Override
	public long sendResetPasswordSms(String recipient) {
		Template template = this.templateService
				.getObjByProperty("mark", "sms_touser_resetpassword_notify");
		String code = CommUtil.randomInt(6);
		Map<String, Object> context = new HashMap<>();
		context.put("code", code);
		String content = VelocityHelper.merge(template.getContent(), context);
		boolean sent = this.msgTools.sendSMS(recipient, content);
		long id = 0L;
		if (sent) {
			MobileVerifyCode mvc = new MobileVerifyCode();
			mvc.setAddTime(new Date());
			mvc.setCode(code);
			mvc.setMobile(recipient);
			this.mobileverifycodeService.save(mvc);
			id = mvc.getId();
		}
		return id;
	}
}
