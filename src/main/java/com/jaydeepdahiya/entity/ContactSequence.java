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
@Table(name = "contacts_sequence")
public class ContactSequence extends BaseEntity {

    /* strategy to ensure hibernate only jumps by 1 after every restart */
    /* NOTE: this is using sequence via table as mysql doesn't support sequence natively */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contacts_gen")
    @SequenceGenerator(
            name = "contacts_gen",
            sequenceName = "contacts_sequence_seq",
            allocationSize = 1
    )
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