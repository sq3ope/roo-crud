package crud.roo.domain;

import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Configurable
public class Pet extends GenericEntity {

    /**
     */
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
