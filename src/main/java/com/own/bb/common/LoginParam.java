package com.own.bb.common;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录用户名
 */
public class LoginParam {
	public static final String LOGIN_URL = "login";
	public static final String LOGIN_ID = "login_id";
	public static final String LOGIN_NAME = "login_name";

	public static String getCurrentLoginId(HttpServletRequest request) {
		String id = (String) request.getSession(true).getAttribute(LoginParam.LOGIN_ID);
		return id;
	}

	public static String getCurrentLoginUsername(HttpServletRequest request) {
		String username = (String) request.getSession(true).getAttribute(LoginParam.LOGIN_NAME);
		return username;
	}

}
