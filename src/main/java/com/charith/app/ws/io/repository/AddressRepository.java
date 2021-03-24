package com.charith.app.ws.io.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.charith.app.ws.io.entity.AddressEntity;
import com.charith.app.ws.io.entity.UserEntity;

@Repository
public interface AddressRepository extends PagingAndSortingRepository<AddressEntity, Long> {

	List<AddressEntity> findAllByUserDetails(UserEntity userDetails);
	AddressEntity findByAddressId(String addressId);
}
