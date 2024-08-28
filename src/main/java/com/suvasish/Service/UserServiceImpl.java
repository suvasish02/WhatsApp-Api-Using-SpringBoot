package com.suvasish.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.suvasish.Config.TokenProvider;
import com.suvasish.Dto.User;
import com.suvasish.Exception.UserException;
import com.suvasish.Repository.UserRepository;
import com.suvasish.request.UpdateUserRequest;
@Service
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	private TokenProvider tokenProvider;
	@Autowired
	public UserServiceImpl(UserRepository userRepository,TokenProvider tokenProvider) {
		this.userRepository=userRepository;
		this.tokenProvider=tokenProvider;
	}
	@Override
	public User findUserById(Integer id) throws UserException {
		Optional<User> opt=userRepository.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("No user Found!!");
	}

	@Override
	public User findUserProfile(String jwt) throws UserException{
		String email=tokenProvider.getEmailFromToken(jwt);
		if(email==null) {
			throw new BadCredentialsException("Recieveed Email is Invalid");
		}
		User user=userRepository.findByEmail(email);
		if(user==null) {
			throw new UserException("User Not Found eith the email");
		}
		return user;
	}

	@Override
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
		User user=findUserById(userId);
		if(req.getFull_name()!=null) {
			user.setFull_name(req.getFull_name());
		}
		if(req.getProfile_picture()!=null) {
			user.setProfile_picture(req.getProfile_picture());
		}
		return userRepository.save(user);
	}

	@Override
	public List<User> searchUser(String q) {
		List<User> users=userRepository.searchUser(q);
		return users;
	}

}
