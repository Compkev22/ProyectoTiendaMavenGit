/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kevinvelasquez.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class GestionController implements Initializable {
    private Main principal;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    /**
     * Initializes the controller class.
     */
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
    
    public void escenaPaginaPrincipal(){
        principal.menu();
    }
    
    public void escenaProductosVentas(){
        principal.escenaProductosVentas();
    }
    
    public void escenaProductos(){
     principal.escenaProductos();
    }
    
    public void escenaFacturas(){
        principal.escenaFacturas();
    }
    
    @FXML
    public void verificarCredenciales() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Debe ingresar usuario y contraseña.");
            return;
        }

        Connection conn = null;
        PreparedStatement enunciado = null;
        ResultSet rs = null;

        try {
            conn = Conexion.getInstancia().getConexion();
            String sql = "select * from RegistroAdministradores where nombreAdministrador = ? and contraseñaAdministrador = ?";
            enunciado = conn.prepareStatement(sql);
            enunciado.setString(1, usuario);
            enunciado.setString(2, contrasena);
            rs = enunciado.executeQuery();

            if (rs.next()) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Inicio de sesión", "Usuario y contraseña correctos.");
                escenaProductos();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Acceso denegado", "Usuario o contraseña incorrectos.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de conexión", "No se pudo conectar con la base de datos.");
        } finally {
            try {
                if (rs != null) rs.close();
                if (enunciado != null) enunciado.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
