/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa;

/**
 *
 * @author santi
 */

import java.util.HashMap;
import java.util.Map;

public class Estados {
    private int mayorApuesta = 0;
    private String mayorApuestaAutor = "";
    private int mejorPuntaje = 0;
    private String mejorPuntajeAutor = "";
    private int partidasJugadas = 0;
    private int partidaMasLarga = 0;
    private final Map<String,Integer> victimas = new HashMap<>();

    public void registrarApuesta(String jugador, int monto) {
        if (monto > mayorApuesta) { mayorApuesta = monto; mayorApuestaAutor = jugador; }
    }
    public void registrarPuntaje(String jugador, int suma) {
        if (suma > mejorPuntaje) { mejorPuntaje = suma; mejorPuntajeAutor = jugador; }
    }
    public void sumarPartida() { partidasJugadas++; }
    public void considerarDuracion(int rondas) { if (rondas > partidaMasLarga) partidaMasLarga = rondas; }
    public void registrarVictima(String jugador) {
        victimas.put(jugador, victimas.getOrDefault(jugador, 0) + 1);
    }

    public int getMayorApuesta() { return mayorApuesta; }
    public String getMayorApuestaAutor() { return mayorApuestaAutor; }
    public int getMejorPuntaje() { return mejorPuntaje; }
    public String getMejorPuntajeAutor() { return mejorPuntajeAutor; }
    public int getPartidasJugadas() { return partidasJugadas; }
    public int getPartidaMasLarga() { return partidaMasLarga; }
    public Map<String,Integer> getVictimas() { return victimas; }
     
}
