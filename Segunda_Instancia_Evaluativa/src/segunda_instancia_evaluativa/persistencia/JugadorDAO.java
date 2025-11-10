/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.persistencia;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import segunda_instancia_evaluativa.modelo.Jugador;
/**
 *
 * @author fedee
 */
public class JugadorDAO {
    public static void guardarJugadores(List<Jugador> jugadores) {
        String deleteSql = "DELETE FROM jugadores"; // o TRUNCATE TABLE jugadores
        String insertSql = "INSERT INTO jugadores (nombre, apodo, tipo, dinero, victorias) VALUES (?,?,?,?,?)";

        try (Connection con = ConexionDB.getConnection();
             Statement st = con.createStatement();
             PreparedStatement ps = con.prepareStatement(insertSql)) {

            st.executeUpdate(deleteSql);

            for (Jugador j : jugadores) {
                ps.setString(1, j.getNombre());
                ps.setString(2, j.getApodo());
                // si no tenés getTipo(), uso el nombre de la clase
                ps.setString(3, j.getClass().getSimpleName());

                // 👇 ajustá estos getters si tus nombres son distintos
                ps.setBigDecimal(4, BigDecimal.valueOf(j.getDinero()));
                ps.setInt(5, j.getVictorias());

                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            System.err.println("Error guardando jugadores en BD: " + e.getMessage());
        }
    }

    /**
     * Devuelve el ranking actual (ordenado por dinero desc)
     * como lista de filas lista para meter en un JTable.
     */
    public static List<Object[]> obtenerRanking() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT nombre, tipo, dinero, victorias FROM jugadores ORDER BY dinero DESC";

        try (Connection con = ConexionDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String tipo   = rs.getString("tipo");
                double dinero = rs.getDouble("dinero");
                int victorias = rs.getInt("victorias");

                lista.add(new Object[]{nombre, tipo, dinero, victorias});
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo ranking: " + e.getMessage());
        }

        return lista;
    }
}
