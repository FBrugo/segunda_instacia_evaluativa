/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa;

import java.util.*;

/**
 *
 * @author rodri
 */
public class JuegoDadosV3 {
    private final Dado d1 = new Dado();
    private final Dado d2 = new Dado();
    private final TrampasEngine trampas;
    private final Estados estados;
    private final TrampasLog trampasLog;

    public JuegoDadosV3(TrampasEngine trampas, Estados estados, TrampasLog trampasLog) {
        this.trampas = trampas;
        this.estados = estados;
        this.trampasLog = trampasLog;
    }

    // retorna false si hay bancarrota
    public boolean jugarRonda(List<Jugador> jugadores, int numeroRonda, Map<Jugador,Integer> apuestas) {
        System.out.println("\n----- Ronda " + numeroRonda + " -----");

        int pozo = 0;
        for (Map.Entry<Jugador,Integer> e : apuestas.entrySet()) {
            Jugador j = e.getKey();
            int ap = e.getValue();
            j.perder(ap);
            pozo += ap;
            estados.registrarApuesta(nombreMostrar(j), ap);
            System.out.printf("%s (%s) apuesta $%d. Saldo: $%d%n",
                    nombreMostrar(j), j.obtenerTipoJugador(), ap, j.getDinero());
        }
        if (pozo == 0) { System.out.println("Nadie pudo apostar. Fin de la partida."); return false; }

        Map<Jugador, Integer> sumaTiradas = new LinkedHashMap<>();

        for (Jugador j : jugadores) {
            if (!apuestas.containsKey(j)) continue;

            int a = d1.tirar();
            int b = d2.tirar();

            if (j instanceof JugadorCasino) {
                int aLoaded = trampas.tirarConDadoCargado(a);
                int bLoaded = trampas.tirarConDadoCargado(b);
                if (aLoaded != a || bLoaded != b) {
                    trampasLog.log("La Casa usó dados cargados: (" + a + "," + b + ") -> (" + aLoaded + "," + bLoaded + ")");
                }
                a = aLoaded; b = bLoaded;
            } else {
                int a2 = trampas.confundirValor(a);
                int b2 = trampas.confundirValor(b);
                if (a2 != a || b2 != b) {
                    trampasLog.log("La Casa confundió a " + nombreMostrar(j) + ": (" + a + "," + b + ") -> (" + a2 + "," + b2 + ")");
                    estados.registrarVictima(nombreMostrar(j));
                }
                a = a2; b = b2;
            }

            int suma = a + b;

            if (j instanceof JugadorVIP) {
                JugadorVIP v = (JugadorVIP) j;
                if (v.tieneReroll() && suma < 8) {
                    int aR = d1.tirar();
                    int bR = d2.tirar();
                    int sumaR = aR + bR;
                    if (sumaR > suma) {
                        v.usarReroll();
                        System.out.printf("%s usó reroll VIP: %d + %d = %d (antes %d)%n",
                                nombreMostrar(j), aR, bR, sumaR, suma);
                        suma = sumaR;
                    }
                }
            }

            sumaTiradas.put(j, suma);
            estados.registrarPuntaje(nombreMostrar(j), suma);
            System.out.printf("%s tiró suma: %d%n", nombreMostrar(j), suma);
        }

        int max = sumaTiradas.values().stream().mapToInt(i -> i).max().orElse(0);
        List<Jugador> ganadores = new ArrayList<>();
        for (Map.Entry<Jugador, Integer> e : sumaTiradas.entrySet()) {
            if (e.getValue() == max) ganadores.add(e.getKey());
        }

        System.out.println("\nPozo: $" + pozo);
        if (ganadores.isEmpty()) {
            System.out.println("Sin ganadores. Pozo vacío.");
        } else if (ganadores.size() == 1) {
            Jugador g = ganadores.get(0);
            g.ganar(pozo);
            g.incrementarPartidasGanadas();
            System.out.printf("Ganador: %s. Gana $%d (Saldo: $%d)%n", nombreMostrar(g), pozo, g.getDinero());
        } else {
            System.out.print("Empate entre: ");
            for (int i = 0; i < ganadores.size(); i++) {
                System.out.print(nombreMostrar(ganadores.get(i)) + (i < ganadores.size()-1 ? ", " : ""));
            }
            System.out.println();
            int base = pozo / ganadores.size();
            int resto = pozo % ganadores.size();
            for (int i = 0; i < ganadores.size(); i++) {
                Jugador g = ganadores.get(i);
                int premio = base + (i < resto ? 1 : 0);
                g.ganar(premio);
                g.incrementarPartidasGanadas();
                System.out.printf("-> %s recibe $%d (Saldo: $%d)%n", nombreMostrar(g), premio, g.getDinero());
            }
        }

        boolean algunoQuebro = jugadores.stream().anyMatch(j -> j.getDinero() <= 0);
        if (algunoQuebro) {
            System.out.println("\nAlguien se quedó sin dinero. Fin de la partida.");
            return false;
        }
        return true;
    }

    private String nombreMostrar(Jugador j) {
        return j.getApodo()!=null ? j.getApodo() : j.getNombre();
    }
}