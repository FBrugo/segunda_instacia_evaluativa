/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa;

/**
 *
 * @author fedee
 */

public abstract class Jugador {
    private String nombre;
    private String apodo;              // se validará con ApodoValidator
    private int dinero;
    private int partidasGanadas;

    public Jugador(String nombre, int dineroInicial) {
        this.nombre = nombre;
        setDinero(dineroInicial);
    }

    public String getNombre() { return nombre; }
    public String getApodo() { return apodo; }
    public void setApodo(String apodo) {
        if (apodo == null || !ValidacionApodo.esValido(apodo)) {
            throw new IllegalArgumentException("Apodo inválido (3-10 letras/espacios).");
        }
        this.apodo = apodo;
    }

    public int getDinero() { return dinero; }
    public void setDinero(int dinero) { this.dinero = Math.max(0, dinero); }

    public int getPartidasGanadas() { return partidasGanadas; }
    public void incrementarPartidasGanadas() { this.partidasGanadas++; }

    public void ganar(int cantidad) {
        if (cantidad > 0) setDinero(this.dinero + cantidad);
    }

    public void perder(int cantidad) {
        if (cantidad > 0) setDinero(this.dinero - Math.min(cantidad, this.dinero));
    }

    public abstract String obtenerTipoJugador();
}

