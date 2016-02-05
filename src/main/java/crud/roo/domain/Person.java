package crud.roo.domain;

import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Configurable
public class Person extends GenericEntity {

    /**
     */
    @NotNull
    private String email;

    /**
     */
    @NotNull
    private String firstName;

    /**
     */
    @NotNull
    private String lastName;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
