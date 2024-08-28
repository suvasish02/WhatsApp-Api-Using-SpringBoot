package com.suvasish.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.suvasish.Dto.User;
import com.suvasish.Repository.UserRepository;
@Service
//this class is implemented to remove the by default password of spring security.
public class CustomUserService implements UserDetailsService{
	private UserRepository userRepository;
	@Autowired
	public CustomUserService(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByEmail(username);
		if(user==null) {
			throw new UsernameNotFoundException("User Not Found with Username");
		}
		List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
		return new  org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),
				authorities);
	}

}
