package com.charith.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.charith.app.ws.exception.UserServiceException;
import com.charith.app.ws.service.impl.AddressServiceImpl;
import com.charith.app.ws.service.impl.UserServiceImpl;
import com.charith.app.ws.shared.dto.AddressDto;
import com.charith.app.ws.shared.dto.UserDto;
import com.charith.app.ws.ui.model.request.UserDetailsRequestModel;
import com.charith.app.ws.ui.model.response.AddressResponse;
import com.charith.app.ws.ui.model.response.ErrorMessages;
import com.charith.app.ws.ui.model.response.OperationStatusModel;
import com.charith.app.ws.ui.model.response.RequestOperationName;
import com.charith.app.ws.ui.model.response.RequestOperationStatus;
import com.charith.app.ws.ui.model.response.UserResponse;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private AddressServiceImpl addressService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponse getUsers(@PathVariable String id) {
		UserResponse returnValue = new UserResponse();
		UserDto userDto = userService.getUserByUserId(id);
//		BeanUtils.copyProperties(userDto, returnValue);
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(userDto, returnValue);
		return returnValue;
	}

	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, 
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserResponse createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		if (userDetails.getFirstName().isEmpty()) {
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		}

		UserResponse returnValue = new UserResponse();	
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
//		Here using ModelMapper because the UserDetailsRequest has a reference to AddressRequest class
//		BeanUtils.copyProperties(userDetails, userDto);
		UserDto createdUser = this.userService.createUser(userDto);
		modelMapper.map(createdUser, returnValue);
		//BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;
	}

	@PutMapping(path = "/{id}")
	public UserResponse updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel user) {
		UserDto userDto = new UserDto();
		UserResponse returnValue = new UserResponse();
		BeanUtils.copyProperties(user, userDto);
		userDto.setUserId(id);
		UserDto updatedUser = this.userService.updateUser(userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;
	}

	@DeleteMapping(path = "/{id}")
	public OperationStatusModel deleteUser(@PathVariable String id) {
		this.userService.deleteUser(id);
		OperationStatusModel returnValue = new OperationStatusModel(RequestOperationName.DELETE.name(), RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	// URL eg: http://localhost:8080/users?page=1&limit=2 
	@GetMapping
	public List<UserResponse> getUsers(@RequestParam(value = "page", defaultValue = "0") int page, 
			@RequestParam(value = "limit", defaultValue = "10") int limit){
		
		List<UserResponse> usersList = new ArrayList<UserResponse>();
		List<UserDto> userDtoList = this.userService.getUsers(page, limit);
		
		
		for (UserDto userDto: userDtoList) {
			UserResponse user = new UserResponse();
			BeanUtils.copyProperties(userDto, user);
			usersList.add(user);
		}
		return usersList;
	}
	
	@GetMapping(path = "/{id}/addresses")
	public List<AddressResponse> getUserAddresses(@PathVariable String id){
		List<AddressResponse> returnValue = new ArrayList<>();
		List<AddressDto> addresses = this.addressService.getUserAddresses(id);
		
		if(addresses != null && !addresses.isEmpty()) {
			java.lang.reflect.Type listType = new TypeToken<List<AddressResponse>>() {}.getType();
			returnValue = new ModelMapper().map(addresses, listType);
		}
		return returnValue;
		
	}
	
	@GetMapping(path = "/{userId}/addresses/{addressId}")
	public AddressResponse getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
		AddressResponse returnValue = new AddressResponse();
		AddressDto addressDto = this.addressService.getAddressByAddressId(addressId);
//		BeanUtils.copyProperties(addressDto, returnValue);
		returnValue = new ModelMapper().map(addressDto, AddressResponse.class);
		// http://localhost:8080/mobile-app-ws/users/{userId}
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(userId)
				.withRel("user");
		
		// http://localhost:8080/mobile-app-ws/users/{userId}/addresses
		Link addressesLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(userId)
				.slash("addresses")
				.withRel("addresses");
		
		// http://localhost:8080/mobile-app-ws/users/{userId}/addresses/{addressId}
		Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class)
				.slash(userId)
				.slash("addresses")
				.slash(addressId)
				.withSelfRel();
		
		returnValue.add(userLink);
		returnValue.add(addressesLink);
		returnValue.add(selfLink);
		return returnValue;
	}
	

}
