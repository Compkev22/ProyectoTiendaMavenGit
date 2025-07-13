
package org.kevinvelasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.kevinvelasquez.system.Main;

/**
 *
 * @author Kevin
 */
public class LoginController implements Initializable {
    @FXML
    private TextField txtUsuario;
    
    @FXML
    private PasswordField pswContraseña;
    
    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }

    public void escenaVolverPaginaInicio() {
        principal.inicio();
    }

    public void escenaRegistrarse() {
        principal.registrar();
    }

    public void escenaIngresar() {
        principal.menu();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String contraseña = pswContraseña.getText();
        
        
        final String UsuarioValido = "1";
        final String ContraseñaValida = "1";
        
        if (UsuarioValido.equals(usuario) && ContraseñaValida.equals(contraseña)) {
            mostrarAlerta("Inicio de sesion exitoso", "Bienvenido al sistema",
                    Alert.AlertType.INFORMATION);
            principal.menu();
        } else {
            mostrarAlerta("Credenciales incorrectas", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
            pswContraseña.clear();
            txtUsuario.requestFocus();
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    @FXML
    private void salir() {
        System.exit(0);
    }
  
}
