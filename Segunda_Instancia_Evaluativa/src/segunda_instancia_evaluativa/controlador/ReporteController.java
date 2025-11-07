package segunda_instancia_evaluativa.controlador;

import java.util.Comparator;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import segunda_instancia_evaluativa.modelo.Jugador;
import segunda_instancia_evaluativa.persistencia.ArchivoJugadoresDAO;
import segunda_instancia_evaluativa.vista.VentanaReporteFinal;

public class ReporteController {

    private final VentanaReporteFinal vista;
    private final List<Jugador> jugadores;

    public ReporteController(VentanaReporteFinal vista, List<Jugador> jugadores) {
        this.vista = vista;
        this.jugadores = jugadores;

        inicializar();
    }

    private void inicializar() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Nombre", "Tipo", "Dinero", "Victorias"}, 0
        );
        vista.getTblRanking().setModel(model);

        jugadores.sort(Comparator.comparingInt(Jugador::getDinero).reversed());
        for (Jugador j : jugadores) {
            model.addRow(new Object[]{
                j.getNombre(), j.getTipo(), j.getDinero(), j.getVictorias()
            });
        }

        ArchivoJugadoresDAO.guardarJugadores(jugadores);
    }
}
