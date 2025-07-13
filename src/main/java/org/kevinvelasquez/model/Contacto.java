
package org.kevinvelasquez.model;

import java.time.LocalDate;

/**
 *
 * @author Kevin
 */
public class Contacto {
    private int idContacto;
    private String nombre;
    private String email;
    private String mensaje;
    private LocalDate fechaEnvio;

    public Contacto() {}

    public Contacto(int idContacto, String nombre, String email, String mensaje, LocalDate fechaEnvio) {
        this.idContacto = idContacto;
        this.nombre = nombre;
        this.email = email;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    public int getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(int idContacto) {
        this.idContacto = idContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDate fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
        
}
