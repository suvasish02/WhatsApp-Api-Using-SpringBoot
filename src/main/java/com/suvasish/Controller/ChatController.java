package com.suvasish.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.suvasish.request.*;
import com.suvasish.Dto.Chat;
import com.suvasish.Dto.User;
import com.suvasish.Exception.ChatException;
import com.suvasish.Exception.UserException;
import com.suvasish.Response.ApiResponse;
import com.suvasish.Service.ChatService;
import com.suvasish.Service.UserService;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
	private ChatService chatService;
	private UserService userService;
	public ChatController(ChatService chatService,UserService userService) {
		this.chatService=chatService;
		this.userService=userService;
	}
	@PostMapping("/single")
	public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest,
			@RequestHeader("Authorization") String jwt)throws UserException,Exception{
		User reqUser=userService.findUserProfile(jwt);
		Chat chat=chatService.createChat(reqUser, singleChatRequest.getUserId());
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	@PostMapping("/group")
	public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest req,
			@RequestHeader("Authorization") String jwt)throws UserException,Exception{
		User reqUser=userService.findUserProfile(jwt);
		Chat chat=chatService.createGroup(req,reqUser);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	@GetMapping("/{chatId}")
	public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId,
			@RequestHeader("Authorization") String jwt)throws UserException,Exception{
		Chat chat=chatService.findChatById(chatId);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	@GetMapping("/user")
	public ResponseEntity<List<Chat>> findAllChatByUserIdHandler(
			@RequestHeader("Authorization") String jwt)throws UserException,Exception{
		User reqUser=userService.findUserProfile(jwt);
		List<Chat> chat=chatService.findAllChatByUserId(reqUser.getId());
		return new ResponseEntity<List<Chat>>(chat,HttpStatus.OK);
	}
	@PutMapping("/{chatId}/add/{userId}")
	public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable Integer chatId,@PathVariable Integer userId,
			@RequestHeader("Authorization") String jwt)throws UserException,Exception{
		User reqUser=userService.findUserProfile(jwt);
		Chat chat=chatService.addUserToGroup(userId,chatId,reqUser);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	@PutMapping("/{chatId}/remove/{userId}")
	public ResponseEntity<Chat> removeUserFromGroupHandler(@PathVariable Integer chatId,@PathVariable Integer userId,
			@RequestHeader("Authorization") String jwt)throws UserException,Exception{
		User reqUser=userService.findUserProfile(jwt);
		Chat chat=chatService.removeFromGroup(userId,chatId,reqUser);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	@PutMapping("/delete/{chatId}")
	public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId,
			@RequestHeader("Authorization") String jwt)throws UserException,Exception{
		User reqUser=userService.findUserProfile(jwt);
		chatService.deleteChat(chatId,reqUser.getId());
		ApiResponse res=new ApiResponse();
		res.setMessage("Chat is deleted success");
		res.setStatus(true);
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
	
}
