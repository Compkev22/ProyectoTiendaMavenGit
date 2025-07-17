/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kevinvelasquez.model;

/**
 *
 * @author Emilio
 */
import java.time.LocalDate;

public class Factura {
    private int idFactura;
    private int idPedido;
    private int idCliente;
    private LocalDate fechaFactura;
    private double total;
    private double impuestos;
    private String metodoPago;
    private String estadoFactura;
    private String numeroFactura;

    public Factura() {
    }

    public Factura(int idFactura, int idPedido, int idCliente, LocalDate fechaFactura, double total, double impuestos, String metodoPago, String estadoFactura, String numeroFactura) {
        this.idFactura = idFactura;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fechaFactura = fechaFactura;
        this.total = total;
        this.impuestos = impuestos;
        this.metodoPago = metodoPago;
        this.estadoFactura = estadoFactura;
        this.numeroFactura = numeroFactura;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(LocalDate fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    
}