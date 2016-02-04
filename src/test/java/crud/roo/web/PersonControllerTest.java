package crud.roo.web;

import crud.roo.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Person.class)
public class PersonControllerTest {
    @Mock
    Person person;
    @Mock
    BindingResult bindingResult;
    @Mock
    HttpServletRequest httpServletRequest;

    PersonController personController;
    Model uiModel;

    @Before
    public void setUp() {
        personController = new PersonController();
        uiModel = new ExtendedModelMap();
    }

    @Test
    public void testCreate() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(person.getId()).thenReturn(17L);

        String resultView = personController.create(person, bindingResult, uiModel, httpServletRequest);

        assertEquals("redirect:/people/17", resultView);
        assertEquals(true, uiModel.asMap().isEmpty());
        verify(person).persist();
    }

    @Test
    public void testCreateBindingError() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultView = personController.create(person, bindingResult, uiModel, httpServletRequest);

        assertEquals("people/create", resultView);
        assertEquals(person, uiModel.asMap().get("person"));
        verify(person, never()).persist();
    }

    @Test
    public void testCreateForm() throws Exception {
        String resultView = personController.createForm(uiModel);

        assertEquals("people/create", resultView);
        assertTrue(uiModel.asMap().get("person") instanceof Person);
    }

    @Test
    public void testShow() throws Exception {
        long id = 17L;
        Person person = new Person();

        PowerMockito.mockStatic(Person.class);
        Mockito.when(Person.findPerson(id)).thenReturn(person);

        String resultView = personController.show(id, uiModel);

        assertEquals("people/show", resultView);
        assertEquals(person, uiModel.asMap().get("person"));
        assertEquals(id, uiModel.asMap().get("itemId"));
    }

    @Test
    public void testList() throws Exception {
        List<Person> people = new ArrayList<>();
        people.add(person);

        PowerMockito.mockStatic(Person.class);
        Mockito.when(Person.findAllPeople(null, null)).thenReturn(people);

        String resultView = personController.list(null, null, null, null, uiModel);

        assertEquals("people/list", resultView);
        assertEquals(people, uiModel.asMap().get("people"));
    }

    @Test
    public void testListPaging() throws Exception {
        List<Person> people = new ArrayList<>();
        people.add(person);

        int page = 2;
        int size = 10;
        int firstResult = 10;
        String sortFieldName = "name";
        String sortOrder = "asc";

        PowerMockito.mockStatic(Person.class);
        Mockito.when(Person.findPersonEntries(firstResult, size, sortFieldName, sortOrder)).thenReturn(people);
        Mockito.when(Person.countPeople()).thenReturn(17L);

        String resultView = personController.list(page, size, sortFieldName, sortOrder, uiModel);

        assertEquals("people/list", resultView);
        assertEquals(people, uiModel.asMap().get("people"));
        assertEquals(2, uiModel.asMap().get("maxPages"));
    }

    @Test
    public void testUpdate() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(person.getId()).thenReturn(17L);

        String resultView = personController.update(person, bindingResult, uiModel, httpServletRequest);

        assertEquals("redirect:/people/17", resultView);
        assertEquals(true, uiModel.asMap().isEmpty());
        verify(person).merge();
    }

    @Test
    public void testUpdateBindingError() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(true);

        String resultView = personController.update(person, bindingResult, uiModel, httpServletRequest);

        assertEquals("people/update", resultView);
        assertEquals(person, uiModel.asMap().get("person"));
        verify(person, never()).merge();
    }

    @Test
    public void testUpdateForm() throws Exception {
        long id = 17L;
        Person person = new Person();

        PowerMockito.mockStatic(Person.class);
        Mockito.when(Person.findPerson(id)).thenReturn(person);

        String resultView = personController.updateForm(id, uiModel);

        assertEquals("people/update", resultView);
        assertEquals(person, uiModel.asMap().get("person"));
    }

    @Test
    public void testDelete() throws Exception {
        long id = 17L;
        int page = 1;
        int size = 10;

        PowerMockito.mockStatic(Person.class);
        Mockito.when(Person.findPerson(id)).thenReturn(person);

        String resultView = personController.delete(id, page, size, uiModel);

        assertEquals("redirect:/people", resultView);
        assertEquals(String.valueOf(page), uiModel.asMap().get("page"));
        assertEquals(String.valueOf(size), uiModel.asMap().get("size"));
        verify(person).remove();
    }

}