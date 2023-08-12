package sn.kiwi.apiwebsms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.kiwi.apiwebsms.models.UserInfo;

import java.util.Arrays;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserInfoService userInfoDAO;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		UserInfo userInfo = userInfoDAO.getUserInfoByUserName(userName);
		//if(userInfo!=null) {
			GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole());
			return new User(userInfo.getUserName(), userInfo.getPassword(), Arrays.asList(authority));
		//}
	}

	public UserInfo loadUser(String userName) throws UsernameNotFoundException {
		UserInfo userInfo = userInfoDAO.getUserInfoByUserName(userName);
		return userInfo;
	}
}
