package privateserializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by r.popov on 6/2/2016.
 */
public class ReflectionFormater {

    public String format(Object o) throws InvocationTargetException, IllegalAccessException {
        Class cl = o.getClass();
        StringBuilder stringBuiler = new StringBuilder();

        stringBuiler.append(String.format("%s:%s\n", "type", cl.getName()));

        Field[] fiedls = cl.getDeclaredFields();
        for (Field f : fiedls) {
            String fieldName = f.getName();
            try {
                Method getFieldValue = cl.getMethod("get" + fieldName.substring(0,1).toUpperCase()+ fieldName.substring(1));
                stringBuiler.append("{" + fieldName + ": " + getFieldValue.invoke(o) + "}");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();

            }

        }
        return stringBuiler.toString();
    }
}
