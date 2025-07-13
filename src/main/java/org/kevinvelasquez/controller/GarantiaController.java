
package org.kevinvelasquez.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.kevinvelasquez.system.Main;

/**
 *
 * @author Kevin
 */
public class GarantiaController implements Initializable {
    private Main principal;

    public GarantiaController() {
    }

    public GarantiaController(Main principal) {
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
        // TODO
    }    
        
}
