/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;

import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import segunda_instancia_evaluativa.modelo.ConfiguracionJuego;
import segunda_instancia_evaluativa.modelo.Estados;
import segunda_instancia_evaluativa.modelo.Jugador;
import segunda_instancia_evaluativa.modelo.JugadorCasino;
import segunda_instancia_evaluativa.modelo.TrampasEngine;
import segunda_instancia_evaluativa.modelo.TrampasLog;
import segunda_instancia_evaluativa.persistencia.ArchivoHistorialDAO;
import segunda_instancia_evaluativa.persistencia.JugadorDAO;
import segunda_instancia_evaluativa.persistencia.PartidaDAO;
import segunda_instancia_evaluativa.vista.frmVentanaJuego;
import segunda_instancia_evaluativa.vista.frmVentanaReporteFinal;

public class JuegoController {
    private final frmVentanaJuego vista;
    private final ConfiguracionJuego config;
    private final DefaultTableModel modeloTabla;

    // Estadísticas y trampas
    private final Estados estados;
    private final TrampasEngine trampas;
    private final TrampasLog trampasLog;

    // Estado de la partida
    private int partidaActual = 1;
    private int rondaActual = 1;
    private int rondasTotales = 0;
    private int pozo = 0;

    public JuegoController(frmVentanaJuego vista, ConfiguracionJuego config, Estados estados) {
        this.vista = vista;
        this.config = config;
        this.estados = estados;

        // Si las trampas están desactivadas, probabilidad 0
        if (config.isTrampasActivadas()) {
            this.trampas = new TrampasEngine(0.25, 0.25); // 25% y 25% de ejemplo
        } else {
            this.trampas = new TrampasEngine(0.0, 0.0);
        }
        this.trampasLog = new TrampasLog();

        this.modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Apodo", "Tipo", "Dinero", "Apuesta", "Tirada", "Victorias"}, 0
        );
        this.vista.getTblJugadores().setModel(modeloTabla);

        cargarJugadores();
        actualizarPanelSuperior();
        inicializarEventos();
    }

    // =========================
    // Inicialización de vista
    // =========================
    private void cargarJugadores() {
        modeloTabla.setRowCount(0);
        for (Jugador j : config.getJugadores()) {
            modeloTabla.addRow(new Object[]{
                j.getNombre(),
                j.getApodo(),
                j.getTipo(),
                j.getDinero(),
                0,          // Apuesta
                "-",        // Tirada
                j.getVictorias()
            });
        }
    }

    private void actualizarPanelSuperior() {
        vista.getLblPartida().setText("Partida " + partidaActual + "/" + config.getCantidadPartidas());
        vista.getLblRonda().setText("Ronda " + rondaActual + "/" + config.getRondasPorPartida());
        vista.getLblPozo().setText("$" + pozo);
    }

    private void inicializarEventos() {
        // Menú "Partida"
        vista.getItemJugarRonda().addActionListener(e -> jugarRonda());
        vista.getItemGuardarPartida().addActionListener(e -> guardarPartida());
        vista.getItemSalirPartida().addActionListener(e -> System.exit(0));

        // Menú "Ver"
        // Ranking: ahora lee desde la BASE DE DATOS
        vista.getItemRanking().addActionListener(e -> mostrarRankingBD());

        // Historial y estadísticas: abre la ventana de reporte (lógica anterior)
        vista.getItemHistorial().addActionListener(e -> abrirReporteFinal());
        vista.getItemEstadisticas().addActionListener(e -> abrirReporteFinal());
    }

    private void abrirReporteFinal() {
        frmVentanaReporteFinal vistaReporte = new frmVentanaReporteFinal();
        new ReporteController(vistaReporte, config.getJugadores(), estados);
        vistaReporte.setLocationRelativeTo(vista);
        vistaReporte.setVisible(true);
    }

    // =========================
    // BONUS: Ranking desde BD
    // =========================
    private void mostrarRankingBD() {
        var filas = JugadorDAO.obtenerRanking();

        if (filas.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "No hay datos de ranking en la base de datos.\n" +
                    "Finalizá al menos una partida para actualizarlo.",
                    "Ranking actual (BD)",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder sb = new StringBuilder("Ranking actual (BD)\n\n");
        for (Object[] f : filas) {
            String nombre   = (String) f[0];
            String tipo     = (String) f[1];
            double dinero   = (double) f[2];
            int victorias   = (int) f[3];

            sb.append(String.format("%s (%s) - $%.2f - %d victorias\n",
                    nombre, tipo, dinero, victorias));
        }

        JOptionPane.showMessageDialog(
                vista,
                sb.toString(),
                "Ranking actual (BD)",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================
    // LÓGICA DE JUEGO
    // =========================
    private void jugarRonda() {
        vista.getTxtLog().append("\n=== RONDA " + rondaActual + " ===\n");
        Random rand = new Random();

        Jugador ganador = null;
        int mejorSuma = 0;

        for (int i = 0; i < config.getJugadores().size(); i++) {
            Jugador j = config.getJugadores().get(i);

            // Si el jugador está en 0, no juega
            if (j.getDinero() <= 0) {
                vista.getTxtLog().append(j.getApodo() + " no puede apostar (saldo $0).\n");
                modeloTabla.setValueAt(0, i, 4);   // Apuesta
                modeloTabla.setValueAt("-", i, 5); // Tirada
                continue;
            }

            // Apuesta fija
            int apuesta = 50;
            if (j.getDinero() < apuesta) {
                apuesta = j.getDinero();
            }

            // Tirada de dados
            int d1 = rand.nextInt(6) + 1;
            int d2 = rand.nextInt(6) + 1;

            // Trampas
            if (config.isTrampasActivadas()) {
                if (j instanceof JugadorCasino) {
                    int antes1 = d1, antes2 = d2;
                    d1 = trampas.tirarConDadoCargado(d1);
                    d2 = trampas.tirarConDadoCargado(d2);
                    if (d1 != antes1 || d2 != antes2) {
                        trampasLog.log("La Casa usa dados cargados (" + antes1 + "," + antes2 + ") → (" + d1 + "," + d2 + ")");
                        vista.getTxtLog().append("La Casa carga los dados: (" + antes1 + "," + antes2 + ") → (" + d1 + "," + d2 + ")\n");
                    }
                } else {
                    int antes1 = d1, antes2 = d2;
                    d1 = trampas.confundirValor(d1);
                    d2 = trampas.confundirValor(d2);
                    if (d1 != antes1 || d2 != antes2) {
                        estados.registrarVictima(j.getApodo());
                        trampasLog.log("La Casa confunde a " + j.getApodo());
                        vista.getTxtLog().append("¡La Casa confunde a " + j.getApodo() + "!\n");
                    }
                }
            }

            int suma = d1 + d2;

            // Actualizar dinero y pozo
            j.setDinero(j.getDinero() - apuesta);
            pozo += apuesta;

            // Registrar estadísticas
            estados.registrarApuesta(j.getApodo(), apuesta);
            estados.registrarPuntaje(j.getApodo(), suma);

            // Log de la jugada
            vista.getTxtLog().append(String.format(
                    "%s (%s) apuesta $%d, tira %d (%d+%d) → saldo $%d\n",
                    j.getApodo(),
                    j.getTipo(),
                    apuesta,
                    suma,
                    d1, d2,
                    j.getDinero()
            ));

            // Actualizar tabla
            modeloTabla.setValueAt(j.getDinero(), i, 3); // Dinero
            modeloTabla.setValueAt(apuesta, i, 4);       // Apuesta
            modeloTabla.setValueAt(suma, i, 5);          // Tirada

            // Elegir ganador de la ronda
            if (suma > mejorSuma) {
                mejorSuma = suma;
                ganador = j;
            }
        }

        // Resolver ronda
        if (ganador != null && pozo > 0) {
            ganador.setDinero(ganador.getDinero() + pozo);
            ganador.incrementarPartidasGanadas();

            vista.getTxtLog().append("→ Ganador: " + ganador.getApodo()
                    + " con " + mejorSuma + " gana el pozo de $" + pozo + "\n");

            // Actualizar fila del ganador (dinero y victorias)
            for (int i = 0; i < config.getJugadores().size(); i++) {
                if (config.getJugadores().get(i) == ganador) {
                    modeloTabla.setValueAt(ganador.getDinero(), i, 3);
                    modeloTabla.setValueAt(ganador.getPartidasGanadas(), i, 6);
                    break;
                }
            }

            pozo = 0;
        }

        rondasTotales++;

        // Siguiente ronda / partida
        rondaActual++;
        if (rondaActual > config.getRondasPorPartida()) {
            partidaActual++;
            rondaActual = 1;
        }

        actualizarPanelSuperior();

        // ¿Se terminaron todas las partidas?
        if (partidaActual > config.getCantidadPartidas()) {
            terminarJuego(ganador);
        }
    }

    // =========================
    // Fin del juego
    // =========================
    private void terminarJuego(Jugador ultimoGanador) {
        vista.getTxtLog().append("\n=== FIN DEL JUEGO ===\n");

        // Actualizar estadísticas globales
        estados.sumarPartida();
        estados.considerarDuracion(rondasTotales);

        String ganadorNombre = (ultimoGanador != null) ? ultimoGanador.getApodo() : "Ninguno";
        String detalle = "PARTIDA - Ganador: " + ganadorNombre
                + " | Rondas: " + rondasTotales;

        // Guardar en historial (archivo)
        ArchivoHistorialDAO.guardarPartida(detalle);
        vista.getTxtLog().append("Resumen guardado en historial.\n");

        // BONUS: guardar también en la base de datos
        // (si no tenés un pozo acumulado por partida, podés dejar 0.0)
        PartidaDAO.guardarPartida(ganadorNombre, rondasTotales, 0.0);

        // No se puede seguir jugando
        vista.getItemJugarRonda().setEnabled(false);
    }

    // =========================
    // Guardado manual
    // =========================
    private void guardarPartida() {
        ArchivoHistorialDAO.guardarPartida("Partida guardada manualmente.");
        vista.getTxtLog().append("Partida guardada.\n");
    }
}