/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.modelo;

/**
 *
 * @author caito
 */
public class JugadorVIP extends Jugador {
    private boolean rerollDisponible = true;

    // Constructor nuevo (con apodo)
    public JugadorVIP(String nombre, String apodo, int dineroInicial) {
        super(nombre, dineroInicial);
        setApodo(apodo);
    }

    // Constructor viejo (compatibilidad con Primera Instancia)
    public JugadorVIP(String nombre, int dineroInicial) {
        this(nombre, nombre, dineroInicial);
    }

    @Override
    public String obtenerTipoJugador() {
        return "VIP";
    }

    // Métodos específicos de este tipo de jugador
    public boolean tieneReroll() {
        return rerollDisponible;
    }

    public void usarReroll() {
        rerollDisponible = false;
    }
}


