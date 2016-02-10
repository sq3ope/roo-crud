package crud.roo.web;

import crud.roo.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:/META-INF/spring/applicationContext*.xml")
public class ApplicationConversionServiceFactoryBeanTest {
    @Autowired
    ConversionService applicationConversionService;

    @Test
    public void testPersonToStringConverter() throws Exception {
        Person person = new Person();
        person.setFirstName("Albin");
        person.setLastName("Gumka");
        person.setEmail("ag@nowhere.com");
        String converted = applicationConversionService.convert(person, String.class);

        assertEquals("ag@nowhere.com Albin Gumka", converted);
    }

    @Test
    public void testIdToPersonConverter() throws Exception {
        Person person = new Person();
        person.setFirstName("Winona");
        person.setLastName("Ryder");
        person.setEmail("wr@nowhere.com");
        person.persist();

        Person person2 = applicationConversionService.convert(person.getId(), Person.class);

        assertEquals("Winona", person2.getFirstName());
    }

    @Test
    public void testStringToPersonConverter() throws Exception {
        Person person = new Person();
        person.setFirstName("Natalie");
        person.setLastName("Portman");
        person.setEmail("np@nowhere.com");
        person.persist();

        Person person2 = applicationConversionService.convert(person.getId().toString(), Person.class);

        assertEquals("Natalie", person2.getFirstName());
    }
}