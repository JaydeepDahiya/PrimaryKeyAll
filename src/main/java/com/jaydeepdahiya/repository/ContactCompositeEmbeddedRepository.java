// Author: Jaydeep Dahiya
package com.jaydeepdahiya.repository;

import com.jaydeepdahiya.entity.compositeembedded.CompositeIdEmbedded;
import com.jaydeepdahiya.entity.compositeembedded.ContactCompositeEmbedded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository // Optional as its extending JPARepository so Spring will auto create the bean
@RepositoryRestResource(path = "contacts-composite-embedded", collectionResourceRel = "contacts-composite-embedded")
public interface ContactCompositeEmbeddedRepository extends JpaRepository<ContactCompositeEmbedded, CompositeIdEmbedded> {
}