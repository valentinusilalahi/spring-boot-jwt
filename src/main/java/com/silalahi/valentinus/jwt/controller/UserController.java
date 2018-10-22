package com.silalahi.valentinus.jwt.controller;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.silalahi.valentinus.jwt.dto.UserDataDto;
import com.silalahi.valentinus.jwt.dto.UserResponseDto;
import com.silalahi.valentinus.jwt.model.User;
import com.silalahi.valentinus.jwt.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users")
@Api(tags="users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@PostMapping("/signin")
	@ApiOperation(value="${UserController.signin}")
	@ApiResponses(value= {
			@ApiResponse(code=400, message="Something went wrong"),
			@ApiResponse(code=422, message="Invalid username/password supplied")
	})
	public String login(
			@ApiParam("username") @RequestParam String username,
			@ApiParam("password") @RequestParam String password
			) {
		return userService.signin(username, password);
	}
	
	@PostMapping("/signup")
	@ApiOperation(value="${UserController.signup}")
	@ApiResponses(value= {
			@ApiResponse(code=400,message="Something went wrong"),
			@ApiResponse(code=403,message="Access Denied"),
			@ApiResponse(code=422,message="Username is already in use"),
			@ApiResponse(code=500,message="Expired or Invalid JWT token")
	})
	public String signup(@ApiParam("Signup user") @RequestBody UserDataDto userDto) {
		return userService.signup(modelMapper.map(userDto, User.class));
	}
	
	@DeleteMapping(value="/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value="${UserController.delete}")
	@ApiResponses(value= {
			@ApiResponse(code=400,message="Something went wrong"),
			@ApiResponse(code=403,message="Access Denied"),
			@ApiResponse(code=422,message="Username is already in use"),
			@ApiResponse(code=500,message="Expired or Invalid JWT token")
	})
	public String delete(@ApiParam("Username") @PathVariable String username) {
		userService.delete(username);
		return username;
	}
	
	@GetMapping(value = "/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "${UserController.search}", response = UserResponseDto.class)
	@ApiResponses(value = {
	      @ApiResponse(code = 400, message = "Something went wrong"),
	      @ApiResponse(code = 403, message = "Access denied"),
	      @ApiResponse(code = 404, message = "The user doesn't exist"),
	      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
	public UserResponseDto search(@ApiParam("Username") @PathVariable String username) {
	    return modelMapper.map(userService.search(username), UserResponseDto.class);
	  }

	  @GetMapping(value = "/me")
	  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	  @ApiOperation(value = "${UserController.me}", response = UserResponseDto.class)
	  @ApiResponses(value = {//
	      @ApiResponse(code = 400, message = "Something went wrong"), //
	      @ApiResponse(code = 403, message = "Access denied"), //
	      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
	  public UserResponseDto whoami(HttpServletRequest request) {
	    return modelMapper.map(userService.whoami(request), UserResponseDto.class);
	  }

	
}
