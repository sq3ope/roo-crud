package crud.roo.domain;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Component
@Configurable
public class PersonDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<? extends GenericEntity> data;

    public Person getNewTransientPerson(int index) {
        Person obj = new Person();
        setEmail(obj, index);
        setFirstName(obj, index);
        setLastName(obj, index);
        return obj;
    }

    public void setEmail(Person obj, int index) {
        String email = "foo" + index + "@bar.com";
        obj.setEmail(email);
    }

    public void setFirstName(Person obj, int index) {
        String firstName = "firstName_" + index;
        obj.setFirstName(firstName);
    }

    public void setLastName(Person obj, int index) {
        String lastName = "lastName_" + index;
        obj.setLastName(lastName);
    }

    public GenericEntity getSpecificPerson(int index) throws InstantiationException, IllegalAccessException {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Person obj = (Person) data.get(index);
        Long id = obj.getId();
        return Person.find(id, Person.class);
    }

    public Person getRandomPerson() throws InstantiationException, IllegalAccessException {
        init();
        Person obj = (Person) data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return (Person) Person.find(id, Person.class);
    }

    public boolean modifyPerson(GenericEntity obj) {
        return false;
    }

    public void init() throws InstantiationException, IllegalAccessException {
        int from = 0;
        int to = 10;
        data = Person.findEntries(from, to, Person.class);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Person' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        ArrayList<Person> tmpdata = new ArrayList<Person>();
        for (int i = 0; i < 10; i++) {
            Person obj = getNewTransientPerson(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext(); ) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            tmpdata.add(obj);
        }
        data = tmpdata;
    }
}
