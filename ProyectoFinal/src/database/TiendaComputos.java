package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

import logico.Cliente;
import logico.Empleado;
import logico.Proveedor;

public class TiendaComputos {

	private ConexionDB conexion = new ConexionDB();

	// MÉTODOS DE USUARIO
	public boolean insertarUsuario(String tipo, String user, String pass) {
		String sql = "INSERT INTO Usuario (Tipo, UserName, Contrasena) VALUES (?, ?, ?)";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, tipo);
			ps.setString(2, user);
			ps.setString(3, pass);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar usuario: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarUsuario(String tipo, String oldUser, String user, String pass) {
		String sql = "UPDATE Usuario SET Tipo = ?, UserName = ?, Contrasena = ? WHERE UserName = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, tipo);
			ps.setString(2, user);
			ps.setString(3, pass);
			ps.setString(4, oldUser);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al modificar usuario: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarUsuario(String user) {
		String sql = "DELETE FROM Usuario WHERE UserName = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, user);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage());
			return false;
		}
	}
	
	public boolean insertarCliente(Cliente cliente) {
	    Connection con = null;
	    PreparedStatement psPersona = null;
	    PreparedStatement psCliente = null;
	    ResultSet rs = null;
	    boolean exito = false;

	    String sqlPersona = "INSERT INTO Persona (Nombre, Correo, Edad, Cedula, Clasificacion) VALUES (?, ?, ?, ?, 'Cliente')";
	    String sqlCliente = "INSERT INTO Cliente (Id_Persona, CodCliente, Clasificacion, CantVentas) VALUES (?, ?, ?, ?)";

	    try {
	        con = ConexionDB.getConexion();
	        con.setAutoCommit(false); // Iniciamos transacción

	        // 1. Insertar en Persona
	        psPersona = con.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
	        psPersona.setString(1, cliente.getNombre());
	        psPersona.setString(2, cliente.getCorreo());
	        psPersona.setInt(3, cliente.getEdad());
	        psPersona.setString(4, cliente.getCedula());
	        psPersona.executeUpdate();

	        // 2. Obtener el ID numérico generado por SQL
	        rs = psPersona.getGeneratedKeys();
	        int idPersonaSQL = 0;
	        if (rs.next()) {
	            idPersonaSQL = rs.getInt(1);
	        }

	        // 3. Insertar en Cliente usando el ID de SQL y el id String de Java (CodCliente)
	        psCliente = con.prepareStatement(sqlCliente);
	        psCliente.setInt(1, idPersonaSQL);
	        psCliente.setString(2, cliente.getId()); // Aquí usas tu "CLI-#"
	        psCliente.setString(3, String.valueOf(cliente.getClasificacion()));
	        psCliente.setInt(4, cliente.getCantVentas());
	        psCliente.executeUpdate();

	        con.commit();
	        exito = true;
	    } catch (SQLException e) {
	        try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	        e.printStackTrace();
	    } finally {
	        // Cerrar recursos aquí
	    }
	    return exito;
	}
	
	public boolean insertarEmpleado(Empleado emp) {
	    Connection con = null;
	    PreparedStatement psBusqueda = null;
	    PreparedStatement psPersona = null;
	    PreparedStatement psEmpleado = null;
	    ResultSet rs = null;
	    boolean exito = false;

	    // 1. Sentencia para obtener el ID técnico del Usuario
	    String sqlBusquedaUser = "SELECT Id_Usuario FROM Usuario WHERE Username = ?";
	    String sqlPersona = "INSERT INTO Persona (Nombre, Correo, Edad, Cedula, Clasificacion) VALUES (?, ?, ?, ?, 'Empleado')";
	    String sqlEmpleado = "INSERT INTO Empleado (Id_Persona, CodEmpleado, ComisionVentas, CantVentas, Id_Usuario, EmpleadoMes) VALUES (?, ?, ?, ?, ?, ?)";

	    try {
	        con = ConexionDB.getConexion(); // Usando tu clase de conexión
	        con.setAutoCommit(false); // Iniciamos transacción por seguridad

	        // PASO 1: Recuperar el Id_Usuario a partir del Username
	        psBusqueda = con.prepareStatement(sqlBusquedaUser);
	        psBusqueda.setString(1, emp.getUsuario().getUserName());
	        rs = psBusqueda.executeQuery();
	        
	        int idUsuarioSQL = -1;
	        if (rs.next()) {
	            idUsuarioSQL = rs.getInt("Id_Usuario");
	        } else {
	            System.out.println("Error: El username no existe en la base de datos.");
	            return false; 
	        }

	        // PASO 2: Insertar en la tabla Persona
	        psPersona = con.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
	        psPersona.setString(1, emp.getNombre());
	        psPersona.setString(2, emp.getCorreo());
	        psPersona.setInt(3, emp.getEdad());
	        psPersona.setString(4, emp.getCedula());
	        psPersona.executeUpdate();

	        // Recuperar el Id_Persona generado por SQL
	        ResultSet rsPersona = psPersona.getGeneratedKeys();
	        int idPersonaSQL = 0;
	        if (rsPersona.next()) {
	            idPersonaSQL = rsPersona.getInt(1);
	        }

	        // PASO 3: Insertar en la tabla Empleado vinculando los IDs
	        psEmpleado = con.prepareStatement(sqlEmpleado);
	        psEmpleado.setInt(1, idPersonaSQL); // ID de Persona (Identity)
	        psEmpleado.setString(2, emp.getId()); // Tu código de negocio EMP-#
	        psEmpleado.setFloat(3, emp.getComisionVentas());
	        psEmpleado.setInt(4, emp.getCantVentas());
	        psEmpleado.setInt(5, idUsuarioSQL); // El ID que recuperamos por Username
	        psEmpleado.setBoolean(6, emp.isEmpleadoMes());
	        psEmpleado.executeUpdate();

	        con.commit(); // Guardamos todo
	        exito = true;
	        System.out.println("Empleado insertado correctamente con su usuario vinculado.");

	    } catch (SQLException e) {
	        if (con != null) {
	            try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	        }
	        e.printStackTrace();
	    } finally {
	        // Importante cerrar los recursos para evitar fugas de memoria
	        try {
	            if (rs != null) rs.close();
	            if (psBusqueda != null) psBusqueda.close();
	            if (psPersona != null) psPersona.close();
	            if (psEmpleado != null) psEmpleado.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return exito;
	}
	
	public boolean insertarProveedor(Proveedor prov) {
	    Connection con = null;
	    PreparedStatement psPersona = null;
	    PreparedStatement psProv = null;
	    ResultSet rs = null;
	    boolean exito = false;

	    String sqlPersona = "INSERT INTO Persona (Nombre, Correo, Edad, Cedula, Clasificacion) VALUES (?, ?, ?, ?, 'Proveedor')";
	    String sqlProveedor = "INSERT INTO Proveedor (Id_Persona, Empresa, CodProveedor) VALUES (?, ?, ?)";

	    try {
	        con = ConexionDB.getConexion();
	        con.setAutoCommit(false);

	        psPersona = con.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
	        psPersona.setString(1, prov.getNombre());
	        psPersona.setString(2, prov.getCorreo());
	        psPersona.setInt(3, prov.getEdad());
	        psPersona.setString(4, prov.getCedula());
	        psPersona.executeUpdate();

	        rs = psPersona.getGeneratedKeys();
	        int idPersonaSQL = 0;
	        if (rs.next()) idPersonaSQL = rs.getInt(1);

	        psProv = con.prepareStatement(sqlProveedor);
	        psProv.setInt(1, idPersonaSQL);
	        psProv.setString(2, prov.getEmpresa());
	        psProv.setString(3, prov.getId()); // Tu "PRV-#"
	        psProv.executeUpdate();

	        con.commit();
	        exito = true;
	    } catch (SQLException e) {
	        try { if (con != null) con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
	        e.printStackTrace();
	    }
	    return exito;
	}
	
	
}