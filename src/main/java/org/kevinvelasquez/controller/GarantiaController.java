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
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Garantia;
import org.kevinvelasquez.model.Producto;

/**
 *
 * @author Kevin
 */
public class GarantiaController implements Initializable {

    @FXML
    private TableView<Garantia> tablaGarantias;
    @FXML
    private TableColumn colIDGarantia, colIDProducto, colDuracion;

    @FXML
    private TextField txtBuscar, txtIDGarantia, txtDuracion;

    @FXML
    private ComboBox<Producto> cbxProductos;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Garantia> listaGarantias;
    private Garantia modeloGarantia;

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

    public void escenaPaginaContacto() {
        principal.escenaContacto();
    }

    public void escenaPaginaEmpresa() {
        principal.escenaEmpresa();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTablaGarantias();
        cargarProductos();
        tablaGarantias.setOnMouseClicked(eh -> cargarGarantiaEnTextField());
    }

    public void configurarColumnas() {
        colIDGarantia.setCellValueFactory(new PropertyValueFactory<Garantia, Integer>("idGarantia"));
        colIDProducto.setCellValueFactory(new PropertyValueFactory<Garantia, Integer>("idProducto"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<Garantia, String>("duracion"));
    }

    private ArrayList<Garantia> listarGarantias() {
        ArrayList<Garantia> garantias = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarGarantias();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                garantias.add(new Garantia(
                        resultado.getInt("ID_GARANTIA"),
                        resultado.getInt("ID_PRODUCTO"),
                        resultado.getString("DURACION")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar garantías: " + ex.getMessage());
            ex.printStackTrace();
        }
        return garantias;
    }

    private ArrayList<Producto> listarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarProductos();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                productos.add(new Producto(
                        resultado.getInt("ID"),
                        resultado.getString("NOMBRE"),
                        resultado.getString("DESCRIPCION"),
                        resultado.getDouble("PRECIO_ACTUAL"),
                        resultado.getDouble("PRECIO_ANTERIOR"),
                        resultado.getString("DESCUENTO"),
                        resultado.getString("CATEGORIA"),
                        resultado.getString("CODIGO_BARRA"),
                        resultado.getDate("ULTIMA_ACTUALIZACION").toLocalDate(),
                        resultado.getInt("STOCK")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar productos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return productos;
    }

    private void cargarProductos() {
        ObservableList<Producto> listaProductos = FXCollections.observableArrayList(listarProductos());
        cbxProductos.setItems(listaProductos);
    }

    private void cargarGarantiaEnTextField() {
        Garantia garantiaSeleccionada = tablaGarantias.getSelectionModel().getSelectedItem();
        if (garantiaSeleccionada != null) {
            txtIDGarantia.setText(String.valueOf(garantiaSeleccionada.getIdGarantia()));

            // Seleccionar el producto correspondiente
            for (Producto p : cbxProductos.getItems()) {
                if (p.getIdProducto() == garantiaSeleccionada.getIdProducto()) {
                    cbxProductos.setValue(p);
                    break;
                }
            }

            txtDuracion.setText(garantiaSeleccionada.getDuracion());
        }
    }

    private void cargarTablaGarantias() {
        listaGarantias = FXCollections.observableArrayList(listarGarantias());
        tablaGarantias.setItems(listaGarantias);
        if (!listaGarantias.isEmpty()) {
            tablaGarantias.getSelectionModel().selectFirst();
            cargarGarantiaEnTextField();
        }
    }

    private Garantia obtenerModeloGarantia() {
        int idGarantia = txtIDGarantia.getText().isEmpty() ? 0 : Integer.parseInt(txtIDGarantia.getText());
        Producto productoSeleccionado = cbxProductos.getSelectionModel().getSelectedItem();
        String duracion = txtDuracion.getText();

        return new Garantia(
                idGarantia,
                productoSeleccionado != null ? productoSeleccionado.getIdProducto() : 0,
                duracion
        );
    }

    private void insertarGarantia() {
        modeloGarantia = obtenerModeloGarantia();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarGarantia(?,?);");
            enunciado.setInt(1, modeloGarantia.getIdProducto());
            enunciado.setString(2, modeloGarantia.getDuracion());
            enunciado.execute();
            cargarTablaGarantias();
        } catch (SQLException ex) {
            System.out.println("Error al agregar garantía: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarGarantia() {
        modeloGarantia = obtenerModeloGarantia();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarGarantia(?,?,?);");
            enunciado.setInt(1, modeloGarantia.getIdGarantia());
            enunciado.setInt(2, modeloGarantia.getIdProducto());
            enunciado.setString(3, modeloGarantia.getDuracion());
            enunciado.execute();
            cargarTablaGarantias();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar garantía: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarGarantia() {
        modeloGarantia = tablaGarantias.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarGarantia(?);");
            enunciado.setInt(1, modeloGarantia.getIdGarantia());
            enunciado.execute();
            cargarTablaGarantias();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar garantía: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDGarantia.clear();
        cbxProductos.getSelectionModel().clearSelection();
        txtDuracion.clear();
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        cbxProductos.setDisable(!activo);
        txtDuracion.setDisable(!activo);

        tablaGarantias.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaGarantias.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaGarantias.getSelectionModel().select(indice - 1);
            cargarGarantiaEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaGarantias.getSelectionModel().getSelectedIndex();
        if (indice < listaGarantias.size() - 1) {
            tablaGarantias.getSelectionModel().select(indice + 1);
            cargarGarantiaEnTextField();
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
                insertarGarantia();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarGarantia();
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
            eliminarGarantia();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarGarantiaEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarGarantiaEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarGarantia();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarGarantia();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarGarantia() {
        String busqueda = txtBuscar.getText().toLowerCase();
        ArrayList<Garantia> resultadoBusqueda = new ArrayList<>();
        for (Garantia g : listaGarantias) {
            if (String.valueOf(g.getIdGarantia()).contains(busqueda)
                    || String.valueOf(g.getIdProducto()).contains(busqueda)
                    || g.getDuracion().toLowerCase().contains(busqueda)) {
                resultadoBusqueda.add(g);
            }
        }
        tablaGarantias.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaGarantias.getSelectionModel().selectFirst();
        }
    }
}
