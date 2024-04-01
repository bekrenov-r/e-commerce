package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.users.dto.CustomerDTO;
import com.bekrenovr.ecommerce.users.dto.mapper.CustomerMapper;
import com.bekrenovr.ecommerce.users.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.users.entity.Customer;
import com.bekrenovr.ecommerce.users.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public ResponseEntity<CustomerDTO> getCustomerById(UUID id){
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No customer found for id: " + id));
        return ResponseEntity.ok(customerMapper.customerToDto(customer));
    }

    @Transactional
    public Customer createCustomer(CustomerRequest request, boolean withUser){
        Customer customer = customerMapper.requestToEntity(request);
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    public ResponseEntity<Customer> update(Customer customer){
        if(customerRepository.existsById(customer.getId())){
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity
                    .ok()
                    .body(updatedCustomer);

        } else {
            throw new EntityNotFoundException("No customer found for id: " + customer.getId());
        }
    }

    public ResponseEntity<Void> delete(UUID id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No customer found for id: " + id));
        customerRepository.delete(customer);
        return ResponseEntity
                .ok()
                .build();
    }
}
