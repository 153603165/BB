package com.own.bb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.own.bb.common.LoginParam;
import com.own.bb.common.SystemConstant;
import com.own.bb.entity.User;
import com.own.bb.service.UserService;
import com.own.bb.utils.JwtUtils;

@Controller
public class LoginController extends BasicController {
	@Autowired
	UserService userService;

	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}

	@ResponseBody
	@RequestMapping(value = "/signIn")
	public ResponseEntity<String> signIn(String username, String password, HttpServletRequest request,
			HttpServletResponse response) {
		User user = userService.findByUsername(username);
		if (user != null) {
			if (user.getPassword().equals(password)) {
				String JWT = JwtUtils.createJWT("1", username, SystemConstant.JWT_TTL);
				request.getSession().setAttribute("JWT", JWT);
				request.getSession().setAttribute(LoginParam.LOGIN_NAME, user.getUsername());
				request.getSession().setAttribute(LoginParam.LOGIN_ID, user.getId());
				return new ResponseEntity<String>(HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("账号密码不正确", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<String>("账号不存在", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/loginOut")
	public String logOut(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("JWT");
		request.getSession().removeAttribute(LoginParam.LOGIN_ID);
		request.getSession().removeAttribute(LoginParam.LOGIN_NAME);
		return "login";

	}

	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}

}
