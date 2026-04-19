package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import logico.Cliente;
import logico.Factura;
import logico.FacturaCompra;
import logico.FacturaVenta;
import logico.Proveedor;
import logico.Tienda;

public class ListadoFacturas extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JButton btnVerDetalleFactura;
    private JTable table;
    private DefaultTableModel tableModel;
    private Object[] rows;
    private String codigo = "";
    private JComboBox<String> comboBoxFiltro;

    public static void main(String[] args) {
        try {
            ListadoFacturas dialog = new ListadoFacturas();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ListadoFacturas() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(ListadoFacturas.class.getResource("/Imagenes/to-do-list.png")));
        setTitle("Lista de Facturas");
        setBounds(100, 100, 1050, 505);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());

        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setBackground(new Color(240, 255, 240));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        JPanel panelFiltro = new JPanel();
        panelFiltro.setBackground(new Color(240, 255, 240));
        panelFiltro.setBorder(new EmptyBorder(5, 10, 5, 5));
        panelFiltro.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel lblFiltro = new JLabel("Filtrar Facturas:");
        lblFiltro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        panelFiltro.add(lblFiltro);

        comboBoxFiltro = new JComboBox<String>();
        comboBoxFiltro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        comboBoxFiltro.addItem("Todas");
        comboBoxFiltro.addItem("Facturas de Venta");
        comboBoxFiltro.addItem("Facturas de Compra");
        comboBoxFiltro.setPrototypeDisplayValue("Facturas de Compra");

        comboBoxFiltro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadFacturas();
            }
        });

        panelFiltro.add(comboBoxFiltro);
        contentPanel.add(panelFiltro, BorderLayout.NORTH);

        String[] columnas = {"ID", "Tipo", "Fecha", "Cantidad", "Monto Total", "Cliente / Proveedor"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    codigo = table.getValueAt(index, 0).toString();
                    btnVerDetalleFactura.setEnabled(true);
                }
            }
        });

        table.setBackground(new Color(240, 255, 240));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Bahnschrift", Font.PLAIN, 14));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        btnVerDetalleFactura = new JButton("Ver detalle de la factura");
        btnVerDetalleFactura.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!codigo.equals("")) {
                    Factura factura = Tienda.getInstance().buscarFacturaId(codigo);

                    if (factura != null) {
                        String detalle = "Factura: " + factura.getId()
                                + "\nFecha: " + factura.getFechaFactura()
                                + "\nCantidad: " + factura.getCantidadxProducto()
                                + "\nMonto Total: " + String.format("%.2f", factura.getmontoTotal());

                        if (factura instanceof FacturaCompra) {
                            FacturaCompra fc = (FacturaCompra) factura;
                            Proveedor proveedor = fc.getProveedor();
                            if (proveedor != null) {
                                detalle += "\nProveedor: " + proveedor.getEmpresa() + " (" + proveedor.getId() + ")";
                            } else {
                                detalle += "\nProveedor: Sin proveedor";
                            }
                        } else if (factura instanceof FacturaVenta) {
                            FacturaVenta fv = (FacturaVenta) factura;
                            Cliente cliente = fv.getCliente();
                            if (cliente != null) {
                                detalle += "\nCliente: " + cliente.getNombre() + " (" + cliente.getId() + ")";
                            } else {
                                detalle += "\nCliente: Sin cliente";
                            }
                        }

                        JOptionPane.showMessageDialog(
                                null,
                                detalle,
                                "Detalle de la Factura",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });

        btnVerDetalleFactura.setForeground(Color.WHITE);
        btnVerDetalleFactura.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnVerDetalleFactura.setEnabled(false);
        btnVerDetalleFactura.setBackground(new Color(80, 180, 152));
        buttonPane.add(btnVerDetalleFactura);

        JButton btnSalir = new JButton("Salir");
        btnSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnSalir.setBackground(new Color(250, 128, 114));
        buttonPane.add(btnSalir);

        loadFacturas();
    }

    public void loadFacturas() {
        tableModel.setRowCount(0);
        rows = new Object[tableModel.getColumnCount()];
        String filtroSeleccionado = (String) comboBoxFiltro.getSelectedItem();

        for (Factura factura : Tienda.getInstance().getListaFacturas()) {

            if (filtroSeleccionado.equals("Todas")
                    || (filtroSeleccionado.equals("Facturas de Venta") && factura instanceof FacturaVenta)
                    || (filtroSeleccionado.equals("Facturas de Compra") && factura instanceof FacturaCompra)) {

                rows[0] = factura.getId();

                if (factura instanceof FacturaVenta) {
                    rows[1] = "Venta";
                    FacturaVenta facturaVenta = (FacturaVenta) factura;
                    if (facturaVenta.getCliente() != null) {
                        rows[5] = facturaVenta.getCliente().getNombre();
                    } else {
                        rows[5] = "Sin cliente";
                    }

                } else if (factura instanceof FacturaCompra) {
                    rows[1] = "Compra";
                    FacturaCompra facturaCompra = (FacturaCompra) factura;
                    if (facturaCompra.getProveedor() != null) {
                        rows[5] = facturaCompra.getProveedor().getEmpresa();
                    } else {
                        rows[5] = "Sin proveedor";
                    }
                }

                rows[2] = factura.getFechaFactura();
                rows[3] = factura.getCantidadxProducto();
                rows[4] = String.format("%.2f", factura.getmontoTotal());

                tableModel.addRow(rows);
            }
        }
    }
}