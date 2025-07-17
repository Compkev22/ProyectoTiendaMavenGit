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
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Factura;
import org.kevinvelasquez.model.Pedidos;
import org.kevinvelasquez.model.Clientes;

public class FacturasController implements Initializable {

    @FXML
    private TableView<Factura> tablaFacturas;
    @FXML
    private TableColumn colIDFactura, colIDPedido, colIDCliente, colFechaFactura,
            colTotal, colImpuestos, colMetodoPago, colEstadoFactura, colNumeroFactura;

    @FXML
    private TextField txtBuscar, txtIDFactura, txtTotal, txtImpuestos, txtNumeroFactura;

    @FXML
    private ComboBox<Pedidos> cbxPedidos;
    @FXML
    private ComboBox<Clientes> cbxClientes;
    @FXML
    private ComboBox<String> cbxMetodoPago;
    @FXML
    private ComboBox<String> cbxEstadoFactura;

    @FXML
    private DatePicker dpFechaFactura;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Factura> listaFacturas;
    private Factura modeloFactura;

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

    public void escenaPaginaPrincipal() {
        principal.menu();
    }

    public void escenaProductosVentas() {
        principal.escenaProductosVentas();
    }

    public void escenaProductos() {
        principal.escenaProductos();
    }

    public void escenaFacturas() {
        principal.escenaFacturas();
    }

    public void escenaGestion() {
        principal.escenaGestion();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTablaFacturas();
        cargarPedidos();
        cargarClientes();
        configurarComboboxes();
        tablaFacturas.setOnMouseClicked(eh -> cargarFacturaEnTextField());

        // Configurar DatePicker con la fecha actual por defecto
        dpFechaFactura.setValue(LocalDate.now());
    }

    public void configurarColumnas() {
        colIDFactura.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("idFactura"));
        colIDPedido.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("idPedido"));
        colIDCliente.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("idCliente"));
        colFechaFactura.setCellValueFactory(new PropertyValueFactory<Factura, LocalDate>("fechaFactura"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Double>("total"));
        colImpuestos.setCellValueFactory(new PropertyValueFactory<Factura, Double>("impuestos"));
        colMetodoPago.setCellValueFactory(new PropertyValueFactory<Factura, String>("metodoPago"));
        colEstadoFactura.setCellValueFactory(new PropertyValueFactory<Factura, String>("estadoFactura"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<Factura, String>("numeroFactura"));
    }

    private void configurarComboboxes() {
        // Configurar métodos de pago
        ObservableList<String> metodosPago = FXCollections.observableArrayList();
        metodosPago.add("Tarjeta");
        metodosPago.add("Efectivo");
        cbxMetodoPago.setItems(metodosPago);

        // Configurar estados de factura
        ObservableList<String> estadosFactura = FXCollections.observableArrayList();
        estadosFactura.add("Pendiente");
        estadosFactura.add("Pagada");
        estadosFactura.add("Anulada");
        cbxEstadoFactura.setItems(estadosFactura);
    }

    private ArrayList<Factura> listarFacturas() {
        ArrayList<Factura> facturas = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarFacturas();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                facturas.add(new Factura(
                        resultado.getInt("idFactura"),
                        resultado.getInt("idPedido"),
                        resultado.getInt("idCliente"),
                        resultado.getDate("fechaFactura").toLocalDate(),
                        resultado.getDouble("totalFactura"),
                        resultado.getDouble("impuestos"),
                        resultado.getString("metodoPago"),
                        resultado.getString("estadoFactura"),
                        resultado.getString("numeroFactura")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar facturas: " + ex.getMessage());
            ex.printStackTrace();
        }
        return facturas;
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
                        resultado.getString("CORREO"),
                        resultado.getDate("FECHA_REGISTRO").toLocalDate()
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar clientes: " + ex.getMessage());
            ex.printStackTrace();
        }
        return clientes;
    }

    private void cargarPedidos() {
        ObservableList<Pedidos> listaPedidos = FXCollections.observableArrayList(listarPedidos());
        cbxPedidos.setItems(listaPedidos);
    }

    private void cargarClientes() {
        ObservableList<Clientes> listaClientes = FXCollections.observableArrayList(listarClientes());
        cbxClientes.setItems(listaClientes);
    }

    private void cargarFacturaEnTextField() {
        Factura facturaSeleccionada = tablaFacturas.getSelectionModel().getSelectedItem();
        if (facturaSeleccionada != null) {
            txtIDFactura.setText(String.valueOf(facturaSeleccionada.getIdFactura()));

            // Seleccionar el pedido correspondiente
            for (Pedidos p : cbxPedidos.getItems()) {
                if (p.getIdPedido() == facturaSeleccionada.getIdPedido()) {
                    cbxPedidos.setValue(p);
                    break;
                }
            }

            // Seleccionar el cliente correspondiente
            for (Clientes c : cbxClientes.getItems()) {
                if (c.getIdCliente() == facturaSeleccionada.getIdCliente()) {
                    cbxClientes.setValue(c);
                    break;
                }
            }

            dpFechaFactura.setValue(facturaSeleccionada.getFechaFactura());
            txtTotal.setText(String.valueOf(facturaSeleccionada.getTotal()));
            txtImpuestos.setText(String.valueOf(facturaSeleccionada.getImpuestos()));
            cbxMetodoPago.setValue(facturaSeleccionada.getMetodoPago());
            cbxEstadoFactura.setValue(facturaSeleccionada.getEstadoFactura());
            txtNumeroFactura.setText(facturaSeleccionada.getNumeroFactura());
        }
    }

    private void cargarTablaFacturas() {
        listaFacturas = FXCollections.observableArrayList(listarFacturas());
        tablaFacturas.setItems(listaFacturas);
        if (!listaFacturas.isEmpty()) {
            tablaFacturas.getSelectionModel().selectFirst();
            cargarFacturaEnTextField();
        }
    }

    private Factura obtenerModeloFactura() {
        int idFactura = txtIDFactura.getText().isEmpty() ? 0 : Integer.parseInt(txtIDFactura.getText());
        Pedidos pedidoSeleccionado = cbxPedidos.getSelectionModel().getSelectedItem();
        Clientes clienteSeleccionado = cbxClientes.getSelectionModel().getSelectedItem();
        LocalDate fechaFactura = dpFechaFactura.getValue();
        double total = txtTotal.getText().isEmpty() ? 0 : Double.parseDouble(txtTotal.getText());
        double impuestos = txtImpuestos.getText().isEmpty() ? 0 : Double.parseDouble(txtImpuestos.getText());
        String metodoPago = cbxMetodoPago.getSelectionModel().getSelectedItem();
        String estadoFactura = cbxEstadoFactura.getSelectionModel().getSelectedItem();
        String numeroFactura = txtNumeroFactura.getText();

        return new Factura(
                idFactura,
                pedidoSeleccionado != null ? pedidoSeleccionado.getIdPedido() : 0,
                clienteSeleccionado != null ? clienteSeleccionado.getIdCliente() : 0,
                fechaFactura != null ? fechaFactura : LocalDate.now(),
                total,
                impuestos,
                metodoPago != null ? metodoPago : "",
                estadoFactura != null ? estadoFactura : "Pendiente",
                numeroFactura != null ? numeroFactura : ""
        );
    }

    private void insertarFactura() {
        modeloFactura = obtenerModeloFactura();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarFactura(?,?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloFactura.getIdPedido());
            enunciado.setDate(2, java.sql.Date.valueOf(modeloFactura.getFechaFactura()));
            enunciado.setDouble(3, modeloFactura.getTotal());
            enunciado.setDouble(4, modeloFactura.getImpuestos());
            enunciado.setString(5, modeloFactura.getMetodoPago());
            enunciado.setString(6, modeloFactura.getEstadoFactura());
            enunciado.setString(7, modeloFactura.getNumeroFactura());
            enunciado.setInt(8, modeloFactura.getIdCliente());
            enunciado.execute();
            cargarTablaFacturas();
        } catch (SQLException ex) {
            System.out.println("Error al agregar factura: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarFactura() {
        modeloFactura = obtenerModeloFactura();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarFactura(?,?,?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloFactura.getIdFactura());
            enunciado.setInt(2, modeloFactura.getIdPedido());
            enunciado.setDate(3, java.sql.Date.valueOf(modeloFactura.getFechaFactura()));
            enunciado.setDouble(4, modeloFactura.getTotal());
            enunciado.setDouble(5, modeloFactura.getImpuestos());
            enunciado.setString(6, modeloFactura.getMetodoPago());
            enunciado.setString(7, modeloFactura.getEstadoFactura());
            enunciado.setString(8, modeloFactura.getNumeroFactura());
            enunciado.setInt(9, modeloFactura.getIdCliente());
            enunciado.execute();
            cargarTablaFacturas();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar factura: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarFactura() {
        modeloFactura = tablaFacturas.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarFactura(?);");
            enunciado.setInt(1, modeloFactura.getIdFactura());
            enunciado.execute();
            cargarTablaFacturas();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar factura: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDFactura.clear();
        cbxPedidos.getSelectionModel().clearSelection();
        cbxClientes.getSelectionModel().clearSelection();
        dpFechaFactura.setValue(LocalDate.now());
        txtTotal.clear();
        txtImpuestos.clear();
        cbxMetodoPago.getSelectionModel().clearSelection();
        cbxEstadoFactura.getSelectionModel().clearSelection();
        txtNumeroFactura.clear();
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        cbxPedidos.setDisable(!activo);
        cbxClientes.setDisable(!activo);
        dpFechaFactura.setDisable(!activo);
        txtTotal.setDisable(!activo);
        txtImpuestos.setDisable(!activo);
        cbxMetodoPago.setDisable(!activo);
        cbxEstadoFactura.setDisable(!activo);
        txtNumeroFactura.setDisable(!activo);

        tablaFacturas.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaFacturas.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaFacturas.getSelectionModel().select(indice - 1);
            cargarFacturaEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaFacturas.getSelectionModel().getSelectedIndex();
        if (indice < listaFacturas.size() - 1) {
            tablaFacturas.getSelectionModel().select(indice + 1);
            cargarFacturaEnTextField();
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
                insertarFactura();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarFactura();
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
            eliminarFactura();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarFacturaEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarFacturaEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarFactura();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarFactura();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

 @FXML
    private void buscarFactura() {
        String busqueda = txtBuscar.getText().trim();
        
        if (busqueda.isEmpty()) {
            cargarTablaFacturas(); // Si está vacío, mostrar todos los registros
            return;
        }
        
        // Primero intenta buscar por ID exacto
        try {
            int idBusqueda = Integer.parseInt(busqueda);
            Factura facturaEncontrada = buscarFacturaPorID(idBusqueda);
            
            if (facturaEncontrada != null) {
                ObservableList<Factura> resultado = FXCollections.observableArrayList();
                resultado.add(facturaEncontrada);
                tablaFacturas.setItems(resultado);
                tablaFacturas.getSelectionModel().selectFirst();
                return;
            }
        } catch (NumberFormatException e) {
            // No es un número, continuar con búsqueda normal
        }
        
        // Búsqueda general si no encontró por ID
        ArrayList<Factura> resultadoBusqueda = new ArrayList<>();
        for (Factura f : listaFacturas) {
            if (String.valueOf(f.getIdPedido()).contains(busqueda) ||
                String.valueOf(f.getIdCliente()).contains(busqueda) ||
                f.getNumeroFactura().toLowerCase().contains(busqueda.toLowerCase()) ||
                f.getMetodoPago().toLowerCase().contains(busqueda.toLowerCase()) ||
                f.getEstadoFactura().toLowerCase().contains(busqueda.toLowerCase())) {
                resultadoBusqueda.add(f);
            }
        }
        
        tablaFacturas.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaFacturas.getSelectionModel().selectFirst();
        } else {
            // Mostrar mensaje de que no se encontraron resultados
            System.out.println("No se encontraron facturas con ese criterio de búsqueda");
        }
    }

    private Factura buscarFacturaPorID(int idFactura) {
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_buscarFactura(?);");
            enunciado.setInt(1, idFactura);
            ResultSet resultado = enunciado.executeQuery();
            
            if (resultado.next()) {
                return new Factura(
                    resultado.getInt("idFactura"),
                    resultado.getInt("idPedido"),
                    resultado.getInt("idCliente"),
                    resultado.getDate("fechaFactura").toLocalDate(),
                    resultado.getDouble("totalFactura"),
                    resultado.getDouble("impuestos"),
                    resultado.getString("metodoPago"),
                    resultado.getString("estadoFactura"),
                    resultado.getString("numeroFactura")
                );
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar factura por ID: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
