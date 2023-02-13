package ma.enset;

import com.sun.javafx.geom.Area;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientTelnet extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    public void start(Stage stage) throws IOException {



        AnchorPane root=new AnchorPane();

        TextField message=new TextField();
        message.setLayoutX(10);
        message.setLayoutY(320);
        message.setPrefWidth(500);
        message.setPrefHeight(30);

        Button send=new Button("Send");
        send.setLayoutX(515);
        send.setLayoutY(320);


        ScrollPane msgPane=new ScrollPane();
        msgPane.setPrefWidth(515);
        msgPane.setPrefHeight(280);
        msgPane.setLayoutX(10);
        msgPane.setLayoutY(35);

        Text to=new Text("To :");
        to.setX(10);
        to.setY(13);

        TextField msgTo=new TextField();
        msgTo.setPrefWidth(490);
        msgTo.setLayoutX(35);
        msgTo.setLayoutY(0);




        AnchorPane contentPane=new AnchorPane();
        contentPane.setStyle("-fx-background-color: WHITE");
        contentPane.setLayoutX(0);
        contentPane.setLayoutY(0);
        contentPane.setPrefWidth(510);
        contentPane.setPrefHeight(280);
        msgPane.setContent(contentPane);


        root.getChildren().add(message);
        root.getChildren().add(send);
        root.getChildren().add(to);
        root.getChildren().add(msgTo);
        root.getChildren().add(msgPane);





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
                    System.out.println("Here "+serverMsg);


                    double y=10;
                    if(contentPane.getChildren().size()-1>=0) {
                        y = contentPane.getChildren().get(contentPane.getChildren().size() - 1).getLayoutY()+20;
                    }


                    Text test=new Text(serverMsg);


                    test.setFill(Color.RED);
                    test.setLayoutX(10);
                    test.setLayoutY(y);
                    Platform.runLater(()->{
                        contentPane.getChildren().add(test);
                    });


                }
            }catch (Exception e){
            }
        }).start();


        send.setOnAction(actionEvent -> {
            double y=10;
            if(contentPane.getChildren().size()-1>=0) {
                y = contentPane.getChildren().get(contentPane.getChildren().size() - 1).getLayoutY()+20;
            }


            Text test=new Text("me: "+message.getText());
            test.setFill(Color.GREEN);
            test.setLayoutX(10);
            test.setLayoutY(y);

            contentPane.getChildren().add(test);
            if(!msgTo.getText().isEmpty()){
                System.out.println(msgTo.getText()+"=>"+message.getText());
                printWriter.println(msgTo.getText()+"=>"+message.getText());
            }
            else{
                printWriter.println(message.getText());
            }




            message.setText("");

        });





        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.setHeight(400);
        stage.setWidth(600);
        stage.setTitle("ClientChat");
        stage.show();

    }
}
