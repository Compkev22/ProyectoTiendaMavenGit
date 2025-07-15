package org.kevinvelasquez.system;
/**
 *
 * @author Diego G
 */
import java.io.InputStream;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.kevinvelasquez.controller.CategoriasController;
import org.kevinvelasquez.controller.ClientesController;
import org.kevinvelasquez.controller.ContactoController;
import org.kevinvelasquez.controller.DetallePedidoController;
import org.kevinvelasquez.controller.EmpresaController;
import org.kevinvelasquez.controller.GarantiaController;
import org.kevinvelasquez.controller.InicioController;
import org.kevinvelasquez.controller.LoginController;
import org.kevinvelasquez.controller.MenuPrincipalController;
import org.kevinvelasquez.controller.PedidosController;
import org.kevinvelasquez.controller.ProductosController;
import org.kevinvelasquez.controller.RegistrarController;

public class Main extends Application{
    private static String URL = "/view/";
    private static String URL_LOGO = "/org/kevinvelasquez/";
    private Stage escenarioPrincipal;
    private Scene escena;

    public static void main(String[] args) {
//        System.out.println("Hello World!");
//        
//        JSONObject persona = new JSONObject();
//        
//        persona.put("nombre: ", "Diego");
//        persona.put("apellido: ", "López");
//        persona.put("edad: ", "17");
//        persona.put("valido: ", "true");
//        
//        System.out.println("Contenido de JSON: ");
//        System.out.println(persona.toString(4));
        launch(args);
    }

    @Override
    public void start(Stage escenario) throws Exception {
        this.escenarioPrincipal = escenario;
        escenaInicio();
        
        escenarioPrincipal.setTitle("Tienda TECHSHOP");
        escenarioPrincipal.show();
//        escenarioPrincipal.getIcons().add(new Image(URL_LOGO +"image/KinalVet.png"));
    }

    public Initializable cambiarEscena(String fxml, double ancho, double alto) throws Exception {
        Initializable interfazCargada = null;

        FXMLLoader cargadorFXML = new FXMLLoader();

        InputStream archivoFXML = Main.class.getResourceAsStream(URL + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Main.class.getResource(URL + fxml));

        escena = new Scene(cargadorFXML.load(archivoFXML), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene(); //usar el tamaño de la escena en el Stage                  

        interfazCargada = cargadorFXML.getController();

        return interfazCargada;
    }

    public void escenaInicio() {
        try {
            InicioController inicio = (InicioController) cambiarEscena("InicioView.fxml", 800, 600);
            inicio.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de inicio");
            ex.printStackTrace();
        }
    }
    
    public void escenaLogin() {
        try {
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml", 800, 600);
            login.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de Login");
            ex.printStackTrace();
        }
    }
    
    public void escenaRegistrar() {
        try {
            RegistrarController registrar = (RegistrarController) cambiarEscena("RegistrarView.fxml", 800, 600);
            registrar.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de Registrarse");
            ex.printStackTrace();
        }
    }
    
    public void menu() {
        try {
            MenuPrincipalController mp = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 1291, 933);
            mp.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambio a la Pagina Menu Principal");
            ex.printStackTrace();
        }
    }
    
    public void escenaProductos() {
        try {
            ProductosController productos = (ProductosController) cambiarEscena("ProductosView.fxml", 800, 600);
            productos.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de productos");
            ex.printStackTrace();
        }
    }
    
    public void escenaClientes() {
        try {
            ClientesController clientes = (ClientesController) cambiarEscena("ClientesView.fxml", 800, 600);
            clientes.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de clientes");
            ex.printStackTrace();
        }
    }

    public void escenaPedidos() {
        try {
            PedidosController pedidos = (PedidosController) cambiarEscena("PedidosView.fxml", 800, 600);
            pedidos.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de pedidos");
            ex.printStackTrace();
        }
    }

    public void escenaDetallePedido() {
        try {
            DetallePedidoController detalle = (DetallePedidoController) cambiarEscena("DetallePedidoView.fxml", 800, 600);
            detalle.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de detalle de pedido");
            ex.printStackTrace();
        }
    }

    public void escenaCategorias() {
        try {
            CategoriasController categorias = (CategoriasController) cambiarEscena("CategoriasView.fxml", 800, 600);
            categorias.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de categorías");
            ex.printStackTrace();
        }
    }

    public void escenaGarantias() {
        try {
            GarantiaController garantias = (GarantiaController) cambiarEscena("GarantiaView.fxml", 800, 600);
            garantias.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de garantías");
            ex.printStackTrace();
        }
    }
        
    public void escenaContacto() {
        try {
            ContactoController contacto = (ContactoController) cambiarEscena("ContactoView.fxml", 800, 600);
            contacto.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de contacto");
            ex.printStackTrace();
        }
    }

    public void escenaEmpresa() {
        try {
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml", 800, 600);
            empresa.setPrincipal(this);
        } catch (Exception ex) {
            System.out.println("Error al cambiar a la escena de empresa");
            ex.printStackTrace();
        }
    }
    
}
