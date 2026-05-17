package org.example.bai02.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private List<Customer> customers = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong(1);

    static class Customer {

        private Long id;
        private String name;
        private String email;

        public Customer() {
        }

        public Customer(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // POST /api/customers
    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @RequestBody Customer customer) {

        customer.setId(nextId.getAndIncrement());

        customers.add(customer);

        return new ResponseEntity<>(
                customer,
                HttpStatus.CREATED
        );
    }

    // PUT /api/customers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer updatedCustomer) {

        Optional<Customer> existingCustomer = customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (existingCustomer.isPresent()) {

            Customer customer = existingCustomer.get();

            customer.setName(updatedCustomer.getName());
            customer.setEmail(updatedCustomer.getEmail());

            return new ResponseEntity<>(
                    customer,
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @PathVariable Long id) {

        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
