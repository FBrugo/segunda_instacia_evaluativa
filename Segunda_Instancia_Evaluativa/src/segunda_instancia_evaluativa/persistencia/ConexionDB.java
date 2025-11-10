/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package segunda_instancia_evaluativa.persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConexionDB {
    // URL de conexión JDBC (puerto por defecto 1433)
    private static final String URL  = "jdbc:sqlserver://localhost:1433;databaseName=CasinoDB;encrypt=false;";
    private static final String USER = "casino";
    private static final String PASS = "casino";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
