/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.modelo;

/**
 *
 * @author caito
 */
public class JugadorNovato extends Jugador {
    // Constructor nuevo (con apodo)
    public JugadorNovato(String nombre, String apodo, int dineroInicial) {
        super(nombre, dineroInicial);
        setApodo(apodo);
    }

    // Constructor viejo (compatibilidad con Primera Instancia)
    public JugadorNovato(String nombre, int dineroInicial) {
        this(nombre, nombre, dineroInicial); // Usa el nombre como apodo por defecto
    }

    @Override
    public String obtenerTipoJugador() {
        return "Novato";
    }
}


