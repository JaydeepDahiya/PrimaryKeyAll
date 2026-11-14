// Author: Jaydeep Dahiya
package com.jaydeepdahiya.entity.composite;

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
public class CompositeId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeId)) return false;
        CompositeId that = (CompositeId) o;
        return Objects.equals(firstname, that.firstname)
                && Objects.equals(lastname, that.lastname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname);
    }

    @Override
    public String toString() {
        return "CompositeId{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
