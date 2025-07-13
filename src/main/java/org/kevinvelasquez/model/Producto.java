
package org.kevinvelasquez.model;

import java.time.LocalDate;

/**
 *
 * @author Kevin
 */
public class Producto {
    private int idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioActual;
    private double precioAnterior;
    private String descuentoProducto;
    private String categoriaProducto;
    private String codigoBarra;
    private LocalDate ultimaActualizacion;
    private int stock;

    // Constructor
    public Producto(int idProducto, String nombreProducto, String descripcionProducto, double precioActual,
                    double precioAnterior, String descuentoProducto, String categoriaProducto,
                    String codigoBarra, LocalDate ultimaActualizacion, int stock) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioActual = precioActual;
        this.precioAnterior = precioAnterior;
        this.descuentoProducto = descuentoProducto;
        this.categoriaProducto = categoriaProducto;
        this.codigoBarra = codigoBarra;
        this.ultimaActualizacion = ultimaActualizacion;
        this.stock = stock;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public double getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(double precioActual) {
        this.precioActual = precioActual;
    }

    public double getPrecioAnterior() {
        return precioAnterior;
    }

    public void setPrecioAnterior(double precioAnterior) {
        this.precioAnterior = precioAnterior;
    }

    public String getDescuentoProducto() {
        return descuentoProducto;
    }

    public void setDescuentoProducto(String descuentoProducto) {
        this.descuentoProducto = descuentoProducto;
    }

    public String getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(String categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public LocalDate getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDate ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
        
}
