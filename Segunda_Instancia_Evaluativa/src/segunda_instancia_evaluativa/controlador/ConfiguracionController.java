/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;
import segunda_instancia_evaluativa.modelo.ConfiguracionJuego;
import segunda_instancia_evaluativa.vista.frmVentanaConfiguracionInicial;
/**
 *
 * @author fedee
 */
public class ConfiguracionController {
    private final frmVentanaConfiguracionInicial vista;
    private final AppController appController;
    private final ConfiguracionJuego config;

    public ConfiguracionController(frmVentanaConfiguracionInicial vista, AppController appController) {
        this.vista = vista;
        this.appController = appController;
        this.config = new ConfiguracionJuego();
        this.appController.setConfiguracion(config);
        inicializar();
    }

    private void inicializar() {
        // AQUI DESPUÉS RODRI AGREGA LISTENERS Y LÓGICA
    }
}
