package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logico.Cliente;
import logico.DiscoDuro;
import logico.Factura;
import logico.FacturaCompra;
import logico.FacturaVenta;
import logico.Microprocesador;
import logico.MotherBoard;
import logico.Producto;
import logico.Proveedor;
import logico.Tienda;

public class RegistrarFactura extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtID;
    private JTextField txtFecha;
    private DefaultTableModel modeloPro = new DefaultTableModel();
    private DefaultTableModel modeloProCarri = new DefaultTableModel();
    private DefaultTableModel modeloCom = new DefaultTableModel();
    private DefaultTableModel modeloComCarri = new DefaultTableModel();
    private Object[] dispProRows;
    private Object[] caProRows;
    private Object[] dispComRows;
    private Object[] caComRows;
    private JTable tableProDisponible;
    private JTable tableProCarrito;
    private JTable tableComDisponible;
    private JTable tableComCarrito;
    private int indexProCarrito = -1;
    private int indexProDisponible = -1;
    private int indexComCarrito = -1;
    private int indexComDisponible = -1;
    private ArrayList<Producto> productosComprados = new ArrayList<Producto>();
    private double precioTotal = 0;
    private JTextField txtTotal;
    private JTextField txtDescuento;
    private JButton btnAgregarPro;
    private JButton btnQuitarPro;
    private JButton btnProducto;
    private JPanel pnlCompra;
    private JTextField txtIdCliente;
    private JTextField txtEmpleado;
    private JPanel pnlVenta;
    private JTextField txtHora;
    private JPanel pnlProDisponible;
    private JPanel pnlProCarrito;
    private JPanel pnlComDisponible;
    private JPanel pnlComCarrito;
    private JButton btnBuscarCliente;
    private JButton btnBuscarProoveedor;
    private JTextField txtProveedor;

    // guardo el producto actualmente seleccionado en cualquier tabla para el botn "Ver Producto"
    private Producto productoSeleccionadoVista = null;

    public static void main(String[] args) {
        try {
            RegistrarFactura dialog = new RegistrarFactura(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RegistrarFactura(boolean esCV) {
        setIconImage(Toolkit.getDefaultToolkit().getImage(RegistrarFactura.class.getResource("/Imagenes/invoice.png")));

        Color cyanOscuro = new Color(70, 133, 133);
        Color cyanMid = new Color(80, 180, 152);
        Color cyanClaro = new Color(222, 249, 196);
        Color fondoClarito = new Color(240, 255, 240);
        MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, cyanOscuro);

        setTitle("Registrar Factura");
        setBounds(100, 100, 750, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        setLocationRelativeTo(null);
        contentPanel.setBackground(fondoClarito);

        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 714, 404);
        contentPanel.add(panel);
        panel.setLayout(null);
        panel.setBackground(fondoClarito);

        JLabel lblID = new JLabel("ID:");
        lblID.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblID.setBounds(10, 13, 71, 14);
        panel.add(lblID);

        txtID = new JTextField();
        txtID.setText("Factura - " + Tienda.getInstance().numFactura);
        txtID.setEditable(false);
        txtID.setBounds(60, 10, 133, 20);
        txtID.setBorder(bottomBorder);
        txtID.setBackground(cyanClaro);
        panel.add(txtID);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblFecha.setBounds(203, 13, 65, 14);
        panel.add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        txtFecha.setBounds(278, 10, 138, 20);
        LocalDate hoy = LocalDate.now();
        txtFecha.setText(hoy.toString());
        txtFecha.setBorder(bottomBorder);
        txtFecha.setBackground(cyanClaro);
        panel.add(txtFecha);

        pnlProDisponible = new JPanel();
        pnlProDisponible.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnlProDisponible.setBounds(10, 165, 300, 200);
        panel.add(pnlProDisponible);
        pnlProDisponible.setLayout(new BorderLayout());

        String[] proHeaders = {"ID", "Num Serie", "Tipo", "Precio"};
        modeloPro.setColumnIdentifiers(proHeaders);
        tableProDisponible = new JTable(modeloPro);
        tableProDisponible.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableProDisponible.setBackground(cyanClaro);
        tableProDisponible.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                indexProDisponible = tableProDisponible.getSelectedRow();
                btnAgregarPro.setEnabled(indexProDisponible >= 0);

                // habilito boton "Ver Producto" y guardo referencia al producto seleccionado
                if (indexProDisponible >= 0 && indexProDisponible < Tienda.getInstance().getProductoNoSeleccionados().size()) {
                    productoSeleccionadoVista = Tienda.getInstance().getProductoNoSeleccionados().get(indexProDisponible);
                    btnProducto.setEnabled(true);
                } else {
                    productoSeleccionadoVista = null;
                    btnProducto.setEnabled(false);
                }
            }
        });

        JScrollPane scrollProDisponible = new JScrollPane(tableProDisponible);
        pnlProDisponible.add(scrollProDisponible, BorderLayout.CENTER);
        scrollProDisponible.getViewport().setBackground(fondoClarito);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(cyanMid);
                setForeground(Color.WHITE);
                setFont(new Font("Bahnschrift", Font.BOLD, 12));
                return this;
            }
        };
        for (int i = 0; i < tableProDisponible.getColumnModel().getColumnCount(); i++) {
            tableProDisponible.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        pnlProCarrito = new JPanel();
        pnlProCarrito.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnlProCarrito.setBounds(400, 165, 300, 200);
        panel.add(pnlProCarrito);
        pnlProCarrito.setLayout(new BorderLayout());

        modeloProCarri.setColumnIdentifiers(proHeaders);
        tableProCarrito = new JTable(modeloProCarri);
        tableProCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableProCarrito.setBackground(cyanClaro);
        tableProCarrito.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                indexProCarrito = tableProCarrito.getSelectedRow();
                btnQuitarPro.setEnabled(indexProCarrito >= 0);

                // habilito boton "Ver Producto" y guardo referencia al carrito
                if (indexProCarrito >= 0 && indexProCarrito < Tienda.getInstance().getProductosSeleccionados().size()) {
                    productoSeleccionadoVista = Tienda.getInstance().getProductosSeleccionados().get(indexProCarrito);
                    btnProducto.setEnabled(true);
                } else {
                    productoSeleccionadoVista = null;
                    btnProducto.setEnabled(false);
                }
            }
        });

        JScrollPane scrollProCarrito = new JScrollPane(tableProCarrito);
        pnlProCarrito.add(scrollProCarrito, BorderLayout.CENTER);
        scrollProCarrito.getViewport().setBackground(fondoClarito);

        DefaultTableCellRenderer headerRendererCarrito = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(cyanMid);
                setForeground(Color.WHITE);
                setFont(new Font("Bahnschrift", Font.BOLD, 12));
                return this;
            }
        };
        for (int i = 0; i < tableProCarrito.getColumnModel().getColumnCount(); i++) {
            tableProCarrito.getColumnModel().getColumn(i).setHeaderRenderer(headerRendererCarrito);
        }

        pnlComDisponible = new JPanel();
        pnlComDisponible.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnlComDisponible.setBounds(10, 165, 300, 200);
        panel.add(pnlComDisponible);
        pnlComDisponible.setLayout(new BorderLayout());

        String[] comHeaders = {"ID", "Num Serie", "Tipo", "Precio"};
        modeloCom.setColumnIdentifiers(comHeaders);
        tableComDisponible = new JTable(modeloCom);
        tableComDisponible.setBackground(cyanClaro);
        tableComDisponible.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                indexComDisponible = tableComDisponible.getSelectedRow();
                btnAgregarPro.setEnabled(indexComDisponible >= 0);

                // habilitar boton "Ver Producto"
                if (indexComDisponible >= 0 && indexComDisponible < Tienda.getInstance().getProductoNoSeleccionados().size()) {
                    productoSeleccionadoVista = Tienda.getInstance().getProductoNoSeleccionados().get(indexComDisponible);
                    btnProducto.setEnabled(true);
                } else {
                    productoSeleccionadoVista = null;
                    btnProducto.setEnabled(false);
                }
            }
        });

        JScrollPane scrollComDisponible = new JScrollPane(tableComDisponible);
        pnlComDisponible.add(scrollComDisponible, BorderLayout.CENTER);
        scrollComDisponible.getViewport().setBackground(fondoClarito);

        DefaultTableCellRenderer headerRendererComDisponible = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(cyanMid);
                setForeground(Color.WHITE);
                setFont(new Font("Bahnschrift", Font.BOLD, 12));
                return this;
            }
        };
        for (int i = 0; i < tableComDisponible.getColumnModel().getColumnCount(); i++) {
            tableComDisponible.getColumnModel().getColumn(i).setHeaderRenderer(headerRendererComDisponible);
        }

        pnlComCarrito = new JPanel();
        pnlComCarrito.setBorder(new EmptyBorder(5, 5, 5, 5));
        pnlComCarrito.setBounds(400, 165, 300, 200);
        panel.add(pnlComCarrito);
        pnlComCarrito.setLayout(new BorderLayout());

        modeloComCarri.setColumnIdentifiers(comHeaders);
        tableComCarrito = new JTable(modeloComCarri);
        tableComCarrito.setBackground(cyanClaro);
        tableComCarrito.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                indexComCarrito = tableComCarrito.getSelectedRow();
                btnQuitarPro.setEnabled(indexComCarrito >= 0);

                // habilitar boton "Ver Producto"
                if (indexComCarrito >= 0 && indexComCarrito < Tienda.getInstance().getProductosSeleccionados().size()) {
                    productoSeleccionadoVista = Tienda.getInstance().getProductosSeleccionados().get(indexComCarrito);
                    btnProducto.setEnabled(true);
                } else {
                    productoSeleccionadoVista = null;
                    btnProducto.setEnabled(false);
                }
            }
        });

        JScrollPane scrollComCarrito = new JScrollPane(tableComCarrito);
        pnlComCarrito.add(scrollComCarrito, BorderLayout.CENTER);
        scrollComCarrito.getViewport().setBackground(fondoClarito);

        DefaultTableCellRenderer headerRendererComCarrito = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(cyanMid);
                setForeground(Color.WHITE);
                setFont(new Font("Bahnschrift", Font.BOLD, 12));
                return this;
            }
        };
        for (int i = 0; i < tableComCarrito.getColumnModel().getColumnCount(); i++) {
            tableComCarrito.getColumnModel().getColumn(i).setHeaderRenderer(headerRendererComCarrito);
        }

        btnAgregarPro = new JButton("Agregar");
        btnAgregarPro.setForeground(Color.WHITE);
        btnAgregarPro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnAgregarPro.setBackground(cyanMid);
        btnAgregarPro.setEnabled(false);
        btnAgregarPro.setBounds(307, 220, 97, 25);
        btnAgregarPro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (esCV) {
                    if (indexProDisponible >= 0) {
                        Producto producto = Tienda.getInstance().getProductoNoSeleccionados().get(indexProDisponible);
                        producto.setSeleccionado(true);
                        productoSeleccionadoVista = null;
                        btnProducto.setEnabled(false);
                        cargaProCarritoDisponible();
                        cargaProductoDisponible();
                    }
                } else {
                    if (indexComDisponible >= 0) {
                        Producto producto = Tienda.getInstance().getProductoNoSeleccionados().get(indexComDisponible);
                        producto.setSeleccionado(true);
                        productoSeleccionadoVista = null;
                        btnProducto.setEnabled(false);
                        cargaProCarritoDisponible();
                        cargaProductoDisponible();
                    }
                }
            }
        });
        panel.add(btnAgregarPro);

        btnQuitarPro = new JButton("Quitar");
        btnQuitarPro.setBounds(307, 260, 97, 25);
        btnQuitarPro.setEnabled(false);
        btnQuitarPro.setBackground(new Color(250, 128, 114));
        btnQuitarPro.setForeground(Color.WHITE);
        btnQuitarPro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnQuitarPro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (esCV) {
                    if (indexProCarrito >= 0) {
                        Producto producto = Tienda.getInstance().getProductosSeleccionados().get(indexProCarrito);
                        producto.setSeleccionado(false);
                        productoSeleccionadoVista = null;
                        btnProducto.setEnabled(false);
                        cargaProCarritoDisponible();
                        cargaProductoDisponible();
                    }
                } else {
                    if (indexComCarrito >= 0) {
                        Producto producto = Tienda.getInstance().getProductosSeleccionados().get(indexComCarrito);
                        producto.setSeleccionado(false);
                        productoSeleccionadoVista = null;
                        btnProducto.setEnabled(false);
                        cargaProCarritoDisponible();
                        cargaProductoDisponible();
                    }
                }
            }
        });
        panel.add(btnQuitarPro);

        pnlCompra = new JPanel();
        pnlCompra.setBounds(10, 53, 656, 52);
        panel.add(pnlCompra);
        pnlCompra.setLayout(null);
        pnlCompra.setBackground(fondoClarito);

        JLabel lblProveedor = new JLabel("Proveedor:");
        lblProveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblProveedor.setBounds(10, 11, 89, 14);
        pnlCompra.add(lblProveedor);

        txtProveedor = new JTextField();
        txtProveedor.setBounds(93, 10, 164, 20);
        txtProveedor.setText("Proveedor - ");
        txtProveedor.setBackground(cyanClaro);
        txtProveedor.setBorder(bottomBorder);
        pnlCompra.add(txtProveedor);

        btnBuscarProoveedor = new JButton("Buscar");
        btnBuscarProoveedor.setBackground(cyanMid);
        btnBuscarProoveedor.setForeground(Color.WHITE);
        btnBuscarProoveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnBuscarProoveedor.setBounds(267, 9, 89, 23);
        btnBuscarProoveedor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Proveedor prove = (Proveedor) Tienda.getInstance().buscarPersonaId(txtProveedor.getText().trim());
                if (prove == null) {
                    btnBuscarProoveedor.setEnabled(false);
                    RegistrarProveedor reg = new RegistrarProveedor(null);
                    reg.setModal(true);
                    reg.setVisible(true);
                    txtProveedor.setText("Proveedor - " + (Tienda.getInstance().numProveedor - 1));
                } else {
                    txtProveedor.setText(prove.getId());
                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Búsqueda exitosa.");
                    mensajito.setModal(true);
                    mensajito.setVisible(true);
                    btnBuscarProoveedor.setEnabled(false);
                }
            }
        });
        pnlCompra.add(btnBuscarProoveedor);

        btnProducto = new JButton(" Ver Producto");
        btnProducto.setEnabled(false);
        btnProducto.setForeground(Color.WHITE);
        btnProducto.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnProducto.setBackground(cyanMid);
        btnProducto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (productoSeleccionadoVista != null) {
                    RegistrarProducto ventanaProducto = new RegistrarProducto(productoSeleccionadoVista, true);
                    ventanaProducto.setModal(true);
                    ventanaProducto.setVisible(true);
                }
            }
        });
        btnProducto.setBounds(10, 131, 108, 23);
        panel.add(btnProducto);

        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setBounds(310, 134, 86, 20);
        txtTotal.setBackground(cyanClaro);
        txtTotal.setBorder(bottomBorder);
        panel.add(txtTotal);

        JLabel lblTotal = new JLabel("Total:");
        lblTotal.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblTotal.setBounds(230, 135, 65, 14);
        panel.add(lblTotal);

        txtDescuento = new JTextField();
        txtDescuento.setEditable(false);
        txtDescuento.setText("0");
        txtDescuento.setBounds(312, 190, 86, 20);
        txtDescuento.setBackground(cyanClaro);
        txtDescuento.setBorder(bottomBorder);
        panel.add(txtDescuento);

        JLabel lblDescuento = new JLabel("Descuento:");
        lblDescuento.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblDescuento.setBounds(312, 165, 83, 14);
        panel.add(lblDescuento);

        pnlVenta = new JPanel();
        pnlVenta.setBounds(10, 53, 656, 52);
        panel.add(pnlVenta);
        pnlVenta.setLayout(null);
        pnlVenta.setBackground(fondoClarito);

        JLabel lblCliente = new JLabel("ID cliente:");
        lblCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblCliente.setBounds(10, 11, 106, 14);
        pnlVenta.add(lblCliente);

        txtIdCliente = new JTextField();
        txtIdCliente.setBounds(89, 10, 86, 20);
        txtIdCliente.setText("Cliente - ");
        txtIdCliente.setBackground(cyanClaro);
        txtIdCliente.setBorder(bottomBorder);
        pnlVenta.add(txtIdCliente);

        btnBuscarCliente = new JButton("Buscar");
        btnBuscarCliente.setForeground(Color.WHITE);
        btnBuscarCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnBuscarCliente.setBackground(cyanMid);
        btnBuscarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cliente client = (Cliente) Tienda.getInstance().buscarPersonaId(txtIdCliente.getText().trim());
                if (client == null) {
                    btnBuscarCliente.setEnabled(false);
                    RegistrarCliente reg = new RegistrarCliente(null);
                    reg.setModal(true);
                    reg.setVisible(true);
                    txtIdCliente.setText("Cliente - " + (Tienda.getInstance().numCliente - 1));
                } else {
                    txtIdCliente.setText(client.getId());
                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Búsqueda exitosa.");
                    mensajito.setModal(true);
                    mensajito.setVisible(true);
                    btnBuscarCliente.setEnabled(false);
                }
            }
        });
        btnBuscarCliente.setBounds(185, 9, 89, 23);
        pnlVenta.add(btnBuscarCliente);

        JLabel lblEmpleado = new JLabel("Empleado:");
        lblEmpleado.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblEmpleado.setBounds(365, 11, 73, 14);
        pnlVenta.add(lblEmpleado);

        txtEmpleado = new JTextField();
        txtEmpleado.setText(Tienda.getInstance().getLoginUser().getUserName());
        txtEmpleado.setEditable(false);
        txtEmpleado.setBounds(450, 10, 134, 20);
        txtEmpleado.setBackground(cyanClaro);
        txtEmpleado.setBorder(bottomBorder);
        pnlVenta.add(txtEmpleado);

        JLabel lblHora = new JLabel("Hora:");
        lblHora.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblHora.setBounds(446, 13, 46, 14);
        panel.add(lblHora);

        txtHora = new JTextField();
        txtHora.setEditable(false);
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        txtHora.setText(ahora.format(formatter));
        txtHora.setBounds(502, 12, 153, 20);
        txtHora.setBackground(cyanClaro);
        txtHora.setBorder(bottomBorder);
        panel.add(txtHora);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrarFactura = new JButton("Registrar Factura");
        btnRegistrarFactura.setForeground(Color.WHITE);
        btnRegistrarFactura.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnRegistrarFactura.setBackground(cyanMid);
        btnRegistrarFactura.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                productosComprados.clear();

                ArrayList<Producto> productos = Tienda.getInstance().getProductosSeleccionados();
                for (Producto prod : productos) {
                    productosComprados.add(prod);
                }

                LocalDate hoy = LocalDate.now();

                if (productosComprados.isEmpty()) {
                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Debe seleccionar al menos un producto.");
                    mensajito.setModal(true);
                    mensajito.setVisible(true);
                    return;
                }

                if (esCV) { // FACTURA DE COMPRA

                    Proveedor proveedor = null;
                    if (!txtProveedor.getText().trim().isEmpty()) {
                        proveedor = (Proveedor) Tienda.getInstance().buscarPersonaId(txtProveedor.getText().trim());
                    }
                    if (proveedor == null) {
                        ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
                        MensajeAlerta mensajito = new MensajeAlerta(iconito, "Debe seleccionar un proveedor válido.");
                        mensajito.setModal(true);
                        mensajito.setVisible(true);
                        return;
                    }

                    int cant = 0;
                    for (Producto pro : productos) {
                        pro.setCantDisponible(pro.getSiemprePedir() + pro.getCantDisponible());
                        cant += pro.getCantDisponible();
                        pro.setSeleccionado(false);
                    }

                    Factura compra = new FacturaCompra(txtID.getText(), hoy, productosComprados, proveedor, cant, precioTotal);
                    Tienda.getInstance().registrarFactura(compra);

                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Factura de compra registrada correctamente.");
                    mensajito.setModal(true);
                    mensajito.setVisible(true);

                    Tienda.getInstance().recargaSelecionado();
                    clean();
                    cargaProCarritoDisponible();
                    cargaProductoDisponible();

                } else { // FACTURA DE VENTA

                    if (txtID.getText().trim().isEmpty() || txtIdCliente.getText().trim().isEmpty()) {
                        ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
                        MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de estar llenos.");
                        mensajito.setModal(true);
                        mensajito.setVisible(true);
                        return;
                    }

                    Cliente cliente = null;
                    if (!txtIdCliente.getText().trim().isEmpty()) {
                        cliente = (Cliente) Tienda.getInstance().buscarPersonaId(txtIdCliente.getText().trim());
                    }
                    if (cliente == null) {
                        ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
                        MensajeAlerta mensajito = new MensajeAlerta(iconito, "Debe seleccionar un cliente válido.");
                        mensajito.setModal(true);
                        mensajito.setVisible(true);
                        return;
                    }

                    int cant = 0;
                    for (Producto pro : productos) {
                        if (pro.getCantDisponible() <= 0) {
                            ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
                            MensajeAlerta mensajito = new MensajeAlerta(iconito, "Uno de los productos no tiene disponibilidad.");
                            mensajito.setModal(true);
                            mensajito.setVisible(true);
                            return;
                        }
                        pro.setCantDisponible(pro.getCantDisponible() - 1);
                        cant++;
                        pro.setSeleccionado(false);
                    }

                    Factura venta = new FacturaVenta(txtID.getText(), hoy, productosComprados, cliente, cant, precioTotal);
                    Tienda.getInstance().registrarFactura(venta);

                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Factura de venta registrada correctamente.");
                    mensajito.setModal(true);
                    mensajito.setVisible(true);

                    Tienda.getInstance().recargaSelecionado();
                    clean();
                    cargaProCarritoDisponible();
                    cargaProductoDisponible();
                }
            }
        });
        buttonPane.add(btnRegistrarFactura);
        getRootPane().setDefaultButton(btnRegistrarFactura);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(250, 128, 114));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPane.add(btnCancelar);

        if (esCV) {
            pnlVenta.setVisible(false);
            pnlCompra.setVisible(true);
            pnlComCarrito.setVisible(false);
            pnlComDisponible.setVisible(false);
            pnlProCarrito.setVisible(true);
            pnlProDisponible.setVisible(true);

            for (Producto pro : Tienda.getInstance().getListaProductos()) {
                pro.setEstado(true);
            }
        } else {
            pnlCompra.setVisible(false);
            pnlVenta.setVisible(true);
            pnlProCarrito.setVisible(false);
            pnlProDisponible.setVisible(false);
            pnlComCarrito.setVisible(true);
            pnlComDisponible.setVisible(true);
        }

        cargaProCarritoDisponible();
        cargaProductoDisponible();

        pnlComCarrito.setBackground(fondoClarito);
        pnlComDisponible.setBackground(fondoClarito);
        pnlProCarrito.setBackground(fondoClarito);
        pnlProDisponible.setBackground(fondoClarito);
    }

    public void cargaProductoDisponible() {
        modeloPro.setRowCount(0);
        modeloCom.setRowCount(0);

        dispProRows = new Object[tableProDisponible.getColumnCount()];
        dispComRows = new Object[tableComDisponible.getColumnCount()];

        for (Producto pro : Tienda.getInstance().getProductoNoSeleccionados()) {
            if (pro.isEstado()) {
                Object[] fila = new Object[4];
                fila[0] = pro.getId();
                fila[1] = pro.getNumSerie();
                if (pro instanceof MotherBoard) {
                    fila[2] = "MotherBoard";
                } else if (pro instanceof Microprocesador) {
                    fila[2] = "Microprocesador";
                } else if (pro instanceof DiscoDuro) {
                    fila[2] = "Disco Duro";
                } else {
                    fila[2] = "Memoria RAM";
                }
                fila[3] = pro.getPrecio();
                modeloPro.addRow(fila);
                modeloCom.addRow(fila);
            }
        }
    }

    public void cargaProCarritoDisponible() {
        precioTotal = 0;
        modeloProCarri.setRowCount(0);
        modeloComCarri.setRowCount(0);

        for (Producto pro : Tienda.getInstance().getProductosSeleccionados()) {
            if (pro.isEstado()) {
                precioTotal += pro.getPrecio();
                Object[] fila = new Object[4];
                fila[0] = pro.getId();
                fila[1] = pro.getNumSerie();
                if (pro instanceof MotherBoard) {
                    fila[2] = "MotherBoard";
                } else if (pro instanceof Microprocesador) {
                    fila[2] = "Microprocesador";
                } else if (pro instanceof DiscoDuro) {
                    fila[2] = "Disco Duro";
                } else {
                    fila[2] = "Memoria RAM";
                }
                fila[3] = pro.getPrecio();
                modeloProCarri.addRow(fila);
                modeloComCarri.addRow(fila);
            }
        }
        txtTotal.setText(String.format("%.2f", precioTotal));
    }

    public void clean() {
        txtID.setText("Factura - " + Tienda.getInstance().numFactura);
        LocalDate hoy = LocalDate.now();
        txtFecha.setText(hoy.toString());
        txtTotal.setText("0.0");
        txtDescuento.setText("0.0");
        txtIdCliente.setText("Cliente - ");
        txtProveedor.setText("Proveedor - ");
        btnBuscarCliente.setEnabled(true);
        btnBuscarProoveedor.setEnabled(true);
        txtEmpleado.setText(Tienda.getInstance().getLoginUser().getUserName());
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        txtHora.setText(ahora.format(formatter));
        modeloPro.setRowCount(0);
        modeloProCarri.setRowCount(0);
        modeloCom.setRowCount(0);
        modeloComCarri.setRowCount(0);
        dispProRows = null;
        caProRows = null;
        dispComRows = null;
        caComRows = null;
        indexProCarrito = -1;
        indexProDisponible = -1;
        indexComCarrito = -1;
        indexComDisponible = -1;
        productosComprados.clear();
        btnAgregarPro.setEnabled(false);
        btnQuitarPro.setEnabled(false);
        btnProducto.setEnabled(false);
        productoSeleccionadoVista = null;
        Tienda.getInstance().recargaSelecionado();
    }
}