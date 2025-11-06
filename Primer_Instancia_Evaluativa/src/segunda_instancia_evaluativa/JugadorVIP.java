/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa;

/**
 *
 * @author caito
 */
public class JugadorVIP extends Jugador {
    private boolean rerollDisponible = true; // 1 vez por partida
    public JugadorVIP(String nombre, int dineroInicial) { super(nombre, dineroInicial); }
    @Override public String obtenerTipoJugador() { return "VIP"; }
    public boolean tieneReroll() { return rerollDisponible; }
    public void usarReroll() { rerollDisponible = false; }
}

