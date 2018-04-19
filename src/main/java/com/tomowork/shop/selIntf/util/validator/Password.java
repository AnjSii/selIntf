package com.tomowork.shop.selIntf.util.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 验证密码格式
 *
 * @author kuangxiang
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatePasswordImpl.class)
public @interface Password {

	String message() default "密码仅允许字母数字和特殊字符组合";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
