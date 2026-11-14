// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass // to tell spring to add these columns to the subclasses
@EntityListeners(AuditingEntityListener.class) // to capture auditing info and map to fields
public class BaseEntity {

    @CreatedDate
    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false) // ensure nullable and updatable is set as false
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // OpenAPI response field only; ignored when deserializing request bodies, i.e. omitted from Swagger request bodies.
    private Instant createdAt;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, length = 20, updatable = false)
    // ensure nullable and updatable is set as false
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // OpenAPI response field only; ignored when deserializing request bodies, i.e. omitted from Swagger request bodies.
    private String createdBy;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "UPDATED_AT", insertable = false) // ensure insertable is set as false
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // OpenAPI response field only; ignored when deserializing request bodies, i.e. omitted from Swagger request bodies.
    private Instant updatedAt;

    @LastModifiedBy
    @Column(name = "UPDATED_BY", length = 20, insertable = false) // ensure insertable is set as false
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // OpenAPI response field only; ignored when deserializing request bodies, i.e. omitted from Swagger request bodies.
    private String updatedBy;

}
