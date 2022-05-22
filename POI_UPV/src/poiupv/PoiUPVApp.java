/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author kalez
 */
public class PoiUPVApp extends Application {
    
    
    @Override
    public void start(Stage Intro) throws Exception {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/Intro/Intro.fxml"));
        Parent root = myLoader.load();
        
        Scene scene = new Scene(root);
        Intro.setTitle("Curso De Navegación");
        Intro.setScene(scene);
        Intro.setMinHeight(440);
        Intro.setMinWidth(720);
        Intro.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
