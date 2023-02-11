package ma.enset;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientTelnet {
    public static void main(String[] args) throws Exception {
        Socket socket=new Socket("localhost",123);
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();

        InputStreamReader isr=new InputStreamReader(is);
        BufferedReader br=new BufferedReader(isr);

        PrintWriter printWriter=new PrintWriter(os,true);


        new Thread(()->{
            String serverMsg;
            try{
            while((serverMsg=br.readLine())!=null){
                System.out.println(serverMsg);
            }
            }catch (Exception e){
            }
        }).start();


        while(true){
            String msgToSrv=new Scanner(System.in).nextLine();
            printWriter.println(msgToSrv);
        }

    }
}
