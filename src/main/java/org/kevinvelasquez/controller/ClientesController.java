
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Clientes;

/**
 *
 * @author Kevin
 */
public class ClientesController implements Initializable {
   @FXML
    private TableView<Clientes> tablaClientes;
    @FXML
    private TableColumn colIDCliente, colNombreCliente, colApellidoCliente, 
            colTelefonoCliente, colDireccionCliente, colEmailCliente, colFechaRegistro;

    @FXML
    private DatePicker dpFechaRegistro;

    @FXML
    private TextField txtBuscar, txtIDCliente, txtNombreCliente, txtApellidoCliente, 
            txtTelefonoCliente, txtDireccionCliente, txtEmailCliente;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Clientes> listaClientes;
    private Clientes modeloCliente;

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
        cargarTablaClientes();
        tablaClientes.setOnMouseClicked(eh -> cargarClienteEnTextField());
    }

    public void configurarColumnas() {
        colIDCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("idCliente"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombreCliente"));
        colApellidoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidoCliente"));
        colTelefonoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefonoCliente"));
        colDireccionCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccionCliente"));
        colEmailCliente.setCellValueFactory(new PropertyValueFactory<Clientes, String>("emailCliente"));
        colFechaRegistro.setCellValueFactory(new PropertyValueFactory<Clientes, LocalDate>("fechaRegistro"));
    }

    private ArrayList<Clientes> listarClientes() {
        ArrayList<Clientes> clientes = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarClientes();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                clientes.add(new Clientes(
                        resultado.getInt("ID"),
                        resultado.getString("NOMBRE"),
                        resultado.getString("APELLIDO"),
                        resultado.getString("TELEFONO"),
                        resultado.getString("DIRECCION"),
                        resultado.getString("EMAIL"),
                        resultado.getDate("FECHA_REGISTRO") != null 
                        ? resultado.getDate("FECHA_REGISTRO").toLocalDate() : null
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar clientes: " + ex.getMessage());
            ex.printStackTrace();
        }
        return clientes;
    }

    private void cargarClienteEnTextField() {
        Clientes clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            txtIDCliente.setText(String.valueOf(clienteSeleccionado.getIdCliente()));
            txtNombreCliente.setText(clienteSeleccionado.getNombreCliente());
            txtApellidoCliente.setText(clienteSeleccionado.getApellidoCliente());
            txtTelefonoCliente.setText(clienteSeleccionado.getTelefonoCliente());
            txtDireccionCliente.setText(clienteSeleccionado.getDireccionCliente());
            txtEmailCliente.setText(clienteSeleccionado.getEmailCliente());
            dpFechaRegistro.setValue(clienteSeleccionado.getFechaRegistro());
        }
    }

    private void cargarTablaClientes() {
        listaClientes = FXCollections.observableArrayList(listarClientes());
        tablaClientes.setItems(listaClientes);
        if (!listaClientes.isEmpty()) {
            tablaClientes.getSelectionModel().selectFirst();
            cargarClienteEnTextField();
        }
    }

    private Clientes obtenerModeloCliente() {
        int idCliente = txtIDCliente.getText().isEmpty() ? 0 : Integer.parseInt(txtIDCliente.getText());
        String nombre = txtNombreCliente.getText();
        String apellido = txtApellidoCliente.getText();
        String telefono = txtTelefonoCliente.getText();
        String direccion = txtDireccionCliente.getText();
        String email = txtEmailCliente.getText();
        LocalDate fechaRegistro = dpFechaRegistro.getValue();

        return new Clientes(
                idCliente,
                nombre,
                apellido,
                telefono,
                direccion,
                email,
                fechaRegistro
        );
    }

    private void insertarCliente() {
        modeloCliente = obtenerModeloCliente();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarCliente(?,?,?,?,?,?);");
            enunciado.setString(1, modeloCliente.getNombreCliente());
            enunciado.setString(2, modeloCliente.getApellidoCliente());
            enunciado.setString(3, modeloCliente.getTelefonoCliente());
            enunciado.setString(4, modeloCliente.getDireccionCliente());
            enunciado.setString(5, modeloCliente.getEmailCliente());
            enunciado.setDate(6, modeloCliente.getFechaRegistro() != null 
                    ? Date.valueOf(modeloCliente.getFechaRegistro()) : null);
            enunciado.execute();
            cargarTablaClientes();
        } catch (SQLException ex) {
            System.out.println("Error al agregar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarCliente() {
        modeloCliente = obtenerModeloCliente();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarCliente(?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloCliente.getIdCliente());
            enunciado.setString(2, modeloCliente.getNombreCliente());
            enunciado.setString(3, modeloCliente.getApellidoCliente());
            enunciado.setString(4, modeloCliente.getTelefonoCliente());
            enunciado.setString(5, modeloCliente.getDireccionCliente());
            enunciado.setString(6, modeloCliente.getEmailCliente());
            enunciado.setDate(7, modeloCliente.getFechaRegistro() != null 
                    ? Date.valueOf(modeloCliente.getFechaRegistro()) : null);
            enunciado.execute();
            cargarTablaClientes();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarCliente() {
        modeloCliente = tablaClientes.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarCliente(?);");
            enunciado.setInt(1, modeloCliente.getIdCliente());
            enunciado.execute();
            cargarTablaClientes();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar cliente: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDCliente.clear();
        txtNombreCliente.clear();
        txtApellidoCliente.clear();
        txtTelefonoCliente.clear();
        txtDireccionCliente.clear();
        txtEmailCliente.clear();
        dpFechaRegistro.setValue(null);
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtNombreCliente.setDisable(!activo);
        txtApellidoCliente.setDisable(!activo);
        txtTelefonoCliente.setDisable(!activo);
        txtDireccionCliente.setDisable(!activo);
        txtEmailCliente.setDisable(!activo);
        dpFechaRegistro.setDisable(!activo);

        tablaClientes.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaClientes.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaClientes.getSelectionModel().select(indice - 1);
            cargarClienteEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaClientes.getSelectionModel().getSelectedIndex();
        if (indice < listaClientes.size() - 1) {
            tablaClientes.getSelectionModel().select(indice + 1);
            cargarClienteEnTextField();
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
                insertarCliente();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarCliente();
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
            eliminarCliente();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarClienteEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarClienteEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarCliente();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarCliente();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarCliente() {
        String nombre = txtBuscar.getText().toLowerCase();
        ArrayList<Clientes> resultadoBusqueda = new ArrayList<>();
        for (Clientes c : listaClientes) {
            if (c.getNombreCliente().toLowerCase().contains(nombre) || 
                c.getApellidoCliente().toLowerCase().contains(nombre)) {
                resultadoBusqueda.add(c);
            }
        }
        tablaClientes.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaClientes.getSelectionModel().selectFirst();
        }
    }   
}
