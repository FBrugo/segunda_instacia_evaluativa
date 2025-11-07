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

        // Configurar modelo de la tabla de jugadores
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"Nombre", "Apodo", "Tipo", "Dinero Inicial"}, 0
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

    private void agregarJugador() {
        limpiarError();

        String nombre = vista.getTxtNombre().getText().trim();
        String apodo = vista.getTxtApodo().getText().trim();
        String tipoStr = (String) vista.getCmbTipoJugador().getSelectedItem();
        String dineroStr = vista.getTxtDineroInicial().getText().trim();

        if (nombre.isEmpty() || apodo.isEmpty() || dineroStr.isEmpty()) {
            mostrarError("Nombre, apodo y dinero son obligatorios.");
            return;
        }

        int dinero;
        try {
            dinero = Integer.parseInt(dineroStr);
            if (dinero <= 0) {
                mostrarError("El dinero inicial debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException ex) {
            mostrarError("El dinero inicial debe ser un número entero.");
            return;
        }

        // Validación de apodo (según tu lógica previa)
        if (!ValidacionApodo.esValido(apodo)) {
            mostrarError("El apodo no es válido según las reglas del juego.");
            return;
        }

        // Apodo único
        for (Jugador j : config.getJugadores()) {
            if (j.getApodo().equalsIgnoreCase(apodo)) {
                mostrarError("Ya existe un jugador con ese apodo.");
                return;
            }
        }

        // 2–4 jugadores total (máximo 4)
        if (config.getJugadores().size() >= 4) {
            mostrarError("No se pueden agregar más de 4 jugadores.");
            return;
        }

        // Crear jugador según el tipo
        Jugador nuevo = crearJugadorPorTipo(nombre, apodo, tipoStr, dinero);
        config.agregarJugador(nuevo);

        // Agregar a la tabla
        modeloTabla.addRow(new Object[]{nombre, apodo, tipoStr, dinero});

        // Limpiar campos
        vista.getTxtNombre().setText("");
        vista.getTxtApodo().setText("");
        vista.getTxtDineroInicial().setText("500");
        vista.getCmbTipoJugador().setSelectedIndex(0);
    }

    private Jugador crearJugadorPorTipo(String nombre, String apodo, String tipoStr, int dinero) {
        // Ajustá los constructores si tus clases tienen otra firma
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
            if (j.getApodo().equalsIgnoreCase(apodo)) {
                aEliminar = j;
                break;
            }
        }
        if (aEliminar != null) {
            config.eliminarJugador(aEliminar);
        }

        modeloTabla.removeRow(fila);
    }

    private void iniciarJuego() {
        limpiarError();

        int cantJugadores = config.getJugadores().size();
        if (cantJugadores < 2 || cantJugadores > 4) {
            mostrarError("La partida requiere entre 2 y 4 jugadores.");
            return;
        }

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

