/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Registro;

import DBAccess.NavegacionDAOException;
import java.awt.Color;
import java.awt.Paint;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Navegacion;
import DBAccess.NavegacionDAO;
import DBAccess.NavegacionDAOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import static model.Navegacion.getSingletonNavegacion;
import model.User;
import poiupv.FXML_mapaController;
import poiupv.Utils;

/**
 * FXML Controller class
 *
 * @author kalez
 */
public class RegistroController implements Initializable {
    
    @FXML //Botón seleccionar avatar
    private Button avatarBtn;
    @FXML
    private TextField userTextRegister;
    @FXML //cuadro de etxto para introducir contraseña
    private TextField passwordTextRegister;
    @FXML //Cuador de texto para introducir email
    private TextField emailTextRegister;
    @FXML //Seleccionador de fecha de nacimiento
    private DatePicker birthTextRegister;
    @FXML //Botón enviar
    private Button enviarBtnRegister;
    @FXML //Botón borrar
    private Button borrarBtnRegister;
    @FXML
    private Text userLabel;
    @FXML
    private Text passwordLabel;
    @FXML
    private Text birthLabel;
    @FXML
    private Text emailLabel;
    @FXML
    private Label userTextRegisterWarning;
    @FXML
    private Label passwordTextRegisterWarning;
    @FXML
    private Label emailTextRegisterWarning;
    @FXML
    private Label birthTextRegisterWarning;
    @FXML
    private Text volverInicioTextRegister;
    private Navegacion navigation;
    @FXML
    private ImageView avatarImage;

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
        
        //add Listener for checking userTextRegister
        userTextRegister.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String userValue = userTextRegister.textProperty().getValueSafe();
            if(!newValue){ // if focus is taken off
                if(!Utils.checkUser(userValue, navigation)){
                    userTextRegisterWarning.visibleProperty().set(true);
                    userTextRegister.setStyle("-fx-border-color: red");
                } else {
                    userTextRegisterWarning.visibleProperty().set(false);
                    userTextRegister.setStyle("-fx-border-color: default");
                }}
        });
        //add Listener for checking passwordTextRegister
        passwordTextRegister.focusedProperty().addListener((observable, oldValue, newValue) -> {
            String passValue = passwordTextRegister.textProperty().getValueSafe();
            if(!newValue){ // if focus is taken off
                if(!Utils.checkPassword(passValue)){
                    passwordTextRegisterWarning.visibleProperty().setValue(Boolean.TRUE);
                    passwordTextRegister.setStyle("-fx-border-color: red");
                } else {
                    passwordTextRegisterWarning.visibleProperty().setValue(Boolean.FALSE);
                    passwordTextRegister.setStyle("-fx-border-color: default");
                }}
        });
        //add Listener for checking emailTextRegister
        emailTextRegister.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){ // if focus is taken off
                if(!Utils.checkEmail(emailTextRegister.textProperty().getValueSafe())){
                    emailTextRegisterWarning.visibleProperty().setValue(Boolean.TRUE);
                    emailTextRegister.setStyle("-fx-border-color: red");
                } else {
                    emailTextRegisterWarning.visibleProperty().setValue(Boolean.FALSE);
                    emailTextRegister.setStyle("-fx-border-color: default");
                }}
        });
        //add Listener for checking birthTextRegister
        birthTextRegister.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){ // if focus is taken off
                if(!Utils.checkBirthDate(birthTextRegister.valueProperty().getValue())){
                    birthTextRegisterWarning.visibleProperty().setValue(Boolean.TRUE);
                    birthTextRegister.setStyle("-fx-border-color: red");
                } else {
                    birthTextRegisterWarning.visibleProperty().setValue(Boolean.FALSE);
                    birthTextRegister.setStyle("-fx-border-color: default");
                }}
        });
        
    }    

    @FXML
    private void handleOnActionEnviarBtnRegister(ActionEvent event) throws IOException, NavegacionDAOException {
        String userValue = userTextRegister.textProperty().getValueSafe();
        String passValue = passwordTextRegister.textProperty().getValueSafe();
        String email = emailTextRegister.textProperty().getValueSafe();
        LocalDate date = birthTextRegister.valueProperty().getValue();
        Image avatar = avatarImage.imageProperty().getValue();
        
        // check if input datas are accepted by the system
        //IF DATA CORRECT -> OPEN ALERT FOR CONFIRMATION
        if(Utils.checkUser(userValue, navigation) && Utils.checkPassword(passValue)
                && Utils.checkBirthDate(date) && Utils.checkEmail(email)){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText("¿Estás seguro?");
            alert.setContentText("Por favor, revisa los datos antes de pulsar OK");
            Optional<ButtonType> result = alert.showAndWait(); // result of interaction with alert (OK/Cancel)
            if(result.isPresent() && result.get() == ButtonType.OK){ // if OK is pressed
                //REGISTER USER IN DB
                User newUser = navigation.registerUser(userValue, email, passValue, date);
                //CREATE NEW SCENE WITH MAP
                FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/poiupv/FXML_mapa.fxml")); // open the map
                Parent root = myLoader.load();
                FXML_mapaController controller = myLoader.<FXML_mapaController>getController();
                controller.initUser(userValue, newUser, avatar);
                Scene scene = new Scene(root);
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Mapa");
                stage.show();
            }
        } else { // IF DATA NOT CORRECT -> OPEN ALERT FOR ERROR
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incorrect format for input data");
            alert.setContentText("Check incorrect datas before submitting");
            alert.showAndWait();
        }
        
    }
    @FXML //reset all datas in the form
    private void handleOnActionBorrarBtnRegister(ActionEvent event) {
        userTextRegister.clear();
        passwordTextRegister.clear();
        emailTextRegister.clear();
        birthTextRegister.valueProperty().setValue(null);
        userTextRegisterWarning.visibleProperty().setValue(Boolean.FALSE);
        passwordTextRegisterWarning.visibleProperty().setValue(Boolean.FALSE);
        emailTextRegisterWarning.visibleProperty().setValue(Boolean.FALSE);
        birthTextRegisterWarning.visibleProperty().setValue(Boolean.FALSE);
        userTextRegister.setStyle("-fx-border-color: default");
        passwordTextRegister.setStyle("-fx-border-color: default");
        emailTextRegister.setStyle("-fx-border-color: default");
        birthTextRegister.setStyle("-fx-border-color: default");
    }

    @FXML
    private void handleOnActionAvatarBtn(ActionEvent event) throws FileNotFoundException {
        //we can't set the initialDirectory because with the change of the terminal, the path will be compromised
        FileChooser file = new FileChooser();
        file.setTitle("Abrir fichero");
        file.getExtensionFilters().addAll(
            new ExtensionFilter("Images", "*.png", "*.jpg", "*.gif"));
        File selectedFile = file.showOpenDialog(
                ((Node) event.getSource()).getScene().getWindow());
        String url = selectedFile.getAbsolutePath();
        Image avatar = new Image(new FileInputStream(url));
        avatarImage.imageProperty().setValue(avatar);
    }
    
    @FXML //Link for getting back on the main page
    private void handleOnMouseClickedVolverInicioText(MouseEvent event) throws IOException {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/Intro/Intro.fxml"));
        Parent root = myLoader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Intro");
        stage.show();
    }
}
    

