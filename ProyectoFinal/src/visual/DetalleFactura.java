package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logico.DetalleFacturaCompra;
import logico.DetalleFacturaVenta;
import logico.Factura;
import logico.FacturaCompra;
import logico.FacturaVenta;
import logico.Producto;

public class DetalleFactura extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtFactura;
    private JTextField txtFecha;
    private JTextField txtTipo;
    private JTextField txtPersona;
    private JTextField txtMontoTotal;

    public DetalleFactura(Factura factura) {
        setIconImage(Toolkit.getDefaultToolkit().getImage(DetalleFactura.class.getResource("/Imagenes/to-do-list.png")));
        setTitle("Detalle de la Factura");
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(new Color(240, 255, 240));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblFactura = new JLabel("Factura:");
        lblFactura.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblFactura.setBounds(10, 11, 60, 20);
        contentPanel.add(lblFactura);

        txtFactura = new JTextField();
        txtFactura.setEditable(false);
        txtFactura.setBounds(80, 11, 150, 20);
        contentPanel.add(txtFactura);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblFecha.setBounds(260, 11, 50, 20);
        contentPanel.add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        txtFecha.setBounds(310, 11, 120, 20);
        contentPanel.add(txtFecha);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblTipo.setBounds(460, 11, 40, 20);
        contentPanel.add(lblTipo);

        txtTipo = new JTextField();
        txtTipo.setEditable(false);
        txtTipo.setBounds(500, 11, 120, 20);
        contentPanel.add(txtTipo);

        JLabel lblPersona = new JLabel("Cliente/Proveedor:");
        lblPersona.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblPersona.setBounds(10, 45, 130, 20);
        contentPanel.add(lblPersona);

        txtPersona = new JTextField();
        txtPersona.setEditable(false);
        txtPersona.setBounds(140, 45, 250, 20);
        contentPanel.add(txtPersona);

        JLabel lblMonto = new JLabel("Monto Total:");
        lblMonto.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblMonto.setBounds(420, 45, 90, 20);
        contentPanel.add(lblMonto);

        txtMontoTotal = new JTextField();
        txtMontoTotal.setEditable(false);
        txtMontoTotal.setBounds(510, 45, 120, 20);
        contentPanel.add(txtMontoTotal);

        String[] columnas = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        model = new DefaultTableModel(columnas, 0);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 90, 764, 320);
        contentPanel.add(scrollPane);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(new Color(240, 255, 240));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(250, 128, 114));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnCerrar.addActionListener(e -> dispose());
        buttonPane.add(btnCerrar);

        cargarFactura(factura);
    }

    private void cargarFactura(Factura factura) {
        txtFactura.setText(factura.getId());
        txtFecha.setText(factura.getFechaFactura().toString());
        txtMontoTotal.setText(String.format("%.2f", factura.getmontoTotal()));

        model.setRowCount(0);

        if (factura instanceof FacturaCompra) {
            FacturaCompra fc = (FacturaCompra) factura;
            txtTipo.setText("Compra");

            if (fc.getProveedor() != null) {
                txtPersona.setText(fc.getProveedor().getEmpresa());
            } else {
                txtPersona.setText("Sin proveedor");
            }

            if (fc.getDetallesCompra() != null && !fc.getDetallesCompra().isEmpty()) {
                for (DetalleFacturaCompra det : fc.getDetallesCompra()) {
                    Producto p = det.getProducto();
                    Object[] fila = {
                        p != null ? armarNombreProducto(p) : "Sin producto",
                        det.getCantidad(),
                        String.format("%.2f", det.getPrecioUnitario()),
                        String.format("%.2f", det.getSubtotal())
                    };
                    model.addRow(fila);
                }
            } else {
                cargarFacturaVieja(fc);
            }

        } else if (factura instanceof FacturaVenta) {
            FacturaVenta fv = (FacturaVenta) factura;
            txtTipo.setText("Venta");

            if (fv.getCliente() != null) {
                txtPersona.setText(fv.getCliente().getNombre());
            } else {
                txtPersona.setText("Sin cliente");
            }

            if (fv.getDetallesVenta() != null && !fv.getDetallesVenta().isEmpty()) {
                for (DetalleFacturaVenta det : fv.getDetallesVenta()) {
                    Producto p = det.getProducto();
                    Object[] fila = {
                        p != null ? armarNombreProducto(p) : "Sin producto",
                        det.getCantidad(),
                        String.format("%.2f", det.getPrecioUnitario()),
                        String.format("%.2f", det.getSubtotal())
                    };
                    model.addRow(fila);
                }
            } else {
                cargarFacturaVieja(fv);
            }
        }
    }

    private void cargarFacturaVieja(Factura factura) {
        if (factura.getProductosFacturados() == null || factura.getProductosFacturados().isEmpty()) {
            return;
        }

        int cantidadTotal = factura.getCantidadxProducto();
        int cantidadProductos = factura.getProductosFacturados().size();

        int cantidadPorProducto = 1;
        if (cantidadProductos > 0 && cantidadTotal > 0) {
            cantidadPorProducto = cantidadTotal / cantidadProductos;
            if (cantidadPorProducto <= 0) {
                cantidadPorProducto = 1;
            }
        }

        for (Producto p : factura.getProductosFacturados()) {
            double precioUnitario = p.getPrecio();
            double subtotal = precioUnitario * cantidadPorProducto;

            Object[] fila = {
                p != null ? armarNombreProducto(p) : "Sin producto",
                cantidadPorProducto,
                String.format("%.2f", precioUnitario),
                String.format("%.2f", subtotal)
            };
            model.addRow(fila);
        }
    }

    private String armarNombreProducto(Producto p) {
        if (p == null) {
            return "Sin producto";
        }

        String marca = p.getMarca() != null ? p.getMarca() : "";
        String serie = p.getNumSerie() != null ? p.getNumSerie() : "";

        if (!marca.isEmpty() && !serie.isEmpty()) {
            return marca + " - " + serie;
        } else if (!marca.isEmpty()) {
            return marca;
        } else if (!serie.isEmpty()) {
            return serie;
        } else {
            return p.getId();
        }
    }
}