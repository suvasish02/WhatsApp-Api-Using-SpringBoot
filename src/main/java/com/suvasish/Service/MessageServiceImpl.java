package com.suvasish.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.suvasish.Dto.Chat;
import com.suvasish.Dto.Message;
import com.suvasish.Dto.User;
import com.suvasish.Exception.ChatException;
import com.suvasish.Exception.MessageException;
import com.suvasish.Exception.UserException;
import com.suvasish.Repository.MessageRepository;
import com.suvasish.request.SendMessageRequest;
@Service
public class MessageServiceImpl implements MessageService{
	private MessageRepository messageRepository;
	private UserService userService;
	private ChatService chatService;
	public MessageServiceImpl(MessageRepository messageRepository,UserService userService,ChatService chatService) {
		this.messageRepository=messageRepository;
		this.userService=userService;
		this.chatService=chatService;
	}
	@Override
	public Message sendMessage(SendMessageRequest req) throws UserException, ChatException,Exception {
		User user=userService.findUserById(req.getUserId());
		Chat chat=chatService.findChatById(req.getChatId());
		Message message=new Message();
		message.setChat(chat);
		message.setUser(user);
		message.setContent(req.getContent());
		message.setTimestamp(LocalDateTime.now());
		return message;
	}

	@Override
	public List<Message> getChatsMessages(Integer chatId,User reqUser) throws ChatException,UserException {
		Chat chat=chatService.findChatById(chatId);
		if(!chat.getUsers().contains(reqUser)) {
			throw new UserException("You are not Related to this Chat "  + chat.getId());
		}
		List<Message> messages=messageRepository.findByChatId(chat.getId());
		return messages;
	}

	@Override
	public Message findMessageById(Integer messageId) throws MessageException {
		Optional<Message> opt=messageRepository.findById(messageId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new MessageException("Message Not Found With id " + messageId);
	}

	@Override
	public void deleteMessage(Integer messageId,User reqUser) throws MessageException,UserException {
		Message message=findMessageById(messageId);
		if(message.getUser().getId().equals(reqUser.getId())) {
			messageRepository.deleteById(messageId);
		}
		throw new UserException("You can't delete another User's message " + reqUser.getFull_name());
	}

}
