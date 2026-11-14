// Author: Jaydeep Dahiya
package com.jaydeepdahiya.repository;

import com.jaydeepdahiya.entity.composite.CompositeId;
import com.jaydeepdahiya.entity.composite.ContactComposite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository // Optional as its extending JPARepository so Spring will auto create the bean
@RepositoryRestResource(path = "contacts-composite", collectionResourceRel = "contacts-composite")
public interface ContactCompositeRepository extends JpaRepository<ContactComposite, CompositeId> {
}