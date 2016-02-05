package crud.roo.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.atteo.evo.inflector.English;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import crud.roo.domain.GenericEntity;
import crud.roo.util.ClassUtil;

public abstract class GenericSpringMVCController<E extends GenericEntity> {

	protected abstract Class<? extends GenericEntity> getEntityClass();
	protected abstract String entityContextPath();
	
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid E entity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, entity);
            return entityContextPath() + "/create";
        }
        uiModel.asMap().clear();
        entity.persist();
        return "redirect:/" + entityContextPath() + "/" + encodeUrlPathSegment(entity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) throws InstantiationException, IllegalAccessException {
        populateEditForm(uiModel, E.newInstance(getEntityClass()));
        return entityContextPath() + "/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) throws InstantiationException, IllegalAccessException {
        uiModel.addAttribute(entityAttributeName(), E.find(id, getEntityClass()));
        uiModel.addAttribute("itemId", id);
        return entityContextPath() + "/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) throws InstantiationException, IllegalAccessException {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute(entityCollectionAttributeName(), E.findEntries(firstResult, sizeNo, sortFieldName, sortOrder, getEntityClass()));
            float nrOfPages = (float) E.count(getEntityClass()) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute(entityCollectionAttributeName(), E.findAll(sortFieldName, sortOrder, getEntityClass()));
        }
        return entityContextPath() + "/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid E entity, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, entity);
            return entityContextPath() + "/update";
        }
        uiModel.asMap().clear();
        entity.merge();
        return "redirect:/" + entityContextPath() + "/" + encodeUrlPathSegment(entity.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) throws InstantiationException, IllegalAccessException {
        populateEditForm(uiModel, E.find(id, getEntityClass()));
        return entityContextPath() + "/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) throws InstantiationException, IllegalAccessException {
        GenericEntity entity = GenericEntity.find(id, getEntityClass());
        entity.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/" + entityContextPath();
    }

	void populateEditForm(Model uiModel, GenericEntity entity) {
        uiModel.addAttribute(entityAttributeName(), entity);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

	protected String entityAttributeName() {
		return ClassUtil.getClassName(getEntityClass()).toLowerCase();
	}

	protected String entityCollectionAttributeName() {
		return English.plural(entityAttributeName());
	}
}
