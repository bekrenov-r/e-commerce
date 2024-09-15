package com.bekrenovr.ecommerce.customers.dto.mapper;

import com.bekrenovr.ecommerce.customers.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.customers.dto.response.CustomerResponse;
import com.bekrenovr.ecommerce.customers.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse customerToResponse(Customer customer);
    Customer requestToEntity(CustomerRequest customerRequest);
}
