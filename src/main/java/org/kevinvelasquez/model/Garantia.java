/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kevinvelasquez.model;

/**
 *
 * @author Emilio
 */
public class Garantia {
    private int idGarantia;
    private int idProducto;
    private String duracion;

    public Garantia() {}

    public Garantia(int idGarantia, int idProducto, String duracion) {
        this.idGarantia = idGarantia;
        this.idProducto = idProducto;
        this.duracion = duracion;
    }

    public int getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(int idGarantia) {
        this.idGarantia = idGarantia;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    
    
}
