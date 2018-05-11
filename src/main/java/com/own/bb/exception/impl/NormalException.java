package com.own.bb.exception.impl;

import com.own.bb.exception.ICustomException;

public class NormalException extends BasicException implements ICustomException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NormalException(String info) {
		super(info);
	}

	public NormalException(String code, String info) {
		super(code, info);
	}

	public NormalException(String code, String info, String message) {
		super(code, info, message);
	}

	public NormalException(String code, String info, String message, Throwable t) {
		super(code, info, message, t);
	}
}
