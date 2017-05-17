package com.framgia.users.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framgia.users.dao.UserDAO;
import com.framgia.users.model.Permissions;
import com.framgia.users.model.Users;

/**
 * MyUserDetailsService.java
 * 
 * @version 16/04/2017
 * @author phan.van.hieu@framgia.com
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDAO userDao;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		Users user = userDao.findByUserName(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getPermissions());

		return buildUserForAuthentication(user, authorities);

	}

	// org.springframework.security.core.userdetails.User
	private User buildUserForAuthentication(Users user, List<GrantedAuthority> authorities) {
		return new User(user.getUserName(), user.getPassWord(), true, true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Permissions permissions) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		setAuths.add(new SimpleGrantedAuthority(permissions.getPermissionName()));

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}
}
