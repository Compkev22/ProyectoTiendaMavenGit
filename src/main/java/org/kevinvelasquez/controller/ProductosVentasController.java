/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kevinvelasquez.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Producto;
import org.kevinvelasquez.system.Main;

/**
 * FXML Controller class
 *
 * @author Emilio
 */
public class ProductosVentasController implements Initializable {
    private Main principal;
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Integer> colID;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private TableView<Producto> tablaCarrito;
    @FXML private TableColumn<Producto, Integer> colIDCarrito;
    @FXML private TableColumn<Producto, String> colNombreCarrito;
    @FXML private TableColumn<Producto, Integer> colCantidadCarrito;

    @FXML private TextField txtBuscar;
    @FXML private TextField txtIDManual;
    @FXML private Button btnAgregar;
    @FXML private Button btnComprar;

    private ObservableList<Producto> productosDisponibles;
    private ObservableList<Producto> carrito;

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void escenaPaginaPrincipal(){
        principal.menu();
    }
    
    public void escenaGestion(){
        principal.escenaGestion();
    }
    
    public void escenaFacturas(){
        principal.escenaFacturas();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
        cargarProductos();
        agregarListeners();
    }

    private void configurarTablas() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colIDCarrito.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colNombreCarrito.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidadCarrito.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void cargarProductos() {
        productosDisponibles = FXCollections.observableArrayList(obtenerProductosDesdeBD());
        tablaProductos.setItems(productosDisponibles);
        carrito = FXCollections.observableArrayList();
        tablaCarrito.setItems(carrito);
    }

    private List<Producto> obtenerProductosDesdeBD() {
        List<Producto> productos = new ArrayList<>();
        try {
            CallableStatement stmt = Conexion.getInstancia().getConexion().prepareCall("call sp_listarProductos()");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                productos.add(new Producto(
                    rs.getInt("ID"),
                    rs.getString("NOMBRE"),
                    rs.getInt("STOCK")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productos;
    }

    private void agregarListeners() {
        tablaProductos.setOnMouseClicked(event -> {
            Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                agregarAlCarrito(seleccionado.getIdProducto());
            }
        });

        txtBuscar.textProperty().addListener((obs, oldVal, newVal) -> filtrarProductos(newVal));
    }

    private void filtrarProductos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tablaProductos.setItems(productosDisponibles);
            return;
        }
        ObservableList<Producto> filtrados = productosDisponibles.filtered(p ->
            p.getNombreProducto().toLowerCase().contains(filtro.toLowerCase())
        );
        tablaProductos.setItems(filtrados);
    }

    @FXML
    private void agregarProductoPorID() {
        try {
            int id = Integer.parseInt(txtIDManual.getText());
            agregarAlCarrito(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }

    private void agregarAlCarrito(int idProducto) {
        Producto producto = buscarProductoPorID(idProducto);
        if (producto != null && producto.getStock() > 0) {
            Optional<Producto> enCarrito = carrito.stream()
                .filter(p -> p.getIdProducto() == idProducto)
                .findFirst();

            if (enCarrito.isPresent()) {
                enCarrito.get().setStock(enCarrito.get().getStock() + 1); 
                tablaCarrito.refresh();
            } else {
                carrito.add(new Producto(producto.getIdProducto(), producto.getNombreProducto(), 1));
            }
        }
    }

    private Producto buscarProductoPorID(int id) {
        return productosDisponibles.stream()
                .filter(p -> p.getIdProducto() == id)
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void comprarProductos() {
        for (Producto item : carrito) {
            Producto disponible = buscarProductoPorID(item.getIdProducto());
            if (disponible != null && item.getStock() > disponible.getStock()) {
                mostrarAlerta("Error de compra", "No puedes comprar más de lo disponible en stock.\n" +
                        "Producto: " + disponible.getNombreProducto() + "\n" +
                        "Stock disponible: " + disponible.getStock() + "\n" +
                        "Cantidad en carrito: " + item.getStock());
                return;
            }
        }

        for (Producto item : carrito) {
            try {
                CallableStatement stmt = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_venderProducto(?, ?)");
                stmt.setInt(1, item.getIdProducto());
                stmt.setInt(2, item.getStock());
                stmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        carrito.clear();
        cargarProductos();
    }
    
    @FXML
    private void eliminarProducto() {
        String idTexto = txtIDManual.getText();
        if (!idTexto.isEmpty()) {
            try {
                int id = Integer.parseInt(idTexto);
                eliminarDelCarritoPorID(id);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
            }
        } else {
            // Eliminar por selección
            Producto seleccionado = tablaCarrito.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                eliminarDelCarrito(seleccionado);
            } else {
                System.out.println("No se ha seleccionado ningún producto en el carrito.");
            }
        }
    }

    private void eliminarDelCarrito(Producto producto) {
        if (producto.getStock() > 1) {
            producto.setStock(producto.getStock() - 1);
        } else {
            carrito.remove(producto); 
        }
        tablaCarrito.refresh();
    }

    private void eliminarDelCarritoPorID(int idProducto) {
        Producto productoAEliminar = carrito.stream()
                .filter(p -> p.getIdProducto() == idProducto)
                .findFirst()
                .orElse(null);

        if (productoAEliminar != null) {
            if (productoAEliminar.getStock() > 1) {
                productoAEliminar.setStock(productoAEliminar.getStock() - 1);
            } else {
                carrito.remove(productoAEliminar); 
            }
            tablaCarrito.refresh();
        } else {
            System.out.println("Producto no encontrado en el carrito.");
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

