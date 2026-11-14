// Author: Jaydeep Dahiya
package com.jaydeepdahiya.repository;

import com.jaydeepdahiya.entity.ContactUUIDBinary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository // Optional as its extending JPARepository so Spring will auto create the bean
@RepositoryRestResource(path = "contacts-uuid-binary", collectionResourceRel = "contacts-uuid-binary")
public interface ContactUUIDBinaryRepository extends JpaRepository<ContactUUIDBinary, UUID> {
}