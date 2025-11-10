/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;

import segunda_instancia_evaluativa.modelo.Estados;
import segunda_instancia_evaluativa.modelo.ConfiguracionJuego;
import segunda_instancia_evaluativa.vista.frmVentanaConfiguracionInicial;
/**
 *
 * @author fedee
 */
public class AppController {
    private ConfiguracionJuego configuracion;
    private final Estados estados;

    public AppController() {
        this.estados = new Estados();
    }

    public void iniciarAplicacion() {
        java.awt.EventQueue.invokeLater(() -> {
            frmVentanaConfiguracionInicial vista = new frmVentanaConfiguracionInicial();
            new ConfiguracionController(vista, this);
            vista.setLocationRelativeTo(null);
            vista.setVisible(true);
        });
    }
        
    public ConfiguracionJuego getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(ConfiguracionJuego configuracion) {
        this.configuracion = configuracion;
    }

    public Estados getEstados() {
        return estados;
    }
}
