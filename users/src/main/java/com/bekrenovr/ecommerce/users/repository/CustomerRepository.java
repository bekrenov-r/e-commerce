package com.bekrenovr.ecommerce.users.repository;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.users.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import static com.bekrenovr.ecommerce.users.exception.UsersApplicationExceptionReason.USER_NOT_FOUND_BY_EMAIL;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);

    default Customer findByEmailOrThrowDefault(String email){
        return findByEmail(email).orElseThrow(() -> new EcommerceApplicationException(USER_NOT_FOUND_BY_EMAIL, email));
    }
}
