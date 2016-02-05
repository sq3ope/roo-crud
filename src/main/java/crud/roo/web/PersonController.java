package crud.roo.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import crud.roo.domain.GenericEntity;
import crud.roo.domain.Person;

@Controller
@RequestMapping("/people")
public class PersonController extends GenericSpringMVCController<Person> {

	@Override
	protected Class<? extends GenericEntity> getEntityClass() {
		return Person.class;
	}

	
}
