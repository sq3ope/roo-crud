package crud.roo.web;

import crud.roo.domain.Person;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Person, String> getPersonToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<crud.roo.domain.Person, java.lang.String>() {
            public String convert(Person person) {
                return new StringBuilder().append(person.getEmail()).append(' ').append(person.getFirstName()).append(' ').append(person.getLastName()).toString();
            }
        };
    }

	public Converter<Long, Person> getIdToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, crud.roo.domain.Person>() {
            public crud.roo.domain.Person convert(java.lang.Long id) {
                return Person.findPerson(id);
            }
        };
    }

	public Converter<String, Person> getStringToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, crud.roo.domain.Person>() {
            public crud.roo.domain.Person convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Person.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getPersonToStringConverter());
        registry.addConverter(getIdToPersonConverter());
        registry.addConverter(getStringToPersonConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
