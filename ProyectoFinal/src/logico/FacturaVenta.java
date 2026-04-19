package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class FacturaVenta extends Factura {
	
	private Cliente cliente;
	private Empleado vendedor;
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Empleado getVendedor() {
		return vendedor;
	}
	public void setVendedor(Empleado vendedor) {
		this.vendedor = vendedor;
	}
	public FacturaVenta(String id, LocalDate fechaFactura, ArrayList<Producto> productosFacturados, Cliente cliente,
	int CantidadxProducto, double precio) {
		super(id, fechaFactura, productosFacturados,CantidadxProducto, precio);
		this.cliente = cliente;
		this.vendedor = vendedor;
		
	}
}
