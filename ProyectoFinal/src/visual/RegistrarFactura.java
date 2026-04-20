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
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logico.Cliente;
import logico.DiscoDuro;
import logico.Factura;
import logico.FacturaCompra;
import logico.FacturaVenta;
import logico.DetalleFacturaCompra;
import logico.DetalleFacturaVenta;
import logico.MemoriaRam;
import logico.Microprocesador;
import logico.MotherBoard;
import logico.Producto;
import logico.Proveedor;
import logico.Tienda;

public class RegistrarFactura extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtID;
    private JTextField txtFecha;

    private DefaultTableModel modeloComDisp  = new DefaultTableModel();
    private DefaultTableModel modeloComCarri = new DefaultTableModel();

    private DefaultTableModel modeloProDisp  = new DefaultTableModel();
    private DefaultTableModel modeloProCarri = new DefaultTableModel();

    private JTable tableProDisponible;
    private JTable tableProCarrito;
    private JTable tableComDisponible;
    private JTable tableComCarrito;

    private int indexProCarrito    = -1;
    private int indexProDisponible = -1;
    private int indexComCarrito    = -1;
    private int indexComDisponible = -1;

    private Map<String, Integer> cantidadesCarrito = new HashMap<String, Integer>();

    private double precioTotal = 0;
    private JTextField txtTotal;
    private JTextField txtSubtotal;

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
    private JButton btnBuscarProveedor;
    private JTextField txtProveedor;

    private JSpinner spnCantidad;

    private Producto productoSeleccionadoVista     = null;
    private Producto productoEnCarritoSeleccionado = null;

    private boolean esCV;
    private boolean proveedorConfirmado = false;

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
        this.esCV = esCV;

        setIconImage(Toolkit.getDefaultToolkit().getImage(
                RegistrarFactura.class.getResource("/Imagenes/invoice.png")));

        Color cyanOscuro   = new Color(70, 133, 133);
        Color cyanMid      = new Color(80, 180, 152);
        Color cyanClaro    = new Color(222, 249, 196);
        Color fondoClarito = new Color(240, 255, 240);
        MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, cyanOscuro);

        setTitle(esCV ? "Registrar Factura de Compra" : "Registrar Factura de Venta");
        setBounds(100, 100, 780, 560);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        setLocationRelativeTo(null);
        contentPanel.setBackground(fondoClarito);
        setModal(true);

        JPanel panel = new JPanel();
        panel.setBounds(10, 11, 744, 460);
        contentPanel.add(panel);
        panel.setLayout(null);
        panel.setBackground(fondoClarito);

        JLabel lblID = new JLabel("ID:");
        lblID.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblID.setBounds(10, 13, 40, 14);
        panel.add(lblID);

        txtID = new JTextField();
        txtID.setText("Factura - " + Tienda.getInstance().numFactura);
        txtID.setEditable(false);
        txtID.setBounds(50, 10, 133, 20);
        txtID.setBorder(bottomBorder);
        txtID.setBackground(cyanClaro);
        panel.add(txtID);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblFecha.setBounds(200, 13, 55, 14);
        panel.add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        txtFecha.setBounds(258, 10, 120, 20);
        txtFecha.setText(LocalDate.now().toString());
        txtFecha.setBorder(bottomBorder);
        txtFecha.setBackground(cyanClaro);
        panel.add(txtFecha);

        JLabel lblHora = new JLabel("Hora:");
        lblHora.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblHora.setBounds(392, 13, 46, 14);
        panel.add(lblHora);

        txtHora = new JTextField();
        txtHora.setEditable(false);
        txtHora.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        txtHora.setBounds(442, 10, 100, 20);
        txtHora.setBackground(cyanClaro);
        txtHora.setBorder(bottomBorder);
        panel.add(txtHora);

        pnlCompra = new JPanel();
        pnlCompra.setBounds(10, 40, 744, 35);
        panel.add(pnlCompra);
        pnlCompra.setLayout(null);
        pnlCompra.setBackground(fondoClarito);

        JLabel lblProveedor = new JLabel("Proveedor:");
        lblProveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblProveedor.setBounds(0, 9, 85, 14);
        pnlCompra.add(lblProveedor);

        txtProveedor = new JTextField();
        txtProveedor.setBounds(88, 7, 160, 20);
        txtProveedor.setText("Proveedor - ");
        txtProveedor.setBackground(cyanClaro);
        txtProveedor.setBorder(bottomBorder);
        pnlCompra.add(txtProveedor);

        btnBuscarProveedor = new JButton("Buscar");
        btnBuscarProveedor.setBackground(cyanMid);
        btnBuscarProveedor.setForeground(Color.WHITE);
        btnBuscarProveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        btnBuscarProveedor.setBounds(258, 6, 80, 22);
        btnBuscarProveedor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Proveedor prove = (Proveedor) Tienda.getInstance()
                        .buscarPersonaId(txtProveedor.getText().trim());

                if (prove == null) {
                    RegistrarProveedor reg = new RegistrarProveedor(null);
                    reg.setModal(true);
                    reg.setVisible(true);

                    txtProveedor.setText("Proveedor - " + (Tienda.getInstance().numProveedor - 1));

                    prove = (Proveedor) Tienda.getInstance()
                            .buscarPersonaId(txtProveedor.getText().trim());
                }

                if (prove != null) {
                    txtProveedor.setText(prove.getId());
                    proveedorConfirmado = true;
                    btnBuscarProveedor.setEnabled(false);
                    habilitarSeleccionCompra();
                    mostrarAlerta("check", "Búsqueda exitosa.");
                } else {
                    proveedorConfirmado = false;
                    bloquearSeleccionCompra();
                    mostrarAlerta("cancel", "Debe seleccionar un proveedor válido.");
                }

                cargaProductoDisponible();
            }
        });
        pnlCompra.add(btnBuscarProveedor);

        txtProveedor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnBuscarProveedor.doClick();
            }
        });

        pnlVenta = new JPanel();
        pnlVenta.setBounds(10, 40, 744, 35);
        panel.add(pnlVenta);
        pnlVenta.setLayout(null);
        pnlVenta.setBackground(fondoClarito);

        JLabel lblCliente = new JLabel("ID Cliente:");
        lblCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblCliente.setBounds(0, 9, 80, 14);
        pnlVenta.add(lblCliente);

        txtIdCliente = new JTextField();
        txtIdCliente.setBounds(84, 7, 100, 20);
        txtIdCliente.setText("Cliente - ");
        txtIdCliente.setBackground(cyanClaro);
        txtIdCliente.setBorder(bottomBorder);
        pnlVenta.add(txtIdCliente);

        btnBuscarCliente = new JButton("Buscar");
        btnBuscarCliente.setForeground(Color.WHITE);
        btnBuscarCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        btnBuscarCliente.setBackground(cyanMid);
        btnBuscarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cliente client = (Cliente) Tienda.getInstance()
                        .buscarPersonaId(txtIdCliente.getText().trim());
                if (client == null) {
                    btnBuscarCliente.setEnabled(false);
                    RegistrarCliente reg = new RegistrarCliente(null);
                    reg.setModal(true);
                    reg.setVisible(true);
                    txtIdCliente.setText("Cliente - " + (Tienda.getInstance().numCliente - 1));
                } else {
                    txtIdCliente.setText(client.getId());
                    mostrarAlerta("check", "Búsqueda exitosa.");
                    btnBuscarCliente.setEnabled(false);
                }
            }
        });
        btnBuscarCliente.setBounds(194, 6, 80, 22);
        pnlVenta.add(btnBuscarCliente);

        txtIdCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnBuscarCliente.doClick();
            }
        });

        JLabel lblEmpleado = new JLabel("Empleado:");
        lblEmpleado.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblEmpleado.setBounds(340, 9, 75, 14);
        pnlVenta.add(lblEmpleado);

        txtEmpleado = new JTextField();
        txtEmpleado.setText(Tienda.getInstance().getLoginUser().getUserName());
        txtEmpleado.setEditable(false);
        txtEmpleado.setBounds(420, 7, 150, 20);
        txtEmpleado.setBackground(cyanClaro);
        txtEmpleado.setBorder(bottomBorder);
        pnlVenta.add(txtEmpleado);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        lblCantidad.setBounds(10, 85, 70, 16);
        panel.add(lblCantidad);

        spnCantidad = new JSpinner();
        spnCantidad.setModel(new SpinnerNumberModel(1, 1, null, 1));
        spnCantidad.setBounds(84, 83, 70, 22);
        spnCantidad.setBackground(cyanClaro);
        spnCantidad.setBorder(bottomBorder);
        panel.add(spnCantidad);

        btnProducto = new JButton("Ver Producto");
        btnProducto.setEnabled(false);
        btnProducto.setForeground(Color.WHITE);
        btnProducto.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        btnProducto.setBackground(cyanMid);
        btnProducto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (productoSeleccionadoVista != null) {
                    RegistrarProducto v = new RegistrarProducto(productoSeleccionadoVista, true);
                    v.setModal(true);
                    v.setVisible(true);
                }
            }
        });
        btnProducto.setBounds(170, 83, 120, 22);
        panel.add(btnProducto);

        JLabel lblDisponibles = new JLabel("Disponibles");
        lblDisponibles.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblDisponibles.setForeground(cyanOscuro);
        lblDisponibles.setBounds(118, 118, 88, 16);
        panel.add(lblDisponibles);

        JLabel lblCarrito = new JLabel("Carrito de Compra");
        lblCarrito.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblCarrito.setForeground(cyanOscuro);
        lblCarrito.setBounds(515, 118, 133, 16);
        panel.add(lblCarrito);

        String[] headersDisp = {"ID", "Serie", "Tipo", "Precio", "Stock"};

        pnlComDisponible = new JPanel(new BorderLayout());
        pnlComDisponible.setBounds(10, 143, 295, 200);
        pnlComDisponible.setBackground(fondoClarito);
        panel.add(pnlComDisponible);

        modeloComDisp.setColumnIdentifiers(headersDisp);
        tableComDisponible = new JTable(modeloComDisp) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableComDisponible.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableComDisponible.setBackground(cyanClaro);
        aplicarHeaderRenderer(tableComDisponible, cyanMid);
        tableComDisponible.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!proveedorConfirmado) {
                    bloquearSeleccionCompra();
                    return;
                }

                indexComDisponible = tableComDisponible.getSelectedRow();
                btnAgregarPro.setEnabled(indexComDisponible >= 0);

                if (indexComDisponible >= 0) {
                    ArrayList<Producto> lista = obtenerProductosDisponibles(true);
                    if (indexComDisponible < lista.size()) {
                        productoSeleccionadoVista = lista.get(indexComDisponible);
                        btnProducto.setEnabled(true);
                        actualizarTotales();
                    }
                }
            }
        });
        pnlComDisponible.add(new JScrollPane(tableComDisponible), BorderLayout.CENTER);

        pnlProDisponible = new JPanel(new BorderLayout());
        pnlProDisponible.setBounds(10, 143, 295, 200);
        pnlProDisponible.setBackground(fondoClarito);
        panel.add(pnlProDisponible);

        modeloProDisp.setColumnIdentifiers(headersDisp);
        tableProDisponible = new JTable(modeloProDisp) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableProDisponible.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableProDisponible.setBackground(cyanClaro);
        aplicarHeaderRenderer(tableProDisponible, cyanMid);
        tableProDisponible.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                indexProDisponible = tableProDisponible.getSelectedRow();
                btnAgregarPro.setEnabled(indexProDisponible >= 0);
                if (indexProDisponible >= 0) {
                    ArrayList<Producto> lista = obtenerProductosDisponibles(false);
                    if (indexProDisponible < lista.size()) {
                        productoSeleccionadoVista = lista.get(indexProDisponible);
                        btnProducto.setEnabled(true);
                    }
                }
            }
        });
        pnlProDisponible.add(new JScrollPane(tableProDisponible), BorderLayout.CENTER);

        btnAgregarPro = new JButton("Agregar ");
        btnAgregarPro.setForeground(Color.WHITE);
        btnAgregarPro.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        btnAgregarPro.setBackground(cyanMid);
        btnAgregarPro.setEnabled(false);
        btnAgregarPro.setBounds(313, 198, 110, 25);
        btnAgregarPro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                agregarAlCarrito();
            }
        });
        panel.add(btnAgregarPro);

        btnQuitarPro = new JButton("Quitar");
        btnQuitarPro.setBounds(313, 238, 110, 25);
        btnQuitarPro.setEnabled(false);
        btnQuitarPro.setBackground(new Color(250, 128, 114));
        btnQuitarPro.setForeground(Color.WHITE);
        btnQuitarPro.setFont(new Font("Bahnschrift", Font.PLAIN, 12));
        btnQuitarPro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quitarDelCarrito();
            }
        });
        panel.add(btnQuitarPro);

        String[] headersCarri = {"ID", "Serie", "Tipo", "Precio", "Cantidad", "Subtotal"};

        pnlComCarrito = new JPanel(new BorderLayout());
        pnlComCarrito.setBounds(431, 143, 308, 200);
        pnlComCarrito.setBackground(fondoClarito);
        panel.add(pnlComCarrito);

        modeloComCarri.setColumnIdentifiers(headersCarri);
        tableComCarrito = new JTable(modeloComCarri) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableComCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableComCarrito.setBackground(cyanClaro);
        aplicarHeaderRenderer(tableComCarrito, cyanMid);
        tableComCarrito.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                indexComCarrito = tableComCarrito.getSelectedRow();
                btnQuitarPro.setEnabled(indexComCarrito >= 0);
                seleccionarProductoCarrito(indexComCarrito);
            }
        });
        pnlComCarrito.add(new JScrollPane(tableComCarrito), BorderLayout.CENTER);

        pnlProCarrito = new JPanel(new BorderLayout());
        pnlProCarrito.setBounds(431, 143, 308, 200);
        pnlProCarrito.setBackground(fondoClarito);
        panel.add(pnlProCarrito);

        modeloProCarri.setColumnIdentifiers(headersCarri);
        tableProCarrito = new JTable(modeloProCarri) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableProCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableProCarrito.setBackground(cyanClaro);
        aplicarHeaderRenderer(tableProCarrito, cyanMid);
        tableProCarrito.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                indexProCarrito = tableProCarrito.getSelectedRow();
                btnQuitarPro.setEnabled(indexProCarrito >= 0);
                seleccionarProductoCarrito(indexProCarrito);
            }
        });
        pnlProCarrito.add(new JScrollPane(tableProCarrito), BorderLayout.CENTER);

        JLabel lblSubtotal = new JLabel("Subtotal producto:");
        lblSubtotal.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        lblSubtotal.setBounds(431, 366, 145, 16);
        panel.add(lblSubtotal);

        txtSubtotal = new JTextField();
        txtSubtotal.setEditable(false);
        txtSubtotal.setText("0.00");
        txtSubtotal.setBounds(542, 364, 100, 20);
        txtSubtotal.setBackground(cyanClaro);
        txtSubtotal.setBorder(bottomBorder);
        panel.add(txtSubtotal);

        JLabel lblTotal = new JLabel("Total factura:");
        lblTotal.setFont(new Font("Bahnschrift", Font.BOLD, 13));
        lblTotal.setBounds(441, 394, 100, 16);
        panel.add(lblTotal);

        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        txtTotal.setText("0.00");
        txtTotal.setBounds(542, 392, 100, 20);
        txtTotal.setBackground(cyanClaro);
        txtTotal.setBorder(bottomBorder);
        panel.add(txtTotal);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton btnRegistrarFactura = new JButton("Registrar Factura");
        btnRegistrarFactura.setForeground(Color.WHITE);
        btnRegistrarFactura.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        btnRegistrarFactura.setBackground(cyanMid);
        btnRegistrarFactura.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarFactura();
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
                Tienda.getInstance().recargaSelecionado();
                cantidadesCarrito.clear();
                dispose();
            }
        });
        buttonPane.add(btnCancelar);

        if (esCV) {
            pnlVenta.setVisible(false);
            pnlCompra.setVisible(true);
            pnlProCarrito.setVisible(false);
            pnlProDisponible.setVisible(false);
            pnlComCarrito.setVisible(true);
            pnlComDisponible.setVisible(true);
        } else {
            pnlCompra.setVisible(false);
            pnlVenta.setVisible(true);
            pnlComCarrito.setVisible(false);
            pnlComDisponible.setVisible(false);
            pnlProCarrito.setVisible(true);
            pnlProDisponible.setVisible(true);
        }

        if (esCV) {
            bloquearSeleccionCompra();
        }

        reinicializarVentana();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            reinicializarVentana();
        }
        super.setVisible(b);
    }

    private void agregarAlCarrito() {
        if (esCV && !proveedorConfirmado) {
            mostrarAlerta("alert", "Debe buscar y confirmar un proveedor antes de agregar productos.");
            bloquearSeleccionCompra();
            return;
        }

        int idx = esCV ? indexComDisponible : indexProDisponible;
        ArrayList<Producto> disponibles = obtenerProductosDisponibles(esCV);
        if (idx < 0 || idx >= disponibles.size()) return;

        Producto producto = disponibles.get(idx);
        int cantPedida = (int) spnCantidad.getValue();

        if (!esCV) {
            if (producto.getCantDisponible() <= 0) {
                mostrarAlerta("cancel", "Este producto no tiene stock disponible.");
                return;
            }
            int yaEnCarrito = cantidadesCarrito.containsKey(producto.getId())
                    ? cantidadesCarrito.get(producto.getId()) : 0;
            if ((yaEnCarrito + cantPedida) > producto.getCantDisponible()) {
                mostrarAlerta("cancel", "La cantidad solicitada supera el stock disponible (" + producto.getCantDisponible() + ").");
                return;
            }
        }

        if (cantidadesCarrito.containsKey(producto.getId())) {
            cantidadesCarrito.put(producto.getId(), cantidadesCarrito.get(producto.getId()) + cantPedida);
        } else {
            producto.setSeleccionado(true);
            cantidadesCarrito.put(producto.getId(), cantPedida);
        }

        productoSeleccionadoVista = null;
        btnProducto.setEnabled(false);
        cargaCarrito();
        cargaProductoDisponible();
    }

    private void quitarDelCarrito() {
        int idx = esCV ? indexComCarrito : indexProCarrito;
        ArrayList<Producto> enCarrito = Tienda.getInstance().getProductosSeleccionados();
        if (idx < 0 || idx >= enCarrito.size()) return;

        Producto producto = enCarrito.get(idx);
        producto.setSeleccionado(false);
        cantidadesCarrito.remove(producto.getId());

        productoSeleccionadoVista = null;
        productoEnCarritoSeleccionado = null;
        btnProducto.setEnabled(false);
        cargaCarrito();
        cargaProductoDisponible();
    }

    private void seleccionarProductoCarrito(int idx) {
        ArrayList<Producto> carrito = Tienda.getInstance().getProductosSeleccionados();
        if (idx >= 0 && idx < carrito.size()) {
            productoEnCarritoSeleccionado = carrito.get(idx);
            productoSeleccionadoVista = productoEnCarritoSeleccionado;
            btnProducto.setEnabled(true);
        } else {
            productoEnCarritoSeleccionado = null;
            productoSeleccionadoVista = null;
            btnProducto.setEnabled(false);
        }
        actualizarTotales();
    }

    private void registrarFactura() {
        ArrayList<Producto> carrito = Tienda.getInstance().getProductosSeleccionados();

        if (carrito.isEmpty()) {
            mostrarAlerta("alert", "Debe seleccionar al menos un producto.");
            return;
        }

        LocalDate hoy = LocalDate.now();

        if (esCV) {
            Proveedor proveedor = (Proveedor) Tienda.getInstance().buscarPersonaId(txtProveedor.getText().trim());
            if (proveedor == null) {
                mostrarAlerta("alert", "Debe seleccionar un proveedor válido.");
                return;
            }

            int cantTotal = 0;
            ArrayList<Producto> copia = new ArrayList<Producto>(carrito);
            for (Producto pro : copia) {
                int cant = cantidadesCarrito.containsKey(pro.getId()) ? cantidadesCarrito.get(pro.getId()) : 1;
                pro.setCantDisponible(pro.getCantDisponible() + cant);
                cantTotal += cant;
                pro.setSeleccionado(false);
            }

            FacturaCompra compra = new FacturaCompra(txtID.getText(), hoy, copia, proveedor, cantTotal, precioTotal);

            ArrayList<DetalleFacturaCompra> detallesCompra = new ArrayList<DetalleFacturaCompra>();
            int numeroLinea = 1;

            for (Producto pro : copia) {
                int cant = cantidadesCarrito.containsKey(pro.getId()) ? cantidadesCarrito.get(pro.getId()) : 1;
                double precioUnitario = pro.getPrecio();

                DetalleFacturaCompra detalle = new DetalleFacturaCompra(
                        compra.getId(),
                        numeroLinea,
                        pro,
                        cant,
                        precioUnitario
                );

                detallesCompra.add(detalle);
                numeroLinea++;
            }

            compra.setDetallesCompra(detallesCompra);

            Tienda.getInstance().registrarFactura(compra);
            mostrarAlerta("check", "Factura de compra registrada correctamente.");

        } else {
            if (txtIdCliente.getText().trim().isEmpty()) {
                mostrarAlerta("alert", "Todos los campos deben estar llenos.");
                return;
            }

            Cliente cliente = (Cliente) Tienda.getInstance().buscarPersonaId(txtIdCliente.getText().trim());
            if (cliente == null) {
                mostrarAlerta("alert", "Debe seleccionar un cliente válido.");
                return;
            }

            for (Producto pro : carrito) {
                int cant = cantidadesCarrito.containsKey(pro.getId()) ? cantidadesCarrito.get(pro.getId()) : 1;
                if (pro.getCantDisponible() < cant) {
                    mostrarAlerta("alert", "El producto " + pro.getId() + " no tiene stock suficiente (" + pro.getCantDisponible() + ").");
                    return;
                }
            }

            int cantTotal = 0;
            ArrayList<Producto> copia = new ArrayList<Producto>(carrito);
            for (Producto pro : copia) {
                int cant = cantidadesCarrito.containsKey(pro.getId()) ? cantidadesCarrito.get(pro.getId()) : 1;
                pro.setCantDisponible(pro.getCantDisponible() - cant);
                cantTotal += cant;
                pro.setSeleccionado(false);
            }

            FacturaVenta venta = new FacturaVenta(txtID.getText(), hoy, copia, cliente, cantTotal, precioTotal);

            ArrayList<DetalleFacturaVenta> detallesVenta = new ArrayList<DetalleFacturaVenta>();
            int numeroLinea = 1;

            for (Producto pro : copia) {
                int cant = cantidadesCarrito.containsKey(pro.getId()) ? cantidadesCarrito.get(pro.getId()) : 1;
                double precioUnitario = pro.getPrecio();

                DetalleFacturaVenta detalle = new DetalleFacturaVenta(
                        venta.getId(),
                        numeroLinea,
                        pro,
                        cant,
                        precioUnitario
                );

                detallesVenta.add(detalle);
                numeroLinea++;
            }

            venta.setDetallesVenta(detallesVenta);

            Tienda.getInstance().registrarFactura(venta);
            mostrarAlerta("check", "Factura de venta registrada correctamente.");
        }

        cantidadesCarrito.clear();
        Tienda.getInstance().recargaSelecionado();
        clean();
    }
    public void cargaProductoDisponible() {
        modeloComDisp.setRowCount(0);
        modeloProDisp.setRowCount(0);

        Proveedor filtroProveedor = null;
        if (esCV) {
            Object selProv = txtProveedor != null ? txtProveedor.getText().trim() : "";
            filtroProveedor = (Proveedor) Tienda.getInstance().buscarPersonaId((String) selProv);

            if (!proveedorConfirmado || filtroProveedor == null) {
                bloquearSeleccionCompra();
                return;
            }
        }

        for (Producto pro : Tienda.getInstance().getProductoNoSeleccionados()) {
            if (!pro.isEstado()) continue;
            if (!esCV && pro.getCantDisponible() <= 0) continue;

            if (esCV) {
                if (pro.getProveedor() == null || !pro.getProveedor().getId().equals(filtroProveedor.getId())) {
                    continue;
                }
            }

            Object[] fila = {
                pro.getId(),
                pro.getNumSerie(),
                getTipo(pro),
                String.format("%.2f", pro.getPrecio()),
                pro.getCantDisponible()
            };

            if (esCV) {
                modeloComDisp.addRow(fila);
            } else {
                modeloProDisp.addRow(fila);
            }
        }
    }

    public void cargaCarrito() {
        modeloComCarri.setRowCount(0);
        modeloProCarri.setRowCount(0);

        for (Producto pro : Tienda.getInstance().getProductosSeleccionados()) {
            int cant = cantidadesCarrito.containsKey(pro.getId()) ? cantidadesCarrito.get(pro.getId()) : 1;
            double subtotal = pro.getPrecio() * cant;
            Object[] fila = {
                pro.getId(),
                pro.getNumSerie(),
                getTipo(pro),
                String.format("%.2f", pro.getPrecio()),
                cant,
                String.format("%.2f", subtotal)
            };
            modeloComCarri.addRow(fila);
            modeloProCarri.addRow(fila);
        }
        actualizarTotales();
    }

    private void actualizarTotales() {
        precioTotal = 0;

        for (Producto pro : Tienda.getInstance().getProductosSeleccionados()) {
            int cant = cantidadesCarrito.containsKey(pro.getId()) ? cantidadesCarrito.get(pro.getId()) : 1;
            double sub = pro.getPrecio() * cant;
            precioTotal += sub;
        }

        double subtotalSel = 0;
        if (productoSeleccionadoVista != null) {
            int cantidadSeleccionada = Integer.parseInt(spnCantidad.getValue().toString());
            subtotalSel = productoSeleccionadoVista.getPrecio() * cantidadSeleccionada;
        }

        if (txtTotal != null) {
            txtTotal.setText(String.format("%.2f", precioTotal));
        }

        if (txtSubtotal != null) {
            txtSubtotal.setText(String.format("%.2f", subtotalSel));
        }
    }

    private ArrayList<Producto> obtenerProductosDisponibles(boolean compra) {
        ArrayList<Producto> resultado = new ArrayList<Producto>();
        Proveedor filtroProveedor = null;
        if (compra && txtProveedor != null) {
            filtroProveedor = (Proveedor) Tienda.getInstance().buscarPersonaId(txtProveedor.getText().trim());
        }

        for (Producto pro : Tienda.getInstance().getProductoNoSeleccionados()) {
            if (!pro.isEstado()) continue;
            if (!compra && pro.getCantDisponible() <= 0) continue;
            if (compra && filtroProveedor != null) {
                if (pro.getProveedor() == null || !pro.getProveedor().getId().equals(filtroProveedor.getId())) continue;
            }
            resultado.add(pro);
        }
        return resultado;
    }

    private String getTipo(Producto pro) {
        if (pro instanceof MotherBoard)     return "MotherBoard";
        if (pro instanceof Microprocesador) return "Microprocesador";
        if (pro instanceof DiscoDuro)       return "Disco Duro";
        if (pro instanceof MemoriaRam)      return "Memoria RAM";
        return "Desconocido";
    }

    private void bloquearSeleccionCompra() {
        if (tableComDisponible != null) {
            tableComDisponible.clearSelection();
            tableComDisponible.setEnabled(false);
        }
        if (tableComCarrito != null) {
            tableComCarrito.clearSelection();
            tableComCarrito.setEnabled(false);
        }

        btnAgregarPro.setEnabled(false);
        btnQuitarPro.setEnabled(false);
        btnProducto.setEnabled(false);

        indexComDisponible = -1;
        indexComCarrito = -1;
        productoSeleccionadoVista = null;
        productoEnCarritoSeleccionado = null;
    }

    private void habilitarSeleccionCompra() {
        if (tableComDisponible != null) {
            tableComDisponible.setEnabled(true);
            tableComDisponible.clearSelection();
        }
        if (tableComCarrito != null) {
            tableComCarrito.setEnabled(true);
            tableComCarrito.clearSelection();
        }

        btnAgregarPro.setEnabled(false);
        btnQuitarPro.setEnabled(false);
        btnProducto.setEnabled(false);

        indexComDisponible = -1;
        indexComCarrito = -1;
        productoSeleccionadoVista = null;
        productoEnCarritoSeleccionado = null;
    }

    private void mostrarAlerta(String icono, String msg) {
        ImageIcon ico = new ImageIcon(
                MensajeAlerta.class.getResource("/Imagenes/" + icono + ".png"));
        MensajeAlerta m = new MensajeAlerta(ico, msg);
        m.setModal(true);
        m.setVisible(true);
    }

    private void aplicarHeaderRenderer(JTable tabla, Color bg) {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(bg);
                setForeground(Color.WHITE);
                setFont(new Font("Bahnschrift", Font.BOLD, 12));
                return this;
            }
        };
        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(r);
        }
    }

    private void reinicializarVentana() {
        txtID.setText("Factura - " + Tienda.getInstance().numFactura);
        txtFecha.setText(LocalDate.now().toString());
        txtHora.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        txtTotal.setText("0.00");
        txtSubtotal.setText("0.00");

        txtIdCliente.setText("Cliente - ");
        txtProveedor.setText("Proveedor - ");

        btnBuscarCliente.setEnabled(true);
        btnBuscarProveedor.setEnabled(true);

        proveedorConfirmado = false;

        spnCantidad.setValue(1);

        txtEmpleado.setText(Tienda.getInstance().getLoginUser().getUserName());

        modeloProDisp.setRowCount(0);
        modeloProCarri.setRowCount(0);
        modeloComDisp.setRowCount(0);
        modeloComCarri.setRowCount(0);

        indexProCarrito = -1;
        indexProDisponible = -1;
        indexComCarrito = -1;
        indexComDisponible = -1;

        cantidadesCarrito.clear();
        precioTotal = 0;

        btnAgregarPro.setEnabled(false);
        btnQuitarPro.setEnabled(false);
        btnProducto.setEnabled(false);

        productoSeleccionadoVista = null;
        productoEnCarritoSeleccionado = null;

        Tienda.getInstance().recargaSelecionado();

        if (esCV) {
            pnlVenta.setVisible(false);
            pnlCompra.setVisible(true);

            pnlProCarrito.setVisible(false);
            pnlProDisponible.setVisible(false);

            pnlComCarrito.setVisible(true);
            pnlComDisponible.setVisible(true);

            bloquearSeleccionCompra();
        } else {
            pnlCompra.setVisible(false);
            pnlVenta.setVisible(true);

            pnlComCarrito.setVisible(false);
            pnlComDisponible.setVisible(false);

            pnlProCarrito.setVisible(true);
            pnlProDisponible.setVisible(true);

            if (tableProDisponible != null) {
                tableProDisponible.setEnabled(true);
                tableProDisponible.clearSelection();
            }
            if (tableProCarrito != null) {
                tableProCarrito.setEnabled(true);
                tableProCarrito.clearSelection();
            }
        }

        cargaProductoDisponible();
        cargaCarrito();
        actualizarTotales();
    }

    public void clean() {
        reinicializarVentana();
    }
}