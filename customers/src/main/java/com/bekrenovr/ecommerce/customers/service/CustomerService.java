package com.bekrenovr.ecommerce.customers.service;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.customers.dto.mapper.CustomerMapper;
import com.bekrenovr.ecommerce.customers.dto.request.CustomerRequest;
import com.bekrenovr.ecommerce.customers.dto.response.CustomerResponse;
import com.bekrenovr.ecommerce.customers.model.Customer;
import com.bekrenovr.ecommerce.customers.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.bekrenovr.ecommerce.customers.exception.UsersApplicationExceptionReason.EMAIL_ALREADY_EXISTS;
import static com.bekrenovr.ecommerce.customers.exception.UsersApplicationExceptionReason.USER_NOT_FOUND_BY_EMAIL;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public void createCustomer(CustomerRequest request) {
        customerRepository.findByEmail(request.getEmail())
                .ifPresentOrElse(customer -> {
                    if (!customer.isRegistered()) {
                        customer.setRegistered(true);
                        customerRepository.save(customer);
                    } else {
                        throw new EcommerceApplicationException(EMAIL_ALREADY_EXISTS, request.getEmail());
                    }
                }, () -> {
                    Customer customer = customerMapper.requestToEntity(request);
                    customer.setRegistered(request.isRegistered());
                    customer.setCreatedAt(LocalDateTime.now());
                    customerRepository.save(customer);
                });
    }

    public void updateCustomer(CustomerRequest customer){
        customerRepository.findByEmail(customer.getEmail())
                .ifPresentOrElse(c -> {
                    c.setFirstName(customer.getFirstName());
                    c.setLastName(customer.getLastName());
                    customerRepository.save(c);
                }, () -> {
                    throw new EcommerceApplicationException(USER_NOT_FOUND_BY_EMAIL);
                });
    }

    public ResponseEntity<Void> delete(UUID id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No customer found for id: " + id));
        customerRepository.delete(customer);
        return ResponseEntity
                .ok()
                .build();
    }

    public CustomerResponse getCustomerByEmail(String email) {
        return customerMapper.customerToResponse(customerRepository.findByEmailOrThrowDefault(email));
    }
}
