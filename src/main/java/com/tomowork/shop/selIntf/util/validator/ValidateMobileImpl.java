package com.tomowork.shop.selIntf.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证手机号码格式
 * @author kuangxiang
 */
public class ValidateMobileImpl implements ConstraintValidator<Mobile, String> {

	//手机号码的正则验证
	private String mobileReg = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0-1,3,5-8])|(18[0-9]))\\d{8}$";

	private Pattern mobilePattern = Pattern.compile(mobileReg);

	@Override
	public void initialize(Mobile constraintAnnotation) {
	}

	@Override
	public boolean isValid(String mobile, ConstraintValidatorContext context) {
		if (mobile == null) {
			return true;
		} else {
			Matcher matcher = mobilePattern.matcher(mobile);
			return matcher.matches();
		}
	}

}
