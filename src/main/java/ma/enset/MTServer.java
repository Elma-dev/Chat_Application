package ma.enset;


import javafx.scene.layout.VBox;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MTServer implements Runnable {
    static ArrayList<String> clientNames=new ArrayList<>();

    public static void main(String[] args) {
        new Thread(new MTServer()).start();

    }

    @Override
    public void run() {
        ArrayList<Socket> clients=new ArrayList<>();
        int clientId=0;
        //ArrayList<Socket> clients=new ArrayList<>();
        try {
            ServerSocket ss=new ServerSocket(123);
            Socket client;

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
                String name = null;
                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();

                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);

                PrintWriter pw=new PrintWriter(os,true);

                //Write Msg To Client


                //Read Msg Of Client
                String msgClient;

                while ((msgClient= br.readLine())!=null){
                    //System.out.println("client "+this.clientId+": "+msgClient);

                    if(msgClient.contains("name:")){
                        name=(msgClient.split(":"))[1];
                        clientNames.add(name);
                        pw.println("Hello "+name+" ,welcom in conversation.");
                        //System.out.println(clientNames);
                    }
                    else if(msgClient.contains("=>")){
                        String to= (msgClient.split("=>"))[0];
                        msgClient= (msgClient.split("=>"))[1];
                        if(to.contains(",")){
                            //List<String> msgTo=Arrays.asList(to.split(","));
                            String[] msgTo=to.split(",");

                            for(String x : msgTo){
                                int index=clientNames.indexOf(x);
                                if(clients.size()>=index && index!=(clientId-1)) {
                                    pw = new PrintWriter(( clients.get(index)).getOutputStream(), true);
                                    pw.println(clientNames.get(clientId-1) + " : " + msgClient);
                                }
                            }
                        }else{

                            int index=clientNames.indexOf(to);
                            //System.out.println(to+" "+index);
                            if(clients.size()>=index && index!=(clientId-1)) {
                                pw = new PrintWriter(( clients.get(index)).getOutputStream(), true);
                                pw.println(clientNames.get(clientId-1) + " : " + msgClient);
                            }
                        }

                    }else {
                        for (Socket c : clients) {
                            if (c != client) {
                                pw = new PrintWriter(c.getOutputStream(), true);
                                pw.println(name + " : " + msgClient);
                            }
                        }
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
