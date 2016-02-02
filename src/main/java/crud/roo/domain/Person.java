package crud.roo.domain;
import org.springframework.roo.addon.javabean.annotations.RooJavaBean;
import org.springframework.roo.addon.javabean.annotations.RooToString;
import org.springframework.roo.addon.jpa.annotations.activerecord.RooJpaActiveRecord;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Person {

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
}
