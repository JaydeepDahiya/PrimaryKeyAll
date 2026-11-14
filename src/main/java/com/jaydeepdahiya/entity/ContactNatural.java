// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contacts_natural")
public class ContactNatural extends BaseEntity {

    @Id
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @ColumnDefault("'NEW'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;
}