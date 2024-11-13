package com.bekrenovr.ecommerce.customers.customer.dto;

import com.bekrenovr.ecommerce.customers.customer.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponse customerToResponse(Customer customer);
    Customer requestToEntity(CustomerRequest customerRequest);
}
