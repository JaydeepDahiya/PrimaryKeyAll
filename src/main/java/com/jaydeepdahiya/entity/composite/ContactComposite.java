// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity.composite;

import com.jaydeepdahiya.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contacts_composite")
@IdClass(CompositeId.class)
public class ContactComposite extends BaseEntity {

    @Id
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Id
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @ColumnDefault("'NEW'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;
}