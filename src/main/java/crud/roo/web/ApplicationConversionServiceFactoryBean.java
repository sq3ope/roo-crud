package crud.roo.web;

import crud.roo.domain.GenericEntity;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import java.util.Set;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

    @Override
    public void setFormatterRegistrars(Set<FormatterRegistrar> formatterRegistrars) {
        super.setFormatterRegistrars(formatterRegistrars);
        // Register application converters and formatters
    }

    public Converter<GenericEntity, String> getGenericEntityToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<crud.roo.domain.GenericEntity, java.lang.String>() {
            public String convert(GenericEntity entity) {
                return new StringBuilder().append(entity.toString()).toString();
            }
        };
    }

    public Converter<Long, ? extends GenericEntity> getIdToGenericEntityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, crud.roo.domain.GenericEntity>() {
            public crud.roo.domain.GenericEntity convert(java.lang.Long id) {
                try {
                    return (GenericEntity) GenericEntity.find(id, GenericEntity.class);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    // TODO
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    // TODO
                    return null;
                }
            }
        };
    }

    public Converter<String, ? extends GenericEntity> getStringToGenericEntityConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, crud.roo.domain.GenericEntity>() {
            public crud.roo.domain.GenericEntity convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), GenericEntity.class);
            }
        };
    }

    public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getGenericEntityToStringConverter());
        registry.addConverter(getIdToGenericEntityConverter());
        registry.addConverter(getStringToGenericEntityConverter());
    }

    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
