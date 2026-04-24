package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class TiendaComputos {

	private ConexionDB conexion = new ConexionDB();

	// MÉTODOS DE USUARIO
	public boolean insertarUsuario(String tipo, String user, String pass) {
		String sql = "INSERT INTO Users (Tipo, UserName, Password) VALUES (?, ?, ?)";

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
		String sql = "UPDATE Users SET Tipo = ?, UserName = ?, Password = ? WHERE UserName = ?";

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
		String sql = "DELETE FROM Users WHERE UserName = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, user);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage());
			return false;
		}
	}

	// MÉTODOS BASE DE PRODUCTO
	private int insertarProductoBase(Connection con, String numSerie, String marca, int cantDisponible, double precio,
			String codProducto) throws SQLException {

		String sql = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio, CodProducto) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, numSerie);
			ps.setString(2, marca);
			ps.setInt(3, cantDisponible);
			ps.setDouble(4, precio);
			ps.setString(5, codProducto);

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		return -1;
	}

	private boolean actualizarProductoBase(Connection con, int idProducto, String numSerie, String marca,
			int cantDisponible, double precio, String codProducto) throws SQLException {

		String sql = "UPDATE Producto SET NumSerie = ?, Marca = ?, CantDisponible = ?, "
				+ "Precio = ?, CodProducto = ? WHERE Id_Producto = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, numSerie);
			ps.setString(2, marca);
			ps.setInt(3, cantDisponible);
			ps.setDouble(4, precio);
			ps.setString(5, codProducto);
			ps.setInt(6, idProducto);

			return ps.executeUpdate() > 0;
		}
	}

	// MOTHERBOARD
	public boolean insertarMotherboard(String numSerie, String marca, int cantDisponible, double precio,
			String codProducto, String modelo, String tipoSocket, String tipoRam, String tipoDiscoDuro) {

		String sql = "INSERT INTO Motherboard (Id_Producto, Modelo, TipoSocket, TipoRam, TipoDiscoDuro) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idProducto = insertarProductoBase(con, numSerie, marca, cantDisponible, precio, codProducto);

				if (idProducto == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idProducto);
					ps.setString(2, modelo);
					ps.setString(3, tipoSocket);
					ps.setString(4, tipoRam);
					ps.setString(5, tipoDiscoDuro);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar motherboard: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarMotherboard(int idProducto, String numSerie, String marca, int cantDisponible,
			double precio, String codProducto, String modelo, String tipoSocket, String tipoRam, String tipoDiscoDuro) {

		String sql = "UPDATE Motherboard SET Modelo = ?, TipoSocket = ?, TipoRam = ?, "
				+ "TipoDiscoDuro = ? WHERE Id_Producto = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarProductoBase(con, idProducto, numSerie, marca, cantDisponible, precio, codProducto);

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, modelo);
					ps.setString(2, tipoSocket);
					ps.setString(3, tipoRam);
					ps.setString(4, tipoDiscoDuro);
					ps.setInt(5, idProducto);

					boolean ok = ps.executeUpdate() > 0;
					con.commit();
					return ok;
				}

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar motherboard: " + e.getMessage());
			return false;
		}
	}
	
	// MEMORIA RAM
	public boolean insertarMemoriaRAM(String numSerie, String marca, int cantDisponible, double precio,
			String codProducto, int cantMemoria, String tipoMemoria) {

		String sql = "INSERT INTO MemoriaRAM (Id_Producto, CantMemoria, TipoMemoria) VALUES (?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idProducto = insertarProductoBase(con, numSerie, marca, cantDisponible, precio, codProducto);

				if (idProducto == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idProducto);
					ps.setInt(2, cantMemoria);
					ps.setString(3, tipoMemoria);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar memoria RAM: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarMemoriaRAM(int idProducto, String numSerie, String marca, int cantDisponible,
			double precio, String codProducto, int cantMemoria, String tipoMemoria) {

		String sql = "UPDATE MemoriaRAM SET CantMemoria = ?, TipoMemoria = ? WHERE Id_Producto = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarProductoBase(con, idProducto, numSerie, marca, cantDisponible, precio, codProducto);

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, cantMemoria);
					ps.setString(2, tipoMemoria);
					ps.setInt(3, idProducto);

					boolean ok = ps.executeUpdate() > 0;
					con.commit();
					return ok;
				}

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar memoria RAM: " + e.getMessage());
			return false;
		}
	}

	// DISCO DURO
	public boolean insertarDiscoDuro(String numSerie, String marca, int cantDisponible, double precio,
			String codProducto, String modelo, int capacidad, String tipoConexion) {

		String sql = "INSERT INTO DiscoDuro (Id_Producto, Modelo, Capacidad, TipoConexion) VALUES (?, ?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idProducto = insertarProductoBase(con, numSerie, marca, cantDisponible, precio, codProducto);

				if (idProducto == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idProducto);
					ps.setString(2, modelo);
					ps.setInt(3, capacidad);
					ps.setString(4, tipoConexion);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar disco duro: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarDiscoDuro(int idProducto, String numSerie, String marca, int cantDisponible, double precio,
			String codProducto, String modelo, int capacidad, String tipoConexion) {

		String sql = "UPDATE DiscoDuro SET Modelo = ?, Capacidad = ?, TipoConexion = ? WHERE Id_Producto = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarProductoBase(con, idProducto, numSerie, marca, cantDisponible, precio, codProducto);

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, modelo);
					ps.setInt(2, capacidad);
					ps.setString(3, tipoConexion);
					ps.setInt(4, idProducto);

					boolean ok = ps.executeUpdate() > 0;
					con.commit();
					return ok;
				}

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar disco duro: " + e.getMessage());
			return false;
		}
	}

	// MICROPROCESADOR
	public boolean insertarMicroprocesador(String numSerie, String marca, int cantDisponible, double precio,
			String codProducto, String modelo, String socket, double velocidadProcesamiento) {

		String sql = "INSERT INTO Microprocesador (Id_Producto, Modelo, Socket, VelocidadProcesamiento) "
				+ "VALUES (?, ?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idProducto = insertarProductoBase(con, numSerie, marca, cantDisponible, precio, codProducto);

				if (idProducto == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idProducto);
					ps.setString(2, modelo);
					ps.setString(3, socket);
					ps.setDouble(4, velocidadProcesamiento);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar microprocesador: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarMicroprocesador(int idProducto, String numSerie, String marca, int cantDisponible,
			double precio, String codProducto, String modelo, String socket, double velocidadProcesamiento) {

		String sql = "UPDATE Microprocesador SET Modelo = ?, Socket = ?, VelocidadProcesamiento = ? "
				+ "WHERE Id_Producto = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarProductoBase(con, idProducto, numSerie, marca, cantDisponible, precio, codProducto);

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, modelo);
					ps.setString(2, socket);
					ps.setDouble(3, velocidadProcesamiento);
					ps.setInt(4, idProducto);

					boolean ok = ps.executeUpdate() > 0;
					con.commit();
					return ok;
				}

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar microprocesador: " + e.getMessage());
			return false;
		}
	}

	// ELIMINAR PRODUCTO
	public boolean eliminarProducto(int idProducto) {
		String sql = "DELETE FROM Producto WHERE Id_Producto = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idProducto);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.getMessage());
			return false;
		}
	}


	// MÉTODOS BASE DE PERSONA
	private int insertarPersonaBase(Connection con, Date fechaRegistro, String nombre, String correo, int edad,
			String cedula) throws SQLException {

		String sql = "INSERT INTO Persona (FechaRegistro, Nombre, Correo, Edad, Cedula) " + "VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setDate(1, fechaRegistro);
			ps.setString(2, nombre);
			ps.setString(3, correo);
			ps.setInt(4, edad);
			ps.setString(5, cedula);

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		return -1;
	}

	private boolean actualizarPersonaBase(Connection con, int idPersona, Date fechaRegistro, String nombre,
			String correo, int edad, String cedula) throws SQLException {

		String sql = "UPDATE Persona SET FechaRegistro = ?, Nombre = ?, Correo = ?, "
				+ "Edad = ?, Cedula = ? WHERE Id_Persona = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDate(1, fechaRegistro);
			ps.setString(2, nombre);
			ps.setString(3, correo);
			ps.setInt(4, edad);
			ps.setString(5, cedula);
			ps.setInt(6, idPersona);

			return ps.executeUpdate() > 0;
		}
	}

	// CLIENTE
	public boolean insertarCliente(Date fechaRegistro, String nombre, String correo, int edad, String cedula,
			int cantVentas, String codCliente, String clasificacion) {

		String sqlCliente = "INSERT INTO Cliente (Id_Persona, CantVentas, CodCliente, Clasificacion) "
				+ "VALUES (?, ?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idPersona = insertarPersonaBase(con, fechaRegistro, nombre, correo, edad, cedula);

				if (idPersona == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sqlCliente)) {
					ps.setInt(1, idPersona);
					ps.setInt(2, cantVentas);
					ps.setString(3, codCliente);
					ps.setString(4, clasificacion);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar cliente: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarCliente(int idPersona, Date fechaRegistro, String nombre, String correo, int edad,
			String cedula, int cantVentas, String codCliente, String clasificacion) {

		String sqlCliente = "UPDATE Cliente SET CantVentas = ?, CodCliente = ?, Clasificacion = ? "
				+ "WHERE Id_Persona = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarPersonaBase(con, idPersona, fechaRegistro, nombre, correo, edad, cedula);

				try (PreparedStatement ps = con.prepareStatement(sqlCliente)) {
					ps.setInt(1, cantVentas);
					ps.setString(2, codCliente);
					ps.setString(3, clasificacion);
					ps.setInt(4, idPersona);

					if (ps.executeUpdate() == 0) {
						con.rollback();
						return false;
					}
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar cliente: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarCliente(int idPersona) {
		return eliminarPersona(idPersona);
	}


	// PROVEEDOR
	public boolean insertarProveedor(Date fechaRegistro, String nombre, String correo, int edad, String cedula,
			String empresa, String codProveedor) {

		String sqlProveedor = "INSERT INTO Proveedor (Id_Persona, Empresa, CodProveedor) VALUES (?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idPersona = insertarPersonaBase(con, fechaRegistro, nombre, correo, edad, cedula);

				if (idPersona == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sqlProveedor)) {
					ps.setInt(1, idPersona);
					ps.setString(2, empresa);
					ps.setString(3, codProveedor);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar proveedor: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarProveedor(int idPersona, Date fechaRegistro, String nombre, String correo, int edad,
			String cedula, String empresa, String codProveedor) {

		String sqlProveedor = "UPDATE Proveedor SET Empresa = ?, CodProveedor = ? WHERE Id_Persona = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarPersonaBase(con, idPersona, fechaRegistro, nombre, correo, edad, cedula);

				try (PreparedStatement ps = con.prepareStatement(sqlProveedor)) {
					ps.setString(1, empresa);
					ps.setString(2, codProveedor);
					ps.setInt(3, idPersona);

					if (ps.executeUpdate() == 0) {
						con.rollback();
						return false;
					}
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar proveedor: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarProveedor(int idPersona) {
		return eliminarPersona(idPersona);
	}


	// EMPLEADO
	public boolean insertarEmpleado(Date fechaRegistro, String nombre, String correo, int edad, String cedula,
			boolean empleadoMes, int cantVentas, double comisionVentas, String codEmpleado, int idUsuario) {

		String sqlEmpleado = "INSERT INTO Empleado (Id_Persona, EmpleadoMes, CantVentas, ComisionVentas, "
				+ "CodEmpleado, Id_Usuario) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idPersona = insertarPersonaBase(con, fechaRegistro, nombre, correo, edad, cedula);

				if (idPersona == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sqlEmpleado)) {
					ps.setInt(1, idPersona);
					ps.setBoolean(2, empleadoMes);
					ps.setInt(3, cantVentas);
					ps.setDouble(4, comisionVentas);
					ps.setString(5, codEmpleado);
					ps.setInt(6, idUsuario);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar empleado: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarEmpleado(int idPersona, Date fechaRegistro, String nombre, String correo, int edad,
			String cedula, boolean empleadoMes, int cantVentas, double comisionVentas, String codEmpleado,
			int idUsuario) {

		String sqlEmpleado = "UPDATE Empleado SET EmpleadoMes = ?, CantVentas = ?, ComisionVentas = ?, "
				+ "CodEmpleado = ?, Id_Usuario = ? WHERE Id_Persona = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarPersonaBase(con, idPersona, fechaRegistro, nombre, correo, edad, cedula);

				try (PreparedStatement ps = con.prepareStatement(sqlEmpleado)) {
					ps.setBoolean(1, empleadoMes);
					ps.setInt(2, cantVentas);
					ps.setDouble(3, comisionVentas);
					ps.setString(4, codEmpleado);
					ps.setInt(5, idUsuario);
					ps.setInt(6, idPersona);

					if (ps.executeUpdate() == 0) {
						con.rollback();
						return false;
					}
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar empleado: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarEmpleado(int idPersona) {
		return eliminarPersona(idPersona);
	}


	// ELIMINAR PERSONA (BASE)
	public boolean eliminarPersona(int idPersona) {
		String sql = "DELETE FROM Persona WHERE Id_Persona = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idPersona);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar persona: " + e.getMessage());
			return false;
		}
	}

	// FACTURA (BASE)
	private int insertarFacturaBase(Connection con, Date fechaFactura, double montoTotal) throws SQLException {
		String sql = "INSERT INTO Factura (FechaFactura, MontoTotal) VALUES (?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setDate(1, fechaFactura);
			ps.setDouble(2, montoTotal);

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}

		return -1;
	}

	private boolean actualizarFacturaBase(Connection con, int idFactura, Date fechaFactura, double montoTotal)
			throws SQLException {

		String sql = "UPDATE Factura SET FechaFactura = ?, MontoTotal = ? WHERE Id_Factura = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setDate(1, fechaFactura);
			ps.setDouble(2, montoTotal);
			ps.setInt(3, idFactura);

			return ps.executeUpdate() > 0;
		}
	}

	public boolean eliminarFactura(int idFactura) {
		String sql = "DELETE FROM Factura WHERE Id_Factura = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idFactura);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar factura: " + e.getMessage());
			return false;
		}
	}

	// FACTURA_COMPRA
	public boolean insertarFacturaCompra(Date fechaFactura, double montoTotal, int idProveedor, String codCompra) {

		String sql = "INSERT INTO Factura_Compra (Id_Factura, Id_P_Proveedor, CodCompra) VALUES (?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idFactura = insertarFacturaBase(con, fechaFactura, montoTotal);

				if (idFactura == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idFactura);
					ps.setInt(2, idProveedor);
					ps.setString(3, codCompra);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar factura de compra: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarFacturaCompra(int idFactura, Date fechaFactura, double montoTotal, int idProveedor,
			String codCompra) {

		String sql = "UPDATE Factura_Compra SET Id_P_Proveedor = ?, CodCompra = ? WHERE Id_Factura = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarFacturaBase(con, idFactura, fechaFactura, montoTotal);

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idProveedor);
					ps.setString(2, codCompra);
					ps.setInt(3, idFactura);

					if (ps.executeUpdate() == 0) {
						con.rollback();
						return false;
					}
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar factura de compra: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarFacturaCompra(int idFactura) {
		return eliminarFactura(idFactura);
	}

	// ============================
	// FACTURA_VENTA
	// ============================

	public boolean insertarFacturaVenta(Date fechaFactura, double montoTotal, int idEmpleado, int idCliente,
			double porcentajeGanancia, String codVenta) {

		String sql = "INSERT INTO Factura_Venta (Id_Factura, Id_Empleado, Id_P_Cliente, "
				+ "PorcentajeGanancia, CodVenta) VALUES (?, ?, ?, ?, ?)";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				int idFactura = insertarFacturaBase(con, fechaFactura, montoTotal);

				if (idFactura == -1) {
					con.rollback();
					return false;
				}

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idFactura);
					ps.setInt(2, idEmpleado);
					ps.setInt(3, idCliente);
					ps.setDouble(4, porcentajeGanancia);
					ps.setString(5, codVenta);
					ps.executeUpdate();
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar factura de venta: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarFacturaVenta(int idFactura, Date fechaFactura, double montoTotal, int idEmpleado,
			int idCliente, double porcentajeGanancia, String codVenta) {

		String sql = "UPDATE Factura_Venta SET Id_Empleado = ?, Id_P_Cliente = ?, "
				+ "PorcentajeGanancia = ?, CodVenta = ? WHERE Id_Factura = ?";

		try (Connection con = conexion.getConexion()) {
			con.setAutoCommit(false);
			try {
				actualizarFacturaBase(con, idFactura, fechaFactura, montoTotal);

				try (PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setInt(1, idEmpleado);
					ps.setInt(2, idCliente);
					ps.setDouble(3, porcentajeGanancia);
					ps.setString(4, codVenta);
					ps.setInt(5, idFactura);

					if (ps.executeUpdate() == 0) {
						con.rollback();
						return false;
					}
				}

				con.commit();
				return true;

			} catch (SQLException e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar factura de venta: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarFacturaVenta(int idFactura) {
		return eliminarFactura(idFactura);
	}

	// DETALLE_FACTURA_COMPRA 
	public boolean insertarDetalleFacturaCompra(int idProducto, int idFacturaCompra, int cantProducto, double subtotal,
			double precioUnitario) {

		String sql = "INSERT INTO Detalle_Factura_Compra (Id_Producto, Id_F_Compra, "
				+ "CantProducto, Subtotal, PrecioUnitario) VALUES (?, ?, ?, ?, ?)";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idProducto);
			ps.setInt(2, idFacturaCompra);
			ps.setInt(3, cantProducto);
			ps.setDouble(4, subtotal);
			ps.setDouble(5, precioUnitario);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar detalle de compra: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarDetalleFacturaCompra(int idProducto, int idFacturaCompra, int cantProducto,
			double subtotal, double precioUnitario) {

		String sql = "UPDATE Detalle_Factura_Compra SET CantProducto = ?, Subtotal = ?, "
				+ "PrecioUnitario = ? WHERE Id_Producto = ? AND Id_F_Compra = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, cantProducto);
			ps.setDouble(2, subtotal);
			ps.setDouble(3, precioUnitario);
			ps.setInt(4, idProducto);
			ps.setInt(5, idFacturaCompra);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar detalle de compra: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarDetalleFacturaCompra(int idProducto, int idFacturaCompra) {
		String sql = "DELETE FROM Detalle_Factura_Compra WHERE Id_Producto = ? AND Id_F_Compra = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idProducto);
			ps.setInt(2, idFacturaCompra);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar detalle de compra: " + e.getMessage());
			return false;
		}
	}

	// DETALLE_FACTURA_VENTA 
	public boolean insertarDetalleFacturaVenta(int idProducto, int idFacturaVenta, double subtotal,
			double precioUnitario) {

		String sql = "INSERT INTO Detalle_Factura_Venta (Id_Producto, Id_F_Venta, "
				+ "Subtotal, PrecioUnitario) VALUES (?, ?, ?, ?)";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idProducto);
			ps.setInt(2, idFacturaVenta);
			ps.setDouble(3, subtotal);
			ps.setDouble(4, precioUnitario);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar detalle de venta: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarDetalleFacturaVenta(int idProducto, int idFacturaVenta, double subtotal,
			double precioUnitario) {

		String sql = "UPDATE Detalle_Factura_Venta SET Subtotal = ?, PrecioUnitario = ? "
				+ "WHERE Id_Producto = ? AND Id_F_Venta = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDouble(1, subtotal);
			ps.setDouble(2, precioUnitario);
			ps.setInt(3, idProducto);
			ps.setInt(4, idFacturaVenta);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar detalle de venta: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarDetalleFacturaVenta(int idProducto, int idFacturaVenta) {
		String sql = "DELETE FROM Detalle_Factura_Venta WHERE Id_Producto = ? AND Id_F_Venta = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idProducto);
			ps.setInt(2, idFacturaVenta);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar detalle de venta: " + e.getMessage());
			return false;
		}
	}

	
	// PROVEEDOR_PRODUCTO 

	public boolean insertarProveedorProducto(int idProducto, int idProveedor, Date fecha) {
		String sql = "INSERT INTO Proveedor_Producto (Id_Producto, Id_P_Proveedor, Fecha) VALUES (?, ?, ?)";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idProducto);
			ps.setInt(2, idProveedor);
			ps.setDate(3, fecha);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar proveedor-producto: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarProveedorProducto(int idProducto, int idProveedor, Date fecha) {
		String sql = "UPDATE Proveedor_Producto SET Fecha = ? WHERE Id_Producto = ? AND Id_P_Proveedor = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setDate(1, fecha);
			ps.setInt(2, idProducto);
			ps.setInt(3, idProveedor);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar proveedor-producto: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarProveedorProducto(int idProducto, int idProveedor) {
		String sql = "DELETE FROM Proveedor_Producto WHERE Id_Producto = ? AND Id_P_Proveedor = ?";

		try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idProducto);
			ps.setInt(2, idProveedor);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al eliminar proveedor-producto: " + e.getMessage());
			return false;
		}
	}
}