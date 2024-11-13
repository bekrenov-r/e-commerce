package com.bekrenovr.ecommerce.customers.customer;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import static com.bekrenovr.ecommerce.customers.exception.CustomersApplicationExceptionReason.USER_NOT_FOUND_BY_EMAIL;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);

    default Customer findByEmailOrThrowDefault(String email){
        return findByEmail(email).orElseThrow(() -> new EcommerceApplicationException(USER_NOT_FOUND_BY_EMAIL, email));
    }
}
