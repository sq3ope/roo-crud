package crud.roo.util;

import crud.roo.domain.GenericEntity;

public class ClassUtil {

    public static String getClassName(Class<? extends GenericEntity> entityClass) {
        String[] splitted = entityClass.getName().split("\\.");
        return splitted[splitted.length - 1];
    }

}
