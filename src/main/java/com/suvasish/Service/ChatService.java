package com.suvasish.Service;

import java.util.List;

import com.suvasish.Dto.Chat;
import com.suvasish.Dto.User;
import com.suvasish.Exception.ChatException;
import com.suvasish.Exception.UserException;
import com.suvasish.request.GroupChatRequest;

public interface ChatService {
	//the below will allow us to create an user chat for populating messages.
	public Chat createChat(User reqUser,Integer userId2) throws UserException,Exception;
	//the below method will help an user search an user chat by the help of that userId.
	public Chat findChatById(Integer chatId) throws ChatException;
	//this method will help us to find all the chat by the help of userId.
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException,Exception;
	//this method will help to create a group
	public Chat createGroup(GroupChatRequest req,User reqUser) throws UserException,Exception;
	//the below method will help us to add a particular user to a group.
	public Chat addUserToGroup(Integer userId,Integer chatId,User reqUser) throws UserException,ChatException,Exception;
	public Chat renameGroup(Integer chatId,String groupName,User reqUser) throws
	ChatException,UserException,Exception;
	public Chat removeFromGroup(Integer chatId,Integer userId,User reqUser)throws UserException,ChatException,Exception;
	public void deleteChat(Integer chatId,Integer userId) throws UserException,ChatException;
}
