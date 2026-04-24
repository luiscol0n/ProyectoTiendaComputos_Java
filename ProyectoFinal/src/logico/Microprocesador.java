package logico;

import java.io.Serializable;

public class Microprocesador extends Producto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String modelo;
	private String socket;
	private int velocidadProcesamiento;
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getSocket() {
		return socket;
	}
	public void setSocket(String socket) {
		this.socket = socket;
	}
	public int getVelocidadProcesamiento() {
		return velocidadProcesamiento;
	}
	public void setVelocidadProcesamiento(int velocidadProcesamiento) {
		this.velocidadProcesamiento = velocidadProcesamiento;
	}
	
	public Microprocesador(String numSerie, int cantDisponible, Persona proveedor, String marca, float precio, String modelo,
			String socket, int velocidadProcesamiento) {
		super(numSerie, cantDisponible, proveedor, marca, precio);
		this.modelo = modelo;
		this.socket = socket;
		this.velocidadProcesamiento = velocidadProcesamiento;
	}
}

//.
