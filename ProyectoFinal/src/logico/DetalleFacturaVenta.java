package logico;

import java.io.Serializable;

public class DetalleFacturaVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idDetalle;
    private String idFactura;
    private Producto producto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleFacturaVenta(String idFactura, int numeroLinea, Producto producto, int cantidad, double precioUnitario) {
        this.idFactura = idFactura;
        this.idDetalle = idFactura + "-" + numeroLinea;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public String getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(String idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        recalcularSubtotal();
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        recalcularSubtotal();
    }

    public double getSubtotal() {
        return subtotal;
    }

    private void recalcularSubtotal() {
        this.subtotal = this.cantidad * this.precioUnitario;
    }
}