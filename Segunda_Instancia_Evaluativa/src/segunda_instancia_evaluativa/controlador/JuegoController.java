/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;
import segunda_instancia_evaluativa.modelo.ConfiguracionJuego;
import segunda_instancia_evaluativa.modelo.Estados;
import segunda_instancia_evaluativa.vista.frmVentanaJuego;

public class JuegoController {

    private final frmVentanaJuego vista;
    private final ConfiguracionJuego config;
    private final Estados estados;

    public JuegoController(frmVentanaJuego vista, ConfiguracionJuego config, Estados estados) {
        this.vista = vista;
        this.config = config;
        this.estados = estados;
        inicializar();
    }

    private void inicializar() {
        // LÓGICA DE MARTIN LUEGO
    }
}