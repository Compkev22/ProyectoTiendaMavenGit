/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kevinvelasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.kevinvelasquez.system.Main;

/**
 *
 * @author Kevin
 */
public class MenuPrincipalController implements Initializable  {
  
    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    } 
    
    public void escenaPaginaInicio() {
        principal.escenaInicio();
    }
    
    public void escenaPaginaLogin() {
        principal.escenaLogin();
    }
    
    public void escenaPaginaProductos() {
        principal.escenaProductos();
    }
    
    public void escenaPaginaClientes() {
        principal.escenaClientes();
    }
    
    public void escenaPaginaPedidos() {
        principal.escenaPedidos();
    }
    
    public void escenaPaginaDetallePedido() {
        principal.escenaDetallePedido();
    }
    
    public void escenaPaginaCategorias() {
        principal.escenaCategorias();
    }
    
    public void escenaPaginaGarantias() {
        principal.escenaGarantias();
    }
    
    public void escenaPaginaContacto() {
        principal.escenaContacto();
    }
    
    public void escenaPaginaEmpresa() {
        principal.escenaEmpresa();
    }
}
