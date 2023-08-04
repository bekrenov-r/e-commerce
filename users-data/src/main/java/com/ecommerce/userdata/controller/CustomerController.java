package com.ecommerce.userdata.controller;

import com.ecommerce.userdata.entity.Customer;
import com.ecommerce.userdata.service.CustomerService;
import com.ecommerce.userdata.dto.CustomerDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(){
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable("id") int id){
        return customerService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer){
        return customerService.create(customer);
    }

    @PutMapping
    public ResponseEntity<Customer> update(@Valid @RequestBody Customer customer){
        return customerService.update(customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id){
        return customerService.delete(id);
    }








    /*@GetMapping("/{customerId}/createOrder/{itemId}")
    public OrderResponse createOrder(@PathVariable("customerId") int customerId,
                            @PathVariable("itemId") int itemId,
                            @RequestParam("quantity") int quantity){
        OrderRequest request = new OrderRequest(customerId, itemId, quantity);
        ResponseEntity<OrderResponse> responseEntity = new RestTemplate()
                .postForEntity("http://localhost:8000/orders", request, OrderResponse.class, (Object) null);

        return responseEntity.getBody();
    }*/

    /*@PostMapping("/{customerId}/createOrder/{itemId}")
    public OrderResponse createOrder(@PathVariable("customerId") int customerId,
                                     @PathVariable("itemId") int itemId,
                                     @RequestParam("quantity") int quantity){
        OrderRequest request = new OrderRequest(customerId, itemId, quantity);
        return orderProxy.createOrder(request);
    }*/

















}
