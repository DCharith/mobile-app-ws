package com.charith.app.ws.service;

import java.util.List;

import com.charith.app.ws.shared.dto.AddressDto;

public interface addressService {
	List<AddressDto> getUserAddresses(String userId);
}
