package ma.enset;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MTServer implements Runnable {

    VBox boxClients=new VBox();
    static ArrayList<Socket> clients=new ArrayList<>();
    public static void main(String[] args) {
        new Thread(new MTServer()).start();

    }

    @Override
    public void run() {
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
                    if(msgClient.contains("=>")){
                        String to= (msgClient.split("=>"))[0];
                        msgClient= (msgClient.split("=>"))[1];
                        if(to.contains(",")){
                            String[] msgTo=to.split(",");
                            for(String x : msgTo){
                                int index=Integer.parseInt(x);
                                if(clients.size()>=index) {
                                    pw = new PrintWriter(((Socket) clients.get(index-1)).getOutputStream(), true);
                                    pw.println("client" + clientId + ": " + msgClient);
                                }
                            }
                        }else{
                            int index=Integer.parseInt(to);
                            if(clients.size()>=index) {
                                pw = new PrintWriter(((Socket) clients.get(index-1)).getOutputStream(), true);
                                pw.println("client" + clientId + ": " + msgClient);
                            }
                        }

                    }else {
                        for (Socket c : clients) {
                            if (c != client) {
                                pw = new PrintWriter(c.getOutputStream(), true);
                                pw.println("client" + clientId + ": " + msgClient);
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
