package privateserializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
    public Object parse(String src) {

        String[] lines = src.split("\n");
        String type = lines[0].split(":")[1];
        Map<String, String> keyValuesMap = new HashMap<>();
        for (int i = 0; i < lines.length; i++) {
            String[] keyValue = lines[i].split(":");
            keyValuesMap.put(keyValue[0], keyValue[1]);
        }
        Object instance = null;
        try {
            Class cl = Class.forName(type);
            instance = cl.newInstance();
            for (String key : keyValuesMap.keySet()) {
                Field field = cl.getDeclaredField(key);

                Class<?> fieldType = field.getType();

                Method setMethod = cl.getMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), fieldType);

                if (int.class == fieldType) {
                    setMethod.invoke(instance, Integer.parseInt(keyValuesMap.get(key)));
                } else if (double.class == fieldType) {
                    setMethod.invoke(instance, Double.parseDouble(keyValuesMap.get(key)));
                } else if (String.class == fieldType) {
                    setMethod.invoke(instance, keyValuesMap.get(key));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }
}
