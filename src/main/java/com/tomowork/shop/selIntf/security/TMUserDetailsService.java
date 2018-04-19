package com.tomowork.shop.selIntf.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.tomowork.shop.foundation.domain.Role;
import com.tomowork.shop.foundation.domain.User;
import com.tomowork.shop.foundation.service.UserService;

/**
 * UserDetailsService.
 *
 * @author zlei
 */
public class TMUserDetailsService implements UserDetailsService, InitializingBean {

	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String[] list = username.split(",");
		String userName = list[0];
		String loginRole = "USER";
		if (list.length == 2) {
			loginRole = list[1];
		}
		// hzl, 仅允许普通用户登录，BUYER / SELLER
		if (!"USER".equalsIgnoreCase(loginRole)) {
			throw new UsernameNotFoundException("User " + userName
					+ " has no GrantedAuthority");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("userName", userName);
		List<User> users = this.userService.query(
				"select obj from User obj where obj.userName =:userName ",
				params, -1, -1);
		if (users.isEmpty()) { // fallback, 用手机号码登录
			users = this.userService.query(
					"select obj from User obj where obj.mobile =:userName ",
					params, -1, -1);
		}
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("User " + userName
					+ " has no GrantedAuthority");
		}
		User user = users.get(0);

		// xluo  这里并不是找不到用户  而是禁止管理员在前台登录
		if ("ADMIN".equalsIgnoreCase(user.getUserRole())) {
			throw new UsernameNotFoundException("User " + userName
					+ " has no GrantedAuthority");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		if (user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				if (loginRole.equalsIgnoreCase("ADMIN")) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
							role.getRoleCode().toUpperCase());
					authorities.add(grantedAuthority);
				} else if (!role.getType().equals("ADMIN")) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
							role.getRoleCode().toUpperCase());
					authorities.add(grantedAuthority);
				}
			}
		}

		return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
				.password(user.getPassword())
				.accountExpired(!user.isAccountNonExpired())
				.accountLocked(!user.isAccountNonLocked())
				.credentialsExpired(!user.isCredentialsNonExpired())
				.disabled(!user.isEnabled())
				.authorities(authorities)
				.build();
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.userService, "A UserService must be set");
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		Assert.notNull(userService, "userService cannot be null");
		this.userService = userService;
	}
}
