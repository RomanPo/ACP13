package multythreadingfilesearch;

import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by r.popov on 6/9/2016.
 */
public class FileSearch {
    public static void main(String[] args) {

        String searchName = "vcs.xml";
        String searchName2 = "modules.xml";
        File root1 = new File("C:\\Serializer\\.idea");
        File root2 = new File("C:\\Serializer\\.idea");

        MyContainer1 myContainer1 = new MyContainer1();

        ExecutorService service = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 2; i++) {
            Future future = service.submit(new FirstThread(root1, searchName, myContainer1));
            Future future1 = service.submit(new SecondThread(root2, searchName2, myContainer1));
        }

        service.shutdown();
        while (!service.isTerminated()) {
            try {
                service.awaitTermination(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static class MyContainer1 {
        File root;
        String searchName;

        public void searchFile(File root, String searchedName) {

            if (root == null) {
                throw new NullPointerException();
            }
            for (File file : root.listFiles()) {
                if (file.isDirectory()) {
                    root = file;
                    searchFile(root, searchedName);
                } else if (file.getName().equals(searchedName)) {

                    System.out.println("This is your file" + file.getName());
                }
            }
        }
    }
}

class FirstThread extends Thread implements Runnable {

    private File fileRoot;
    private String name;
    private FileSearch.MyContainer1 container;

    public FirstThread(File fileRoot, String name, FileSearch.MyContainer1 myContainer1) {
        this.fileRoot = fileRoot;
        this.name = name;
        this.container = myContainer1;
    }

    @Override
    public void run() {
        container.searchFile(fileRoot, name);
    }
}

class SecondThread extends Thread implements Runnable {

    private File fileRoot;
    private String name;
    private FileSearch.MyContainer1 container1;

    public SecondThread(File fileRoot, String name, FileSearch.MyContainer1 myContainer1) {
        this.fileRoot = fileRoot;
        this.name = name;
        this.container1 = myContainer1;
    }

    @Override
    public void run() {
        container1.searchFile(fileRoot, name);
    }
}

