package ma.enset;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket=new Socket("localhost",123);
        //
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();
        //read String
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);
        //Write String
        PrintWriter pw=new PrintWriter(os,true);

        //Write Msg to Srv
        System.out.println("Enter Your msg to server: ");
        String toSrv=new Scanner(System.in).nextLine();
        pw.println(toSrv);

        //Read Srv Msg
        String srvMsg=br.readLine();
        System.out.println("Server: "+srvMsg);

        //close the cnx
        socket.close();

    }
}
