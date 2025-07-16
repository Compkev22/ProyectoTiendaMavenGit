package org.kevinvelasquez.model;

/**
 *
 * @author Kevin
 */
public class Categorias {
    private int idCategoria;
    private String nombreCategoria;
    private String descripcionCategoria;
    private String tipoCategoria;

    public Categorias() {}

    public Categorias(int idCategoria, String nombreCategoria,
        String descripcionCategoria, String tipoCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
        this.tipoCategoria = tipoCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public String getTipoCategoria() {
        return tipoCategoria;
    }

    public void setTipoCategoria(String tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
    }
    
    @Override
    public String toString() {
        return nombreCategoria;
    }    
}
