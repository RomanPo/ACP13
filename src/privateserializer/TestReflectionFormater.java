package privateserializer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by r.popov on 6/2/2016.
 */
public class TestReflectionFormater {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, IOException {
       ReflectionFormater reflectionFormater = new ReflectionFormater();
        Car car = new Car("lada", 1989, 2.0);
        System.out.println(reflectionFormater.format(car));
        FileOutputStream fl = new FileOutputStream("C:\\Serializer\\src\\privateserializer/test.json");
        fl.write(reflectionFormater.format(car).getBytes());
    }
}
