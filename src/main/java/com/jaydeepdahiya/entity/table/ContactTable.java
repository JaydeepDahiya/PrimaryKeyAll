// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity.table;

import com.jaydeepdahiya.entity.BaseEntity;
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
@Table(name = "contacts_table")
public class ContactTable extends BaseEntity {

    /* The sequence table used here is common and can hold sequence nos for multiple entity tables with the primary key
    for respective entity as the entity name i.e. seq_name has value contacts for contacts sequence */
    /* try adding a new contact via spring docs or postman and see the content of common_seq table */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "common_seq_gen")
    @TableGenerator(
            name = "common_seq_gen",
            table = "common_seq",
            pkColumnName = "seq_name",
            pkColumnValue = "contacts",
            valueColumnName = "seq_value",
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