// Author: Jaydeep Dahiya
package com.jaydeepdahiya.repository;

import com.jaydeepdahiya.entity.table.BookTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by jt on 6/12/21.
 */
/*
Note: Spring Data JPA discovers BookRepository because it extends JpaRepository and registers it as a repository bean
(assuming repository scanning is enabled, which Spring Boot configures automatically in the usual setup).
Adding @Repository is unnecessary.
 */
@RepositoryRestResource(path = "books-table", collectionResourceRel = "books-table")
public interface BookTableRepository extends JpaRepository<BookTable, Long> {
}
