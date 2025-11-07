/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.modelo;

/**
 *
 * @author caito
 */
public class JugadorCasino extends Jugador {
    public JugadorCasino(String nombre, String apodo, int dineroInicial) { super(nombre, dineroInicial); }
    @Override public String obtenerTipoJugador() { return "La Casa"; }
}

