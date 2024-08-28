package com.suvasish.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suvasish.Dto.Message;
import com.suvasish.Dto.User;
import com.suvasish.Exception.ChatException;
import com.suvasish.Response.ApiResponse;
import com.suvasish.Service.MessageService;
import com.suvasish.Service.UserService;
import com.suvasish.request.SendMessageRequest;

@RestController
@RequestMapping("/api/message")
public class MessageController {
	private MessageService messageService;
	private UserService userService;
	public MessageController(MessageService messageService,UserService userService) {
		this.messageService=messageService;
		this.userService=userService;
	}
	@PostMapping("/create")
	public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest req,@RequestHeader("Authorization") String jwt) throws ChatException, Exception{
		User user=userService.findUserProfile(jwt);
		req.setUserId(user.getId());
		Message message=messageService.sendMessage(req);
		return new ResponseEntity<Message>(message,HttpStatus.OK);
	}
	@GetMapping("/chat/{id}")
	public ResponseEntity<List<Message>> getChatsMessagesHandler(@PathVariable Integer chatId,@RequestHeader("Authorization") String jwt) throws ChatException, Exception{
		User user=userService.findUserProfile(jwt);
		List<Message> message=messageService.getChatsMessages(chatId,user);
		return new ResponseEntity<List<Message>>(message,HttpStatus.OK);
	}
	@DeleteMapping("/{messageId}")
	public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer chatId,@RequestHeader("Authorization") String jwt) throws ChatException, Exception{
		User user=userService.findUserProfile(jwt);
		messageService.deleteMessage(chatId, user);
		ApiResponse res=new ApiResponse();
		res.setMessage("Message Deleted Success");
		res.setStatus(true);
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
}
