/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package segunda_instancia_evaluativa;
import java.util.*;
/**
 *
 * @author fedee
 */



public class Primer_Instancia_Evaluativa {
    private static final List<String> HISTORIAL = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== PRIMERA INSTANCIA EVALUATIVA - CASINO DE DADOS ===");

        int n = pedirEntero(sc, "¿Cuántos jugadores participarán? (2-4, sin contar a La Casa): ", 2, 4);

        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            System.out.print("Nombre real del jugador " + i + ": ");
            String nombre = sc.nextLine().trim();

            String apodo;
            while (true) {
                System.out.print("Apodo (3-10, solo letras y espacios): ");
                apodo = sc.nextLine().trim();
                if (ValidacionApodo.esValido(apodo)) break;
                System.out.println("Apodo inválido. Intente nuevamente.");
            }

            int tipo = pedirEntero(sc, "Tipo (1=Novato, 2=Experto, 3=VIP): ", 1, 3);
            int dineroInicial = pedirEntero(sc, "¿Con cuánta plata ingresa? (mín 1): ", 1, Integer.MAX_VALUE);

            Jugador j = crearJugadorPorTipo(nombre, tipo, dineroInicial);
            j.setApodo(apodo);
            jugadores.add(j);
        }

        // Agregar La Casa
        Jugador laCasa = new JugadorCasino("Casino", 5000);
        laCasa.setApodo("La Casa");
        jugadores.add(laCasa);

        Estados estados = new Estados();
        TrampasLog trampasLog = new TrampasLog();
        TrampasEngine trampas = new TrampasEngine(0.30, 0.30);
        JuegoDadosV3 juego = new JuegoDadosV3(trampas, estados, trampasLog);
        ProcesadorComandos comandos = new ProcesadorComandos(jugadores, estados, HISTORIAL, trampasLog);

        int rondas = 0;
        boolean seguir = true;

        while (seguir && rondas < 3) {
            rondas++;

            // Apuestas manuales
            Map<Jugador,Integer> apuestas = new LinkedHashMap<>();
            for (Jugador j : jugadores) {
                if (j.getDinero() <= 0) continue;
                int ap = pedirEntero(sc,
                        String.format("Apuesta de %s (%s) - Saldo $%d: ",
                                nombreMostrar(j), j.obtenerTipoJugador(), j.getDinero()),
                        1, j.getDinero());
                apuestas.put(j, ap);
            }

            seguir = juego.jugarRonda(jugadores, rondas, apuestas);

            // Comandos / continuar
            System.out.print("\n(Comando opcional) ESTADOS | HISTORY | RANKING | TRAMPAS | SAVE [nombre] | CONTINUAR | QUIT : ");
            String line = sc.nextLine();
            if (!line.equalsIgnoreCase("CONTINUAR") && !line.isBlank()) {
                if (!comandos.procesar(line)) break;
            }
        }

        estados.sumarPartida();
        estados.considerarDuracion(rondas);

        // Historial de la partida
        String detalle = armarDetallePartida(jugadores, rondas, estados.getPartidasJugadas());
        guardarPartida(detalle);
        mostrarHistorial();

        // Reporte final
        String reporte = armarReporteFinal(jugadores, estados);
        System.out.println(reporte);

        sc.close();
    }

    private static Jugador crearJugadorPorTipo(String nombre, int tipo, int dineroInicial) {
        switch (tipo) {
            case 1: return new JugadorNovato(nombre, dineroInicial);
            case 2: return new JugadorExperto(nombre, dineroInicial);
            case 3: return new JugadorVIP(nombre, dineroInicial);
            default: return new JugadorNovato(nombre, dineroInicial);
        }
    }

    private static int pedirEntero(Scanner sc, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.println("Valor inválido. Debe ser un número entre " + min + " y " + (max==Integer.MAX_VALUE?"∞":max));
        }
    }

    private static String nombreMostrar(Jugador j) {
        return j.getApodo()!=null ? j.getApodo() : j.getNombre();
    }

    // ==== Historial (últimas 5 partidas) ====
    private static void guardarPartida(String detalle) {
        HISTORIAL.add(detalle);
        while (HISTORIAL.size() > 5) HISTORIAL.remove(0);
    }

    private static void mostrarHistorial() {
        System.out.println("\n--- HISTORIAL RECIENTE (máx 5) ---");
        if (HISTORIAL.isEmpty()) System.out.println("(vacío)");
        else for (String h : HISTORIAL) System.out.println("• " + h);
    }

    private static String armarDetallePartida(List<Jugador> jugadores, int rondas, int nro) {
        StringBuilder sb = new StringBuilder();
        sb.append("PARTIDA #").append(nro).append(" - Jugadores: ");
        boolean first = true;
        for (Jugador j : jugadores) {
            if (j instanceof JugadorCasino) continue;
            if (!first) sb.append(",");
            sb.append(nombreMostrar(j));
            first = false;
        }
        Jugador ganador = jugadores.stream().max(Comparator.comparingInt(Jugador::getDinero)).orElse(null);
        sb.append(" | Ganador: ").append(ganador!=null? nombreMostrar(ganador) : "N/D");
        sb.append(" | Rondas: ").append(rondas);
        return sb.toString();
    }

    // ==== Reporte Final ====
    private static String armarReporteFinal(List<Jugador> jugadores, Estados estados) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================================\n");
        sb.append("REPORTE FINAL DEL CASINO\n");
        sb.append("=======================================\n");
        long cantHumanos = jugadores.stream().filter(j -> !(j instanceof JugadorCasino)).count();
        sb.append("Jugadores participantes: ").append(cantHumanos).append("\n");
        sb.append("Total de partidas jugadas: ").append(estados.getPartidasJugadas()).append("\n");

        sb.append("--- RANKING FINAL ---\n");
        jugadores.stream()
            .sorted((a,b) -> Integer.compare(b.getDinero(), a.getDinero()))
            .forEach(j -> sb.append(String.format("%s - $%d - %d partidas ganadas (%s)\n",
                nombreMostrar(j), j.getDinero(), j.getPartidasGanadas(), j.obtenerTipoJugador())));

        sb.append("\n--- ESTADÍSTICAS GENERALES ---\n");
        sb.append("Mayor apuesta realizada: $").append(estados.getMayorApuesta())
          .append(" (").append(estados.getMayorApuestaAutor()).append(")\n");
        sb.append("Mejor puntaje de dados: ").append(estados.getMejorPuntaje())
          .append(" (").append(estados.getMejorPuntajeAutor()).append(")\n");

        if (estados.getVictimas().isEmpty()) {
            sb.append("Jugadores afectados por trampas: (ninguno)\n");
        } else {
            sb.append("Jugadores afectados por trampas: ");
            boolean first = true;
            for (Map.Entry<String,Integer> e : estados.getVictimas().entrySet()) {
                if (!first) sb.append(", ");
                sb.append(e.getKey()).append("(").append(e.getValue()).append(")");
                first = false;
            }
            sb.append("\n");
        }
        sb.append("Partida más larga: ").append(estados.getPartidaMasLarga()).append(" rondas\n");

        sb.append("\n--- HISTORIAL RECIENTE ---\n");
        if (HISTORIAL.isEmpty()) sb.append("(vacío)\n");
        else for (String h : HISTORIAL) sb.append(h).append("\n");

        sb.append("=======================================\n");
        return sb.toString();
    }
}

