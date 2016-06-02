package privateserializer;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by r.popov on 6/2/2016.
 */
public class TestReflectionFormater {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        ReflectionFormater reflectionFormater = new ReflectionFormater();
        Car car = new Car("lada", 1989, 2.0);

        reflectionFormater.format(car);
    }
}
