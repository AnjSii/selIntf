package com.tomowork.shop.selIntf.util.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 验证手机号码格式
 *
 * @author kuangxiang
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateMobileImpl.class)
public @interface Mobile {

	String message() default "输入的手机号码格式不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
