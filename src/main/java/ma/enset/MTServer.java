package ma.enset;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MTServer extends Thread{
    public static void main(String[] args) {
        new MTServer().start();
    }

    @Override
    public void run() {
        int clientId=0;
        ArrayList<Socket> clients=new ArrayList<>();
        try {
            ServerSocket ss=new ServerSocket(123);
            Socket client;
            InputStream is;
            OutputStream os;

            while (true){
                client=ss.accept();
                clients.add(client);
                new Conversation(client,++clientId,clients).start();
                System.out.println("Client "+clientId+" Joined Conversation IP="+client.getRemoteSocketAddress());


            }




        }catch (Exception e){

        }

    }

    public class Conversation extends Thread{
        private Socket client;
        private int clientId;
        private ArrayList<Socket> clients;
        public Conversation(Socket client,int clientId,ArrayList<Socket> clients){
            this.client=client;
            this.clientId=clientId;
            this.clients=clients;
        }
        @Override
        public void run() {
            try {
                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();

                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);

                PrintWriter pw=new PrintWriter(os,true);

                //Write Msg To Client
                pw.println("Hello, welcom in conversation.");

                //Read Msg Of Client
                String msgClient;

                while ((msgClient= br.readLine())!=null){
                    System.out.println("client "+this.clientId+": "+msgClient);
                    for(Socket c : clients){
                        if(c!=client) {
                            pw = new PrintWriter(c.getOutputStream(), true);
                            pw.println("client" + clientId + ": " + msgClient);
                        }
                    }

                }

            }catch (Exception e){

            }

        }
    }
}
