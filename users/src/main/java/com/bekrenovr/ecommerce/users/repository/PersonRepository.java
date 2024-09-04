package com.bekrenovr.ecommerce.users.repository;

import com.bekrenovr.ecommerce.common.exception.EcommerceApplicationException;
import com.bekrenovr.ecommerce.users.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.bekrenovr.ecommerce.users.exception.UsersApplicationExceptionReason.USER_NOT_FOUND_BY_EMAIL;

@Repository
@RequiredArgsConstructor
public class PersonRepository {
    private final CustomerRepository customerRepository;

    public Optional<Person> findByEmail(String email){
        return customerRepository.findByEmail(email).map(Person.class::cast);
    }

    public Person findByEmailOrThrowDefault(String email) {
        return findByEmail(email).orElseThrow(() -> new EcommerceApplicationException(USER_NOT_FOUND_BY_EMAIL, email));
    };
}
