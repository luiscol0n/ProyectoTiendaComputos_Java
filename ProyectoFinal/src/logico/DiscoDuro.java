package logico;

import java.io.Serializable;

public class DiscoDuro extends Producto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String modelo;
	private float capacidad;
	private String tipoConexion;
	
	public String getModelo() {
		return modelo;
	}
	
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	
	public float getCapacidad() {
		return capacidad;
	}
	
	public void setCapacidad(float capacidad) {
		this.capacidad = capacidad;
	}
	
	public String getTipoConexion() {
		return tipoConexion;
	}
	
	public void setTipoConexion(String tipoConexion) {
		this.tipoConexion = tipoConexion;
	}
	
	public DiscoDuro(String numSerie, int cantDisponible, Persona proveedor, String marca, float precio, String modelo, float capacidad,
			String tipoConexion) {
		super(numSerie, cantDisponible, proveedor, marca, precio);
		this.modelo = modelo;
		this.capacidad = capacidad;
		this.tipoConexion = tipoConexion;
	}

}

//.