package com.tomowork.shop.selIntf.exception;

/**
 * 实体已经存在.
 *
 * @author zlei
 */
public class EntityExistsException extends RuntimeException {
	private static final long serialVersionUID = 1;

	public EntityExistsException() {
		super();
	}

	public EntityExistsException(String message) {
		super(message);
	}

	public EntityExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityExistsException(Throwable cause) {
		super(cause);
	}
}
