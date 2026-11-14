// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contacts_identity")
public class ContactIdentity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // OpenAPI response field only; omitted from Swagger request bodies.
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @ColumnDefault("'NEW'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

}