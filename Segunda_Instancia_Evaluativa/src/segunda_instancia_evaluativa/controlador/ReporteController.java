/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;

import segunda_instancia_evaluativa.modelo.Estados;
import segunda_instancia_evaluativa.vista.frmVentanaReporteFinal;
import java.util.List;
import segunda_instancia_evaluativa.persistencia.ArchivoHistorialDAO;

import java.util.Comparator;

import javax.swing.table.DefaultTableModel;
import segunda_instancia_evaluativa.modelo.Jugador;
import segunda_instancia_evaluativa.persistencia.ArchivoJugadoresDAO;


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
        // LÓGICA DE SANTI Y CARLOS
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

    List<String> ultimas = ArchivoHistorialDAO.obtenerUltimas(3);
    vista.getLstHistorial().setListData(ultimas.toArray(new String[0]));
    
    }
}
