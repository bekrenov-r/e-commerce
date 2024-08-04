package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static com.bekrenovr.ecommerce.users.exception.UsersApplicationExceptionReason.EMAIL_ALREADY_EXISTS;

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
    public void createCustomer(CustomerRequest request, boolean isRegistered){
        if(customerRepository.existsByEmail(request.getEmail()))
            throw new EcommerceApplicationException(EMAIL_ALREADY_EXISTS, request.getEmail());
        Customer customer = customerMapper.requestToEntity(request);
        customer.setRegistered(isRegistered);
        customer.setCreatedAt(LocalDateTime.now());
        customerRepository.save(customer);
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
