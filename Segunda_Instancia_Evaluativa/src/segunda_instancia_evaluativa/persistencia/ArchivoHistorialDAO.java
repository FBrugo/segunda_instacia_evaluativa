/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.persistencia;

import java.io.*;
import java.util.*;
/**
 *
 * @author fedee
 */
public class ArchivoHistorialDAO {
    private static final String ARCHIVO = "historial.txt";

    public static void guardarPartida(String descripcion) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write(descripcion);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar historial: " + e.getMessage());
        }
    }

    public static List<String> obtenerUltimas(int n) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo historial: " + e.getMessage());
        }

        int size = lineas.size();
        return size <= n ? lineas : lineas.subList(size - n, size);
    }
}
