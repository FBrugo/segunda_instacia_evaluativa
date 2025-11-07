/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa;

import segunda_instancia_evaluativa.modelo.TrampasLog;
import segunda_instancia_evaluativa.modelo.Estados;
import segunda_instancia_evaluativa.modelo.Jugador;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rodri
 */
public class ProcesadorComandos {
    private final List<Jugador> jugadores;
    private final Estados estados;
    private final List<String> historial;
    private final TrampasLog trampasLog;

    public ProcesadorComandos(List<Jugador> jugadores, Estados estados, List<String> historial, TrampasLog trampasLog) {
        this.jugadores = jugadores;
        this.estados = estados;
        this.historial = historial;
        this.trampasLog = trampasLog;
    }

    public boolean procesar(String linea) {
        if (linea == null) return true;
        String cmd = linea.trim();
        if (cmd.equalsIgnoreCase("ESTADOS")) {
            mostrarEstados();
        } else if (cmd.equalsIgnoreCase("HISTORY")) {
            mostrarHistorial();
        } else if (cmd.equalsIgnoreCase("RANKING")) {
            mostrarRanking();
        } else if (cmd.equalsIgnoreCase("TRAMPAS")) {
            trampasLog.mostrar();
        } else if (cmd.equalsIgnoreCase("QUIT")) {
            System.out.println("Saliendo por comando...");
            return false;
        } else if (cmd.toUpperCase().startsWith("SAVE ")) {
            String nombre = cmd.substring(5).trim();
            if (nombre.isEmpty()) nombre = "Partida";
            historial.add("[SAVE] " + nombre);
            recortarHistorial();
            System.out.println("Partida guardada como: " + nombre);
        } else if (!cmd.isEmpty() && !cmd.equalsIgnoreCase("CONTINUAR")) {
            System.out.println("Comandos: ESTADOS | HISTORY | RANKING | TRAMPAS | SAVE [nombre] | CONTINUAR | QUIT");
        }
        return true;
    }

    private void mostrarEstados() {
        System.out.println("\n--- ESTADÍSTICAS ---");
        System.out.println("Mayor apuesta: $" + estados.getMayorApuesta() + " (" + estados.getMayorApuestaAutor() + ")");
        System.out.println("Mejor puntaje: " + estados.getMejorPuntaje() + " (" + estados.getMejorPuntajeAutor() + ")");
        if (estados.getVictimas().isEmpty()) System.out.println("Víctimas del casino: (ninguna)");
        else {
            System.out.print("Víctimas del casino: ");
            boolean first=true;
            for (Map.Entry<String,Integer> e : estados.getVictimas().entrySet()) {
                System.out.print((first?"":", ") + e.getKey() + "(" + e.getValue() + ")");
                first=false;
            }
            System.out.println();
        }
        System.out.println("Partida más larga: " + estados.getPartidaMasLarga() + " rondas");
    }

    private void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL (ult. 5) ---");
        if (historial.isEmpty()) { System.out.println("(vacío)"); return; }
        for (String h : historial) System.out.println("• " + h);
    }

    private void mostrarRanking() {
        System.out.println("\n--- RANKING ACTUAL ---");
        jugadores.stream()
            .sorted((a,b) -> Integer.compare(b.getDinero(), a.getDinero()))
            .forEach(j -> System.out.printf("%s (%s) - $%d - %d victorias%n",
                    nombreMostrar(j), j.obtenerTipoJugador(), j.getDinero(), j.getPartidasGanadas()));
    }

    private String nombreMostrar(Jugador j) {
        return j.getApodo()!=null ? j.getApodo() : j.getNombre();
    }

    private void recortarHistorial() {
        while (historial.size() > 5) historial.remove(0);
    }
}