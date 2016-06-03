package serverclienttask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by r.popov on 6/3/2016.
 */
public class Server {
    private int id;
    List<String> list;

    public Server(){
        list = new ArrayList<>();
    }
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket client = serverSocket.accept();
                String clientINfo = String.format("count % d, address %s, port %d\n", id++, client.getInetAddress(), client.getPort());
                list.add(clientINfo);
                System.out.println(clientINfo);

                PrintWriter pw = new PrintWriter(client.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                pw.printf("Hello from server\n" + clientINfo, new Date());
                Scanner scanner = new Scanner(System.in);
                pw.printf(scanner.nextLine());
                pw.flush();
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
