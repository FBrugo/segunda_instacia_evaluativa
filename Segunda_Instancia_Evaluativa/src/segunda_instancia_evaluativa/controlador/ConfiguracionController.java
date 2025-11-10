/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.controlador;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import segunda_instancia_evaluativa.modelo.ConfiguracionJuego;
import segunda_instancia_evaluativa.modelo.Jugador;
import segunda_instancia_evaluativa.modelo.JugadorCasino;
import segunda_instancia_evaluativa.modelo.JugadorExperto;
import segunda_instancia_evaluativa.modelo.JugadorNovato;
import segunda_instancia_evaluativa.modelo.JugadorVIP;
import segunda_instancia_evaluativa.modelo.ValidacionApodo;
import segunda_instancia_evaluativa.vista.frmVentanaConfiguracionInicial;
import segunda_instancia_evaluativa.vista.frmVentanaJuego;

public class ConfiguracionController {

    private final frmVentanaConfiguracionInicial vista;
    private final AppController appController;
    private final ConfiguracionJuego config;
    private final DefaultTableModel modeloTabla;

    public ConfiguracionController(frmVentanaConfiguracionInicial vista, AppController appController) {
        this.vista = vista;
        this.appController = appController;

        this.config = new ConfiguracionJuego();
        this.appController.setConfiguracion(config);

        // Tabla de jugadores: solo muestra Nombre, Apodo y Tipo
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Apodo", "Tipo"}, 0
        );
        this.vista.getTblJugadores().setModel(modeloTabla);

        inicializar();
    }

    private void inicializar() {
        vista.getBtnAgregarJugador().addActionListener(e -> agregarJugador());
        vista.getBtnEliminarJugador().addActionListener(e -> eliminarJugador());
        vista.getBtnIniciarJuego().addActionListener(e -> iniciarJuego());
        vista.getBtnSalir().addActionListener(e -> salir());

        vista.getLblErrores().setText("");
    }

    private void mostrarError(String mensaje) {
        vista.getLblErrores().setText(mensaje);
    }

    private void limpiarError() {
        vista.getLblErrores().setText("");
    }

    /** Agrega un jugador a la configuración (sin dinero individual). */
    private void agregarJugador() {
        limpiarError();

        String nombre = vista.getTxtNombre().getText().trim();
        String apodo = vista.getTxtApodo().getText().trim();
        String tipoStr = (String) vista.getCmbTipoJugador().getSelectedItem();

        if (nombre.isEmpty() || apodo.isEmpty()) {
            mostrarError("Nombre y apodo son obligatorios.");
            return;
        }

        // Validación de apodo
        if (!ValidacionApodo.esValido(apodo)) {
            mostrarError("El apodo no es válido según las reglas del juego.");
            return;
        }

        // Apodo único
        for (Jugador j : config.getJugadores()) {
            String apodoExistente = j.getApodo();
            if (apodoExistente != null && apodoExistente.equalsIgnoreCase(apodo)) {
                mostrarError("Ya existe un jugador con ese apodo.");
                return;
            }
        }

        // Límite de jugadores
        if (config.getJugadores().size() >= 4) {
            mostrarError("No se pueden agregar más de 4 jugadores.");
            return;
        }

        // Crear jugador con dinero 0 (se asignará después al iniciar juego)
        Jugador nuevo = crearJugadorPorTipo(nombre, apodo, tipoStr, 0);
        config.agregarJugador(nuevo);

        // Agregar solo los datos visibles a la tabla
        modeloTabla.addRow(new Object[]{nombre, apodo, tipoStr});

        // Limpiar campos
        vista.getTxtNombre().setText("");
        vista.getTxtApodo().setText("");
        vista.getCmbTipoJugador().setSelectedIndex(0);
    }

    private Jugador crearJugadorPorTipo(String nombre, String apodo, String tipoStr, int dinero) {
        switch (tipoStr.toUpperCase()) {
            case "NOVATO":
                return new JugadorNovato(nombre, apodo, dinero);
            case "EXPERTO":
                return new JugadorExperto(nombre, apodo, dinero);
            case "VIP":
                return new JugadorVIP(nombre, apodo, dinero);
            case "CASINO":
                return new JugadorCasino(nombre, apodo, dinero);
            default:
                return new JugadorNovato(nombre, apodo, dinero);
        }
    }

    private void eliminarJugador() {
        limpiarError();
        int fila = vista.getTblJugadores().getSelectedRow();

        if (fila == -1) {
            mostrarError("Seleccioná un jugador de la tabla para eliminar.");
            return;
        }

        String apodo = (String) modeloTabla.getValueAt(fila, 1);

        Jugador aEliminar = null;
        for (Jugador j : config.getJugadores()) {
            if (j.getApodo() != null && j.getApodo().equalsIgnoreCase(apodo)) {
                aEliminar = j;
                break;
            }
        }
        if (aEliminar != null) {
            config.eliminarJugador(aEliminar);
        }

        modeloTabla.removeRow(fila);
    }

    /** Valida configuración general y abre la ventana de juego. */
    private void iniciarJuego() {
        limpiarError();

        int cantJugadores = config.getJugadores().size();
        if (cantJugadores < 2 || cantJugadores > 4) {
            mostrarError("La partida requiere entre 2 y 4 jugadores.");
            return;
        }

        // Leer dinero inicial (global)
        String dineroStr = vista.getTxtDineroInicial().getText().trim();
        if (dineroStr.isEmpty()) {
            mostrarError("Debés indicar el dinero inicial de la partida.");
            return;
        }

        int dineroInicial;
        try {
            dineroInicial = Integer.parseInt(dineroStr);
            if (dineroInicial <= 0) {
                mostrarError("El dinero inicial debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            mostrarError("El dinero inicial debe ser un número entero.");
            return;
        }

        // Guardar configuración global
        config.setDineroInicial(dineroInicial);
        for (Jugador j : config.getJugadores()) {
            j.setDinero(dineroInicial);
        }

        // Configuración de partidas/rondas/trampas
        String cantPartidasStr = (String) vista.getCmbCantidadPartidas().getSelectedItem();
        String rondasStr = (String) vista.getCmbRondasPorPartida().getSelectedItem();

        int cantPartidas = Integer.parseInt(cantPartidasStr);
        int rondas = Integer.parseInt(rondasStr);

        config.setCantidadPartidas(cantPartidas);
        config.setRondasPorPartida(rondas);
        config.setTrampasActivadas(vista.getChkTrampasActivadas().isSelected());

        // Abrir ventana de juego
        frmVentanaJuego vistaJuego = new frmVentanaJuego();
        new JuegoController(vistaJuego, config, appController.getEstados());
        vistaJuego.setLocationRelativeTo(vista);
        vistaJuego.setVisible(true);

        vista.dispose();
    }

    private void salir() {
        int opc = JOptionPane.showConfirmDialog(
                vista,
                "¿Seguro que querés salir?",
                "Salir",
                JOptionPane.YES_NO_OPTION
        );
        if (opc == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}

