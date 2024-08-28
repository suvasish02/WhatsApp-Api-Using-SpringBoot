package com.suvasish.Service;

import java.util.List;

import com.suvasish.Dto.User;
import com.suvasish.Exception.UserException;
import com.suvasish.request.UpdateUserRequest;

public interface UserService {
	public User findUserById(Integer id) throws Exception;
	public User findUserProfile(String jwt) throws UserException;
	public User updateUser(Integer userId,UpdateUserRequest req) throws UserException;
	public List<User> searchUser(String q);
}
