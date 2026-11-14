// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity.compositeembedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CompositeIdEmbedded implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "firstname", nullable = false)
    private String firstname;


    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeIdEmbedded)) return false;
        CompositeIdEmbedded that = (CompositeIdEmbedded) o;
        return Objects.equals(firstname, that.firstname)
                && Objects.equals(lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname);
    }

    @Override
    public String toString() {
        return "CompositeIdEmbedded{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
