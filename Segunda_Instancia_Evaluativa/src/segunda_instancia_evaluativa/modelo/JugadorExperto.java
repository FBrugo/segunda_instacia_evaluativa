/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.modelo;

/**
 *
 * @author caito
 */
public class JugadorExperto extends Jugador {
    public JugadorExperto(String nombre, int dineroInicial) { super(nombre, dineroInicial); }
    @Override public String obtenerTipoJugador() { return "Experto"; }
}

