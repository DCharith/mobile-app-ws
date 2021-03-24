package com.charith.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.charith.app.ws.exception.UserServiceException;
import com.charith.app.ws.io.entity.UserEntity;
import com.charith.app.ws.io.repository.UserRepository;
import com.charith.app.ws.service.UserService;
import com.charith.app.ws.shared.Utils;
import com.charith.app.ws.shared.dto.AddressDto;
import com.charith.app.ws.shared.dto.UserDto;
import com.charith.app.ws.ui.model.response.ErrorMessages;


@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Utils utils;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		if(this.userRepository.findByEmail(user.getEmail()) != null)
			throw new RuntimeException("Email address alredy exists!");
		
		for (int i=0; i<user.getAddresses().size(); i++) {
			AddressDto addressDto = user.getAddresses().get(i);
			addressDto.setUserDetails(user);
			addressDto.setAddressId(this.utils.generateAddressId(30));
			user.getAddresses().set(i, addressDto);
		}
		
//		UserEntity userEntity = new UserEntity();
//		BeanUtils.copyProperties(user, userEntity);
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);
		
		String userId = this.utils.generateUserId(30);
		userEntity.setUserId(userId);
		userEntity.setEncryptedPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		
		
		
		UserEntity storedUser = this.userRepository.save(userEntity);
		UserDto returnValue = new UserDto();
//		ModelMapper modelMapper2 = new ModelMapper();
		modelMapper.map(storedUser, returnValue);
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = this.userRepository.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}
	
	@Override
	public UserDto getUser(String email) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = new UserEntity();
		userEntity = this.userRepository.findByEmail(email);
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		// TODO Auto-generated method stub
		UserDto returnValue = new UserDto();
		UserEntity userEntity = new UserEntity();
		userEntity = this.userRepository.findByUserId(userId);
//		BeanUtils.copyProperties(userEntity, returnValue);
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(userEntity, returnValue);
		return returnValue;
	}

	
	@Override
	public UserDto updateUser(UserDto user) {
		UserDto returnValue = new UserDto();
		UserEntity userEntity = this.userRepository.findByUserId(user.getUserId());
		if(userEntity == null) {
			throw new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage());
		}
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		UserEntity updatedUser = this.userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = this.userRepository.findByUserId(userId);
		if (userEntity == null) {
			throw new UserServiceException(ErrorMessages.USER_NOT_FOUND.getErrorMessage());
		}
		this.userRepository.delete(userEntity);
		
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnValue = new ArrayList<>();
		Pageable pageableRequest = PageRequest.of(page, limit);
		Page<UserEntity> usersPage = this.userRepository.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		for (UserEntity user: users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user, userDto);
			returnValue.add(userDto);
		}
		return returnValue;
	}

}
