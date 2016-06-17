package clientserverchat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by r.popov on 6/13/2016.
 */
public class Server {
    private ServerSocket serverSocket;
    private PrintWriter writer;
    private Socket client;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 2; i++) {
            executorService.submit(new ThreadForWrite());
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ThreadForRead implements Runnable {
    private ServerSocket serverSocket;
    private PrintWriter writer1;
    private Socket client;
    private Scanner scanner;

    @Override
    public void run() {
        InputStream is = null;
        try {
            is = client.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder s = new StringBuilder();
        String line = null;

        try {
            while((line = br.readLine()) != null){
                s.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(s.toString());
    }
}

class ThreadForWrite implements Runnable {
    private ServerSocket serverSocket;
    private PrintWriter writer1;
    private Socket client;
    private Scanner scanner;
    private OutputStream output;

    public ThreadForWrite() {
    }

    public ThreadForWrite(ServerSocket serverSocket, PrintWriter writer, Socket client, Scanner scanner) {
        this.serverSocket = serverSocket;
        this.writer1 = writer;
        this.client = client;
        this.scanner = scanner;
    }

    @Override
    public void run() {

        try {
            output = new FileOutputStream("C:\\Serializer\\out\\production\\Serializer/test.text");

            serverSocket = new ServerSocket(8080);
            client = serverSocket.accept();
            writer1 = new PrintWriter(client.getOutputStream());
            String outputstring = scanner.nextLine();
            writer1.print(output);
            output.write(Integer.parseInt(outputstring));
            writer1.flush();
            writer1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}