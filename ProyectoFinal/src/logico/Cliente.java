package logico;

import java.io.Serializable;

public class Cliente extends Persona implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private char clasificacion;
	private int cantVentas;
	public char getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(char clasificacion) {
		this.clasificacion = clasificacion;
	}
	public int getCantVentas() {
		return cantVentas;
	}
	public void setCantVentas(int cantVentas) {
		this.cantVentas = cantVentas;
	}
	public Cliente(String nombre, int edad, String cedula, String correo) {
		super(nombre, edad, cedula, correo);
		super.id = Tienda.getInstance().generarIdCliente();
		this.clasificacion = 'C'; /*Nota: Es para que empiece siempre en Comun*/
		this.cantVentas = 0;
	}
	
	

}


//.