package com.suvasish.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.suvasish.Config.TokenProvider;
import com.suvasish.Dto.User;
import com.suvasish.Exception.UserException;
import com.suvasish.Repository.UserRepository;
import com.suvasish.Response.AuthResponse;
import com.suvasish.Service.CustomUserService;
import com.suvasish.request.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private TokenProvider tokenProvider;
	private CustomUserService customUserService;
	@Autowired
	public AuthController(UserRepository userRepository,CustomUserService customUserService
			,PasswordEncoder passwordEncoder) {
		// TODO Auto-generated constructor stub
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.customUserService=customUserService;
	}
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
		String email=user.getEmail();
		String full_name=user.getFull_name();
		String password=user.getPassword();
		//now we will have to check wether the email is associated with another account or not?
		User isUser=userRepository.findByEmail(email);
		if(isUser!=null) {
			throw new UserException("Email is used with another account " + email);
		}
		User createdUser=new User();
		createdUser.setEmail(email);
		createdUser.setFull_name(full_name);
		createdUser.setPassword(passwordEncoder.encode(password));
		userRepository.save(createdUser);
		Authentication authentication=new UsernamePasswordAuthenticationToken(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt=tokenProvider.generateToken(authentication);
		AuthResponse res=new AuthResponse();
		res.setJwt(jwt);
		res.setAuth(true);
		return new ResponseEntity<AuthResponse>(res,HttpStatus.ACCEPTED);
	}
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req){
		String email=req.getEmail();
		String password=req.getPassword();
		Authentication authentication=authenticate(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt=tokenProvider.generateToken(authentication);
		AuthResponse res=new AuthResponse();
		res.setAuth(true);
		res.setJwt(jwt);
		return new ResponseEntity<AuthResponse>(res,HttpStatus.ACCEPTED);
	}
	public Authentication authenticate(String username,String password) {
		UserDetails userDetails=customUserService.loadUserByUsername(username);
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid Username");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password or username");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
	}
}
