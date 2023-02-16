package ma.enset;

import com.sun.javafx.geom.Area;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;


public class ClientTelnet extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    public void start(Stage stage) throws IOException {

        AnchorPane root2=new AnchorPane();
        VBox centring=new VBox();
        TextField name=new TextField();
        Button accept=new Button("Accepte");
        centring.setSpacing(10);

        //headText
        Text firstTxt=new Text("MyChat Application");
        Text second = new Text("broad-multi cast chat...");


        firstTxt.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 30));
        firstTxt.setLayoutX(135);
        firstTxt.setLayoutY(80);
        firstTxt.setFill(Color.valueOf("#FFFFFFFF"));
        root2.getChildren().add(firstTxt);

        second.setFont(Font.font("Calibri",20));
        second.setLayoutX(205);
        second.setLayoutY(100);
        second.setFill(Color.valueOf("#FFFFFFFF"));
        root2.getChildren().add(second);


        name.setPromptText("Username");

        centring.getChildren().add(name);
        centring.getChildren().add(accept);
        centring.setAlignment(Pos.CENTER);
        centring.setLayoutX(220);
        centring.setLayoutY(120);

        Text alert=new Text();
        alert.setFill(Color.RED);
        centring.getChildren().add(0,alert);


        root2.getChildren().add(centring);

        Scene userScene=new Scene(root2);
        //System.out.println(getClass().getResource("/style/styleUI1.css").toExternalForm());
        userScene.getStylesheets().add(getClass().getResource("/style/styleUI1.css").toExternalForm());
        stage.setScene(userScene);







        AnchorPane root=new AnchorPane();

        TextField message=new TextField();
        message.setStyle("-fx-border-color: #3469de");
        message.setLayoutX(10);
        message.setLayoutY(320);
        message.setPrefWidth(518);
        message.setPrefHeight(30);

        Button send=new Button();
        send.setLayoutX(528);
        send.setLayoutY(317);
        ImageView m=new ImageView(getClass().getResource("/style/send.png").toExternalForm());
        m.setFitWidth(30);
        m.setFitHeight(25);
        send.setGraphic(m);
        send.setBackground(null);


        ScrollPane msgPane=new ScrollPane();
        msgPane.setStyle("-fx-border-color: #3469de");
        msgPane.setPrefWidth(560);
        msgPane.setPrefHeight(280);
        msgPane.setLayoutX(10);
        msgPane.setLayoutY(35);
        msgPane.setPrefViewportWidth(560);
        msgPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        msgPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        Text to=new Text("To :");
        to.setX(10);
        to.setY(13);
        to.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 12));

        TextField msgTo=new TextField();
        msgTo.setStyle("-fx-border-color: #3469de");
        msgTo.setPrefWidth(534);
        msgTo.setLayoutX(35);
        msgTo.setLayoutY(0);




        AnchorPane contentPane=new AnchorPane();
        contentPane.setId("contentPane");
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


                    Button test=new Button(serverMsg);
                    //test.setDisable(true);
                    test.setId("inMsg");

                    StackPane spane=new StackPane();
                    spane.getChildren().add(test);
                    spane.setPrefWidth(550);
                    spane.setPrefHeight(30);
                    spane.setLayoutX(0);
                    spane.setLayoutY(y+10);
                    spane.setAlignment(Pos.BASELINE_RIGHT);



                    Platform.runLater(()->{


                        contentPane.getChildren().add(spane);

                    });


                }
            }catch (Exception e){
            }
        }).start();


        send.setOnAction(actionEvent -> {
            if(!message.getText().isEmpty()){
                double y=10;
                if(contentPane.getChildren().size()-1>=0) {
                    y = contentPane.getChildren().get(contentPane.getChildren().size() - 1).getLayoutY()+20;
                }

                /*
                Text test=new Text("me: "+message.getText());
                test.setFill(Color.GREEN);
                test.setLayoutX(5);
                test.setLayoutY(y);
                test.setId("outMsg");
                test.setStyle("-fx-background-color:red ");
                */

                Button test=new Button("me: "+message.getText());
                //test.setDisable(true);
                test.setLayoutX(5);
                test.setLayoutY(y+10);
                test.setId("outMsg");



                contentPane.getChildren().add(test);
                if(!msgTo.getText().isEmpty()){
                    //System.out.println(msgTo.getText()+"=>"+message.getText());
                    printWriter.println(msgTo.getText()+"=>"+message.getText());
                }
                else{
                    printWriter.println(message.getText());
                }

                message.setText("");
            }

        });


        accept.setOnAction(actionEvent -> {
            if(!name.getText().isEmpty() ){
                printWriter.println("name:"+name.getText());
                Scene scene=new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/style/chat.css").toExternalForm());
                stage.setScene(scene);
            }
            else {
                alert.setText("Please Enter Your Username ⚠");
            }

        });





        stage.setHeight(400);
        stage.setWidth(600);
        stage.setTitle("MyChat");
        stage.setResizable(false);
        stage.show();

    }
}
