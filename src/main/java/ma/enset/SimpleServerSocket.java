package ma.enset;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleServerSocket {
    public static void main(String[] args) throws Exception {
        ServerSocket ss=new ServerSocket(123);
        //
        Socket socket=ss.accept();
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();
        //
        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);
        //
        PrintWriter pw=new PrintWriter(os,true);

        //read Msg of Client
        String msgClient=br.readLine();
        System.out.println("Client: "+msgClient);

        //Write a msg to client
        System.out.println("Entrer Msg:");
        String msgToClient=new Scanner(System.in).nextLine();
        pw.println(msgToClient);

    }
}
