package database; // O el paquete donde la hayas creado

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class TiendaComputos {
    
    // Instancia de tu clase de conexión (ajusta el nombre si es distinto)
    private ConexionDB conexion = new ConexionDB();

    /**
     * Método para insertar un nuevo usuario en la tabla Users
     */
    public boolean insertarUsuario(String tipo, String user, String pass) {
        String sql = "INSERT INTO Users (Tipo, UserName, Password) VALUES (?, ?, ?)";
        
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, tipo);
            ps.setString(2, user);
            ps.setString(3, pass);
            
            int resultado = ps.executeUpdate();
            return resultado > 0; // Retorna true si se insertó al menos una fila
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarUsuario(String tipo, String oldUser, String user, String pass) {
        String sql = "UPDATE Users SET Tipo = ?, UserName = ?, Password = ? where UserName = ?";
        
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setString(2, user);
            ps.setString(3, pass);
            ps.setString(4, oldUser);
            int resultado = ps.executeUpdate();
            return resultado > 0; // Retorna true si se modificó al menos una fila
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarUsuario(String user) {
        String sql = "DELETE FROM Users where UserName = ?";
        
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            int resultado = ps.executeUpdate();
            return resultado > 0; // Retorna true si se borró al menos una fila
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }
    

    // Aquí puedes seguir agregando métodos como:
    // public boolean insertarProducto(...) { ... }
    // public boolean insertarCliente(...) { ... }
}
//.