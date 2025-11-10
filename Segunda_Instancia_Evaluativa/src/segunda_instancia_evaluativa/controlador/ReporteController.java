/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;

import java.util.Comparator;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import segunda_instancia_evaluativa.modelo.Estados;
import segunda_instancia_evaluativa.modelo.Jugador;
import segunda_instancia_evaluativa.persistencia.ArchivoHistorialDAO;
import segunda_instancia_evaluativa.persistencia.ArchivoJugadoresDAO;
import segunda_instancia_evaluativa.vista.frmVentanaReporteFinal;
import segunda_instancia_evaluativa.persistencia.JugadorDAO;
import segunda_instancia_evaluativa.persistencia.PartidaDAO;



public class ReporteController {
    private final frmVentanaReporteFinal vista;
    private final Estados estados;
    private final List<Jugador> jugadores;

    public ReporteController(frmVentanaReporteFinal vista, List<Jugador> jugadores, Estados estados) {
        this.vista = vista;
        this.estados = estados;
        this.jugadores = jugadores;

        inicializar();
    }

    private void inicializar() {
        // =======================
        // 1) RANKING
        // =======================
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Nombre", "Tipo", "Dinero", "Victorias"}, 0
        );
        vista.getTblRanking().setModel(model);

        jugadores.sort(Comparator.comparingInt(Jugador::getDinero).reversed());
        for (Jugador j : jugadores) {
            model.addRow(new Object[]{
                j.getNombre(),
                j.getTipo(),
                j.getDinero(),
                j.getVictorias()
            });
        }

        // Guardar jugadores en archivo (lógica original)
        ArchivoJugadoresDAO.guardarJugadores(jugadores);

        // BONUS: Guardar jugadores también en BD
        JugadorDAO.guardarJugadores(jugadores);

        // =======================
        // 2) ESTADÍSTICAS
        // =======================

        String txtMayorApuesta = "Mayor apuesta: $" + estados.getMayorApuesta();
        if (estados.getMayorApuesta() > 0 && estados.getMayorApuestaAutor() != null) {
            txtMayorApuesta += " (" + estados.getMayorApuestaAutor() + ")";
        }
        vista.getLblMayorApuesta().setText(txtMayorApuesta);

        String txtMejorPuntaje = "Mejor puntaje: " + estados.getMejorPuntaje();
        if (estados.getMejorPuntaje() > 0 && estados.getMejorPuntajeAutor() != null) {
            txtMejorPuntaje += " (" + estados.getMejorPuntajeAutor() + ")";
        }
        vista.getLblMejorPuntaje().setText(txtMejorPuntaje);

        int cantVictimas = estados.getVictimas()
                                  .values()
                                  .stream()
                                  .mapToInt(Integer::intValue)
                                  .sum();
        String txtVictimas = "Jugadores afectados por trampas: " + cantVictimas;
        vista.getLblVictimasTrampas().setText(txtVictimas);

        // =======================
        // 3) HISTORIAL (últimas 3 partidas)
        // =======================
        // Se pueden mostrar desde BD:
        List<String> ultimas = PartidaDAO.obtenerUltimas(3);
        vista.getLstHistorial().setListData(ultimas.toArray(new String[0]));

        // =======================
        // 4) Botón Cerrar
        // =======================
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
    }
}
