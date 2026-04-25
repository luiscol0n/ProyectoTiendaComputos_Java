package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.time.LocalDate;

import logico.Tienda;
import logico.Persona;
import logico.Cliente;
import logico.Empleado;
import logico.Proveedor;
import logico.Producto;
import logico.MotherBoard;
import logico.DiscoDuro;
import logico.MemoriaRam;
import logico.Microprocesador;
import logico.FacturaVenta;
import logico.FacturaCompra;
import logico.DetalleFacturaCompra;
import logico.DetalleFacturaVenta;

public class TiendaComputos {

	private static TiendaComputos instance = null;
	private Connection con;

	private TiendaComputos() {
		this.con = ConexionDB.getConexion();
	}

	public static TiendaComputos getInstance() {
		if (instance == null) {
			instance = new TiendaComputos();
		}
		return instance;
	}

	private String ultimoErrorProducto = "";

	public String getUltimoErrorProducto() {
		return ultimoErrorProducto;
	}

	public void limpiarTablasTotal() {
		Connection con = null;
		Statement st = null;
		try {
			con = ConexionDB.getConexion();
			st = con.createStatement();

			// 1. Desactivar temporalmente las restricciones de llaves for�neas
			// Esto evita errores de "conflicto con FK" al borrar en desorden
			st.executeUpdate("EXEC sp_MSforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'");

			// 2. Borrar los datos de todas las tablas
			// El orden ideal es de hijas a padres
			st.executeUpdate("DELETE FROM DetalleFacturaVenta");
			st.executeUpdate("DELETE FROM DetalleFacturaCompra");
			st.executeUpdate("DELETE FROM FacturaVenta");
			st.executeUpdate("DELETE FROM FacturaCompra");
			st.executeUpdate("DELETE FROM Factura");
			st.executeUpdate("DELETE FROM Empleado");
			st.executeUpdate("DELETE FROM Cliente");
			st.executeUpdate("DELETE FROM Proveedor");
			st.executeUpdate("DELETE FROM Producto");
			st.executeUpdate("DELETE FROM Usuario");
			st.executeUpdate("DELETE FROM Persona");

			// 3. Reiniciar los contadores IDENTITY de las tablas principales
			st.executeUpdate("DBCC CHECKIDENT ('Persona', RESEED, 0)");
			st.executeUpdate("DBCC CHECKIDENT ('Usuario', RESEED, 0)");
			st.executeUpdate("DBCC CHECKIDENT ('Producto', RESEED, 0)");
			st.executeUpdate("DBCC CHECKIDENT ('Factura', RESEED, 0)");

			// 4. Reactivar las restricciones de llaves for�neas
			st.executeUpdate("EXEC sp_MSforeachtable 'ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL'");

			System.out.println("Base de datos reseteada por completo: AML Tech est� en blanco.");

		} catch (SQLException e) {
			System.err.println("Error al resetear la base de datos: " + e.getMessage());
		} finally {
			// Cerrar recursos
		}
	}

	public int recuperarIdPersonaPorCodigo(String codigo, String tipo) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int idEncontrado = -1;

		// Usamos el c�digo de la tabla hija para encontrar el ID en la tabla padre
		String sql = "";
		if (tipo.equalsIgnoreCase("Empleado")) {
			sql = "SELECT Id_Persona FROM Empleado WHERE CodEmpleado = ?";
		} else if (tipo.equalsIgnoreCase("Cliente")) {
			sql = "SELECT Id_Persona FROM Cliente WHERE CodCliente = ?";
		} else if (tipo.equalsIgnoreCase("Proveedor")) {
			sql = "SELECT Id_Persona FROM Proveedor WHERE CodProveedor = ?";
		}

		try {
			con = ConexionDB.getConexion();
			ps = con.prepareStatement(sql);
			ps.setString(1, codigo);
			rs = ps.executeQuery();

			if (rs.next()) {
				idEncontrado = rs.getInt("Id_Persona");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Cerrar recursos
		}
		return idEncontrado;
	}

	public boolean eliminarPorCodigo(String codigo, String tipo) {
		Connection con = null;
		PreparedStatement psId = null;
		PreparedStatement psDel = null;
		ResultSet rs = null;
		boolean exito = false;

		// Determinamos en qu� tabla buscar el ID t�cnico primero
		String tabla = tipo.equalsIgnoreCase("Empleado") ? "Empleado"
				: tipo.equalsIgnoreCase("Cliente") ? "Cliente" : "Proveedor";
		String columnaCod = tipo.equalsIgnoreCase("Empleado") ? "CodEmpleado"
				: tipo.equalsIgnoreCase("Cliente") ? "CodCliente" : "CodProveedor";

		try {
			con = ConexionDB.getConexion();

			// 1. Buscamos el ID t�cnico (int)
			String sqlId = "SELECT Id_Persona FROM " + tabla + " WHERE " + columnaCod + " = ?";
			psId = con.prepareStatement(sqlId);
			psId.setString(1, codigo);
			rs = psId.executeQuery();

			if (rs.next()) {
				int idSQL = rs.getInt("Id_Persona");

				// 2. Borramos de la tabla Persona (SQL borrar� autom�ticamente en las hijas por
				// el CASCADE)
				String sqlDel = "DELETE FROM Persona WHERE Id_Persona = ?";
				psDel = con.prepareStatement(sqlDel);
				psDel.setInt(1, idSQL);

				if (psDel.executeUpdate() > 0) {
					exito = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exito;
	}

	// METODOS DE USUARIO
	public boolean insertarUsuario(String tipo, String user, String pass) {
		String sql = "INSERT INTO Usuario (Tipo, UserName, Contrasena) VALUES (?, ?, ?)";

		try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

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

		try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

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

		try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, user);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage());
			return false;
		}
	}

	// M�TODOS DE CLIENTE
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
			con.setAutoCommit(false); // Iniciamos transaccion

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

			// 3. Insertar en Cliente usando el ID de SQL y el id String de Java
			// (CodCliente)
			psCliente = con.prepareStatement(sqlCliente);
			psCliente.setInt(1, idPersonaSQL);
			psCliente.setString(2, cliente.getId()); // Aquí usas tu "CLI-#"
			psCliente.setString(3, String.valueOf(cliente.getClasificacion()));
			psCliente.setInt(4, cliente.getCantVentas());
			psCliente.executeUpdate();

			con.commit();
			exito = true;
		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			// Cerrar recursos aquí
		}
		return exito;
	}

	public boolean modificarCliente(Cliente cliente) {
		Connection con = null;
		PreparedStatement psPersona = null;
		PreparedStatement psCliente = null;
		PreparedStatement psId = null;
		ResultSet rs = null;
		boolean exito = false;

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			// 1. Buscamos el ID t�cnico (int) usando el c�digo String (CLI-#)
			String sqlId = "SELECT Id_Persona FROM Cliente WHERE CodCliente = ?";
			psId = con.prepareStatement(sqlId);
			psId.setString(1, cliente.getId());
			rs = psId.executeQuery();

			if (rs.next()) {
				int idSQL = rs.getInt("Id_Persona");

				// 2. Actualizamos la tabla Persona
				String sqlP = "UPDATE Persona SET Nombre = ?, Correo = ?, Edad = ?, Cedula = ? WHERE Id_Persona = ?";
				psPersona = con.prepareStatement(sqlP);
				psPersona.setString(1, cliente.getNombre());
				psPersona.setString(2, cliente.getCorreo());
				psPersona.setInt(3, cliente.getEdad());
				psPersona.setString(4, cliente.getCedula());
				psPersona.setInt(5, idSQL);
				psPersona.executeUpdate();

				// 3. Actualizamos la tabla Cliente
				String sqlC = "UPDATE Cliente SET Clasificacion = ?, CantVentas = ? WHERE Id_Persona = ?";
				psCliente = con.prepareStatement(sqlC);
				psCliente.setString(1, String.valueOf(cliente.getClasificacion()));
				psCliente.setInt(2, cliente.getCantVentas());
				psCliente.setInt(3, idSQL);
				psCliente.executeUpdate();

				con.commit();
				exito = true;
			}
		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			// Cerrar recursos (psId, psPersona, psCliente, rs, con)
		}
		return exito;
	}

	// M�TODOS DE EMPLEADO
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
				try {
					con.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
		} finally {
			// Importante cerrar los recursos para evitar fugas de memoria
			try {
				if (rs != null)
					rs.close();
				if (psBusqueda != null)
					psBusqueda.close();
				if (psPersona != null)
					psPersona.close();
				if (psEmpleado != null)
					psEmpleado.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	public boolean modificarEmpleado(Empleado emp) {
		Connection con = null;
		PreparedStatement psP = null;
		PreparedStatement psE = null;
		PreparedStatement psId = null;
		ResultSet rs = null;
		boolean exito = false;

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			String sqlId = "SELECT Id_Persona FROM Empleado WHERE CodEmpleado = ?";
			psId = con.prepareStatement(sqlId);
			psId.setString(1, emp.getId());
			rs = psId.executeQuery();

			if (rs.next()) {
				int idSQL = rs.getInt("Id_Persona");

				// Update Persona
				String sqlP = "UPDATE Persona SET Nombre = ?, Correo = ?, Edad = ?, Cedula = ? WHERE Id_Persona = ?";
				psP = con.prepareStatement(sqlP);
				psP.setString(1, emp.getNombre());
				psP.setString(2, emp.getCorreo());
				psP.setInt(3, emp.getEdad());
				psP.setString(4, emp.getCedula());
				psP.setInt(5, idSQL);
				psP.executeUpdate();

				// Update Empleado
				String sqlE = "UPDATE Empleado SET ComisionVentas = ?, EmpleadoMes = ?, CantVentas = ? WHERE Id_Persona = ?";
				psE = con.prepareStatement(sqlE);
				psE.setFloat(1, emp.getComisionVentas());
				psE.setBoolean(2, emp.isEmpleadoMes());
				psE.setInt(3, emp.getCantVentas());
				psE.setInt(4, idSQL);
				psE.executeUpdate();

				con.commit();
				exito = true;
			}
		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
			}
			e.printStackTrace();
		}
		return exito;
	}

	// M�TODOS DE PROVEEDOR
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
			if (rs.next())
				idPersonaSQL = rs.getInt(1);

			psProv = con.prepareStatement(sqlProveedor);
			psProv.setInt(1, idPersonaSQL);
			psProv.setString(2, prov.getEmpresa());
			psProv.setString(3, prov.getId()); // Tu "PRV-#"
			psProv.executeUpdate();

			con.commit();
			exito = true;
		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
		return exito;
	}

	public boolean modificarProveedor(Proveedor prov) {
		Connection con = null;
		PreparedStatement psP = null;
		PreparedStatement psPr = null;
		PreparedStatement psId = null;
		ResultSet rs = null;
		boolean exito = false;

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			String sqlId = "SELECT Id_Persona FROM Proveedor WHERE CodProveedor = ?";
			psId = con.prepareStatement(sqlId);
			psId.setString(1, prov.getId());
			rs = psId.executeQuery();

			if (rs.next()) {
				int idSQL = rs.getInt("Id_Persona");

				String sqlP = "UPDATE Persona SET Nombre = ?, Correo = ?, Edad = ?, Cedula = ? WHERE Id_Persona = ?";
				psP = con.prepareStatement(sqlP);
				psP.setString(1, prov.getNombre());
				psP.setString(2, prov.getCorreo());
				psP.setInt(3, prov.getEdad());
				psP.setString(4, prov.getCedula());
				psP.setInt(5, idSQL);
				psP.executeUpdate();

				String sqlPr = "UPDATE Proveedor SET Empresa = ? WHERE Id_Persona = ?";
				psPr = con.prepareStatement(sqlPr);
				psPr.setString(1, prov.getEmpresa());
				psPr.setInt(2, idSQL);
				psPr.executeUpdate();

				con.commit();
				exito = true;
			}
		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
			}
			e.printStackTrace();
		}
		return exito;
	}

	// M�TODOS DE PRODUCTO

	public boolean insertarProducto(Producto p) {
		String sql = "INSERT INTO Producto (Id_Producto, NumSerie, Marca, CantDisponible, Precio) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, p.getId());
			ps.setString(2, p.getNumSerie());
			ps.setString(3, p.getMarca());
			ps.setInt(4, p.getCantDisponible());
			ps.setFloat(5, p.getPrecio());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar producto: " + e.getMessage());
			return false;
		}
	}

	public boolean insertarMotherBoard(MotherBoard mb) {
		Connection con = null;
		boolean exito = false;

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio, TipoProducto) VALUES (?, ?, ?, ?, ?)";
		String sqlMB = "INSERT INTO Motherboard (Id_Producto, Modelo, TipoSocket, TipoRam, TipoDiscoDuro) VALUES (?, ?, ?, ?, ?)";

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			// 1. Insertar en Producto y recuperar ID generado por SQL
			int idSQL = 0;
			try (PreparedStatement ps = con.prepareStatement(sqlProd, PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, mb.getNumSerie());
				ps.setString(2, mb.getMarca());
				ps.setInt(3, mb.getCantDisponible());
				ps.setFloat(4, mb.getPrecio());
				ps.setString(5, "MotherBoard");
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
					idSQL = rs.getInt(1);
			}

			// 2. Insertar en Motherboard usando el ID de SQL
			String tiposDiscos = String.join(",", mb.getListaDiscoDuroAceptados());
			try (PreparedStatement ps = con.prepareStatement(sqlMB)) {
				ps.setInt(1, idSQL);
				ps.setString(2, mb.getModelo());
				ps.setString(3, mb.getTipoSocket());
				ps.setString(4, mb.getTipoRam());
				ps.setString(5, tiposDiscos);
				ps.executeUpdate();
			}

			con.commit();
			exito = true;

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Error al insertar MotherBoard: " + e.getMessage());
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	public boolean insertarDiscoDuro(DiscoDuro dd) {
		Connection con = null;
		boolean exito = false;

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio, TipoProducto) VALUES (?, ?, ?, ?, ?)";
		String sqlDD = "INSERT INTO Discoduro (Id_Producto, Modelo, Capacidad, TipoConexion) VALUES (?, ?, ?, ?)";

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			int idSQL = 0;
			try (PreparedStatement ps = con.prepareStatement(sqlProd, PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, dd.getNumSerie());
				ps.setString(2, dd.getMarca());
				ps.setInt(3, dd.getCantDisponible());
				ps.setFloat(4, dd.getPrecio());
				ps.setString(5, "DiscoDuro");
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
					idSQL = rs.getInt(1);
			}

			try (PreparedStatement ps = con.prepareStatement(sqlDD)) {
				ps.setInt(1, idSQL);
				ps.setString(2, dd.getModelo());
				ps.setFloat(3, dd.getCapacidad());
				ps.setString(4, dd.getTipoConexion());
				ps.executeUpdate();
			}

			con.commit();
			exito = true;

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Error al insertar Disco Duro: " + e.getMessage());
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	public boolean insertarMemoriaRam(MemoriaRam mr) {
		Connection con = null;
		boolean exito = false;

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio, TipoProducto) VALUES (?, ?, ?, ?, ?)";
		String sqlMR = "INSERT INTO MemoriaRAM (Id_Producto, CantMemoria, TipoMemoria) VALUES (?, ?, ?)";

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			int idSQL = 0;
			try (PreparedStatement ps = con.prepareStatement(sqlProd, PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, mr.getNumSerie());
				ps.setString(2, mr.getMarca());
				ps.setInt(3, mr.getCantDisponible());
				ps.setFloat(4, mr.getPrecio());
				ps.setString(5, "MemoriaRAM");
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
					idSQL = rs.getInt(1);
			}

			try (PreparedStatement ps = con.prepareStatement(sqlMR)) {
				ps.setInt(1, idSQL);
				ps.setInt(2, mr.getCantMemoria());
				ps.setString(3, mr.getTipoMemoria());
				ps.executeUpdate();
			}

			con.commit();
			exito = true;

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Error al insertar Memoria RAM: " + e.getMessage());
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	public boolean insertarMicroprocesador(Microprocesador mp) {
		Connection con = null;
		boolean exito = false;

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio, TipoProducto) VALUES (?, ?, ?, ?, ?)";
		String sqlMP = "INSERT INTO Microprocesador (Id_Producto, Modelo, TipoSocket, VelocidadProcesamiento) VALUES (?, ?, ?, ?)";

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			int idSQL = 0;
			try (PreparedStatement ps = con.prepareStatement(sqlProd, PreparedStatement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, mp.getNumSerie());
				ps.setString(2, mp.getMarca());
				ps.setInt(3, mp.getCantDisponible());
				ps.setFloat(4, mp.getPrecio());
				ps.setString(5, "Microprocesador");
				ps.executeUpdate();

				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next())
					idSQL = rs.getInt(1);
			}

			try (PreparedStatement ps = con.prepareStatement(sqlMP)) {
				ps.setInt(1, idSQL);
				ps.setString(2, mp.getModelo());
				ps.setString(3, mp.getSocket());
				ps.setInt(4, mp.getVelocidadProcesamiento());
				ps.executeUpdate();
			}

			con.commit();
			exito = true;

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Error al insertar Microprocesador: " + e.getMessage());
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	// M�TODOS DE FACTURA
	public boolean insertarFacturaVenta(FacturaVenta factura) {
		Connection con = null;
		PreparedStatement psFactura = null;
		PreparedStatement psVenta = null;
		PreparedStatement psDetalle = null;
		ResultSet rs = null;

		String sqlFactura = "INSERT INTO Factura (FechaFactura, MontoTotal, TipoFactura) VALUES (?, ?, 'Venta')";
		String sqlVenta = "INSERT INTO FacturaVenta (Id_Factura, Id_P_Empleado, Id_P_Cliente, PorcentajeGanancia, CodVenta) VALUES (?, ?, ?, ?, ?)";
		String sqlDetalle = "INSERT INTO DetalleFacturaVenta (Id_Producto, Id_F_Venta, CantProducto, SubTotal, PrecioUnitario) VALUES (?, ?, ?, ?, ?)";

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false); // Iniciamos transacci�n

			// 1. Insertar en la tabla Factura (Cabezal General)
			psFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS);
			// Convertimos LocalDate a java.sql.Date
			psFactura.setDate(1, java.sql.Date.valueOf(factura.getFechaFactura()));
			psFactura.setDouble(2, factura.getmontoTotal());
			psFactura.executeUpdate();

			rs = psFactura.getGeneratedKeys();
			int idFacturaSQL = 0;
			if (rs.next()) {
				idFacturaSQL = rs.getInt(1);
			}

			// 2. Insertar en FacturaVenta (Relacionando con Empleado y Cliente por C�dula)
			psVenta = con.prepareStatement(sqlVenta);
			psVenta.setInt(1, idFacturaSQL);

			// Buscamos los IDs de SQL usando los m�todos auxiliares
			int idEmpleadoSQL = obtenerIdPersonaPorCedula(factura.getVendedor().getCedula());
			int idClienteSQL = obtenerIdPersonaPorCedula(factura.getCliente().getCedula());

			psVenta.setInt(2, idEmpleadoSQL);
			psVenta.setInt(3, idClienteSQL);
			psVenta.setDouble(4, 0.15); // Porcentaje de ganancia (puedes parametrizarlo)
			psVenta.setString(5, factura.getId()); // Tu c�digo FVE-#
			psVenta.executeUpdate();

			// 3. Insertar Detalles de la Factura
			psDetalle = con.prepareStatement(sqlDetalle);
			for (DetalleFacturaVenta det : factura.getDetallesVenta()) {
				// Buscamos el ID del producto por su n�mero de serie �nico
				int idProductoSQL = obtenerIdProductoPorSerie(det.getProducto().getNumSerie());

				psDetalle.setInt(1, idProductoSQL);
				psDetalle.setInt(2, idFacturaSQL);
				psDetalle.setInt(3, det.getCantidad());
				psDetalle.setDouble(4, det.getSubtotal());
				psDetalle.setDouble(5, det.getPrecioUnitario());
				psDetalle.addBatch(); // A�adimos al lote
			}
			psDetalle.executeBatch(); // Ejecutamos todos los detalles juntos

			con.commit(); // Si todo sali� bien, guardamos cambios
			return true;

		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback(); // Si algo fall�, deshacemos todo
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			return false;
		} finally {
			// Cerrar recursos para liberar memoria
			try {
				if (rs != null)
					rs.close();
				if (psFactura != null)
					psFactura.close();
				if (psVenta != null)
					psVenta.close();
				if (psDetalle != null)
					psDetalle.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// M�TODOS AUXILIARES DE B�SQUEDA
	private int obtenerIdPersonaPorCedula(String cedula) throws SQLException {
		int idEncontrado = -1;
		String sql = "SELECT Id_Persona FROM Persona WHERE Cedula = ?";
		// Usamos una conexi�n interna o la misma si prefieres pasarla por par�metro
		try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, cedula);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					idEncontrado = rs.getInt("Id_Persona");
				}
			}
		}
		return idEncontrado;
	}

	private int obtenerIdProductoPorSerie(String numSerie) throws SQLException {
		int idEncontrado = -1;
		String sql = "SELECT Id_Producto FROM Producto WHERE NumSerie = ?";
		try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, numSerie);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					idEncontrado = rs.getInt("Id_Producto");
				}
			}
		}
		return idEncontrado;
	}

	public boolean insertarFacturaCompra(FacturaCompra factura) {
		Connection con = null;
		PreparedStatement psFactura = null;
		PreparedStatement psCompra = null;
		PreparedStatement psDetalle = null;
		ResultSet rs = null;

		String sqlFactura = "INSERT INTO Factura (FechaFactura, MontoTotal, TipoFactura) VALUES (?, ?, 'Compra')";
		String sqlCompra = "INSERT INTO FacturaCompra (Id_Factura, Id_P_Proveedor, CodCompra) VALUES (?, ?, ?)";
		String sqlDetalle = "INSERT INTO DetalleFacturaCompra (Id_Producto, Id_F_Compra, CantProducto, SubTotal, PrecioUnitario) VALUES (?, ?, ?, ?, ?)";

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false); // Iniciamos transacci�n

			// 1. Insertar en la tabla Factura (Cabezal General)
			psFactura = con.prepareStatement(sqlFactura, java.sql.Statement.RETURN_GENERATED_KEYS);
			psFactura.setDate(1, java.sql.Date.valueOf(factura.getFechaFactura()));
			psFactura.setDouble(2, factura.getmontoTotal());
			psFactura.executeUpdate();

			rs = psFactura.getGeneratedKeys();
			int idFacturaSQL = 0;
			if (rs.next()) {
				idFacturaSQL = rs.getInt(1);
			}

			// 2. Insertar en FacturaCompra (Relacionando con Proveedor por C�dula/RNC)
			psCompra = con.prepareStatement(sqlCompra);
			psCompra.setInt(1, idFacturaSQL);

			// Buscamos el ID t�cnico del proveedor usando su c�dula
			int idProveedorSQL = obtenerIdPersonaPorCedula(factura.getProveedor().getCedula());

			if (idProveedorSQL == -1) {
				throw new SQLException("No se encontr� el ID del proveedor con la c�dula proporcionada.");
			}

			psCompra.setInt(2, idProveedorSQL);
			psCompra.setString(3, factura.getId()); // Tu c�digo FCO-# de Java
			psCompra.executeUpdate();

			// 3. Insertar Detalles de la Factura de Compra
			psDetalle = con.prepareStatement(sqlDetalle);
			for (DetalleFacturaCompra det : factura.getDetallesCompra()) {
				// Buscamos el ID del producto por su n�mero de serie �nico
				int idProductoSQL = obtenerIdProductoPorSerie(det.getProducto().getNumSerie());

				if (idProductoSQL == -1) {
					throw new SQLException("No se encontr� el producto con serie: " + det.getProducto().getNumSerie());
				}

				psDetalle.setInt(1, idProductoSQL);
				psDetalle.setInt(2, idFacturaSQL);
				psDetalle.setInt(3, det.getCantidad());
				psDetalle.setDouble(4, det.getSubtotal());
				psDetalle.setDouble(5, det.getPrecioUnitario());
				psDetalle.addBatch();
			}
			psDetalle.executeBatch();

			con.commit(); // �xito total
			return true;

		} catch (SQLException e) {
			if (con != null) {
				try {
					con.rollback(); // Error: deshacer todo
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			return false;
		} finally {
			// Cerrar recursos
			try {
				if (rs != null)
					rs.close();
				if (psFactura != null)
					psFactura.close();
				if (psCompra != null)
					psCompra.close();
				if (psDetalle != null)
					psDetalle.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean eliminarFactura(String codFactura, String tipo) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean exito = false;

		// Buscamos el ID t�cnico primero para poder borrar en la tabla padre
		String sql = "DELETE FROM Factura WHERE Id_Factura = (SELECT Id_Factura FROM "
				+ (tipo.equalsIgnoreCase("Venta") ? "FacturaVenta WHERE CodVenta = ?"
						: "FacturaCompra WHERE CodCompra = ?")
				+ ")";

		try {
			con = ConexionDB.getConexion();
			ps = con.prepareStatement(sql);
			ps.setString(1, codFactura);

			int filas = ps.executeUpdate();
			if (filas > 0)
				exito = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	private int obtenerIdFacturaPorCodigo(String codigo, String tipo) throws SQLException {
		int id = -1;
		String sql = tipo.equalsIgnoreCase("Venta") ? "SELECT Id_Factura FROM FacturaVenta WHERE CodVenta = ?"
				: "SELECT Id_Factura FROM FacturaCompra WHERE CodCompra = ?";

		try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, codigo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					id = rs.getInt(1);
			}
		}
		return id;
	}

	public boolean actualizarFacturaVenta(FacturaVenta factura) {
		Connection con = null;
		PreparedStatement psFactura = null;
		PreparedStatement psVenta = null;
		PreparedStatement psDeleteDetalle = null;
		PreparedStatement psInsertDetalle = null;

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			// 1. Obtener el ID real de la factura por su c�digo
			int idFacturaSQL = obtenerIdFacturaPorCodigo(factura.getId(), "Venta");

			// 2. Actualizar tabla Factura (Monto y Fecha)
			String sqlFactura = "UPDATE Factura SET MontoTotal = ?, FechaFactura = ? WHERE Id_Factura = ?";
			psFactura = con.prepareStatement(sqlFactura);
			psFactura.setDouble(1, factura.getmontoTotal());
			psFactura.setDate(2, java.sql.Date.valueOf(factura.getFechaFactura()));
			psFactura.setInt(3, idFacturaSQL);
			psFactura.executeUpdate();

			// 3. Borrar detalles viejos para insertar los nuevos (Sincronizaci�n limpia)
			String sqlDelDet = "DELETE FROM DetalleFacturaVenta WHERE Id_F_Venta = ?";
			psDeleteDetalle = con.prepareStatement(sqlDelDet);
			psDeleteDetalle.setInt(1, idFacturaSQL);
			psDeleteDetalle.executeUpdate();

			// 4. Insertar los nuevos detalles del ArrayList
			String sqlInsDet = "INSERT INTO DetalleFacturaVenta (Id_Producto, Id_F_Venta, CantProducto, SubTotal, PrecioUnitario) VALUES (?, ?, ?, ?, ?)";
			psInsertDetalle = con.prepareStatement(sqlInsDet);
			for (DetalleFacturaVenta det : factura.getDetallesVenta()) {
				psInsertDetalle.setInt(1, obtenerIdProductoPorSerie(det.getProducto().getNumSerie()));
				psInsertDetalle.setInt(2, idFacturaSQL);
				psInsertDetalle.setInt(3, det.getCantidad());
				psInsertDetalle.setDouble(4, det.getSubtotal());
				psInsertDetalle.setDouble(5, det.getPrecioUnitario());
				psInsertDetalle.addBatch();
			}
			psInsertDetalle.executeBatch();

			con.commit();
			return true;
		} catch (SQLException e) {
			if (con != null)
				try {
					con.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			e.printStackTrace();
			return false;
		} finally {
			// Cerrar recursos
		}
	}

	public boolean actualizarFacturaCompra(FacturaCompra factura) {
		Connection con = null;
		PreparedStatement psFactura = null;
		PreparedStatement psDeleteDetalle = null;
		PreparedStatement psInsertDetalle = null;

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			int idFacturaSQL = obtenerIdFacturaPorCodigo(factura.getId(), "Compra");

			// Actualizar Cabezal
			String sqlFactura = "UPDATE Factura SET MontoTotal = ?, FechaFactura = ? WHERE Id_Factura = ?";
			psFactura = con.prepareStatement(sqlFactura);
			psFactura.setDouble(1, factura.getmontoTotal());
			psFactura.setDate(2, java.sql.Date.valueOf(factura.getFechaFactura()));
			psFactura.setInt(3, idFacturaSQL);
			psFactura.executeUpdate();

			// Borrar y re-insertar detalles
			psDeleteDetalle = con.prepareStatement("DELETE FROM DetalleFacturaCompra WHERE Id_F_Compra = ?");
			psDeleteDetalle.setInt(1, idFacturaSQL);
			psDeleteDetalle.executeUpdate();

			psInsertDetalle = con.prepareStatement(
					"INSERT INTO DetalleFacturaCompra (Id_Producto, Id_F_Compra, CantProducto, SubTotal, PrecioUnitario) VALUES (?, ?, ?, ?, ?)");
			for (DetalleFacturaCompra det : factura.getDetallesCompra()) {
				psInsertDetalle.setInt(1, obtenerIdProductoPorSerie(det.getProducto().getNumSerie()));
				psInsertDetalle.setInt(2, idFacturaSQL);
				psInsertDetalle.setInt(3, det.getCantidad());
				psInsertDetalle.setDouble(4, det.getSubtotal());
				psInsertDetalle.setDouble(5, det.getPrecioUnitario());
				psInsertDetalle.addBatch();
			}
			psInsertDetalle.executeBatch();

			con.commit();
			return true;
		} catch (SQLException e) {
			if (con != null)
				try {
					con.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<FacturaVenta> leerFacturasVenta() {
		ArrayList<FacturaVenta> lista = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// Unimos Factura con FacturaVenta
		String sql = "SELECT F.Id_Factura, F.FechaFactura, F.MontoTotal, FV.CodVenta, FV.Id_P_Cliente, FV.Id_P_Empleado, FV.PorcentajeGanancia "
				+ "FROM Factura F INNER JOIN FacturaVenta FV ON F.Id_Factura = FV.Id_Factura";

		try {
			con = ConexionDB.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				int idSQL = rs.getInt("IdFactura");
				LocalDate fecha = rs.getDate("FechaFactura").toLocalDate();
				double monto = rs.getDouble("MontoTotal");
				String codVenta = rs.getString("CodVenta");

				// Aqui buscas tus objetos l�gicos usando los IDs de SQL
				Cliente cliente = Tienda.getInstance().buscarClientePorIdSQL(rs.getInt("Id_P_Cliente"));
				Empleado vendedor = Tienda.getInstance().buscarEmpleadoPorIdSQL(rs.getInt("Id_P_Empleado"));

				// Creamos el objeto (usando el constructor que definiste)
				FacturaVenta fv = new FacturaVenta(codVenta, fecha, new ArrayList<>(), cliente, 0, monto);
				fv.setVendedor(vendedor);

				// Cargamos sus detalles (M�todo que haremos a continuaci�n)
				fv.setDetallesVenta(leerDetallesVenta(idSQL, codVenta));

				lista.add(fv);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	private ArrayList<DetalleFacturaVenta> leerDetallesVenta(int idFacturaSQL, String codFactura) {
		ArrayList<DetalleFacturaVenta> detalles = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT Id_Producto, CantProducto, PrecioUnitario FROM DetalleFacturaVenta WHERE Id_F_Venta = ?";

		try {
			con = ConexionDB.getConexion();
			ps = con.prepareStatement(sql);
			ps.setInt(1, idFacturaSQL);
			rs = ps.executeQuery();

			int linea = 1;
			while (rs.next()) {
				Producto prod = Tienda.getInstance().buscarProductoPorIdSQL(rs.getInt("Id_Producto"));
				int cant = rs.getInt("CantProducto");
				double precio = rs.getDouble("PrecioUnitario");

				DetalleFacturaVenta det = new DetalleFacturaVenta(codFactura, linea, prod, cant, precio);
				detalles.add(det);
				linea++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return detalles;
	}

	public ArrayList<FacturaCompra> leerFacturasCompra() {
		ArrayList<FacturaCompra> lista = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT F.Id_Factura, F.FechaFactura, F.MontoTotal, FC.CodCompra, FC.Id_P_Proveedor "
				+ "FROM Factura F INNER JOIN FacturaCompra FC ON F.Id_Factura = FC.Id_Factura";

		try {
			con = ConexionDB.getConexion();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				int idSQL = rs.getInt("Id_Factura");
				LocalDate fecha = rs.getDate("FechaFactura").toLocalDate();
				double monto = rs.getDouble("MontoTotal");
				String codCompra = rs.getString("CodCompra");

				Proveedor prov = Tienda.getInstance().buscarProveedorPorIdSQL(rs.getInt("Id_P_Proveedor"));

				FacturaCompra fc = new FacturaCompra(codCompra, fecha, new ArrayList<>(), prov, 0, monto);

				// Cargamos sus detalles
				fc.setDetallesCompra(leerDetallesCompra(idSQL, codCompra));

				lista.add(fc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}

	private ArrayList<DetalleFacturaCompra> leerDetallesCompra(int idFacturaSQL, String codFactura) {
		ArrayList<DetalleFacturaCompra> detalles = new ArrayList<>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// Consulta a la tabla de detalles de compra
		String sql = "SELECT Id_Producto, CantProducto, PrecioUnitario FROM DetalleFacturaCompra WHERE Id_F_Compra = ?";

		try {
			con = ConexionDB.getConexion();
			ps = con.prepareStatement(sql);
			ps.setInt(1, idFacturaSQL);
			rs = ps.executeQuery();

			int linea = 1;
			while (rs.next()) {
				// Buscamos el producto en la memoria usando el ID de SQL
				int idProdSQL = rs.getInt("Id_Producto");
				Producto prod = Tienda.getInstance().buscarProductoPorIdSQL(idProdSQL);

				int cant = rs.getInt("CantProducto");
				double precio = rs.getDouble("PrecioUnitario");

				// Creamos el detalle
				// Nota: numeroLinea se usa para generar el idDetalle (FVE-1-1, FVE-1-2...)
				DetalleFacturaCompra det = new DetalleFacturaCompra(codFactura, linea, prod, cant, precio);
				detalles.add(det);
				linea++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// Es buena pr�ctica cerrar los recursos locales
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				// No cerramos la conexi�n aqu� si se est� usando una compartida,
				// pero si es una nueva por cada llamada, se deber�a cerrar.
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return detalles;
	}

	// ACTUALIZAR PRODUCTO
	public boolean modificarProducto(Producto producto) {
		Connection con = null;
		PreparedStatement psProd = null;
		PreparedStatement psHija = null;
		PreparedStatement psId = null;
		ResultSet rs = null;
		boolean exito = false;

		try {
			con = ConexionDB.getConexion();
			con.setAutoCommit(false);

			// 1. Buscar el ID técnico por NumSerie
			String sqlId = "SELECT Id_Producto FROM Producto WHERE NumSerie = ?";
			psId = con.prepareStatement(sqlId);
			psId.setString(1, producto.getNumSerie());
			rs = psId.executeQuery();

			if (!rs.next()) {
				System.out.println("Producto no encontrado en SQL.");
				return false;
			}
			int idSQL = rs.getInt("Id_Producto");

			// 2. Actualizar tabla Producto (datos generales)
			String sqlProd = "UPDATE Producto SET Marca = ?, CantDisponible = ?, Precio = ? WHERE Id_Producto = ?";
			psProd = con.prepareStatement(sqlProd);
			psProd.setString(1, producto.getMarca());
			psProd.setInt(2, producto.getCantDisponible());
			psProd.setFloat(3, producto.getPrecio());
			psProd.setInt(4, idSQL);
			psProd.executeUpdate();

			// 3. Actualizar tabla hija según el tipo
			if (producto instanceof MotherBoard) {
				MotherBoard mb = (MotherBoard) producto;
				String tiposDiscos = String.join(",", mb.getListaDiscoDuroAceptados());
				String sqlMB = "UPDATE Motherboard SET Modelo = ?, TipoSocket = ?, TipoRam = ?, TipoDiscoDuro = ? WHERE Id_Producto = ?";
				psHija = con.prepareStatement(sqlMB);
				psHija.setString(1, mb.getModelo());
				psHija.setString(2, mb.getTipoSocket());
				psHija.setString(3, mb.getTipoRam());
				psHija.setString(4, tiposDiscos);
				psHija.setInt(5, idSQL);
				psHija.executeUpdate();

			} else if (producto instanceof DiscoDuro) {
				DiscoDuro dd = (DiscoDuro) producto;
				String sqlDD = "UPDATE Discoduro SET Modelo = ?, Capacidad = ?, TipoConexion = ? WHERE Id_Producto = ?";
				psHija = con.prepareStatement(sqlDD);
				psHija.setString(1, dd.getModelo());
				psHija.setFloat(2, dd.getCapacidad());
				psHija.setString(3, dd.getTipoConexion());
				psHija.setInt(4, idSQL);
				psHija.executeUpdate();

			} else if (producto instanceof MemoriaRam) {
				MemoriaRam mr = (MemoriaRam) producto;
				String sqlMR = "UPDATE MemoriaRAM SET CantMemoria = ?, TipoMemoria = ? WHERE Id_Producto = ?";
				psHija = con.prepareStatement(sqlMR);
				psHija.setInt(1, mr.getCantMemoria());
				psHija.setString(2, mr.getTipoMemoria());
				psHija.setInt(3, idSQL);
				psHija.executeUpdate();

			} else if (producto instanceof Microprocesador) {
				Microprocesador mp = (Microprocesador) producto;
				String sqlMP = "UPDATE Microprocesador SET Modelo = ?, TipoSocket = ?, VelocidadProcesamiento = ? WHERE Id_Producto = ?";
				psHija = con.prepareStatement(sqlMP);
				psHija.setString(1, mp.getModelo());
				psHija.setString(2, mp.getSocket());
				psHija.setInt(3, mp.getVelocidadProcesamiento());
				psHija.setInt(4, idSQL);
				psHija.executeUpdate();
			}

			con.commit();
			exito = true;

		} catch (SQLException e) {
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Error al modificar producto: " + e.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (psId != null)
					psId.close();
				if (psProd != null)
					psProd.close();
				if (psHija != null)
					psHija.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	// ELIMINAR PRODUCTO
	public boolean eliminarProducto(String numSerie) {
		Connection con = null;
		PreparedStatement psId = null;
		PreparedStatement psDel = null;
		ResultSet rs = null;
		boolean exito = false;

		try {
			con = ConexionDB.getConexion();

			// 1. Buscar el ID técnico por NumSerie
			String sqlId = "SELECT Id_Producto FROM Producto WHERE NumSerie = ?";
			psId = con.prepareStatement(sqlId);
			psId.setString(1, numSerie);
			rs = psId.executeQuery();

			if (!rs.next()) {
				System.out.println("Producto no encontrado para eliminar.");
				return false;
			}
			int idSQL = rs.getInt("Id_Producto");

			// 2. Borrar desde Producto (SQL elimina la tabla hija automaticamente por
			// CASCADE)
			String sqlDel = "DELETE FROM Producto WHERE Id_Producto = ?";
			psDel = con.prepareStatement(sqlDel);
			psDel.setInt(1, idSQL);

			if (psDel.executeUpdate() > 0)
				exito = true;

		} catch (SQLException e) {

			if (e.getMessage().contains("FK_DetalleFacturaCompra_Producto")
					|| e.getMessage().contains("FK_DetalleFacturaVenta_Producto")) {

				ultimoErrorProducto = "No se puede eliminar este producto porque ya esta asociado a una factura.\n";

			} else {
				e.printStackTrace();
				ultimoErrorProducto = "Error al eliminar producto:\n" + e.getMessage();
			}

			return false;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (psId != null)
					psId.close();
				if (psDel != null)
					psDel.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exito;
	}

	public boolean insertarProveedorProducto(String numSerieProducto, String codProveedor) {
		String sqlIdProducto = "SELECT Id_Producto FROM Producto WHERE NumSerie = ?";
		String sqlIdProveedor = "SELECT Id_Persona FROM Proveedor WHERE CodProveedor = ?";
		String sqlInsert = "INSERT INTO ProveedorProducto (Id_Producto, Id_P_Proveedor) VALUES (?, ?)";

		try (Connection con = ConexionDB.getConexion()) {

			int idProducto = -1;
			int idProveedor = -1;

			try (PreparedStatement ps = con.prepareStatement(sqlIdProducto)) {
				ps.setString(1, numSerieProducto);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						idProducto = rs.getInt("Id_Producto");
					}
				}
			}

			if (idProducto == -1) {
				JOptionPane.showMessageDialog(null,
						"No se encontró el producto con número de serie: " + numSerieProducto);
				return false;
			}

			try (PreparedStatement ps = con.prepareStatement(sqlIdProveedor)) {
				ps.setString(1, codProveedor);

				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						idProveedor = rs.getInt("Id_Persona");
					}
				}
			}

			if (idProveedor == -1) {
				JOptionPane.showMessageDialog(null, "No se encontró el proveedor con código: " + codProveedor);
				return false;
			}

			try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
				ps.setInt(1, idProducto);
				ps.setInt(2, idProveedor);

				return ps.executeUpdate() > 0;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar ProveedorProducto: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}