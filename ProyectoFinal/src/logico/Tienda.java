package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Tienda implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public static int numCliente = 1;
	public static int numEmpleado = 1;
	public static int numProveedor = 1;
	public static int numProducto = 1;
	public static int numFactura =1;
	
	private ArrayList<User> misUsers;
	private static User loginUser;

	
	private ArrayList <Persona> listaPersonas;
	private ArrayList <Producto> listaProductos;
	private ArrayList <Factura> listaFacturas;
	
	private static Tienda miTienda = null;

	
	public static Tienda getInstance(){
		if(miTienda == null){
			miTienda = new Tienda();
		}return miTienda;
	}
	
	public static Tienda getMiTienda() {
		return miTienda;
	}

	public static void setMiTienda(Tienda miTienda) {
		Tienda.miTienda = miTienda;
	}
	
	public String generarIdProducto() {		
		String id = "PDT-" + numProducto;
		numProducto++;
		return id;

	}
	
	public String generarIdCliente() {		
		String id = "CLI-" + numCliente;
		numCliente++;
		return id;

	}
	
	public String generarIdEmpleado() {		
		String id = "EMP-" + numEmpleado;
		numEmpleado++;
		return id;

	}
	
	public String generarIdProveedor() {		
		String id = "PVD-" + numProveedor;
		numProveedor++;
		return id;

	}
	
	public String generarIdFactura() {		
		String id = "FAC-" + numFactura;
		numFactura++;
		return id;

	}

	public static int getNumCliente() {
		return numCliente;
	}

	public static void setNumCliente(int numCliente) {
		Tienda.numCliente = numCliente;
	}

	public static int getNumEmpleado() {
		return numEmpleado;
	}

	public static void setNumEmpleado(int numEmpleado) {
		Tienda.numEmpleado = numEmpleado;
	}

	public static int getNumProveedor() {
		return numProveedor;
	}

	public static void setNumProveedor(int numProveedor) {
		Tienda.numProveedor = numProveedor;
	}

	public static int getNumProducto() {
		return numProducto;
	}

	public static void setNumProducto(int numProducto) {
		Tienda.numProducto = numProducto;
	}

	public static int getNumFactura() {
		return numFactura;
	}

	public static void setNumFactura(int numFactura) {
		Tienda.numFactura = numFactura;
	}
	
	public ArrayList<User> getMisUsers() {
		return misUsers;
	}

	public void setMisUsers(ArrayList<User> misUsers) {
		this.misUsers = misUsers;
	}

	public static User getLoginUser() {
		return loginUser;
	}

	public static void setLoginUser(User loginUser) {
		Tienda.loginUser = loginUser;
	}

	public ArrayList<Persona> getListaPersonas() {
		return listaPersonas;
	}

	public void setListaPersonas(ArrayList<Persona> listaPersonas) {
		this.listaPersonas = listaPersonas;
	}

	public ArrayList<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(ArrayList<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public ArrayList<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public void setListaFacturas(ArrayList<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

	public Tienda() {
		super();
		this.listaPersonas = new ArrayList<>();
		this.listaProductos = new ArrayList<>();
		this.listaFacturas = new ArrayList<>();
		this.misUsers = new ArrayList<>();
	}
	
	// Funcion que genera los IDs
	public void generarIds() {
	    // 1. ANALIZAR PRODUCTOS
	    if (!getListaProductos().isEmpty()) {
	        String lastId = getListaProductos().get(getListaProductos().size() - 1).getId();
	        // Usamos indexOf("-") + 1 para saltar el prefijo dinámicamente
	        numProducto = Integer.parseInt(lastId.substring(lastId.indexOf("-") + 1)) + 1;
	    } else {
	        numProducto = 1;
	    }

	    // 2. ANALIZAR PERSONAS (Fuera del bucle para evitar reinicios)
	    numCliente = 1;
	    numEmpleado = 1;
	    numProveedor = 1;

	    if (!getListaPersonas().isEmpty()) {
	        for (Persona persona : getListaPersonas()) {
	            if (persona instanceof Cliente) {
	                String id = ((Cliente) persona).getId();
	                int val = Integer.parseInt(id.substring(id.indexOf("-") + 1));
	                if (val >= numCliente) numCliente = val + 1;
	            }
	            if (persona instanceof Empleado) {
	                // Aquí usamos el método que tengas para obtener el String "EMP-X"
	                String id = ((Empleado) persona).getId(); 
	                int val = Integer.parseInt(id.substring(id.indexOf("-") + 1));
	                if (val >= numEmpleado) numEmpleado = val + 1;
	            }
	            if (persona instanceof Proveedor) {
	                String id = ((Proveedor) persona).getId(); // Ajustar según tu atributo
	                int val = Integer.parseInt(id.substring(id.indexOf("-") + 1));
	                if (val >= numProveedor) numProveedor = val + 1;
	            }
	        }
	    }

	    // 3. ANALIZAR FACTURAS
	    if (!getListaFacturas().isEmpty()) {
	        String lastId = getListaFacturas().get(getListaFacturas().size() - 1).getId();
	        numFactura = Integer.parseInt(lastId.substring(lastId.indexOf("-") + 1)) + 1;
	    } else {
	        numFactura = 1;
	    }
	}
	
	public boolean registrarPersona(Persona newPersona) {
		listaPersonas.add(newPersona);
		return true;
		
	}
	
	public boolean registrarProducto(Producto newProducto) {
		listaProductos.add(newProducto);
		return true;
		
	}
	
	public boolean registrarFactura(Factura newFactura) {
		listaFacturas.add(newFactura);
		return true;
		
	}
	
	public Factura buscarFacturaId(String idFactura) {
		Factura factura=null;
		boolean encontrado = false;
		int i = 0;
			while (!encontrado && i < listaFacturas.size()) {
				if(listaFacturas.get(i).getId().equalsIgnoreCase(idFactura)){
					encontrado = true;
					factura= listaFacturas.get(i);
				}
				i++;
		}
		
		return factura;
	}
	
	public Producto buscarProductoId(String idProducto) {
		Producto producto=null;
		boolean encontrado = false;
		int i = 0;
			while (!encontrado && i < listaProductos.size()) {
				if(listaProductos.get(i).getId().equalsIgnoreCase(idProducto)){
					encontrado = true;
					producto= listaProductos.get(i);
				}
				i++;
		}
		
		return producto;
	}
	public Persona buscarPersonaId(String idPersona) {
		Persona persona=null;
		boolean encontrado = false;
		int i = 0;
			while (!encontrado && i < listaPersonas.size()) {
				if(listaPersonas.get(i).getId().equalsIgnoreCase(idPersona)){
					encontrado = true;
					persona= listaPersonas.get(i);
				}
				i++;
		}
		
		return persona;
	}
	
	public int buscarPersonaByIdgetIndex(String idPersona) {
		int persona = -1;
		boolean encontrado  = false;
		int i =0;
		while (!encontrado && i < listaPersonas.size()) {
			if(listaPersonas.get(i).getId().equalsIgnoreCase(idPersona)){
				persona = i;
				encontrado = true;
			}
			i++;
		}

		return persona;
	}

	public void updatePersona(Persona persona) {
		int index = buscarPersonaByIdgetIndex(persona.getId());
		if(index!= -1){
			listaPersonas.set(index, persona);
		}
		
	}

	public void eliminarPersona(String idPersonaSeleccionada) {
		Persona aux = buscarPersonaId(idPersonaSeleccionada);
		if(aux!=null){
			listaPersonas.remove(aux);
		}
		
	}

	public void verSiClienteVIP() { /*Nota: Lo hice de manera en que se llame esta funcion para actualizar a TODOS los Clientes de la lista*/
		
		for (Persona cliente : listaPersonas) {
			
			if(cliente instanceof Cliente) {
				
				int cantidadVentas = ((Cliente) cliente).getCantVentas();
				
				if(cantidadVentas > 5) {
					((Cliente) cliente).setClasificacion('V');
					
				}
			}
			
		}
		
	}
	
	public float calcularSalarioEmpleado(Empleado empleado) {
		float totalSalario =0;
		totalSalario  = empleado.getComisionVentas() * empleado.getCantVentas();
		return totalSalario;
	}

	public int buscarProductoByIdgetIndex(String idProducto) {
	    int index = -1;
	    boolean encontrado = false;
	    int i = 0;
	    while (!encontrado && i < listaProductos.size()) {
	        if (listaProductos.get(i).getId().equalsIgnoreCase(idProducto)) {
	            index = i;
	            encontrado = true;
	        }
	        i++;
	    }
	    return index;
	}

	public void updateProducto(Producto producto) {
	    int index = buscarProductoByIdgetIndex(producto.getId());
	    if (index != -1) {
	        listaProductos.set(index, producto);
	    }
	}

	public void eliminarProducto(String idProducto) {
	    Producto aux = buscarProductoId(idProducto);
	    if (aux != null) {
	        listaProductos.remove(aux);
	    }
	}

	public int buscarFacturaByIdgetIndex(String idFactura) {
	    int index = -1;
	    boolean encontrado = false;
	    int i = 0;
	    while (!encontrado && i < listaFacturas.size()) {
	        if (listaFacturas.get(i).getId().equalsIgnoreCase(idFactura)) {
	            index = i;
	            encontrado = true;
	        }
	        i++;
	    }
	    return index;
	}

	public void updateFactura(Factura factura) {
	    int index = buscarFacturaByIdgetIndex(factura.getId());
	    if (index != -1) {
	        listaFacturas.set(index, factura);
	    }
	}

	public void eliminarFactura(String idFactura) {
	    Factura aux = buscarFacturaId(idFactura);
	    if (aux != null) {
	        listaFacturas.remove(aux);
	    }
	}
	
	/*Nota:* Produce una alarma si el producto se esta agotando*/
	
	public boolean alarmaProducto(String idProducto)
	{
		Boolean alarma=false;
		Producto producto=buscarProductoId(idProducto);
		if(producto.cantDisponible<=1)
		{
			alarma=true;
			
		}
		return alarma;
	}

	// Nota: el total de todas las facturas de venta
	public float totalVentas()
	{
		float total=0;
		for (Factura factu : listaFacturas) {
			if(factu instanceof FacturaVenta)
			{
				total+=calculaPrecioProducto(factu.getProductosFacturados(), ((FacturaVenta) factu).getCliente().getId());
	        
			}
			
		}
		return total;
	}
	//Nota: el total de la facturas de compra
	public float totalCompra()
	{
		float total=0;
		for (Factura fat : listaFacturas) {
			if(fat instanceof FacturaCompra)
			{
				total+=calculaPrecioProducto(fat.getProductosFacturados(), "");
			}
			
		}
		return total;
	}
	// Nota: total de las ganancias producidas por la tienda
	public float totalGanancias() {
	    float total = 0;
	    for (Factura fat : listaFacturas) {
	        if (fat instanceof FacturaVenta) {
	            FacturaVenta factu = (FacturaVenta) fat;
	            total += calculaPrecioProducto(factu.getProductosFacturados(), factu.getCliente().getId());
	        } else if (fat instanceof FacturaCompra) {
	            FacturaCompra fact = (FacturaCompra) fat;
	            total -= calculaPrecioProducto(fact.getProductosFacturados(), " ");
	        }
	    }
	    return total;
	}

	// Nota: esta dice el producto favorito
	public Producto productoFavorito()
	{
		Producto pro = null;
		int cantMay=0;
		int cantP=0;
		for (Producto prod : listaProductos) {
			cantP=0;
			for (Factura fat : listaFacturas) {
				for (Producto producto : fat.getProductosFacturados()) {
					if(producto.getId().equalsIgnoreCase(prod.getId()))
					{
						cantP++;
					}
				}
				
			}
			if(cantMay<cantP)
			{
				cantMay=cantP;
				pro= prod;
			}
			
		}
		return pro;
	}

	//  Nota: esta funcion determina el total de los productos en inventario
	public int[] cantInventario()
	{
		int [] array =new int[4];
		for (Producto producto : listaProductos) {
			int cantidad = producto.getCantDisponible();
				if(producto instanceof MotherBoard)
				{
					array[0] += cantidad;
				}
				else if (producto instanceof DiscoDuro) {
					array[1] += cantidad; 
				} else if (producto instanceof MemoriaRam) {
					array[2] += cantidad;
				} else if (producto instanceof Microprocesador) {
					array[3] += cantidad;
				}	
		}
		return array;
	}
	
	//Nota: Esta funcion te dice si el producto esta disponible o no
	public boolean disponibleProducto(String id)
	{
		boolean disponible=false;
		Producto producto= buscarProductoId(id);
		if(producto != null && producto.isEstado()) {
			disponible=true;}
		return disponible;
	}
	//Nota: Calcula el descuento a aplicarse
	public float descuentoAplicado(String idCliente,boolean esCombo)
	{
		float descuento=0;
		Cliente clien=(Cliente)buscarPersonaId(idCliente);
		if(clien!=null)
		{
			if(clien.getClasificacion()=='V')
			{
				descuento+=5;
			}
		}
		if(esCombo)
		{
			descuento+=10;
		}
		return descuento;
	}
	public float calculaPrecioProducto(ArrayList<Producto> productos, String idCliente) {
	    float precio = 0;
	    for (Producto producto : productos) {
	        precio += producto.getPrecio();  
	    }
	   
	    return precio;
	}

	
	public ArrayList<Producto> getProductoNoSeleccionados() {
		ArrayList<Producto> noSeleccionadoArrayList =new ArrayList<>();
		for (Producto pro : listaProductos) {
			if(!pro.isSeleccionado())
			{
				noSeleccionadoArrayList.add(pro);
			}
			
		}
		return noSeleccionadoArrayList;
	}

	public int getCantProductos() {
		int cant=0;
		for (Producto produ : listaProductos) {
			if(produ.isSeleccionado())
			{
				cant++;
			}
			
		}
		return cant;
	}

	public ArrayList<Producto> getProductosSeleccionados() {
		ArrayList<Producto> SeleccionadoArrayList =new ArrayList<>();
		for (Producto produ : listaProductos) {
			if(produ.isSeleccionado())
			{
				SeleccionadoArrayList.add(produ);
			}
			
		}
		return SeleccionadoArrayList;
	}
	
	public void RegistrarUser(User user) {
		misUsers.add(user);
	}

	//Nota: Verifica que el login sea legítimo
	public boolean confirmLogin(String text, String text2) {
		boolean login = false;
		for (User user : misUsers) {
			if(user.getUserName().equals(text) && user.getPass().equals(text2)){
				loginUser = user;
				login = true;
			}
		}
		return login;
	}
	
	// Funciones para los Usuarios
	public int buscarUsuarioByIdgetIndex(String nombreUsuario) {
		int index = -1;
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && i < listaProductos.size()) {
			if (misUsers.get(i).getUserName().equalsIgnoreCase(nombreUsuario)) {
				index = i;
				encontrado = true;
			}
			i++;
		}
		return index;
	}
	
	public User buscarUsuarioPorUsername(String nombreUsuario) {
		User usuario = null;
		boolean encontrado = false;
		int i = 0;
		while (!encontrado && i < misUsers.size()) {
			if(misUsers.get(i).getUserName().equalsIgnoreCase(nombreUsuario)){
				encontrado = true;
				usuario = misUsers.get(i);
			}
			i++;
		}

		return usuario;
	}

	public void updateUsuario(User usuario) {
		int index = buscarUsuarioByIdgetIndex(usuario.getUserName());
		if (index != -1) {
			misUsers.set(index, usuario);
		}
	}

	public void eliminarUsuario(String nombreUsuario) {
		User aux = buscarUsuarioPorUsername(nombreUsuario);
		if (aux != null) {
			misUsers.remove(aux);
		}
	}
	public void recargaSelecionado()
	{
		for (Producto product : listaProductos) {
			if(product.getCantDisponible()>0)
			{
				product.setSeleccionado(false);
			}
		}
	}

	public Empleado buscarEmpleadoPorUsuario(User usuario) {
	    Empleado empleadoEncontrado = null;
	    
	    for (Persona p : listaPersonas) {
	        if (p instanceof Empleado) {
	            Empleado emp = (Empleado) p;
	            // Comparamos el ID del usuario del empleado con el ID del usuario que recibimos
	            if (emp.getUsuario() != null && emp.getUsuario().getUserName() == usuario.getUserName()) {
	                empleadoEncontrado = emp;
	                break; // Terminamos la búsqueda al encontrarlo
	            }
	        }
	    }
	    
	    return empleadoEncontrado;
	}

	public ArrayList<User> usuariosSinEmpleado() {
	    ArrayList<User> disponibles = new ArrayList<>();
	    
	    for (User user : misUsers) {
	        boolean asignado = false;
	        
	        // Que el usuario no sea el Admin, Admin.
	        if (user.getUserName().equalsIgnoreCase("Admin")) {
	        	continue;
	        }
	        
	        // Buscamos si este usuario ya lo tiene algún empleado
	        for (Persona persona : listaPersonas) {
	        	if (persona instanceof Empleado) {
	        		Empleado emp = (Empleado) persona;
		            // Comparamos por el nombre de usuario
		            if (emp.getUsuario() != null && emp.getUsuario().getUserName().equals(user.getUserName())) {
		                asignado = true;
		                break;
		            }
	        	}
	        }
	        // Si nadie lo tiene, lo agregamos a la lista de disponibles
	        if (!asignado) {
	            disponibles.add(user);
	        }
	    }
	    
	    return disponibles;
	}
	// AGREGAR ESTO EN Tienda.java (paquete logico)

	public Cliente buscarClientePorIdSQL(int idSQL) {
	    for (Persona p : listaPersonas) {
	        if (p instanceof Cliente && p.getIdSQL() == idSQL) {
	            return (Cliente) p;
	        }
	    }
	    return null;
	}

	public Empleado buscarEmpleadoPorIdSQL(int idSQL) {
	    for (Persona p : listaPersonas) {
	        if (p instanceof Empleado && p.getIdSQL() == idSQL) {
	            return (Empleado) p;
	        }
	    }
	    return null;
	}

	public Proveedor buscarProveedorPorIdSQL(int idSQL) {
	    for (Persona p : listaPersonas) {
	        if (p instanceof Proveedor && p.getIdSQL() == idSQL) {
	            return (Proveedor) p;
	        }
	    }
	    return null;
	}

	public Producto buscarProductoPorIdSQL(int idSQL) {
	    for (Producto p : listaProductos) {
	        if (p.getIdSQL() == idSQL) {
	            return p;
	        }
	    }
	    return null;
	}
	
}


