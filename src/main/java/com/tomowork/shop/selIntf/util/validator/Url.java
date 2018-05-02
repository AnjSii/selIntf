package com.tomowork.shop.selIntf.util.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 验证url
 *
 * @author wuxun
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateUrlImpl.class)
public @interface Url {

	String message() default "输入的url格式不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
