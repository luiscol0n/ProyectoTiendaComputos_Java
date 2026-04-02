package database;

import java.sql.Connection;

public class PruebaConexion {
    public static void main(String[] args) {
        Connection cn = ConexionDB.getConexion();
        
        if (cn != null) {
            System.out.println("¡Conexión establecida con TiendaComputos!");
            try {
                cn.close(); // Siempre cerramos la conexión al terminar
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Fallo en la conexión. Revisa el usuario o el puerto 1433.");
        }
    }
}