package com.suvasish.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import com.suvasish.Dto.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByEmail(String email);
	@Query("select u from User u where u.full_name like %:query% or u.email like %:query%")
	public List<User> searchUser(@Param("query") String  query);
}
