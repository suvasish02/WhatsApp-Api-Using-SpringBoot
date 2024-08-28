package com.suvasish.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suvasish.Dto.Chat;
import com.suvasish.Dto.User;
import com.suvasish.Exception.ChatException;
import com.suvasish.Exception.UserException;
import com.suvasish.Repository.ChatRepository;
import com.suvasish.request.GroupChatRequest;

@Service
public class ChatServiceImpl implements ChatService{
	private ChatRepository chatRepository;
	private UserService userService;
	@Autowired
	public ChatServiceImpl(ChatRepository chatRepository,UserService userService) {
		this.chatRepository=chatRepository;
		this.userService=userService;
	}
	@Override
	public Chat createChat(User reqUser, Integer userId2) throws Exception {
		User user=userService.findUserById(userId2);
		Chat isChatExist=chatRepository.findSingleChatByUserIds(user, reqUser);
		if(isChatExist!=null) {
			return isChatExist;
		}
		Chat c=new Chat();
		c.setCreatedBy(reqUser);
		c.getUsers().add(user);
		c.getUsers().add(reqUser);
		c.setGroup(false);
		return c;
	}

	@Override
	public Chat findChatById(Integer chatId) throws ChatException {
		Optional<Chat> chat=chatRepository.findById(chatId);
		if(chat.isPresent()) {
			return chat.get();
		}
		throw new ChatException("Chat Not found with id " + chatId);
	}

	@Override
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException,Exception {
		User user=userService.findUserById(userId);
		List<Chat> chats=chatRepository.findChatByUserId(user.getId());
		return chats;
	}

	@Override
	public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException,Exception {
		Chat group=new Chat();
		group.setGroup(true);
		group.setChat_image(req.getChat_image());
		group.setChat_name(req.getChat_name());
		group.setCreatedBy(reqUser);
		group.getAdmins().add(reqUser);
		for(Integer ids:req.getUserIds()) {
			User user=userService.findUserById(ids);
			group.getUsers().add(user);
		}
		return group;
	}

	@Override
	public Chat addUserToGroup(Integer userId, Integer chatId,User reqUser) throws UserException, ChatException,Exception {
		Optional<Chat> chat=chatRepository.findById(chatId);
		User user=userService.findUserById(userId);
		if(chat.isPresent()) {
			Chat c=chat.get();
			if(c.getAdmins().contains(reqUser)) {
				chat.get().getUsers().add(user);
				return chatRepository.save(c);
			}
			else {
				throw new UserException("Invalid User To Add Another member to group");
			}
		}
		throw new ChatException("Invalid Chat Exception");
	}

	@Override
	public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
		Optional<Chat> opt=chatRepository.findById(chatId);
		if(opt.isPresent()) {
			Chat chat=opt.get();
			if(chat.getUsers().contains(reqUser)) {
				chat.setChat_name(groupName);
				return chatRepository.save(chat);
			}
			throw new UserException("Invalid user");
		}
		throw new ChatException("Invalid Chat Exception");
	}

	@Override
	public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException,Exception {
		Optional<Chat> opt=chatRepository.findById(chatId);
		User user=userService.findUserById(userId);
		if(opt.isPresent()) {
			Chat chat=opt.get();
			if(chat.getAdmins().contains(reqUser)) {
				chat.getUsers().remove(user);
				return chatRepository.save(chat);
			}
			else if(chat.getUsers().contains(reqUser)) {
				if(user.getId().equals(reqUser.getId())) {
					chat.getUsers().remove(user);
					return chatRepository.save(chat);
				}
			}
			throw new UserException("You cannot remove another user");
		}
		throw new ChatException("Chat Not found with id " + chatId);
	}

	@Override
	public void deleteChat(Integer chatId, Integer userId) throws UserException, ChatException {
		Optional<Chat> opt=chatRepository.findById(chatId);
		if(opt.isPresent()) {
			Chat chat=opt.get();
			chatRepository.deleteById(chat.getId());
		}
	}

}
