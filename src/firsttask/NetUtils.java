package firsttask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by r.popov on 5/30/2016.
 */
public class NetUtils {

    public static void load(String url, String filePath) throws URISyntaxException, IOException {
            try (InputStream is = new URI(url).toURL().openStream();
                 OutputStream os = new FileOutputStream(filePath)) {
                byte[] buff = new byte[1024];
                int count = 0;
                while ((count = is.read(buff)) != -1) {
                    os.write(buff, 0, count);
                    os.flush();
                }


            }
        }
    }


