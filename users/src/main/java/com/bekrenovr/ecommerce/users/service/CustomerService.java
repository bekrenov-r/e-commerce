package com.bekrenovr.ecommerce.users.service;

import com.bekrenovr.ecommerce.common.ResponseSource;
import com.bekrenovr.ecommerce.users.dto.CustomerDTO;
import com.bekrenovr.ecommerce.users.dto.CustomerToDtoMapper;
import com.bekrenovr.ecommerce.users.entity.Customer;
import com.bekrenovr.ecommerce.users.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ResponseSource responseSource;

    public CustomerService(CustomerRepository customerRepository, ResponseSource responseSource) {
        this.customerRepository = customerRepository;
        this.responseSource = responseSource;
    }

    public ResponseEntity<List<CustomerDTO>> findAll(){
        List<CustomerDTO> customers =
                customerRepository.findAll().stream().map(CustomerToDtoMapper::customerToDto).toList();
        return ResponseEntity
                .ok()
                .header("Response Source", responseSource.toString())
                .body(customers);
    }

    public ResponseEntity<CustomerDTO> findById(int id){
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No customer found for id: " + id));

        CustomerDTO customerDTO = CustomerToDtoMapper.customerToDto(customer);
        return ResponseEntity
                .ok()
                .header("Response Source", responseSource.toString())
                .body(customerDTO);
    }

    public ResponseEntity<Customer> create(Customer customer){
        customer.setId(0);
        customer.getAddress().setId(0);
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity
                .created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(savedCustomer.getId())
                                .toUri()
                )
                .header("Response Source", responseSource.toString())
                .body(savedCustomer);
    }

    public ResponseEntity<Customer> update(Customer customer){
        if(customerRepository.existsById(customer.getId())){
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity
                    .ok()
                    .header("Response Source", responseSource.toString())
                    .body(updatedCustomer);

        } else {
            throw new EntityNotFoundException("No customer found for id: " + customer.getId());
        }
    }

    public ResponseEntity<Void> delete(int id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No customer found for id: " + id));
        customerRepository.delete(customer);
        return ResponseEntity
                .ok()
                .header("Response Source", responseSource.toString())
                .build();
    }
}
