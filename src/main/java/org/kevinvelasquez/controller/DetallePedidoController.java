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
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.DetallePedido;
import org.kevinvelasquez.model.Pedidos;
import org.kevinvelasquez.model.Producto;

/**
 *
 * @author Kevin
 */
public class DetallePedidoController implements Initializable {

    @FXML
    private TableView<DetallePedido> tablaDetallePedido;
    @FXML
    private TableColumn colIDDetalle, colIDPedido, colIDProducto, colCantidad, colPrecioUnitario;

    @FXML
    private TextField txtBuscar, txtIDDetalle, txtPrecioUnitario;

    @FXML
    private ComboBox<Pedidos> cbxPedidos;
    @FXML
    private ComboBox<Producto> cbxProductos;

    @FXML
    private Spinner<Integer> spCantidad;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<DetallePedido> listaDetallePedido;
    private DetallePedido modeloDetallePedido;

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
        configurarSpinner();
        cargarTablaDetallePedido();
        cargarPedidos();
        cargarProductos();
        tablaDetallePedido.setOnMouseClicked(eh -> cargarDetalleEnTextField());
    }

    public void configurarColumnas() {
        colIDDetalle.setCellValueFactory(new PropertyValueFactory<DetallePedido, Integer>("idDetalle"));
        colIDPedido.setCellValueFactory(new PropertyValueFactory<DetallePedido, Integer>("idPedido"));
        colIDProducto.setCellValueFactory(new PropertyValueFactory<DetallePedido, Integer>("idProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetallePedido, Integer>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<DetallePedido, Double>("precioUnitario"));
    }

    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valoresEnteros
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spCantidad.setValueFactory(valoresEnteros);
    }

    private ArrayList<DetallePedido> listarDetallePedido() {
        ArrayList<DetallePedido> detalles = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarDetallesPedido();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                detalles.add(new DetallePedido(
                        resultado.getInt("idDetalle"),
                        resultado.getInt("idPedido"),
                        resultado.getInt("idProducto"),
                        resultado.getInt("cantidad"),
                        resultado.getDouble("precioUnitario")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar detalles de pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
        return detalles;
    }

    private ArrayList<Pedidos> listarPedidos() {
        ArrayList<Pedidos> pedidos = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarPedidos();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                pedidos.add(new Pedidos(
                        resultado.getInt("ID"),
                        resultado.getInt("ID_CLIENTE"),
                        resultado.getTimestamp("FECHA_PEDIDO").toLocalDateTime(),
                        resultado.getString("ESTADO_PEDIDO"),
                        resultado.getString("ESTADO_PAGO"),
                        resultado.getString("METODO_PAGO"),
                        resultado.getString("TIPO_ENTREGA"),
                        resultado.getDouble("TOTAL"),
                        resultado.getDouble("DESCUENTO"),
                        resultado.getInt("TIEMPO_ESTIMADO")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar pedidos: " + ex.getMessage());
            ex.printStackTrace();
        }
        return pedidos;
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

    private void cargarPedidos() {
        ObservableList<Pedidos> listaPedidos = FXCollections.observableArrayList(listarPedidos());
        cbxPedidos.setItems(listaPedidos);
    }

    private void cargarProductos() {
        ObservableList<Producto> listaProductos = FXCollections.observableArrayList(listarProductos());
        cbxProductos.setItems(listaProductos);
    }

    private void cargarDetalleEnTextField() {
        DetallePedido detalleSeleccionado = tablaDetallePedido.getSelectionModel().getSelectedItem();
        if (detalleSeleccionado != null) {
            txtIDDetalle.setText(String.valueOf(detalleSeleccionado.getIdDetalle()));

            // Seleccionar el pedido correspondiente
            for (Pedidos p : cbxPedidos.getItems()) {
                if (p.getIdPedido() == detalleSeleccionado.getIdPedido()) {
                    cbxPedidos.setValue(p);
                    break;
                }
            }

            // Seleccionar el producto correspondiente
            for (Producto pr : cbxProductos.getItems()) {
                if (pr.getIdProducto() == detalleSeleccionado.getIdProducto()) {
                    cbxProductos.setValue(pr);
                    break;
                }
            }

            spCantidad.getValueFactory().setValue(detalleSeleccionado.getCantidad());
            txtPrecioUnitario.setText(String.valueOf(detalleSeleccionado.getPrecioUnitario()));
        }
    }

    private void cargarTablaDetallePedido() {
        listaDetallePedido = FXCollections.observableArrayList(listarDetallePedido());
        tablaDetallePedido.setItems(listaDetallePedido);
        if (!listaDetallePedido.isEmpty()) {
            tablaDetallePedido.getSelectionModel().selectFirst();
            cargarDetalleEnTextField();
        }
    }

    private DetallePedido obtenerModeloDetallePedido() {
        int idDetalle = txtIDDetalle.getText().isEmpty() ? 0 : Integer.parseInt(txtIDDetalle.getText());
        Pedidos pedidoSeleccionado = cbxPedidos.getSelectionModel().getSelectedItem();
        Producto productoSeleccionado = cbxProductos.getSelectionModel().getSelectedItem();
        int cantidad = spCantidad.getValue();
        double precioUnitario = txtPrecioUnitario.getText().isEmpty() ? 0 : Double.parseDouble(txtPrecioUnitario.getText());

        return new DetallePedido(
                idDetalle,
                pedidoSeleccionado != null ? pedidoSeleccionado.getIdPedido() : 0,
                productoSeleccionado != null ? productoSeleccionado.getIdProducto() : 0,
                cantidad,
                precioUnitario
        );
    }

    private void insertarDetallePedido() {
        modeloDetallePedido = obtenerModeloDetallePedido();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarDetallePedido(?,?,?,?);");
            enunciado.setInt(1, modeloDetallePedido.getIdPedido());
            enunciado.setInt(2, modeloDetallePedido.getIdProducto());
            enunciado.setInt(3, modeloDetallePedido.getCantidad());
            enunciado.setDouble(4, modeloDetallePedido.getPrecioUnitario());
            enunciado.execute();
            cargarTablaDetallePedido();
        } catch (SQLException ex) {
            System.out.println("Error al agregar detalle de pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarDetallePedido() {
        modeloDetallePedido = obtenerModeloDetallePedido();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarDetallePedido(?,?,?,?,?);");
            enunciado.setInt(1, modeloDetallePedido.getIdDetalle());
            enunciado.setInt(2, modeloDetallePedido.getIdPedido());
            enunciado.setInt(3, modeloDetallePedido.getIdProducto());
            enunciado.setInt(4, modeloDetallePedido.getCantidad());
            enunciado.setDouble(5, modeloDetallePedido.getPrecioUnitario());
            enunciado.execute();
            cargarTablaDetallePedido();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar detalle de pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarDetallePedido() {
        modeloDetallePedido = tablaDetallePedido.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarDetallePedido(?);");
            enunciado.setInt(1, modeloDetallePedido.getIdDetalle());
            enunciado.execute();
            cargarTablaDetallePedido();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar detalle de pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDDetalle.clear();
        cbxPedidos.getSelectionModel().clearSelection();
        cbxProductos.getSelectionModel().clearSelection();
        spCantidad.getValueFactory().setValue(1);
        txtPrecioUnitario.clear();
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        cbxPedidos.setDisable(!activo);
        cbxProductos.setDisable(!activo);
        spCantidad.setDisable(!activo);
        txtPrecioUnitario.setDisable(!activo);

        tablaDetallePedido.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaDetallePedido.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaDetallePedido.getSelectionModel().select(indice - 1);
            cargarDetalleEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaDetallePedido.getSelectionModel().getSelectedIndex();
        if (indice < listaDetallePedido.size() - 1) {
            tablaDetallePedido.getSelectionModel().select(indice + 1);
            cargarDetalleEnTextField();
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
                insertarDetallePedido();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarDetallePedido();
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
            eliminarDetallePedido();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarDetalleEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarDetalleEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarDetallePedido();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarDetallePedido();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarDetallePedido() {
        String busqueda = txtBuscar.getText().toLowerCase();
        ArrayList<DetallePedido> resultadoBusqueda = new ArrayList<>();
        for (DetallePedido dp : listaDetallePedido) {
            if (String.valueOf(dp.getIdDetalle()).contains(busqueda)
                    || String.valueOf(dp.getIdPedido()).contains(busqueda)
                    || String.valueOf(dp.getIdProducto()).contains(busqueda)) {
                resultadoBusqueda.add(dp);
            }
        }
        tablaDetallePedido.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaDetallePedido.getSelectionModel().selectFirst();
        }
    }
}
