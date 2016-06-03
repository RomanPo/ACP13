package serverclienttask;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by r.popov on 6/3/2016.
 */
public class RunClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("10.100.27.130", 8080);

        DataOutputStream outputLine = new DataOutputStream(socket.getOutputStream());
        outputLine = new DataOutputStream(new BufferedOutputStream(System.out));

        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder s = new StringBuilder();
        String line = null;

        while((line = br.readLine()) != null){
        s.append(line).append("\n");
        }
        System.out.println(s.toString());
        Scanner scan = new Scanner(System.in);

        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        System.out.println("Введите свой текст");
        String str = "";
        while (!str.equals("exit")) {
            str = scan.nextLine();
           pw.printf(str);
        }
    }

}
