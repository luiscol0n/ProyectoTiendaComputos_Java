package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class FacturaCompra extends Factura implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Proveedor proveedor;
	private ArrayList<DetalleFacturaCompra> detallesCompra;
	
	public Proveedor getProveedor() {
		return proveedor;
	}

	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public ArrayList<DetalleFacturaCompra> getDetallesCompra() {
	    return detallesCompra;
	}

	public void setDetallesCompra(ArrayList<DetalleFacturaCompra> detallesCompra) {
	    this.detallesCompra = detallesCompra;
	}
	
	public FacturaCompra(String id, LocalDate fechaFactura, ArrayList<Producto> productosFacturados,
			Proveedor proveedor,int CantidadxProducto, double precio) {
		super(id, fechaFactura, productosFacturados,CantidadxProducto, precio);
		this.proveedor = proveedor;
		this.detallesCompra = new ArrayList<DetalleFacturaCompra>();
	
	}

}
