
package org.kevinvelasquez.model;

import java.time.LocalDateTime;

/**
 *
 * @author Kevin
 */
public class Pedidos {
    private int idPedido;
    private int idCliente;
    private LocalDateTime fechaPedido;
    private String estadoPedido;
    private String estadoPago;
    private String metodoPago;
    private String tipoEntrega;
    private double total;
    private double descuento;
    private int tiempoEstimado;

    public Pedidos() {}

    public Pedidos(int idPedido, int idCliente, LocalDateTime fechaPedido,
                  String estadoPedido, String estadoPago, String metodoPago,
                  String tipoEntrega, double total, double descuento,
                  int tiempoEstimado) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.estadoPago = estadoPago;
        this.metodoPago = metodoPago;
        this.tipoEntrega = tipoEntrega;
        this.total = total;
        this.descuento = descuento;
        this.tiempoEstimado = tiempoEstimado;
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

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }
    
        
}
