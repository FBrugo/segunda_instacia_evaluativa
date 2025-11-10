/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.persistencia;

import java.sql.Connection;
import java.sql.SQLException;

public class pruebaDB {
    public static void main(String[] args) {
        try (Connection con = ConexionDB.getConnection()) {
            System.out.println("✅ Conexión exitosa a " + con.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar: " + e.getMessage());
        }
    }
}
