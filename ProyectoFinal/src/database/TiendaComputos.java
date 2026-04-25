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

	private ConexionDB conexion = new ConexionDB();

	// METODOS DE USUARIO
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
			con.setAutoCommit(false); // Iniciamos transacciÃ³n

			// 1. Insertar en Persona
			psPersona = con.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS);
			psPersona.setString(1, cliente.getNombre());
			psPersona.setString(2, cliente.getCorreo());
			psPersona.setInt(3, cliente.getEdad());
			psPersona.setString(4, cliente.getCedula());
			psPersona.executeUpdate();

			// 2. Obtener el ID numÃ©rico generado por SQL
			rs = psPersona.getGeneratedKeys();
			int idPersonaSQL = 0;
			if (rs.next()) {
				idPersonaSQL = rs.getInt(1);
			}

			// 3. Insertar en Cliente usando el ID de SQL y el id String de Java
			// (CodCliente)
			psCliente = con.prepareStatement(sqlCliente);
			psCliente.setInt(1, idPersonaSQL);
			psCliente.setString(2, cliente.getId()); // AquÃ­ usas tu "CLI-#"
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
			// Cerrar recursos aquÃ­
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

		// 1. Sentencia para obtener el ID tÃ©cnico del Usuario
		String sqlBusquedaUser = "SELECT Id_Usuario FROM Usuario WHERE Username = ?";
		String sqlPersona = "INSERT INTO Persona (Nombre, Correo, Edad, Cedula, Clasificacion) VALUES (?, ?, ?, ?, 'Empleado')";
		String sqlEmpleado = "INSERT INTO Empleado (Id_Persona, CodEmpleado, ComisionVentas, CantVentas, Id_Usuario, EmpleadoMes) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			con = ConexionDB.getConexion(); // Usando tu clase de conexiÃ³n
			con.setAutoCommit(false); // Iniciamos transacciÃ³n por seguridad

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
			psEmpleado.setString(2, emp.getId()); // Tu cÃ³digo de negocio EMP-#
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

	public boolean insertarMotherBoard(MotherBoard mb) {
		Connection con = null;
		boolean exito = false;

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio) VALUES (?, ?, ?, ?)";
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

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio) VALUES (?, ?, ?, ?)";
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

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio) VALUES (?, ?, ?, ?)";
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

		String sqlProd = "INSERT INTO Producto (NumSerie, Marca, CantDisponible, Precio) VALUES (?, ?, ?, ?)";
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
	        con.setAutoCommit(false); // Iniciamos transacción

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

	        // 2. Insertar en FacturaVenta (Relacionando con Empleado y Cliente por Cédula)
	        psVenta = con.prepareStatement(sqlVenta);
	        psVenta.setInt(1, idFacturaSQL);
	        
	        // Buscamos los IDs de SQL usando los métodos auxiliares
	        int idEmpleadoSQL = obtenerIdPersonaPorCedula(factura.getVendedor().getCedula());
	        int idClienteSQL = obtenerIdPersonaPorCedula(factura.getCliente().getCedula());
	        
	        psVenta.setInt(2, idEmpleadoSQL);
	        psVenta.setInt(3, idClienteSQL);
	        psVenta.setDouble(4, 0.15); // Porcentaje de ganancia (puedes parametrizarlo)
	        psVenta.setString(5, factura.getId()); // Tu código FVE-#
	        psVenta.executeUpdate();

	        // 3. Insertar Detalles de la Factura
	        psDetalle = con.prepareStatement(sqlDetalle);
	        for (DetalleFacturaVenta det : factura.getDetallesVenta()) {
	            // Buscamos el ID del producto por su número de serie único
	            int idProductoSQL = obtenerIdProductoPorSerie(det.getProducto().getNumSerie());
	            
	            psDetalle.setInt(1, idProductoSQL);
	            psDetalle.setInt(2, idFacturaSQL);
	            psDetalle.setInt(3, det.getCantidad());
	            psDetalle.setDouble(4, det.getSubtotal());
	            psDetalle.setDouble(5, det.getPrecioUnitario());
	            psDetalle.addBatch(); // Añadimos al lote
	        }
	        psDetalle.executeBatch(); // Ejecutamos todos los detalles juntos

	        con.commit(); // Si todo salió bien, guardamos cambios
	        return true;

	    } catch (SQLException e) {
	        if (con != null) {
	            try {
	                con.rollback(); // Si algo falló, deshacemos todo
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        // Cerrar recursos para liberar memoria
	        try {
	            if (rs != null) rs.close();
	            if (psFactura != null) psFactura.close();
	            if (psVenta != null) psVenta.close();
	            if (psDetalle != null) psDetalle.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	// MÉTODOS AUXILIARES DE BÚSQUEDA 

	private int obtenerIdPersonaPorCedula(String cedula) throws SQLException {
	    int idEncontrado = -1;
	    String sql = "SELECT Id_Persona FROM Persona WHERE Cedula = ?";
	    // Usamos una conexión interna o la misma si prefieres pasarla por parámetro
	    try (Connection con = ConexionDB.getConexion(); 
	         PreparedStatement ps = con.prepareStatement(sql)) {
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
	    try (Connection con = ConexionDB.getConexion(); 
	         PreparedStatement ps = con.prepareStatement(sql)) {
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
	        con.setAutoCommit(false); // Iniciamos transacción

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

	        // 2. Insertar en FacturaCompra (Relacionando con Proveedor por Cédula/RNC)
	        psCompra = con.prepareStatement(sqlCompra);
	        psCompra.setInt(1, idFacturaSQL);
	        
	        // Buscamos el ID técnico del proveedor usando su cédula
	        int idProveedorSQL = obtenerIdPersonaPorCedula(factura.getProveedor().getCedula());
	        
	        if (idProveedorSQL == -1) {
	            throw new SQLException("No se encontró el ID del proveedor con la cédula proporcionada.");
	        }

	        psCompra.setInt(2, idProveedorSQL);
	        psCompra.setString(3, factura.getId()); // Tu código FCO-# de Java
	        psCompra.executeUpdate();

	        // 3. Insertar Detalles de la Factura de Compra
	        psDetalle = con.prepareStatement(sqlDetalle);
	        for (DetalleFacturaCompra det : factura.getDetallesCompra()) {
	            // Buscamos el ID del producto por su número de serie único
	            int idProductoSQL = obtenerIdProductoPorSerie(det.getProducto().getNumSerie());
	            
	            if (idProductoSQL == -1) {
	                throw new SQLException("No se encontró el producto con serie: " + det.getProducto().getNumSerie());
	            }

	            psDetalle.setInt(1, idProductoSQL);
	            psDetalle.setInt(2, idFacturaSQL);
	            psDetalle.setInt(3, det.getCantidad());
	            psDetalle.setDouble(4, det.getSubtotal());
	            psDetalle.setDouble(5, det.getPrecioUnitario());
	            psDetalle.addBatch();
	        }
	        psDetalle.executeBatch();

	        con.commit(); // Éxito total
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
	            if (rs != null) rs.close();
	            if (psFactura != null) psFactura.close();
	            if (psCompra != null) psCompra.close();
	            if (psDetalle != null) psDetalle.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
}