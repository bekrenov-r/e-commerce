package com.bekrenovr.ecommerce.users.dto.mapper;

import com.bekrenovr.ecommerce.users.dto.CustomerDTO;
import com.bekrenovr.ecommerce.users.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.users.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDTO customerToDto(Customer customer);
    Customer requestToEntity(CustomerRequest customerRequest);
}
