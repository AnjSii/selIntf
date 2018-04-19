package com.tomowork.shop.selIntf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tomowork.shop.core.tools.CommUtil;
import com.tomowork.shop.core.tools.Md5Encrypt;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.UserService;
import com.tomowork.shop.selIntf.exception.ViolationException;
import com.tomowork.shop.selIntf.service.RegisterService;

/**
 * @author kuangxiang
 */
@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private UserService userService;

	@Override
	public void register(String mobile, String password) throws ViolationException, NullPointerException, IllegalArgumentException {
		if (password == null) {
			throw new NullPointerException();
		}
		if (password.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (mobile == null) {
			throw new NullPointerException();
		}
		if (mobile.isEmpty()) {
			throw new IllegalArgumentException();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("mobile", mobile);
		List<User> olduser = this.userService.query("select obj from User obj where obj.mobile=:mobile", params, -1, -1);
		if (olduser.size() > 0) {
			throw new ViolationException("该手机号码已被注册过");
		}

		String userName;
		List<User> olduserName;
		do {
			userName = randomUserName();
			params.clear();
			params.put("userName", userName);
			olduserName = this.userService.query("select obj from User obj where obj.userName=:userName", params, -1, -1);
		} while (!olduserName.isEmpty());

		User user = new User();
		user.setAddTime(new Date());
		user.setSex(-1);
		user.setUserRole("BUYER");
		user.setMobile(mobile);
		user.setUserName(userName);
		user.setPassword(Md5Encrypt.md5(password).toLowerCase());
		this.userService.save(user);
	}

	private String randomUserName() {
		StringBuilder userName = new StringBuilder();
		Random random = new Random();
		//随机生成，大写，小写字母，数字组成的八位用户名
		for (int i = 0; i < 8; i++) {
			//求余，如果余数为零那么就是输出字符串，余数不为零输出数字。
			boolean charOrNum = random.nextInt(2) % 2 == 0;
			//输出字母还是数字
			if (charOrNum) {
				//输出是大写字母还是小写字母,65～90号为26个大写英文字母，97～122号为26个小写英文字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				userName.append((char) (random.nextInt(26) + temp));
			} else {
				userName.append(random.nextInt(10));
			}
		}
		if (CommUtil.null2Int(userName) != 0) {
			return randomUserName();
		}
		return userName.toString();
	}
}
