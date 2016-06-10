package multythreadingfilesearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by r.popov on 6/9/2016.
 */
public class ExDownloader {
    public static void main(String[] args) throws IOException {

        BlockingQueue<Element> buff = new ArrayBlockingQueue<Element>(250,true);

       Document document = Jsoup.parse(new URL("http://www.ex.ua/98554142"), 112);
        Element element = document.body();
        Elements elements = element.getElementsByTag("a");
        for (Element el : elements){
            String href = el.attr("href");
            if (href.contains("https://")|| (href.contains("/load"))) {
                buff.add(el);
            }
        }
        for (int i = 0; i < 10; i++){
          new Thread(new Runnable() {
              @Override
              public void run() {
                 BlockingQueue<Element> childrenLinksBuffer = new ArrayBlockingQueue<Element>(300, true);

                  for(Element el : buff){
                      if (el.attr("href").contains("https://"));
                      try {
                          Document document = Jsoup.parse(new URL("http://www.ex.ua" + el.attr("href") ), 200000);
                          Element element = document.body();
                          Elements elements = element.getElementsByTag("a");
                          for (Element ele : elements){
                              String href = ele.attr("href");
                              if (href.contains("https://")){
                                  childrenLinksBuffer.add(ele);
                              }
                          }
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }
              }
          }).start();

            new Thread(new Runnable() {

                @Override
                public void run() {
                    for (Element el  : buff){
                    if (el.attr("href").contains("/load")){
                        String href = el.attr("href");
                        try (InputStream is = new URI("http://ex.ua" + href).toURL().openStream();
                             OutputStream os = new FileOutputStream("C:\\Serializer\\out\\production\\Serializer/folder")) {

                            int count;
                            while ((count = is.read()) != -1) {
                                os.write(count);
                                os.flush();
                            }
                            System.out.println("Progress: " + (count++) + " of " + " files are downloaded");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }
                }
            }).start();
        }

    }

}
