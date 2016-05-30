package firsttask;

import com.google.gson.Gson;
import firsttask.HTMLOBject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by r.popov on 5/30/2016.
 */
public class HTMLParser {
    private static List<HTMLOBject> listOfObjects = new ArrayList<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        OutputStream os = new FileOutputStream("C:\\ACPHomework\\src\\resources/temp.txt");
        Document document = Jsoup.parse(new URL("http://www.ex.ua/98554142&r=71793,23776"), 112);
        Element element = document.body();
        Elements elements = element.getElementsByTag("a");
        for (Element elemen : elements) {
            String href = elemen.attr("href");
            if (href.contains("/ru")) {
                listOfObjects.add(new HTMLOBject(href));
            }
        }
        convertToJson();

    }

    public static void convertToJson() {
        for (HTMLOBject htmloBject : listOfObjects) {
            String json = gson.toJson(htmloBject);
            System.out.println(json);
        }
    }
}
