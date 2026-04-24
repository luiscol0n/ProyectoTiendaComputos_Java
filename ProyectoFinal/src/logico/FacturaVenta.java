package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class FacturaVenta extends Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    private Cliente cliente;
    private Empleado vendedor;
    private ArrayList<DetalleFacturaVenta> detallesVenta;

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

    public ArrayList<DetalleFacturaVenta> getDetallesVenta() {
        return detallesVenta;
    }

    public void setDetallesVenta(ArrayList<DetalleFacturaVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }

    public FacturaVenta(String id, LocalDate fechaFactura, ArrayList<Producto> productosFacturados, Cliente cliente,
            int CantidadxProducto, double precio) {
        super(id, fechaFactura, productosFacturados, CantidadxProducto, precio);
        this.cliente = cliente;
        this.vendedor = null;
        this.detallesVenta = new ArrayList<DetalleFacturaVenta>();
    }
}

//.