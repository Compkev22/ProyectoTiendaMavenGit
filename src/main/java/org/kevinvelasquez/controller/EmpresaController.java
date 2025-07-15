package org.kevinvelasquez.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.kevinvelasquez.system.Main;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Empresa;

/**
 *
 * @author Kevin
 */
public class EmpresaController implements Initializable {

    @FXML
    private TableView<Empresa> tablaEmpresa;
    @FXML
    private TableColumn colIDEmpresa, colNombre, colDireccion, colTelefono, colHorario;

    @FXML
    private TextField txtBuscar, txtIDEmpresa, txtNombre, txtDireccion, txtTelefono, txtHorario;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Empresa> listaEmpresas;
    private Empresa modeloEmpresa;

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

    public void escenaPaginaProductos() {
        principal.escenaProductos();
    }

    public void escenaPaginaClientes() {
        principal.escenaClientes();
    }

    public void escenaPaginaCategorias() {
        principal.escenaCategorias();
    }

    public void escenaPaginaPedidos() {
        principal.escenaPedidos();
    }

    public void escenaPaginaDetallePedido() {
        principal.escenaDetallePedido();
    }

    public void escenaPaginaGarantias() {
        principal.escenaGarantias();
    }

    public void escenaPaginaContacto() {
        principal.escenaContacto();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTablaEmpresa();
        tablaEmpresa.setOnMouseClicked(eh -> cargarEmpresaEnTextField());
    }

    public void configurarColumnas() {
        colIDEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("idEmpresa"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
        colHorario.setCellValueFactory(new PropertyValueFactory<Empresa, String>("horario"));
    }

    private ArrayList<Empresa> listarEmpresas() {
        ArrayList<Empresa> empresas = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarEmpresas();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                empresas.add(new Empresa(
                        resultado.getInt("ID"),
                        resultado.getString("NOMBRE"),
                        resultado.getString("DIRECCION"),
                        resultado.getString("TELEFONO"),
                        resultado.getString("HORARIO")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar empresas: " + ex.getMessage());
            ex.printStackTrace();
        }
        return empresas;
    }

    private void cargarEmpresaEnTextField() {
        Empresa empresaSeleccionada = tablaEmpresa.getSelectionModel().getSelectedItem();
        if (empresaSeleccionada != null) {
            txtIDEmpresa.setText(String.valueOf(empresaSeleccionada.getIdEmpresa()));
            txtNombre.setText(empresaSeleccionada.getNombre());
            txtDireccion.setText(empresaSeleccionada.getDireccion());
            txtTelefono.setText(empresaSeleccionada.getTelefono());
            txtHorario.setText(empresaSeleccionada.getHorario());
        }
    }

    private void cargarTablaEmpresa() {
        listaEmpresas = FXCollections.observableArrayList(listarEmpresas());
        tablaEmpresa.setItems(listaEmpresas);
        if (!listaEmpresas.isEmpty()) {
            tablaEmpresa.getSelectionModel().selectFirst();
            cargarEmpresaEnTextField();
        }
    }

    private Empresa obtenerModeloEmpresa() {
        int idEmpresa = txtIDEmpresa.getText().isEmpty() ? 0 : Integer.parseInt(txtIDEmpresa.getText());
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();
        String horario = txtHorario.getText();

        return new Empresa(
                idEmpresa,
                nombre,
                direccion,
                telefono,
                horario
        );
    }

    private void insertarEmpresa() {
        modeloEmpresa = obtenerModeloEmpresa();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarEmpresa(?,?,?,?);");
            enunciado.setString(1, modeloEmpresa.getNombre());
            enunciado.setString(2, modeloEmpresa.getDireccion());
            enunciado.setString(3, modeloEmpresa.getTelefono());
            enunciado.setString(4, modeloEmpresa.getHorario());
            enunciado.execute();
            cargarTablaEmpresa();
        } catch (SQLException ex) {
            System.out.println("Error al agregar empresa: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarEmpresa() {
        modeloEmpresa = obtenerModeloEmpresa();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarEmpresa(?,?,?,?,?);");
            enunciado.setInt(1, modeloEmpresa.getIdEmpresa());
            enunciado.setString(2, modeloEmpresa.getNombre());
            enunciado.setString(3, modeloEmpresa.getDireccion());
            enunciado.setString(4, modeloEmpresa.getTelefono());
            enunciado.setString(5, modeloEmpresa.getHorario());
            enunciado.execute();
            cargarTablaEmpresa();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar empresa: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarEmpresa() {
        modeloEmpresa = tablaEmpresa.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarEmpresa(?);");
            enunciado.setInt(1, modeloEmpresa.getIdEmpresa());
            enunciado.execute();
            cargarTablaEmpresa();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar empresa: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDEmpresa.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtHorario.clear();
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtNombre.setDisable(!activo);
        txtDireccion.setDisable(!activo);
        txtTelefono.setDisable(!activo);
        txtHorario.setDisable(!activo);

        tablaEmpresa.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaEmpresa.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaEmpresa.getSelectionModel().select(indice - 1);
            cargarEmpresaEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaEmpresa.getSelectionModel().getSelectedIndex();
        if (indice < listaEmpresas.size() - 1) {
            tablaEmpresa.getSelectionModel().select(indice + 1);
            cargarEmpresaEnTextField();
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
                insertarEmpresa();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarEmpresa();
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
            eliminarEmpresa();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarEmpresaEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarEmpresaEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarEmpresa();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarEmpresa();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarEmpresa() {
        String busqueda = txtBuscar.getText().toLowerCase();
        ArrayList<Empresa> resultadoBusqueda = new ArrayList<>();
        for (Empresa e : listaEmpresas) {
            if (e.getNombre().toLowerCase().contains(busqueda)
                    || e.getDireccion().toLowerCase().contains(busqueda)
                    || e.getTelefono().toLowerCase().contains(busqueda)) {
                resultadoBusqueda.add(e);
            }
        }
        tablaEmpresa.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaEmpresa.getSelectionModel().selectFirst();
        }
    }
}
