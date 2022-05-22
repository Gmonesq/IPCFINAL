/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;
import poiupv.Poi;

/**
 *
 * @author jsoler
 */
public class FXML_mapaController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;
    
   
    @FXML
    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private ColorPicker pencilColor;
    @FXML
    private Slider sizePencil;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private MenuItem cerrarFicheroMenu;
    @FXML
    private MenuItem borrarMenu;
    @FXML
    private MenuItem acercaMenu;
    @FXML
    private Label coordinatesLabel;
    @FXML
    private ToggleButton pointerBtn;
    @FXML
    private ToggleButton pencilBtn;
    @FXML
    private ToggleButton eraserBtn;
    private Toggle aux;
    @FXML
    private ToggleButton lineBtn;
    @FXML
    private ToggleButton arcBtn;
    @FXML
    private ToggleButton addTextBtn;
    @FXML
    private ToggleButton protractorBtn;
    @FXML
    private ToggleButton coordinatesBtn;
    @FXML
    private ToggleButton latBtn;
    @FXML
    private ToggleButton bucketBtn;
    @FXML
    private ToggleGroup btn;
    @FXML
    private Text infoLabel;
    @FXML
    private Text userTextMapa;
    private TextField t;
    private Text coor;
    private User userObj;
    @FXML
    private ImageView profileImage;
    @FXML
    private Pane zoom;
   
    private Line linePainting;
    
    private Line lineH;
    
    private Line lineV;
    private Line auxL;
    
    private Circle cir;
     private Circle circlePainting;
     
    private Paint paint= Color.BLACK;
    
    @FXML
    private ImageView tran;
    private double Arc; 
            
    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la posicion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la posicion del POI
        double pinW = map_pin.getBoundsInLocal().getWidth();
        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
        
    }

    private void initData() {
        hm.put("2F", new Poi("2F", "Edificion del DSIC", 325, 225));
        hm.put("Agora", new Poi("Agora", "Agora", 600, 360));
        map_listview.getItems().add(hm.get("2F"));
        map_listview.getItems().add(hm.get("Agora"));
    }
    
    public void initUser(String user, User userObjecto, Image avatar){
        userTextMapa.textProperty().setValue(user);
        userObj = userObjecto;
        profileImage.imageProperty().setValue(avatar);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
        
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
         zoomGroup.getChildren().add(tran);
        
        

    }

    @FXML
    private void muestraPosicion(MouseEvent event) {
        coordinatesLabel.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    @FXML
    private void handleOnActionCerrarAplicacion(ActionEvent event) {
        ((Stage)zoom_slider.getScene().getWindow()).close();
    }


    @FXML
    private void handleOnActionAcercaMenu(ActionEvent event) { // Help -> About Item
        Alert mensaje= new Alert(Alert.AlertType.INFORMATION);
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2022");
        mensaje.showAndWait();
    }

    @FXML
    private void handleOnActionBorrarMenu(ActionEvent event) {
        zoomGroup.getChildren().removeIf(Circle.class::isInstance);
        zoomGroup.getChildren().removeIf(TextField.class::isInstance);
        zoomGroup.getChildren().removeIf(Line.class::isInstance);
        zoomGroup.getChildren().removeIf(Text.class::isInstance);
    }

    @FXML
    private void handleOnActionPointerBtn(ActionEvent event) {
         btn.selectToggle(pointerBtn);
         aux=btn.getSelectedToggle();
        
    }

    @FXML
    private void handleOnActionPencilBtn(ActionEvent event) {
        btn.selectToggle(pencilBtn);
        aux=btn.getSelectedToggle();
    }

    @FXML
    private void handleOnActionEraserBtn(ActionEvent event) {
        btn.selectToggle(eraserBtn);
        aux=btn.getSelectedToggle();
        
        
    }

    @FXML
    private void handleOnActionPencilColor(ActionEvent event) {
       paint= pencilColor.getValue();
    }

    @FXML
    private void handleOnActionLineBtn(ActionEvent event) {
        btn.selectToggle(lineBtn);
        aux=btn.getSelectedToggle();
    }

    @FXML
    private void handleOnActionArcBtn(ActionEvent event) {
        btn.selectToggle(arcBtn);
        aux=btn.getSelectedToggle();
    }

    @FXML
    private void handleOnActionAddTextBtn(ActionEvent event) {
        btn.selectToggle(addTextBtn);
        aux=btn.getSelectedToggle();
    }

    @FXML
    private void handleOnActionProtractorBtn(ActionEvent event){
        aux=btn.getSelectedToggle();
        tran.setVisible(true);
        tran.setOpacity(0.5);
        tran.setX(100);
        tran.setY(200);
        if(!protractorBtn.isSelected()){
            tran.setVisible(false);
           
        }
        
    }

    @FXML
    private void handleOnActionCoordinatesBtn(ActionEvent event) {
        btn.selectToggle(coordinatesBtn);
        
        aux=btn.getSelectedToggle();
    }

    @FXML
    private void handleOnActionModificarPerfil(ActionEvent event) {
    }

    @FXML
    private void handleOnActionLogOut(ActionEvent event) {
    }

    @FXML
    private void handleOnActionReleaseMouse(MouseEvent event) {
    }

    @FXML
    private void handleOnActionClickedMouse(MouseEvent event) {
    }

    @FXML
    private void handleOnActionPressMouse(MouseEvent event) {
        
    }

    @FXML
    private void handleOnActionMousePressed(MouseEvent event) {
        aux=btn.getSelectedToggle();
        if(lineBtn.isSelected()){
            linePainting = new Line(event.getX(),event.getY(),event.getX(),event.getY());
            linePainting.setStroke(paint);
            linePainting.setStrokeWidth(sizePencil.getValue());
                linePainting.setOnMouseClicked(e ->  {
                if(eraserBtn.isSelected()){
                   zoomGroup.getChildren().remove(e.getTarget()); //.setVisible(false);
                }
                else if(bucketBtn.isSelected()){
                    this.linePainting.setStroke(paint);//(e.getTarget());//e.getTarget().setStyle(--) 
                    
                }});
            zoomGroup.getChildren().add(linePainting);
            
        }
        else if (pencilBtn.isSelected()){
            
            cir = new Circle ();
            cir.setCenterX(event.getX());
            cir.setCenterY(event.getY());
            cir.setRadius(sizePencil.getValue());
            cir.setFill(paint);
            cir.setOnMouseClicked(e ->  {
                if(eraserBtn.isSelected()){
                   zoomGroup.getChildren().remove(e.getTarget()); //.setVisible(false);
                }
                else if(bucketBtn.isSelected()){
                    cir.setFill(paint);
                }});
            zoomGroup.getChildren().add(cir);
        }
       else if (addTextBtn.isSelected()){
            t = new TextField();
            t.setTranslateX(event.getX()-60);
            t.setTranslateY(event.getY()-10);
            t.setOnMouseClicked(e ->  {
                if(eraserBtn.isSelected()){
                   zoomGroup.getChildren().remove(e.getTarget()); //.setVisible(false);
                }
                else if(bucketBtn.isSelected()){
                    //t.setStyle("-fx-fill" + paint.toString());
                }});
            zoomGroup.getChildren().add(t);
            
        }
       else if (coordinatesBtn.isSelected()){
           coor= new Text(((int)event.getX())+","+((int)event.getY()));
           coor.setX((int)event.getX());
           coor.setY((int)event.getY());
           coor.setOnMouseClicked(e ->  {
                if(eraserBtn.isSelected()){
                   zoomGroup.getChildren().remove(e.getTarget()); //.setVisible(false);
                }
                else if(bucketBtn.isSelected()){
                    //coor.setStyle("-fx-fill" + paint.toString());
                }});
           zoomGroup.getChildren().add(coor);
       }
       else if (protractorBtn.isSelected()){
            tran.setX(event.getX());
            tran.setY(event.getY());
        }
        else if (latBtn.isSelected()){
           lineV= new Line(event.getX(),0,event.getX(),3204);
           lineH= new Line(0,event.getY(),4945,event.getY());
           lineV.setStroke(paint);
           lineV.setStrokeWidth(sizePencil.getValue());
                lineV.setOnMouseClicked(e ->  {
                if(eraserBtn.isSelected()){
                   zoomGroup.getChildren().remove(e.getTarget()); //.setVisible(false);
                }
                else if(bucketBtn.isSelected()){
                    
                }});
            zoomGroup.getChildren().add(lineV);
           lineH.setStroke(paint);
           lineH.setStrokeWidth(sizePencil.getValue());
                lineH.setOnMouseClicked(e ->  {
                if(eraserBtn.isSelected()){
                   zoomGroup.getChildren().remove(e.getTarget()); //.setVisible(false);
                }
                else if(bucketBtn.isSelected()){
                    this.lineH.setStroke(paint);
                    this.lineV.setStroke(paint);
                }});
            zoomGroup.getChildren().add(lineH);
        }
        else if (arcBtn.isSelected()){
            circlePainting = new Circle(sizePencil.getValue());
             circlePainting.setStrokeWidth(sizePencil.getValue());
            circlePainting.setStroke(paint);
            circlePainting.setFill(Color.TRANSPARENT);
            
            circlePainting.setCenterX(event.getX());
            circlePainting.setCenterY(event.getY());
            Arc = event.getX();
            circlePainting.setOnMouseClicked(e ->  {
            if(eraserBtn.isSelected()){
                               zoomGroup.getChildren().remove(e.getTarget()); //.setVisible(false);
                            }
                            else if(bucketBtn.isSelected()){
                                this.circlePainting.setStroke(paint);
                            }
            });
          
                        zoomGroup.getChildren().add(circlePainting);
        }


        
       if(!pointerBtn.isSelected()||protractorBtn.isSelected()){
            map_scrollpane.setPannable(false);
        }
         else{
             map_scrollpane.setPannable(true);
        }
       
        
    }
    @FXML
     private void handleOnActionDraggedMouse(MouseEvent event) {
        if(lineBtn.isSelected()){
        linePainting.setEndX(event.getX());
        linePainting.setEndY(event.getY());
        }
        else if (arcBtn.isSelected()){
            double radio = Math.abs(event.getX()-Arc);
            circlePainting.setRadius(radio);
            event.consume();
       }
        
         
    }

    @FXML
    private void trandragged(MouseEvent event) {
        if(pointerBtn.isSelected()){
        tran.setX(event.getX()-140);
        tran.setY(event.getY()-140);}
        else {
        handleOnActionDraggedMouse(event);
        }
    }


    @FXML
    private void handleOnActionLatBtn(ActionEvent event) {
        btn.selectToggle(latBtn);
        
        aux=btn.getSelectedToggle();
    }

    @FXML
    private void handleOnActionBucketBtn(ActionEvent event) {
        btn.selectToggle(bucketBtn);
        
        aux=btn.getSelectedToggle();
    }

}
