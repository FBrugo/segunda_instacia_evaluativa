/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.persistencia;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import segunda_instancia_evaluativa.modelo.Jugador;

public class ArchivoJugadoresDAO {

    public static void guardarJugadores(List<Jugador> jugadores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("jugadores.txt", false))) {
            for (Jugador j : jugadores) {
                bw.write(j.getNombre() + ";" + j.getApodo() + ";" + j.getTipo() + ";" +
                         j.getDinero() + ";" + j.getVictorias());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error guardando jugadores: " + e.getMessage());
        }
    }
}
