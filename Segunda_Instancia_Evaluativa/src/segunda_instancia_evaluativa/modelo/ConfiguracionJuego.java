/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.modelo;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author fedee
 */
public class ConfiguracionJuego {
    
    private int dineroInicial = 500;
    private int cantidadPartidas = 3;
    private int rondasPorPartida = 3;
    private boolean trampasActivadas = true;
    private final List<Jugador> jugadores = new ArrayList<>();

    public int getDineroInicial() {
        return dineroInicial;
    }

    public void setDineroInicial(int dineroInicial) {
        this.dineroInicial = dineroInicial;
    }

    public int getCantidadPartidas() {
        return cantidadPartidas;
    }

    public void setCantidadPartidas(int cantidadPartidas) {
        this.cantidadPartidas = cantidadPartidas;
    }

    public int getRondasPorPartida() {
        return rondasPorPartida;
    }

    public void setRondasPorPartida(int rondasPorPartida) {
        this.rondasPorPartida = rondasPorPartida;
    }

    public boolean isTrampasActivadas() {
        return trampasActivadas;
    }

    public void setTrampasActivadas(boolean trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void agregarJugador(Jugador j) {
        jugadores.add(j);
    }

    public void eliminarJugador(Jugador j) {
        jugadores.remove(j);
    }
}
