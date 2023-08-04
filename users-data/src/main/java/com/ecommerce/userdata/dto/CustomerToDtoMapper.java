package com.ecommerce.userdata.dto;

import com.ecommerce.userdata.entity.Customer;

public class CustomerToDtoMapper {

    public static CustomerDTO customerToDto(Customer customer){
        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName()
        );
    }

}
