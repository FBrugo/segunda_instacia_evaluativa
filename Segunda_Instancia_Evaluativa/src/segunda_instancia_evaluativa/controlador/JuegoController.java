/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;

import java.util.List;
import java.util.Random;
import javax.swing.table.DefaultTableModel;
import segunda_instancia_evaluativa.modelo.ConfiguracionJuego;
import segunda_instancia_evaluativa.modelo.Jugador;
import segunda_instancia_evaluativa.vista.VentanaJuego;
import segunda_instancia_evaluativa.persistencia.ArchivoHistorialDAO;

public class JuegoController {

    private final VentanaJuego vista;
    private final ConfiguracionJuego config;
    private final DefaultTableModel modeloTabla;
    private int partidaActual = 1;
    private int rondaActual = 1;
    private int pozo = 0;

    public JuegoController(VentanaJuego vista, ConfiguracionJuego config, Object estados) {
        this.vista = vista;
        this.config = config;

        this.modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Apodo", "Tipo", "Dinero", "Apuesta", "Tirada", "Victorias"}, 0
        );
        this.vista.getTblJugadores().setModel(modeloTabla);

        cargarJugadores();
        actualizarPanelSuperior();
        inicializarEventos();
    }

    private void cargarJugadores() {
        modeloTabla.setRowCount(0);
        for (Jugador j : config.getJugadores()) {
            modeloTabla.addRow(new Object[]{
                j.getNombre(), j.getApodo(), j.getTipo(), j.getDinero(), 0, "-", j.getVictorias()
            });
        }
    }

    private void actualizarPanelSuperior() {
        vista.getLblPartida().setText("Partida " + partidaActual + "/" + config.getCantidadPartidas());
        vista.getLblRonda().setText("Ronda " + rondaActual + "/" + config.getRondasPorPartida());
        vista.getLblPozo().setText("$" + pozo);
    }

    private void inicializarEventos() {
        vista.getBtnJugarRonda().addActionListener(e -> jugarRonda());
        vista.getBtnGuardarPartida().addActionListener(e -> guardarPartida());
        vista.getBtnSalir().addActionListener(e -> System.exit(0));
    }

    private void jugarRonda() {
        vista.getTxtLog().append("=== RONDA " + rondaActual + " ===\n");
        Random rand = new Random();

        Jugador ganador = null;
        int mejorTirada = 0;

        for (int i = 0; i < config.getJugadores().size(); i++) {
            Jugador j = config.getJugadores().get(i);
            int apuesta = 50;
            int tirada = rand.nextInt(12) + 1;

            j.setDinero(j.getDinero() - apuesta);
            pozo += apuesta;

            if (tirada > mejorTirada) {
                mejorTirada = tirada;
                ganador = j;
            }

            modeloTabla.setValueAt(j.getDinero(), i, 3);
            modeloTabla.setValueAt(apuesta, i, 4);
            modeloTabla.setValueAt(tirada, i, 5);
        }

        if (ganador != null) {
            ganador.setDinero(ganador.getDinero() + pozo);
            ganador.incrementarVictorias();
            vista.getTxtLog().append("Ganador: " + ganador.getApodo() + " (" + mejorTirada + ")\n");
            vista.getTxtLog().append("Pozo ganado: $" + pozo + "\n\n");
            pozo = 0;
        }

        rondaActual++;
        if (rondaActual > config.getRondasPorPartida()) {
            partidaActual++;
            rondaActual = 1;
        }

        actualizarPanelSuperior();

        if (partidaActual > config.getCantidadPartidas()) {
            vista.getTxtLog().append("=== FIN DEL JUEGO ===\n");
            ArchivoHistorialDAO.guardarPartida("Partida finalizada - Ganador: " + ganador.getApodo());
            vista.getBtnJugarRonda().setEnabled(false);
        }
    }

    private void guardarPartida() {
        ArchivoHistorialDAO.guardarPartida("Partida guardada manualmente.");
        vista.getTxtLog().append("Partida guardada.\n");
    }
}