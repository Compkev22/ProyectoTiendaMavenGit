/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Producto;
import org.kevinvelasquez.system.Main;

/**
 *
 * @author Kevin
 */
public class ProductosController implements Initializable {

    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn colIDProducto, colNombre, colDescripcion, colPrecioActual,
            colPrecioAnterior, colDescuento, colCategoria, colCodigoBarra,
            colUltimaActualizacion, colStock;

    @FXML
    private TextField txtBuscar, txtIDProducto, txtNombre, txtPrecioActual,
            txtPrecioAnterior, txtDescuento, txtCategoria, txtCodigoBarra, txtStock;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private DatePicker dpUltimaActualizacion;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Producto> listaProductos;
    private Producto modeloProducto;

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

    public void escenaPaginaClientes() {
        principal.escenaClientes();
    }

    public void escenaPaginaPedidos() {
        principal.escenaPedidos();
    }

    public void escenaPaginaDetallePedido() {
        principal.escenaDetallePedido();
    }

    public void escenaPaginaCategorias() {
        principal.escenaCategorias();
    }

    public void escenaPaginaGarantias() {
        principal.escenaGarantias();
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
        cargarTablaProductos();
        tablaProductos.setOnMouseClicked(eh -> cargarProductoEnTextField());
    }

    public void configurarColumnas() {
        colIDProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
        colPrecioActual.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioActual"));
        colPrecioAnterior.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioAnterior"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<Producto, String>("descuentoProducto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("categoriaProducto"));
        colCodigoBarra.setCellValueFactory(new PropertyValueFactory<Producto, String>("codigoBarra"));
        colUltimaActualizacion.setCellValueFactory(new PropertyValueFactory<Producto, LocalDate>("ultimaActualizacion"));
        colStock.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("stock"));
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
                        resultado.getDate("ULTIMA_ACTUALIZACION") != null
                        ? resultado.getDate("ULTIMA_ACTUALIZACION").toLocalDate() : null,
                        resultado.getInt("STOCK")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar productos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return productos;
    }

    private void cargarProductoEnTextField() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            txtIDProducto.setText(String.valueOf(productoSeleccionado.getIdProducto()));
            txtNombre.setText(productoSeleccionado.getNombreProducto());
            txtDescripcion.setText(productoSeleccionado.getDescripcionProducto());
            txtPrecioActual.setText(String.valueOf(productoSeleccionado.getPrecioActual()));
            txtPrecioAnterior.setText(String.valueOf(productoSeleccionado.getPrecioAnterior()));
            txtDescuento.setText(productoSeleccionado.getDescuentoProducto());
            txtCategoria.setText(productoSeleccionado.getCategoriaProducto());
            txtCodigoBarra.setText(productoSeleccionado.getCodigoBarra());
            dpUltimaActualizacion.setValue(productoSeleccionado.getUltimaActualizacion());
            txtStock.setText(String.valueOf(productoSeleccionado.getStock()));
        }
    }

    private void cargarTablaProductos() {
        listaProductos = FXCollections.observableArrayList(listarProductos());
        tablaProductos.setItems(listaProductos);
        if (!listaProductos.isEmpty()) {
            tablaProductos.getSelectionModel().selectFirst();
            cargarProductoEnTextField();
        }
    }

    private Producto obtenerModeloProducto() {
        int idProducto = txtIDProducto.getText().isEmpty() ? 0 : Integer.parseInt(txtIDProducto.getText());
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        double precioActual = txtPrecioActual.getText().isEmpty() ? 0 : Double.parseDouble(txtPrecioActual.getText());
        double precioAnterior = txtPrecioAnterior.getText().isEmpty() ? 0 : Double.parseDouble(txtPrecioAnterior.getText());
        String descuento = txtDescuento.getText();
        String categoria = txtCategoria.getText();
        String codigoBarra = txtCodigoBarra.getText();
        LocalDate ultimaActualizacion = dpUltimaActualizacion.getValue();
        int stock = txtStock.getText().isEmpty() ? 0 : Integer.parseInt(txtStock.getText());

        return new Producto(
                idProducto,
                nombre,
                descripcion,
                precioActual,
                precioAnterior,
                descuento,
                categoria,
                codigoBarra,
                ultimaActualizacion,
                stock
        );
    }

    private void insertarProducto() {
        modeloProducto = obtenerModeloProducto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarProducto(?,?,?,?,?,?,?,?,?);");
            enunciado.setString(1, modeloProducto.getNombreProducto());
            enunciado.setString(2, modeloProducto.getDescripcionProducto());
            enunciado.setDouble(3, modeloProducto.getPrecioActual());
            enunciado.setDouble(4, modeloProducto.getPrecioAnterior());
            enunciado.setString(5, modeloProducto.getDescuentoProducto());
            enunciado.setString(6, modeloProducto.getCategoriaProducto());
            enunciado.setString(7, modeloProducto.getCodigoBarra());
            enunciado.setDate(8, modeloProducto.getUltimaActualizacion() != null
                    ? Date.valueOf(modeloProducto.getUltimaActualizacion()) : null);
            enunciado.setInt(9, modeloProducto.getStock());
            enunciado.execute();
            cargarTablaProductos();
        } catch (SQLException ex) {
            System.out.println("Error al agregar producto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarProducto() {
        modeloProducto = obtenerModeloProducto();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarProducto(?,?,?,?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloProducto.getIdProducto());
            enunciado.setString(2, modeloProducto.getNombreProducto());
            enunciado.setString(3, modeloProducto.getDescripcionProducto());
            enunciado.setDouble(4, modeloProducto.getPrecioActual());
            enunciado.setDouble(5, modeloProducto.getPrecioAnterior());
            enunciado.setString(6, modeloProducto.getDescuentoProducto());
            enunciado.setString(7, modeloProducto.getCategoriaProducto());
            enunciado.setString(8, modeloProducto.getCodigoBarra());
            enunciado.setDate(9, modeloProducto.getUltimaActualizacion() != null
                    ? Date.valueOf(modeloProducto.getUltimaActualizacion()) : null);
            enunciado.setInt(10, modeloProducto.getStock());
            enunciado.execute();
            cargarTablaProductos();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar producto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarProducto() {
        modeloProducto = tablaProductos.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarProducto(?);");
            enunciado.setInt(1, modeloProducto.getIdProducto());
            enunciado.execute();
            cargarTablaProductos();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar producto: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDProducto.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecioActual.clear();
        txtPrecioAnterior.clear();
        txtDescuento.clear();
        txtCategoria.clear();
        txtCodigoBarra.clear();
        dpUltimaActualizacion.setValue(null);
        txtStock.clear();
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtNombre.setDisable(!activo);
        txtDescripcion.setDisable(!activo);
        txtPrecioActual.setDisable(!activo);
        txtPrecioAnterior.setDisable(!activo);
        txtDescuento.setDisable(!activo);
        txtCategoria.setDisable(!activo);
        txtCodigoBarra.setDisable(!activo);
        dpUltimaActualizacion.setDisable(!activo);
        txtStock.setDisable(!activo);

        tablaProductos.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaProductos.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaProductos.getSelectionModel().select(indice - 1);
            cargarProductoEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaProductos.getSelectionModel().getSelectedIndex();
        if (indice < listaProductos.size() - 1) {
            tablaProductos.getSelectionModel().select(indice + 1);
            cargarProductoEnTextField();
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
                insertarProducto();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarProducto();
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
            eliminarProducto();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarProductoEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarProductoEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarProducto();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarProducto();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarProducto() {
        String busqueda = txtBuscar.getText().toLowerCase();
        ArrayList<Producto> resultadoBusqueda = new ArrayList<>();
        for (Producto p : listaProductos) {
            if (p.getNombreProducto().toLowerCase().contains(busqueda)
                    || p.getDescripcionProducto().toLowerCase().contains(busqueda)
                    || p.getCategoriaProducto().toLowerCase().contains(busqueda)
                    || p.getCodigoBarra().toLowerCase().contains(busqueda)) {
                resultadoBusqueda.add(p);
            }
        }
        tablaProductos.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaProductos.getSelectionModel().selectFirst();
        }
    }
}
