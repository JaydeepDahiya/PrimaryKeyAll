// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contacts_uuid")
public class ContactUUID extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(value = Types.VARCHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
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