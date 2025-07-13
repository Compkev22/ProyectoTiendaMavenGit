package org.kevinvelasquez.system;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;
/**
 *
 * @author Kevin
 */
public class Main extends Application {

    public static void main(String[] args) {
//        System.out.println("Hello World!");
//        
//        JSONObject persona = new JSONObject();
//        
//        persona.put("nombre: ", "Kevin");
//        persona.put("apellido: ", "Velasquez");
//        persona.put("edad: ", "17");
//        persona.put("valido: ", "true");
//        
//        System.out.println("Contenido de JSON: ");
//        System.out.println(persona.toString(4));
        launch(args);
    }

    @Override
    public void start(Stage escenario) throws Exception {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/view/PruebaClase5View.fxml"));
        
        Parent raiz = cargador.load();
        
        Scene escena = new Scene(raiz);
        
        escenario.setScene(escena);
        escenario.show();
    }
}
