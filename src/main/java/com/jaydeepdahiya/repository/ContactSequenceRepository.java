// Author: Jaydeep Dahiya
package com.jaydeepdahiya.repository;

import com.jaydeepdahiya.entity.ContactSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository // Optional as its extending JPARepository so Spring will auto create the bean
@RepositoryRestResource(path = "contacts-sequence", collectionResourceRel = "contacts-sequence")
public interface ContactSequenceRepository extends JpaRepository<ContactSequence, Long> {
}