package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=TiendaComputos;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "proyectotiendabd";
    private static final String PASS = "proyectotiendabd";

    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }
}
