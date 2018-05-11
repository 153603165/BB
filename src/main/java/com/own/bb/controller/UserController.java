package com.own.bb.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.own.bb.common.LoginParam;
import com.own.bb.entity.User;
import com.own.bb.exception.impl.NormalException;
import com.own.bb.service.UserService;
import com.own.bb.utils.ExcelUtil;

@Controller
@RequestMapping("/user")
public class UserController extends BasicController {
	@Autowired
	UserService userService;

	@ResponseBody
	@RequestMapping(value = "/description")
	public ResponseEntity<String> description(@RequestParam(required = true) String username) {
		User user = userService.findByUsername(username);
		return new ResponseEntity<String>(JSONObject.toJSONString(user.getDescription()), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/update")
	public ResponseEntity<String> update(HttpServletRequest request,
			@RequestParam(required = true) String description) {
		String username = LoginParam.getCurrentLoginUsername(request);
		User user = userService.findByUsername(username);
		user.setDescription(description);
		userService.update(user);
		return new ResponseEntity<String>(JSONObject.toJSONString(user.getDescription()), HttpStatus.OK);
	}

	@RequestMapping(value = "/export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) {
		List<User> userList = new ArrayList<>();
		User user1 = new User("路飞", "passwordqwdqwd", "1", new Date(), "xxxx");
		User user2 = new User("娜美", "passwordwdwdw", "2", new Date(), "xxxx");
		User user3 = new User("索隆", "password213213", "1", new Date(), "xxxx");
		User user4 = new User("小狸猫", "password123wqdwqd213", "2", new Date(), "xxxx");
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		userList.add(user4);
		try {
			ExcelUtil.exportExcel(userList, "花名册", "草帽一伙", User.class, "海贼王.xls", response);
		} catch (NormalException e) {
			e.printStackTrace();
		}

	}

	@ResponseBody
	@PostMapping("/upload")
	public void importExcel(@RequestParam(required = true, value = "upload_file") MultipartFile file) {
		List<User> userList = new ArrayList<>();
		try {
			userList = ExcelUtil.importExcel(file, 1, 1, User.class);
			System.out.println("导入数据一共【" + userList.size() + "】行");
			for (User user : userList) {
				System.out.println(user);
			}
		} catch (NormalException e) {
			e.printStackTrace();
		}
	}

}
