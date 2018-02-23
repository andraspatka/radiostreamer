package com.andras.json.GUI;
/**
 * Created by Patka Zsolt-Andras on 08.12.2017.
 * JavaFX MainApplication
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainApplication extends Application {
    /**
     * ExecutorService with a fixed thread pool of 2 threads. The Threads are used for audio playback
     * and calls from the Shoutcast API
     */
    public static final ExecutorService executor = Executors.newFixedThreadPool(2);

        @Override
        public void start(Stage primaryStage) throws Exception{
            Parent root = FXMLLoader.load(getClass().getResource("RadioStationUI.fxml"));
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });

            primaryStage.setScene(new Scene(root, 839, 551));
            primaryStage.setTitle("Radio Player");
            primaryStage.getIcons().add(new Image("file:pic/icon.png"));
            primaryStage.setResizable(false);

            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
}