/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kevinvelasquez.controller;

import com.mysql.jdbc.CallableStatement;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Usuarios;
import org.kevinvelasquez.system.Main;

/**
 * FXML Controller class
 *
 * @author Emilio
 */
public class RegistrarseController implements Initializable {
    private Main principal;   
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContraseña;

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void escenaInicioSesion(){
        principal.escenaLogin();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public boolean registrarUsuario(Usuarios nuevoUsuario) {
        boolean exito = false;
        try {
            Connection conexion = (Connection) Conexion.getInstancia().getConexion();
            CallableStatement enunciado = (CallableStatement) conexion.prepareCall("{call sp_AgregarUsuario(?, ?, ?, ?)}");
            enunciado.setString(1, nuevoUsuario.getNombreRealUsuario());
            enunciado.setString(2, nuevoUsuario.getApellidoUsuario());
            enunciado.setString(3, nuevoUsuario.getNombreUsuario());
            enunciado.setString(4, nuevoUsuario.getContraseñaUsuario());

            enunciado.execute();
            exito = true;
            enunciado.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exito;
    }
    
    @FXML
    public void registrarNuevoUsuario() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String usuario = txtUsuario.getText();
        String contraseña = txtContraseña.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
            mostrarAlerta("Campos Vacíos", "Por favor, completa todos los campos.");
            return;
        }

        Usuarios nuevo = new Usuarios(nombre, apellido, usuario, contraseña);
        boolean resultado = registrarUsuario(nuevo);

        if (resultado) {
            mostrarAlerta("Registro Exitoso", "El usuario ha sido registrado.");
            escenaInicioSesion();
        } else {
            mostrarAlerta("Error", "No se pudo registrar el usuario.");
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
