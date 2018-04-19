package com.tomowork.shop.selIntf.exception;

/**
 * 违规异常.
 *
 * @author zlei
 */
public class ViolationException extends RuntimeException {
	private static final long serialVersionUID = 1;

	public ViolationException() {
	}

	public ViolationException(String message) {
		super(message);
	}

	public ViolationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ViolationException(Throwable cause) {
		super(cause);
	}
}
