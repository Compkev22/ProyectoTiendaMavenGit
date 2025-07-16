/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.kevinvelasquez.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.kevinvelasquez.system.Main;
import java.sql.CallableStatement;
import java.sql.Connection;
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
import org.kevinvelasquez.model.Categorias;

/**
 *
 * @author Kevin
 */
public class CategoriasController implements Initializable {

    @FXML
    private TableView<Categorias> tablaCategorias;
    @FXML
    private TableColumn colIDCategoria, colNombreCategoria, colDescripcionCategoria, colTipoCategoria;

    @FXML
    private TextField txtBuscar, txtIDCategoria, txtNombreCategoria, txtDescripcionCategoria, txtTipoCategoria;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo, btnEditar, btnEliminar,
            btnGuardar, btnCancelar, btnBuscar;

    private Main principal;
    private ObservableList<Categorias> listaCategorias;
    private Categorias modeloCategoria;

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

    public void escenaPaginaEmpresa() {
        principal.escenaEmpresa();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTablaCategorias();
        tablaCategorias.setOnMouseClicked(eh -> cargarCategoriaEnTextField());
    }

    public void configurarColumnas() {
        colIDCategoria.setCellValueFactory(new PropertyValueFactory<Categorias, Integer>("idCategoria"));
        colNombreCategoria.setCellValueFactory(new PropertyValueFactory<Categorias, String>("nombreCategoria"));
        colDescripcionCategoria.setCellValueFactory(new PropertyValueFactory<Categorias, String>("descripcionCategoria"));
        colTipoCategoria.setCellValueFactory(new PropertyValueFactory<Categorias, String>("tipoCategoria"));
    }

    private ArrayList<Categorias> listarCategorias() {
        ArrayList<Categorias> categorias = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_listarCategorias();");
            ResultSet resultado = enunciado.executeQuery();
            while (resultado.next()) {
                categorias.add(new Categorias(
                        resultado.getInt("ID"),
                        resultado.getString("NOMBRE"),
                        resultado.getString("DESCRIPCION"),
                        resultado.getString("TIPO")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar categorías: " + ex.getMessage());
            ex.printStackTrace();
        }
        return categorias;
    }

    private void cargarCategoriaEnTextField() {
        Categorias categoriaSeleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (categoriaSeleccionada != null) {
            txtIDCategoria.setText(String.valueOf(categoriaSeleccionada.getIdCategoria()));
            txtNombreCategoria.setText(categoriaSeleccionada.getNombreCategoria());
            txtDescripcionCategoria.setText(categoriaSeleccionada.getDescripcionCategoria());
            txtTipoCategoria.setText(categoriaSeleccionada.getTipoCategoria());
        }
    }

    private void cargarTablaCategorias() {
        listaCategorias = FXCollections.observableArrayList(listarCategorias());
        tablaCategorias.setItems(listaCategorias);
        if (!listaCategorias.isEmpty()) {
            tablaCategorias.getSelectionModel().selectFirst();
            cargarCategoriaEnTextField();
        }
    }

    private Categorias obtenerModeloCategoria() {
        int idCategoria = txtIDCategoria.getText().isEmpty() ? 0 : Integer.parseInt(txtIDCategoria.getText());
        String nombre = txtNombreCategoria.getText();
        String descripcion = txtDescripcionCategoria.getText();
        String tipo = txtTipoCategoria.getText();

        return new Categorias(
                idCategoria,
                nombre,
                descripcion,
                tipo
        );
    }

    private void insertarCategoria() {
        modeloCategoria = obtenerModeloCategoria();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_agregarCategoria(?,?,?);");
            enunciado.setString(1, modeloCategoria.getNombreCategoria());
            enunciado.setString(2, modeloCategoria.getDescripcionCategoria());
            enunciado.setString(3, modeloCategoria.getTipoCategoria());
            enunciado.execute();
            cargarTablaCategorias();
        } catch (SQLException ex) {
            System.out.println("Error al agregar categoría: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void actualizarCategoria() {
        modeloCategoria = obtenerModeloCategoria();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_editarCategoria(?,?,?,?);");
            enunciado.setInt(1, modeloCategoria.getIdCategoria());
            enunciado.setString(2, modeloCategoria.getNombreCategoria());
            enunciado.setString(3, modeloCategoria.getDescripcionCategoria());
            enunciado.setString(4, modeloCategoria.getTipoCategoria());
            enunciado.execute();
            cargarTablaCategorias();
        } catch (SQLException ex) {
            System.out.println("Error al actualizar categoría: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void eliminarCategoria() {
        modeloCategoria = tablaCategorias.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_eliminarCategoria(?);");
            enunciado.setInt(1, modeloCategoria.getIdCategoria());
            enunciado.execute();
            cargarTablaCategorias();
        } catch (SQLException ex) {
            System.out.println("Error al eliminar categoría: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtIDCategoria.clear();
        txtNombreCategoria.clear();
        txtDescripcionCategoria.clear();
        txtTipoCategoria.clear();
    }

    private void actualizarEstadoFormulario(EstadoFormulario estado) {
        EstadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.ACTUALIZAR);

        txtNombreCategoria.setDisable(!activo);
        txtDescripcionCategoria.setDisable(!activo);
        txtTipoCategoria.setDisable(!activo);

        tablaCategorias.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnNuevo.setText(activo ? "Guardar" : "Nuevo");
        btnEliminar.setText(activo ? "Cancelar" : "Eliminar");
        btnEditar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaCategorias.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaCategorias.getSelectionModel().select(indice - 1);
            cargarCategoriaEnTextField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaCategorias.getSelectionModel().getSelectedIndex();
        if (indice < listaCategorias.size() - 1) {
            tablaCategorias.getSelectionModel().select(indice + 1);
            cargarCategoriaEnTextField();
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
                insertarCategoria();
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case ACTUALIZAR:
                actualizarCategoria();
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
            eliminarCategoria();
        } else {
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
            cargarCategoriaEnTextField();
        }
    }

    @FXML
    private void btnCancelarAction() {
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        cargarCategoriaEnTextField();
    }

    @FXML
    private void btnGuardarAction() {
        if (EstadoActual == EstadoFormulario.AGREGAR) {
            insertarCategoria();
        } else if (EstadoActual == EstadoFormulario.ACTUALIZAR) {
            actualizarCategoria();
        }
        actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
    }

    @FXML
    private void buscarCategoria() {
        String nombre = txtBuscar.getText().toLowerCase();
        ArrayList<Categorias> resultadoBusqueda = new ArrayList<>();
        for (Categorias c : listaCategorias) {
            if (c.getNombreCategoria().toLowerCase().contains(nombre)) {
                resultadoBusqueda.add(c);
            }
        }
        tablaCategorias.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaCategorias.getSelectionModel().selectFirst();
        }
    }
}
