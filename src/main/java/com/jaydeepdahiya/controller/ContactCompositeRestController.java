// Author: Jaydeep Dahiya
package com.jaydeepdahiya.controller;

import com.jaydeepdahiya.config.CompositeBackendIdConverter;
import com.jaydeepdahiya.entity.composite.ContactComposite;
import com.jaydeepdahiya.repository.ContactCompositeRepository;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@BasePathAwareController
public class ContactCompositeRestController {

    private final ContactCompositeRepository repository;

    public ContactCompositeRestController(ContactCompositeRepository repository) {
        this.repository = repository;
    }

    // Spring Data REST's default DELETE path treats the encoded composite URL ID as a String
    // when calling deleteById, instead of converting it to the full CompositeId. This causes a
    // String-to-CompositeId conversion failure, so this endpoint decodes both key fields and
    // deletes the entity directly; POST and GET remain handled by Spring Data REST.
    @DeleteMapping("/contacts-composite/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        var compositeId = CompositeBackendIdConverter.decodeIdClassCompositeId(id);
        return repository.findById(compositeId)
                .map(this::delete)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<Void> delete(ContactComposite contact) {
        repository.delete(contact);
        return ResponseEntity.noContent().build();
    }
}
