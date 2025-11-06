/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa;

/**
 *
 * @author caito
 */
public class JugadorNovato extends Jugador {
    public JugadorNovato(String nombre, int dineroInicial) { super(nombre, dineroInicial); }
    @Override public String obtenerTipoJugador() { return "Novato"; }
}

