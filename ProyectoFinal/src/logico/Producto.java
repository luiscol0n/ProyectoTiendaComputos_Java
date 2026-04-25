package logico;

import java.io.Serializable;

public class Producto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String numSerie;
	protected int cantDisponible;
	protected Persona proveedor;
	protected boolean estado;
	protected String marca;
	protected float precio;
	protected boolean seleccionado;
	protected int siemprePedir;
	protected int idSQL; 

	public int getIdSQL() { 
		return idSQL; 
	}
	public void setIdSQL(int idSQL) { 
		this.idSQL = idSQL; 
	}
	
	public String getId() {
		return id;
	}
	
	public String getNumSerie() {
		return numSerie;
	}
	
	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
	}
	
	public int getCantDisponible() {
		return cantDisponible;
	}
	
	public void setCantDisponible(int cantDisponible) {
		this.cantDisponible = cantDisponible;
	}
	
	public boolean isEstado() {
		return estado;
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public String getMarca() {
		return marca;
	}
	
	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	public float getPrecio() {
		return precio;
	}
	
	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public Persona getProveedor() {
		return proveedor;
	}

	public void setProveedor(Persona proveedor) {
		this.proveedor = proveedor;
	}

	public Producto(String numSerie, int cantDisponible, Persona proveedor, String marca, float precio) {
		super();
		this.id = Tienda.getInstance().generarIdProducto();
		this.numSerie = numSerie;
		this.cantDisponible = cantDisponible;
		this.proveedor = proveedor;
		this.estado = true;
		this.marca = marca;
		this.precio = precio;
		this.seleccionado=false;
		this.siemprePedir= cantDisponible;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public int getSiemprePedir() {
		return siemprePedir;
	}

	public void setSiemprePedir(int siemprePedir) {
		this.siemprePedir = siemprePedir;
	}
	


}

//.
