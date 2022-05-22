/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Intro;

import DBAccess.NavegacionDAOException;
import Registro.RegistroController;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Navegacion;
import static model.Navegacion.getSingletonNavegacion;
import model.User;
import poiupv.FXML_mapaController;
import poiupv.Utils;

/**
 * FXML Controller class
 *
 * @author kalez
 */
public class IntroController implements Initializable {

    @FXML //Cuadro de texto para introducir usuario
    private PasswordField passwordTextLogin;
    @FXML //Botón usuario
    private TextField userTextLogin;
    @FXML //Botón registrarse
    private Button iniciarBtnLogin;
    @FXML //Texto usuario
    private Button registrarseBtnMain;
    @FXML
    private Text userLabel;
    @FXML
    private Text passwordLabel;
    @FXML
    private Label userTextLoginWarning;
    @FXML
    private Label passwordTextLoginWarning;
    private Navegacion navigation;
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try { // USE A TRY-CATCH IN ORDER TO SOLVE THE NavegacionDAOException ERROR
            navigation = getSingletonNavegacion();
        } catch (NavegacionDAOException ex) {
            Logger.getLogger(RegistroController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //INIT LISTENERS FOR RESETTING STYLE ON TEXT FIELDS
        userTextLogin.textProperty().addListener((o, oldValue, newValue) -> {
            userTextLoginWarning.visibleProperty().setValue(Boolean.FALSE);
            userTextLogin.setStyle("-fx-border-color: default");
        });
        passwordTextLogin.textProperty().addListener((o, oldValue, newValue) -> {
            passwordTextLoginWarning.visibleProperty().setValue(Boolean.FALSE);
            passwordTextLogin.setStyle("-fx-border-color: default");
        });
    }

    @FXML
    private void handleOnActionIniciarBtnLogin(ActionEvent event) throws IOException {
        String user = userTextLogin.textProperty().getValueSafe();
        String pass = passwordTextLogin.textProperty().getValueSafe();
        
        //CHECK IF USERNAME EXIST
        if(navigation.exitsNickName(user)){
            User userObj = navigation.loginUser(user, pass); //obtain the object<User>}

            //IF USER EXIST BUT PASS IS NOT CORRECT
            if(Objects.isNull(userObj)){ //because if password doesn't match with nickname, loginUser returns null
                
                passwordTextLogin.setStyle("-fx-border-color: red");
                passwordTextLoginWarning.visibleProperty().setValue(Boolean.TRUE);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User not identified");
                alert.setContentText("Password incorrect for account: " + user);
                alert.showAndWait();
                
            //IF USER EXIST AND PASS IS CORRECT, LOG IN AND UPLOAD THE MAP
            } else if(userObj.checkCredentials(user, pass)){
                
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/FXML_mapa.fxml"));
                Parent root = myLoader.load();
                FXML_mapaController controller = myLoader.<FXML_mapaController>getController();
                controller.initUser(user, userObj, userObj.getAvatar());
                Scene scene = new Scene(root);
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Mapa");
                stage.show();
            }
            
        } else { //IF USERNAME DOES NOT EXIST
            
            userTextLogin.setStyle("-fx-border-color: red");
            passwordTextLogin.setStyle("-fx-border-color: red");
            userTextLoginWarning.visibleProperty().setValue(Boolean.TRUE);
            passwordTextLoginWarning.visibleProperty().setValue(Boolean.TRUE);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("User not identified");
            alert.setContentText("Username or password incorrect");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleOnActionRegistrarseBtnMain(ActionEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/Registro/Registro.fxml"));
        Parent root = myLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Registro");
        stage.show();
    }
    
}
