package com.bekrenovr.ecommerce.users.dto;

import com.bekrenovr.ecommerce.users.entity.Customer;

public class CustomerToDtoMapper {

    public static CustomerDTO customerToDto(Customer customer){
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName()
        );
    }

}
