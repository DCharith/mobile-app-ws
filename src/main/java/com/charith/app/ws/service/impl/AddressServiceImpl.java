package com.charith.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charith.app.ws.io.entity.AddressEntity;
import com.charith.app.ws.io.entity.UserEntity;
import com.charith.app.ws.io.repository.AddressRepository;
import com.charith.app.ws.io.repository.UserRepository;
import com.charith.app.ws.service.addressService;
import com.charith.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements addressService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Override
	public List<AddressDto> getUserAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = this.userRepository.findByUserId(userId);
		if (userEntity == null)
			return returnValue;
		
		Iterable<AddressEntity> addresses = this.addressRepository.findAllByUserDetails(userEntity);
		for(AddressEntity address: addresses) {
			AddressDto addressDto = modelMapper.map(address, AddressDto.class);
			returnValue.add(addressDto);
		}
		
		return returnValue;
	}

	public AddressDto getAddressByAddressId(String addressId) {
		ModelMapper modelMapper = new ModelMapper();
		AddressEntity addressEntity = this.addressRepository.findByAddressId(addressId);
		AddressDto returnValue = modelMapper.map(addressEntity, AddressDto.class);
		return returnValue;
	}

}
