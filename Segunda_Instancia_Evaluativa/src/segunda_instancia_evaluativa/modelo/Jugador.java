/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.modelo;

/**
 *
 * @author fedee
 */

public abstract class Jugador {
    private String nombre;
    private String apodo;              // Validado con ValidacionApodo
    private int dinero;
    private int partidasGanadas;

    // ====== CONSTRUCTOR BASE ======
    public Jugador(String nombre, int dineroInicial) {
        this.nombre = nombre;
        setDinero(dineroInicial);
    }

    // ====== GETTERS Y SETTERS ======
    public String getNombre() {
        return nombre;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        // Validación básica de apodo
        if (apodo == null || !ValidacionApodo.esValido(apodo)) {
            throw new IllegalArgumentException("Apodo inválido (3-10 letras/espacios).");
        }
        this.apodo = apodo;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = Math.max(0, dinero);
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    public void incrementarPartidasGanadas() {
        this.partidasGanadas++;
    }

    // ====== MÉTODOS DE LÓGICA ======
    public void ganar(int cantidad) {
        if (cantidad > 0)
            setDinero(this.dinero + cantidad);
    }

    public void perder(int cantidad) {
        if (cantidad > 0)
            setDinero(this.dinero - Math.min(cantidad, this.dinero));
    }

    // ====== MÉTODOS ABSTRACTOS ======
    public abstract String obtenerTipoJugador();

    // ====== MÉTODOS DE COMPATIBILIDAD ======
    /**
     * Antes se usaba getTipo() → ahora devuelve obtenerTipoJugador()
     */
    public String getTipo() {
        return obtenerTipoJugador();
    }

    /**
     * Compatibilidad con código viejo (getVictorias / incrementarVictorias)
     */
    public int getVictorias() {
        return getPartidasGanadas();
    }

    public void incrementarVictorias() {
        incrementarPartidasGanadas();
    }

    @Override
    public String toString() {
        return nombre + " (" + apodo + ") - " + obtenerTipoJugador() + " - $" + dinero;
    }
}

