package crud.roo.web;

import crud.roo.domain.GenericEntity;
import crud.roo.domain.Person;
import crud.roo.domain.Pet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/" + PetController.ENTITY_CONTEXT_PATH)
public class PetController extends GenericSpringMVCController<Pet> {

    protected static final String ENTITY_CONTEXT_PATH = "pets";

    @Override
    protected Class<? extends GenericEntity> getEntityClass() {
        return Pet.class;
    }

    @Override
    protected String entityContextPath() {
        return ENTITY_CONTEXT_PATH;
    }

}
