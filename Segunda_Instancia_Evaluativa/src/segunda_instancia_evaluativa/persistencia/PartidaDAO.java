/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.persistencia;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author fedee
 */
public class PartidaDAO {
    // Guarda UNA partida en la tabla partidas
    public static void guardarPartida(String ganadorApodo, int rondas, double pozo) {
        String sql = "INSERT INTO partidas (ganador_apodo, rondas, pozo) VALUES (?,?,?)";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ganadorApodo);
            ps.setInt(2, rondas);
            ps.setBigDecimal(3, BigDecimal.valueOf(pozo));

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error guardando partida en BD: " + e.getMessage());
        }
    }

    // Devuelve últimas N partidas formateadas
    public static List<String> obtenerUltimas(int n) {
        List<String> lista = new ArrayList<>();
        String sql =
            "SELECT TOP (?) id, fecha, ganador_apodo, rondas, pozo " +
            "FROM partidas ORDER BY id DESC";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    Timestamp fecha = rs.getTimestamp("fecha");
                    String ganador = rs.getString("ganador_apodo");
                    int rondas = rs.getInt("rondas");
                    double pozo = rs.getDouble("pozo");

                    String linea = "PARTIDA #" + id +
                            " - Fecha: " + fecha +
                            " - Ganador: " + ganador +
                            " | Rondas: " + rondas +
                            " | Pozo: $" + pozo;

                    lista.add(linea);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo historial de partidas: " + e.getMessage());
        }

        return lista;
    }
}
