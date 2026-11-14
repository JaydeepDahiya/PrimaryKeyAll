// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contacts_uuid_binary")
public class ContactUUIDBinary extends BaseEntity {
    /*
        @Id
        @GeneratedValue(generator = "customUuidGen")
        //  "uuid2" is the built-in implementation Hibernate knows how to execute
        @GenericGenerator(name = "customUuidGen", strategy = "uuid2")
        @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
        private UUID id;
    */
    // NOTE: @GenericGenerator has been deprecated since Hibernate 6.5 and instead @UuidGenerator is recommended
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // OpenAPI response field only; omitted from Swagger request bodies.
    private UUID id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("'NEW'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

}