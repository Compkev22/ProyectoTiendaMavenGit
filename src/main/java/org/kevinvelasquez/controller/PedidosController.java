
package org.kevinvelasquez.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.kevinvelasquez.system.Main;
import java.sql.Timestamp;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Pedidos;
import org.kevinvelasquez.model.Clientes;

/**
 *
 * @author Kevin
 */
public class PedidosController implements Initializable {
    @FXML
    private TableView<Pedidos> tablaPedidos;
    @FXML
    private TableColumn colIDPedido, colIDCliente, colFechaPedido, colEstadoPedido,
            colEstadoPago, colMetodoPago, colTipoEntrega, colTotal, colDescuento, colTiempoEstimado;

    @FXML
    private DatePicker dpFechaPedido;
    @FXML
    private ComboBox<String> cbxEstadoPedido, cbxEstadoPago, cbxMetodoPago, cbxTipoEntrega;
    @FXML
    private ComboBox<Clientes> cbxClientes;

    @FXML
    private TextField txtBuscar, txtIDPedido, txtTotal, txtDescuento;

    @FXML
    private Spinner<Integer> spTiempoEstimado;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Pedidos> listaPedidos;
    private Pedidos modeloPedido;

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
        configurarSpinner();
        cargarComboboxes();
        cargarTablaPedidos();
        tablaPedidos.setOnMouseClicked(eh -> cargarPedidoEnTextField());
    }

    public void configurarColumnas() {
        colIDPedido.setCellValueFactory(new PropertyValueFactory<Pedidos, Integer>("idPedido"));
        colIDCliente.setCellValueFactory(new PropertyValueFactory<Pedidos, Integer>("idCliente"));
        colFechaPedido.setCellValueFactory(new PropertyValueFactory<Pedidos, LocalDateTime>("fechaPedido"));
        colEstadoPedido.setCellValueFactory(new PropertyValueFactory<Pedidos, String>("estadoPedido"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<Pedidos, String>("estadoPago"));
        colMetodoPago.setCellValueFactory(new PropertyValueFactory<Pedidos, String>("metodoPago"));
        colTipoEntrega.setCellValueFactory(new PropertyValueFactory<Pedidos, String>("tipoEntrega"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Pedidos, Double>("total"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<Pedidos, Double>("descuento"));
        colTiempoEstimado.setCellValueFactory(new PropertyValueFactory<Pedidos, Integer>("tiempoEstimado"));
    }

    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valoresEnteros
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1440, 30, 5);
        spTiempoEstimado.setValueFactory(valoresEnteros);
    }

    private void cargarComboboxes() {
        // Estados predefinidos
        cbxEstadoPedido.getItems().addAll("Pendiente", "En proceso", "Enviado", "Entregado", "Cancelado");
        cbxEstadoPago.getItems().addAll("Pendiente", "Pagado", "Reembolsado", "Cancelado");
        cbxMetodoPago.getItems().addAll("Efectivo", "Tarjeta de crédito", "Tarjeta de débito", "Transferencia", "PayPal");
        cbxTipoEntrega.getItems().addAll("Domicilio", "Recogida en tienda", "Punto de entrega");
        
        // Cargar clientes
        cargarClientes();
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
                        resultado.getDate("FECHA_REGISTRO").toLocalDate()
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar clientes: " + ex.getMessage());
            ex.printStackTrace();
        }
        return clientes;
    }

    private void cargarClientes() {
        ObservableList<Clientes> listaClientes = FXCollections.observableArrayList(listarClientes());
        cbxClientes.setItems(listaClientes);
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

    private void cargarPedidoEnTextField() {
        Pedidos pedidoSeleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (pedidoSeleccionado != null) {
            txtIDPedido.setText(String.valueOf(pedidoSeleccionado.getIdPedido()));
            
            // Seleccionar el cliente correspondiente
            for (Clientes c : cbxClientes.getItems()) {
                if (c.getIdCliente() == pedidoSeleccionado.getIdCliente()) {
                    cbxClientes.setValue(c);
                    break;
                }
            }
            
            dpFechaPedido.setValue(pedidoSeleccionado.getFechaPedido().toLocalDate());
            cbxEstadoPedido.setValue(pedidoSeleccionado.getEstadoPedido());
            cbxEstadoPago.setValue(pedidoSeleccionado.getEstadoPago());
            cbxMetodoPago.setValue(pedidoSeleccionado.getMetodoPago());
            cbxTipoEntrega.setValue(pedidoSeleccionado.getTipoEntrega());
            txtTotal.setText(String.valueOf(pedidoSeleccionado.getTotal()));
            txtDescuento.setText(String.valueOf(pedidoSeleccionado.getDescuento()));
            spTiempoEstimado.getValueFactory().setValue(pedidoSeleccionado.getTiempoEstimado());
        }
    }

    private void cargarTablaPedidos() {
        listaPedidos = FXCollections.observableArrayList(listarPedidos());
        tablaPedidos.setItems(listaPedidos);
        if (!listaPedidos.isEmpty()) {
            tablaPedidos.getSelectionModel().selectFirst();
            cargarPedidoEnTextField();
        }
    }

    private Pedidos obtenerModeloPedido() {
        int idPedido = txtIDPedido.getText().isEmpty() ? 0 : Integer.parseInt(txtIDPedido.getText());
        Clientes clienteSeleccionado = cbxClientes.getSelectionModel().getSelectedItem();
        LocalDateTime fechaPedido = dpFechaPedido.getValue() != null ? 
                LocalDateTime.of(dpFechaPedido.getValue(), LocalTime.now()) : null;
        String estadoPedido = cbxEstadoPedido.getValue();
        String estadoPago = cbxEstadoPago.getValue();
        String metodoPago = cbxMetodoPago.getValue();
        String tipoEntrega = cbxTipoEntrega.getValue();
        double total = txtTotal.getText().isEmpty() ? 0 : Double.parseDouble(txtTotal.getText());
        double descuento = txtDescuento.getText().isEmpty() ? 0 : Double.parseDouble(txtDescuento.getText());
        int tiempoEstimado = spTiempoEstimado.getValue();

        return new Pedidos(
                idPedido,
                clienteSeleccionado != null ? clienteSeleccionado.getIdCliente() : 0,
                fechaPedido,
                estadoPedido,
                estadoPago,
                metodoPago,
                tipoEntrega,
                total,
                descuento,
                tiempoEstimado
        );
    }

    private void insertarPedido() {
        modeloPedido = obtenerModeloPedido();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarPedido(?,?,?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloPedido.getIdCliente());
            enunciado.setString(2, modeloPedido.getEstadoPedido());
            enunciado.setString(3, modeloPedido.getEstadoPago());
            enunciado.setString(4, modeloPedido.getMetodoPago());
            enunciado.setString(5, modeloPedido.getTipoEntrega());
            enunciado.setDouble(6, modeloPedido.getTotal());
            enunciado.setDouble(7, modeloPedido.getDescuento());
            enunciado.setInt(8, modeloPedido.getTiempoEstimado());
            enunciado.setTimestamp(9, modeloPedido.getFechaPedido() != null ? 
                    Timestamp.valueOf(modeloPedido.getFechaPedido()) : null);
            enunciado.execute();
            cargarTablaPedidos();
        } catch (SQLException ex) {
            System.out.println("Error al agregar pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarPedido() {
        modeloPedido = obtenerModeloPedido();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarPedido(?,?,?,?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloPedido.getIdPedido());
            enunciado.setInt(2, modeloPedido.getIdCliente());
            enunciado.setString(3, modeloPedido.getEstadoPedido());
            enunciado.setString(4, modeloPedido.getEstadoPago());
            enunciado.setString(5, modeloPedido.getMetodoPago());
            enunciado.setString(6, modeloPedido.getTipoEntrega());
            enunciado.setDouble(7, modeloPedido.getTotal());
            enunciado.setDouble(8, modeloPedido.getDescuento());
            enunciado.setInt(9, modeloPedido.getTiempoEstimado());
            enunciado.setTimestamp(10, modeloPedido.getFechaPedido() != null ? 
                    Timestamp.valueOf(modeloPedido.getFechaPedido()) : null);
            enunciado.execute();
            cargarTablaPedidos();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarPedido() {
        modeloPedido = tablaPedidos.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarPedido(?);");
            enunciado.setInt(1, modeloPedido.getIdPedido());
            enunciado.execute();
            cargarTablaPedidos();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar pedido: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDPedido.clear();
        cbxClientes.getSelectionModel().clearSelection();
        dpFechaPedido.setValue(null);
        cbxEstadoPedido.getSelectionModel().clearSelection();
        cbxEstadoPago.getSelectionModel().clearSelection();
        cbxMetodoPago.getSelectionModel().clearSelection();
        cbxTipoEntrega.getSelectionModel().clearSelection();
        txtTotal.clear();
        txtDescuento.clear();
        spTiempoEstimado.getValueFactory().setValue(30);
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        cbxClientes.setDisable(!activo);
        dpFechaPedido.setDisable(!activo);
        cbxEstadoPedido.setDisable(!activo);
        cbxEstadoPago.setDisable(!activo);
        cbxMetodoPago.setDisable(!activo);
        cbxTipoEntrega.setDisable(!activo);
        txtTotal.setDisable(!activo);
        txtDescuento.setDisable(!activo);
        spTiempoEstimado.setDisable(!activo);

        tablaPedidos.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaPedidos.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaPedidos.getSelectionModel().select(indice - 1);
            cargarPedidoEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaPedidos.getSelectionModel().getSelectedIndex();
        if (indice < listaPedidos.size() - 1) {
            tablaPedidos.getSelectionModel().select(indice + 1);
            cargarPedidoEnTextField();
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
                insertarPedido();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarPedido();
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
            eliminarPedido();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarPedidoEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarPedidoEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarPedido();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarPedido();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarPedido() {
        String busqueda = txtBuscar.getText().toLowerCase();
        ArrayList<Pedidos> resultadoBusqueda = new ArrayList<>();
        for (Pedidos p : listaPedidos) {
            if (String.valueOf(p.getIdPedido()).contains(busqueda) ||
                String.valueOf(p.getIdCliente()).contains(busqueda) ||
                p.getEstadoPedido().toLowerCase().contains(busqueda) ||
                p.getMetodoPago().toLowerCase().contains(busqueda)) {
                resultadoBusqueda.add(p);
            }
        }
        tablaPedidos.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaPedidos.getSelectionModel().selectFirst();
        }
    }
}   

