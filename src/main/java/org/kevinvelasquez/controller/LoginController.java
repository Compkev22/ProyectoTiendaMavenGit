/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kevinvelasquez.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.system.Main;

/**
 * FXML Controller class
 *
 * @author Emilio
 */
public class LoginController implements Initializable {
    private Main principal;   
    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContraseña;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void escenaMenuPrincipal(){
        principal.menu();
    }
    
    public void escenaPaginaInicio(){
        principal.escenaInicio();
    }
    
    public void escenaRegistrarse(){
        principal.escenaRegistrar();
    }
    
    @FXML
    public void verificarCredenciales() {
        String usuario = txtNombreUsuario.getText();
        String contraseña = txtContraseña.getText();

        if (usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor ingresa usuario y contraseña.");
            return;
        }

        try {
            Connection conn = Conexion.getInstancia().getConexion();
            String query = "select * from RegistroUsuarios where nombreUsuario = ? and contraseñaUsuario = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                mostrarAlerta("Acceso Concedido", "Usuario y Contraseña Correctos.");
                escenaMenuPrincipal();
            } else {
                mostrarAlerta("Acceso Denegado", "Usuario o Contraseña Incorrectos.");
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Error al intentar conectar con la base de datos.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

