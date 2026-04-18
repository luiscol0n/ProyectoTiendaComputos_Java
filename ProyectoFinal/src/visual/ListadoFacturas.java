package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import logico.Factura;
import logico.FacturaCompra;
import logico.FacturaVenta;
import logico.Persona;
import logico.Tienda;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListadoFacturas extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JButton btnVerPersona;
    private JTable table;
    private DefaultTableModel tableModel;
    private Object[] rows;
    private String codigo = "";
    private String codigoPersona = "";
    private Persona persona = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ListadoFacturas dialog = new ListadoFacturas();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ListadoFacturas() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(ListadoFacturas.class.getResource("/Imagenes/to-do-list.png")));
        setTitle("Lista de Facturas");
        setBounds(100, 100, 919, 505);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setBackground(new Color(240, 255, 240));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        
        String[] columnas = {"ID", "Tipo", "Fecha", "Cantidad", "Monto Total"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                int index = table.getSelectedRow();
                if (index >= 0) {
                    codigo = table.getValueAt(index, 0).toString();
                    //codigoPersona = table.getValueAt(index, 2).toString();
                    btnVerPersona.setEnabled(true);
                }

                Factura factura = Tienda.getInstance().buscarFacturaId(codigo);

                if (factura instanceof FacturaCompra) {
                    btnVerPersona.setText("Ver Proveedor");
                } else if (factura instanceof FacturaVenta) {
                    btnVerPersona.setText("Ver Cliente");
                }

                persona = Tienda.getInstance().buscarPersonaId(codigoPersona);
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
        
        btnVerPersona = new JButton("Ver Persona");
        btnVerPersona.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (persona != null) {
                    InformacionPersona verInfo = new InformacionPersona(persona);
                    verInfo.setVisible(true);
                }
            }
        });
        btnVerPersona.setForeground(Color.WHITE);
        btnVerPersona.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnVerPersona.setEnabled(false);
        btnVerPersona.setBackground(new Color(80, 180, 152));
        btnVerPersona.setActionCommand("OK");
        buttonPane.add(btnVerPersona);

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
        for (Factura factura : Tienda.getInstance().getListaFacturas()) {
            rows[0] = factura.getId();

            if (factura instanceof FacturaVenta) {
                rows[1] = "Venta";
               // rows[2] = ((FacturaVenta) factura).getCliente().getId();
            } else if (factura instanceof FacturaCompra) {
                rows[1] = "Compra";
                //rows[2] = ((FacturaCompra) factura).getProveedor().getId();
                
            }

            rows[2] = factura.getFechaFactura();
            rows[3] = factura.getCantidadxProducto();
            rows[4] = String.format("%.2f", factura.getmontoTotal());

            tableModel.addRow(rows);
        }
    }
}
