package com.bekrenovr.ecommerce.customers.customer;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.customers.customer.dto.CustomerMapper;
import com.bekrenovr.ecommerce.customers.customer.dto.CustomerRequest;
import com.bekrenovr.ecommerce.customers.customer.dto.CustomerResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.bekrenovr.ecommerce.customers.exception.CustomersApplicationExceptionReason.EMAIL_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponse getByEmail(String email) {
        return customerMapper.customerToResponse(customerRepository.findByEmailOrThrowDefault(email));
    }

    @Transactional
    public HttpStatus create(CustomerRequest request) {
        return customerRepository.findByEmail(request.getEmail())
                .map(customer -> {
                    if (!customer.isRegistered()) {
                        customer.setRegistered(true);
                        customerRepository.save(customer);
                        return HttpStatus.OK;
                    } else {
                        throw new EcommerceApplicationException(EMAIL_ALREADY_EXISTS, request.getEmail());
                    }
                }).orElseGet(() -> {
                    Customer customer = customerMapper.requestToEntity(request);
                    customer.setRegistered(request.isRegistered());
                    customer.setCreatedAt(LocalDateTime.now());
                    customerRepository.save(customer);
                    return HttpStatus.CREATED;
                });
    }

    public CustomerResponse update(CustomerRequest request){
        Customer customer = customerRepository.findByEmailOrThrowDefault(request.getEmail());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customerRepository.save(customer);
        return customerMapper.customerToResponse(customer);
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
