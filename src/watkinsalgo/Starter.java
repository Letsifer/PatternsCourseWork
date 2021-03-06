/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package watkinsalgo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Евгений
 */
public class Starter extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/watkinsalgo/Starter.fxml"));

            Scene scene = new Scene(fXMLLoader.load());

            primaryStage.setTitle("Интервальный алгоритм");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
