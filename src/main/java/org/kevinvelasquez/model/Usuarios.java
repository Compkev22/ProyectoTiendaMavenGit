/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kevinvelasquez.model;

/**
 *
 * @author Emilio
 */
public class Usuarios {
    private int idUsuario;
    private String nombreRealUsuario;
    private String apellidoUsuario;
    private String nombreUsuario;
    private String contraseñaUsuario;

    public Usuarios(int idUsuario, String nombreRealUsuario, String apellidoUsuario, String nombreUsuario, String contraseñaUsuario) {
        this.idUsuario = idUsuario;
        this.nombreRealUsuario = nombreRealUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contraseñaUsuario = contraseñaUsuario;
    }

    public Usuarios(String nombreRealUsuario, String apellidoUsuario, String nombreUsuario, String contraseñaUsuario) {
        this.nombreRealUsuario = nombreRealUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contraseñaUsuario = contraseñaUsuario;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreRealUsuario() {
        return nombreRealUsuario;
    }

    public void setNombreRealUsuario(String nombreRealUsuario) {
        this.nombreRealUsuario = nombreRealUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseñaUsuario() {
        return contraseñaUsuario;
    }

    public void setContraseñaUsuario(String contraseñaUsuario) {
        this.contraseñaUsuario = contraseñaUsuario;
    }
    
    
}
