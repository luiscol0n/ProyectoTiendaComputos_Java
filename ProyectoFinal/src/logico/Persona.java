package logico;

import java.io.Serializable;

public class Persona implements Serializable {
	
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String nombre;
	protected int edad;
	protected String cedula;
	protected String correo;
	public String getId() {
		return id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getCedula() {
		return cedula;
	}
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Persona(String nombre, int edad, String cedula, String correo) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.cedula = cedula;
		this.correo = correo;
	}

}


//.