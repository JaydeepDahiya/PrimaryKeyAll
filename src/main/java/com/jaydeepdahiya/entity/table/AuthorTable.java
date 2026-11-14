// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity.table;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "authors_table")
public class AuthorTable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "common_seq_gen")
    @TableGenerator(
            name = "common_seq_gen",
            table = "common_seq",
            pkColumnName = "seq_name",
            pkColumnValue = "authors",
            valueColumnName = "seq_value",
            allocationSize = 1
    )
    @Schema(accessMode = Schema.AccessMode.READ_ONLY) // OpenAPI response field only; omitted from Swagger request bodies.
    private Long id;

    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorTable other)) return false;

        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return AuthorTable.class.hashCode();
    }
}
