
package org.kevinvelasquez.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kevinvelasquez.database.Conexion;
import org.kevinvelasquez.model.Producto;
import org.kevinvelasquez.system.Main;

/**
 *
 * @author Kevin
 */
public class ProductosController implements Initializable {
    private Main principal;

//    private ObservableList<Producto> listaProductos;
//
//    @FXML private TableView<Producto> tablaProductos;
//    @FXML private TableColumn<Producto, Integer> colIdProducto;
//    @FXML private TableColumn<Producto, String> colNombreProducto;
//    @FXML private TableColumn<Producto, String> colDescripcionProducto;
//    @FXML private TableColumn<Producto, Double> colPrecioActual;
//    @FXML private TableColumn<Producto, Double> colPrecioAnterior;
//    @FXML private TableColumn<Producto, String> colDescuentoProducto;
//    @FXML private TableColumn<Producto, String> colCategoriaProducto;
//    @FXML private TableColumn<Producto, String> colCodigoBarra;
//    @FXML private TableColumn<Producto, String> colUltimaActualizacion;
//    @FXML private TableColumn<Producto, Integer> colStock;
//    
//    @FXML private TextField txtNombre, txtDescripcion, txtPrecioActual, txtPrecioAnterior, txtDescuento,
//                            txtCategoria, txtCodigoBarra, txtStock, txtBuscar;
//    @FXML private DatePicker dpUltimaActualizacion;

    public ProductosController() {
    }
    
    public ProductosController(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        configurarColumnas();
//        cargarDatos();
    }    
    
//    private void configurarColumnas() {
//        colIdProducto.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
//        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
//        colDescripcionProducto.setCellValueFactory(new PropertyValueFactory<>("descripcionProducto"));
//        colPrecioActual.setCellValueFactory(new PropertyValueFactory<>("precioActual"));
//        colPrecioAnterior.setCellValueFactory(new PropertyValueFactory<>("precioAnterior"));
//        colDescuentoProducto.setCellValueFactory(new PropertyValueFactory<>("descuentoProducto"));
//        colCategoriaProducto.setCellValueFactory(new PropertyValueFactory<>("categoriaProducto"));
//        colCodigoBarra.setCellValueFactory(new PropertyValueFactory<>("codigoBarra"));
//        colUltimaActualizacion.setCellValueFactory(new PropertyValueFactory<>("ultimaActualizacion"));
//        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
//    }
//
//    private ArrayList<Producto> obtenerProductos() {
//        ArrayList<Producto> productos = new ArrayList<>();
//
//        try {
//            CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("{call sp_listarProductos()}");
//            ResultSet rs = cs.executeQuery();
//
//            while (rs.next()) {
//                productos.add(new Producto(
//                    rs.getInt("ID"),
//                    rs.getString("NOMBRE"),
//                    rs.getString("DESCRIPCION"),
//                    rs.getDouble("PRECIO_ACTUAL"),
//                    rs.getDouble("PRECIO_ANTERIOR"),
//                    rs.getString("DESCUENTO"),
//                    rs.getString("CATEGORIA"),
//                    rs.getString("CODIGO_BARRA"),
//                    rs.getDate("ULTIMA_ACTUALIZACION").toLocalDate(),
//                    rs.getInt("STOCK")
//                ));
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Error al cargar productos");
//            e.printStackTrace();
//        }
//
//        return productos;
//    }
//
//    private void cargarDatos() {
//        listaProductos = FXCollections.observableArrayList(obtenerProductos());
//        tablaProductos.setItems(listaProductos);
//    }
//    
//     @FXML
//    private void agregarProducto() {
//        try {
//            CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("{call sp_agregarProducto(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
//            cs.setString(1, txtNombre.getText());
//            cs.setString(2, txtDescripcion.getText());
//            cs.setDouble(3, Double.parseDouble(txtPrecioActual.getText()));
//            cs.setDouble(4, Double.parseDouble(txtPrecioAnterior.getText()));
//            cs.setString(5, txtDescuento.getText());
//            cs.setString(6, txtCategoria.getText());
//            cs.setString(7, txtCodigoBarra.getText());
//            cs.setDate(8, Date.valueOf(dpUltimaActualizacion.getValue()));
//            cs.setInt(9, Integer.parseInt(txtStock.getText()));
//            cs.execute();
//            cargarDatos();
//            limpiarCampos();
//            System.out.println("Producto agregado correctamente.");
//        } catch (SQLException e) {
//            System.out.println("Error al agregar producto");
//            e.printStackTrace();
//        }
//    }
//
//    @FXML
//    private void eliminarProducto() {
//        Producto producto = tablaProductos.getSelectionModel().getSelectedItem();
//        if (producto != null) {
//            try {
//                CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
//                cs.setInt(1, producto.getIdProducto());
//                cs.execute();
//                cargarDatos();
//                limpiarCampos();
//                System.out.println("Producto eliminado correctamente.");
//            } catch (SQLException e) {
//                System.out.println("Error al eliminar producto");
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("Seleccione un producto para eliminar.");
//        }
//    }
//
//    @FXML
//    private void buscarProducto() {
//        String texto = txtBuscar.getText().toLowerCase();
//        ArrayList<Producto> resultados = new ArrayList<>();
//        for (Producto p : listaProductos) {
//            if (p.getNombreProducto().toLowerCase().contains(texto)) {
//                resultados.add(p);
//            }
//        }
//        tablaProductos.setItems(FXCollections.observableArrayList(resultados));
//        if (!resultados.isEmpty()) {
//            tablaProductos.getSelectionModel().selectFirst();
//        }
//    }
//
//    private void limpiarCampos() {
//        txtNombre.clear(); txtDescripcion.clear(); txtPrecioActual.clear(); txtPrecioAnterior.clear();
//        txtDescuento.clear(); txtCategoria.clear(); txtCodigoBarra.clear(); txtStock.clear();
//        dpUltimaActualizacion.setValue(null);
//    }    
}
