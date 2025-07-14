
package org.kevinvelasquez.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.kevinvelasquez.system.Main;
import java.sql.Date;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Contacto;

/**
 *
 * @author Kevin
 */
public class ContactoController implements Initializable {
 @FXML
    private TableView<Contacto> tablaContactos;
    @FXML
    private TableColumn colIDContacto, colNombre, colEmail, colMensaje, colFechaEnvio;

    @FXML
    private DatePicker dpFechaEnvio;

    @FXML
    private TextField txtBuscar, txtIDContacto, txtNombre, txtEmail;

    @FXML
    private TextArea txtMensaje;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Contacto> listaContactos;
    private Contacto modeloContacto;

    private enum EstadoFormulario {
        AGREGAR, ELIMINAR, ACTUALIZAR, NINGUNA
    }
    EstadoFormulario EstadoActual = EstadoFormulario.NINGUNA;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }

    public void escenaMenuPrincipal() {
        principal.menu();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTablaContactos();
        tablaContactos.setOnMouseClicked(eh -> cargarContactoEnTextField());
    }

    public void configurarColumnas() {
        colIDContacto.setCellValueFactory(new PropertyValueFactory<Contacto, Integer>("idContacto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Contacto, String>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Contacto, String>("email"));
        colMensaje.setCellValueFactory(new PropertyValueFactory<Contacto, String>("mensaje"));
        colFechaEnvio.setCellValueFactory(new PropertyValueFactory<Contacto, LocalDate>("fechaEnvio"));
    }

    private ArrayList<Contacto> listarContactos() {
        ArrayList<Contacto> contactos = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarContacto();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                contactos.add(new Contacto(
                        resultado.getInt("idContacto"),
                        resultado.getString("nombre"),
                        resultado.getString("email"),
                        resultado.getString("mensaje"),
                        resultado.getDate("fechaEnvio") != null 
                        ? resultado.getDate("fechaEnvio").toLocalDate() : null
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar contactos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return contactos;
    }

    private void cargarContactoEnTextField() {
        Contacto contactoSeleccionado = tablaContactos.getSelectionModel().getSelectedItem();
        if (contactoSeleccionado != null) {
            txtIDContacto.setText(String.valueOf(contactoSeleccionado.getIdContacto()));
            txtNombre.setText(contactoSeleccionado.getNombre());
            txtEmail.setText(contactoSeleccionado.getEmail());
            txtMensaje.setText(contactoSeleccionado.getMensaje());
            dpFechaEnvio.setValue(contactoSeleccionado.getFechaEnvio());
        }
    }

    private void cargarTablaContactos() {
        listaContactos = FXCollections.observableArrayList(listarContactos());
        tablaContactos.setItems(listaContactos);
        if (!listaContactos.isEmpty()) {
            tablaContactos.getSelectionModel().selectFirst();
            cargarContactoEnTextField();
        }
    }

    private Contacto obtenerModeloContacto() {
        int idContacto = txtIDContacto.getText().isEmpty() ? 0 : Integer.parseInt(txtIDContacto.getText());
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String mensaje = txtMensaje.getText();
        LocalDate fechaEnvio = dpFechaEnvio.getValue();

        return new Contacto(
                idContacto,
                nombre,
                email,
                mensaje,
                fechaEnvio
        );
    }

    private void insertarContacto() {
        modeloContacto = obtenerModeloContacto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarContacto(?,?,?,?);");
            enunciado.setString(1, modeloContacto.getNombre());
            enunciado.setString(2, modeloContacto.getEmail());
            enunciado.setString(3, modeloContacto.getMensaje());
            enunciado.setDate(4, modeloContacto.getFechaEnvio() != null 
                    ? Date.valueOf(modeloContacto.getFechaEnvio()) : null);
            enunciado.execute();
            cargarTablaContactos();
        } catch (SQLException ex) {
            System.out.println("Error al agregar contacto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarContacto() {
        modeloContacto = obtenerModeloContacto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EditarContacto(?,?,?,?,?);");
            enunciado.setInt(1, modeloContacto.getIdContacto());
            enunciado.setString(2, modeloContacto.getNombre());
            enunciado.setString(3, modeloContacto.getEmail());
            enunciado.setString(4, modeloContacto.getMensaje());
            enunciado.setDate(5, modeloContacto.getFechaEnvio() != null 
                    ? Date.valueOf(modeloContacto.getFechaEnvio()) : null);
            enunciado.execute();
            cargarTablaContactos();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar contacto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarContacto() {
        modeloContacto = tablaContactos.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarContacto(?);");
            enunciado.setInt(1, modeloContacto.getIdContacto());
            enunciado.execute();
            cargarTablaContactos();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar contacto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDContacto.clear();
        txtNombre.clear();
        txtEmail.clear();
        txtMensaje.clear();
        dpFechaEnvio.setValue(null);
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtNombre.setDisable(!activo);
        txtEmail.setDisable(!activo);
        txtMensaje.setDisable(!activo);
        dpFechaEnvio.setDisable(!activo);

        tablaContactos.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaContactos.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaContactos.getSelectionModel().select(indice - 1);
            cargarContactoEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaContactos.getSelectionModel().getSelectedIndex();
        if (indice < listaContactos.size() - 1) {
            tablaContactos.getSelectionModel().select(indice + 1);
            cargarContactoEnTextField();
        }
    }

    @FXML
    private void btnNuevoAction() {
        switch (EstadoActual) {
            case NINGUNA:
                limpiarCampos();
                actualizarEstadoFormulario(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                insertarContacto();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarContacto();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
        }
    }

    @FXML
    private void btnEditarAction() {
        actualizarEstadoFormulario(EstadoFormulario.ACTUALIZAR);
    }

    @FXML
    private void btnEliminarAction() {
        if (EstadoActual == EstadoFormulario.NINGUNA) {
            eliminarContacto();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarContactoEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarContactoEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarContacto();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarContacto();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarContacto() {
        String nombre = txtBuscar.getText().toLowerCase();
        ArrayList<Contacto> resultadoBusqueda = new ArrayList<>();
        for (Contacto c : listaContactos) {
            if (c.getNombre().toLowerCase().contains(nombre) || 
                c.getEmail().toLowerCase().contains(nombre)) {
                resultadoBusqueda.add(c);
            }
        }
        tablaContactos.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaContactos.getSelectionModel().selectFirst();
        }
    }   
        
}
