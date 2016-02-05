package crud.roo.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import crud.roo.domain.GenericEntity;
import crud.roo.domain.Person;

@Controller
@RequestMapping("/" + PersonController.ENTITY_CONTEXT_PATH)
public class PersonController extends GenericSpringMVCController<Person> {

	protected static final String ENTITY_CONTEXT_PATH = "people";

	@Override
	protected Class<? extends GenericEntity> getEntityClass() {
		return Person.class;
	}

	@Override
	protected String entityContextPath() {
		return ENTITY_CONTEXT_PATH;
	}

	@Override
	protected String entityCollectionAttributeName() {
		// This one has to be overriden in this case, because evo-inflector uses incorrect form "persons"
		// In most cases this is not neccesary
		return "people";
	}

}
