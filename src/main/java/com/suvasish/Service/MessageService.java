package com.suvasish.Service;

import java.util.List;

import com.suvasish.Dto.Message;
import com.suvasish.Dto.User;
import com.suvasish.Exception.ChatException;
import com.suvasish.Exception.MessageException;
import com.suvasish.Exception.UserException;
import com.suvasish.request.SendMessageRequest;

public interface MessageService {
	public Message sendMessage(SendMessageRequest req) throws UserException,ChatException,Exception;
	public List<Message> getChatsMessages(Integer chatId,User reqUser) throws ChatException,Exception;
	public Message findMessageById(Integer messageId)throws MessageException,Exception;
	public void deleteMessage(Integer messageId,User reqUser)throws MessageException,UserException,Exception;
}
