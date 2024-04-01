package com.bekrenovr.ecommerce.users.dto.mapper;

import com.bekrenovr.ecommerce.common.entity.Address;
import com.bekrenovr.ecommerce.users.dto.request.AddressRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address requestToEntity(AddressRequest request);
}
