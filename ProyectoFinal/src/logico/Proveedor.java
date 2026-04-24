package logico;

import java.io.Serializable;

public class Proveedor extends Persona implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String empresa; 
	
	public String getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	public Proveedor(String nombre, int edad, String cedula, String correo, String empresa) {
		super(nombre, edad, cedula, correo);
		super.id = Tienda.getInstance().generarIdProveedor();
		this.empresa = empresa;	
	}
	
	@Override
	public String toString() {
		return id;
	}
}


//.