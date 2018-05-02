package com.tomowork.shop.selIntf.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 验证url格式
 * @author wuxun
 */
public class ValidateUrlImpl implements ConstraintValidator<Url, String> {

	private String urlReg = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

	private Pattern urlPattern = Pattern.compile(urlReg);

	@Override
	public void initialize(Url constraintAnnotation) {
	}

	@Override
	public boolean isValid(String url, ConstraintValidatorContext context) {
		if (url == null) {
			return true;
		} else {
			Matcher matcher = urlPattern.matcher(url);
			return matcher.matches();
		}
	}

}
