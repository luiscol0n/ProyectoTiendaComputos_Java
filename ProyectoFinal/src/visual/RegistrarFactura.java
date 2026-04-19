package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JComboBox;
import java.awt.Toolkit;

public class RegistrarFactura extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtID;
	private JTextField txtFecha;
	private DefaultTableModel modeloPro = new DefaultTableModel();
	private DefaultTableModel modeloProCarri = new DefaultTableModel();
	private DefaultTableModel modeloCom = new DefaultTableModel();
	private DefaultTableModel modeloComCarri = new DefaultTableModel();
	private DefaultComboBoxModel<Proveedor> proveedoresRegistrados = new DefaultComboBoxModel<Proveedor>();
	private Object[] dispProRows;
	private Object[] caProRows;
	private Object[] dispComRows;
	private Object[] caComRows;
	private JTable tableProDisponible;
	private JTable tableProCarrito;
	private JTable tableComDisponible;
	private JTable tableComCarrito;
	private int indexProCarrito;
	private int indexProDisponible;
	private int indexComCarrito;
	private int indexComDisponible;
	private ArrayList<Producto> productosComprados = new ArrayList<Producto>();
	private double precioTotal = 0;
	private double descuentoTotal = 0;
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


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegistrarFactura dialog = new RegistrarFactura(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegistrarFactura(boolean esCV) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegistrarFactura.class.getResource("/Imagenes/invoice.png")));

		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color FondoClarito = new Color(240, 255, 240);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);

		setTitle("Registrar Factura");
		setBounds(100, 100, 750, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		contentPanel.setBackground(FondoClarito);


		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 714, 404);
		contentPanel.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 13, 71, 14);
		panel.add(lblNewLabel);
		panel.setBackground(FondoClarito);

		txtID = new JTextField();
		txtID.setText("Factura - "+Tienda.getInstance().numFactura);
		txtID.setEditable(false);
		txtID.setBounds(60, 10, 133, 20);
		txtID.setBorder(bottomBorder);
		txtID.setBackground(CyanClaro);
		panel.add(txtID);
		txtID.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Fecha:");
		lblNewLabel_1.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(203, 13, 65, 14);
		panel.add(lblNewLabel_1);

		txtFecha = new JTextField();
		txtFecha.setEditable(false);
		txtFecha.setBounds(278, 10, 138, 20);
		LocalDate hoy= LocalDate.now();
		txtFecha.setText(hoy.toString());
		txtFecha.setBorder(bottomBorder);
		txtFecha.setBackground(CyanClaro);
		panel.add(txtFecha);
		txtFecha.setColumns(10);
		
		pnlProDisponible = new JPanel();
		pnlProDisponible.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlProDisponible.setBounds(10, 165, 300, 200);
		panel.add(pnlProDisponible);
		pnlProDisponible.setLayout(new BorderLayout());

		String[] proHeaders = { "ID", "Num Serie", "Tipo", "Precio" };
		modeloPro.setColumnIdentifiers(proHeaders);
		tableProDisponible = new JTable(modeloPro);
		tableProDisponible.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tableProDisponible.setBackground(CyanClaro);
		tableProDisponible.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				indexProDisponible = tableProDisponible.getSelectedRow();
				btnAgregarPro.setEnabled(true);
				btnQuitarPro.setEnabled(true);
			}
		});

		JScrollPane scrollProDisponible = new JScrollPane(tableProDisponible);
		pnlProDisponible.add(scrollProDisponible, BorderLayout.CENTER);
		scrollProDisponible.getViewport().setBackground(FondoClarito);
		
		/*Nota: Aqui estoy modificando como se va a ver el header de la tabla*/
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        setBackground(CyanMid);  
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
		
		tableProCarrito.setBackground(CyanClaro);
		tableProCarrito.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				indexProCarrito = tableProCarrito.getSelectedRow();
				btnAgregarPro.setEnabled(true);
				btnQuitarPro.setEnabled(true);

			}
		});

		JScrollPane scrollProCarrito = new JScrollPane(tableProCarrito);
		pnlProCarrito.add(scrollProCarrito, BorderLayout.CENTER);
		scrollProCarrito.getViewport().setBackground(FondoClarito);

		/*Nota: Aqui estoy modificando como se va a ver el header de la tabla*/
		DefaultTableCellRenderer headerRendererCarrito = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        setBackground(CyanMid);  
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

		String[] comHeaders = { "Nombre", "Precio" };
		modeloCom.setColumnIdentifiers(comHeaders);
		tableComDisponible = new JTable(modeloCom);
		tableComDisponible.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				indexComDisponible = tableComDisponible.getSelectedRow();
				btnAgregarPro.setEnabled(true);
				btnQuitarPro.setEnabled(true);
			}
		});
		

		JScrollPane scrollComDisponible = new JScrollPane(tableComDisponible);
		pnlComDisponible.add(scrollComDisponible, BorderLayout.CENTER);
		scrollComDisponible.getViewport().setBackground(FondoClarito);

		/*Nota: Aqui estoy modificando como se va a ver el header de la tabla*/
		DefaultTableCellRenderer headerRendererComDisponible = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        setBackground(CyanMid);  
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
		tableComCarrito.setBackground(CyanClaro);
		tableComCarrito.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				indexComCarrito = tableComCarrito.getSelectedRow();
				btnAgregarPro.setEnabled(true);
				btnQuitarPro.setEnabled(true);
			}
		});

		JScrollPane scrollComCarrito = new JScrollPane(tableComCarrito);
		scrollComCarrito.setBounds(5, 5, 290, 140);
		pnlComCarrito.add(scrollComCarrito, BorderLayout.CENTER);
		scrollComCarrito.getViewport().setBackground(FondoClarito);

		/*Nota: Aqui estoy modificando como se va a ver el header de la tabla*/
		DefaultTableCellRenderer headerRendererComCarrito = new DefaultTableCellRenderer() {
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        setBackground(CyanMid);  
		        setForeground(Color.WHITE);  
		        setFont(new Font("Bahnschrift", Font.BOLD, 12)); 
		        return this;
		    }
		};
		for (int i = 0; i < tableComCarrito.getColumnModel().getColumnCount(); i++) {
		    tableComCarrito.getColumnModel().getColumn(i).setHeaderRenderer(headerRendererComCarrito);
		}

		pnlComCarrito.add(scrollComCarrito);
		
		
		btnAgregarPro = new JButton("Agregar ");
		btnAgregarPro.setForeground(new Color(255, 255, 255));
		btnAgregarPro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnAgregarPro.setBackground(CyanMid);
		btnAgregarPro.setEnabled(false);
		btnAgregarPro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnAgregarPro.setBounds(307, 220, 97, 25);
		btnAgregarPro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (indexProDisponible >= 0) {
					Producto producto = Tienda.getInstance().getProductoNoSeleccionados().get(indexProDisponible);
					producto.setSeleccionado(true);
					cargaProCarritoDisponible();
					cargaProductoDisponible();
				}
			}
		});
		panel.add(btnAgregarPro);

		btnQuitarPro = new JButton("Quitar");
		btnQuitarPro.setSize(97, 25);
		btnQuitarPro.setLocation(307, 260);
		btnQuitarPro.setEnabled(false);
		btnQuitarPro.setBackground(new Color(250, 128, 114));
		btnQuitarPro.setForeground(new Color(255, 255, 255));
		btnQuitarPro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnQuitarPro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (indexProCarrito >= 0) {
					Producto producto = Tienda.getInstance().getProductosSeleccionados().get(indexProCarrito);
					producto.setSeleccionado(false);
					cargaProCarritoDisponible();
					cargaProductoDisponible();
				}
			}
		});
		panel.add(btnQuitarPro);

		pnlCompra = new JPanel();
		pnlCompra.setBounds(10, 53, 656, 52);
		panel.add(pnlCompra);
		pnlCompra.setLayout(null);

		JLabel lblNewLabel_4 = new JLabel("Proveedor:");
		lblNewLabel_4.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_4.setBounds(10, 11, 89, 14);
		pnlCompra.add(lblNewLabel_4);


		btnProducto = new JButton("Productos");
		btnProducto.setEnabled(false);
		btnProducto.setForeground(new Color(255, 255, 255));
		btnProducto.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnProducto.setBackground(CyanMid);
		btnProducto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				btnProducto.setEnabled(false);
				pnlComCarrito.setVisible(false);
				pnlComDisponible.setVisible(false);
				pnlProCarrito.setVisible(true);
				pnlProDisponible.setVisible(true);
			}

		});
		btnProducto.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnProducto.setBounds(10, 131, 108, 23);
		panel.add(btnProducto);

		txtTotal = new JTextField();
		txtTotal.setEditable(false);
		txtTotal.setBounds(310, 134, 86, 20);
		txtTotal.setBackground(CyanClaro);
		txtTotal.setBorder(bottomBorder);
		panel.add(txtTotal);
		txtTotal.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Total:");
		lblNewLabel_2.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_2.setBounds(230, 135, 65, 14);
		panel.add(lblNewLabel_2);

		txtDescuento = new JTextField();
		txtDescuento.setEditable(false);
		txtDescuento.setText("0");
		txtDescuento.setBounds(312, 190, 86, 20);
		txtDescuento.setBackground(CyanClaro);
		txtDescuento.setBorder(bottomBorder);
		panel.add(txtDescuento);
		txtDescuento.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Descuento:");
		lblNewLabel_3.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_3.setBounds(312, 165, 83, 14);
		panel.add(lblNewLabel_3);

		pnlVenta = new JPanel();
		pnlVenta.setBounds(10, 53, 656, 52);
		panel.add(pnlVenta);
		pnlVenta.setLayout(null);

		JLabel lblNewLabel_5 = new JLabel("ID cliente:");
		lblNewLabel_5.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_5.setBounds(10, 11, 106, 14);
		pnlVenta.add(lblNewLabel_5);

		txtIdCliente = new JTextField();
		txtIdCliente.setBounds(89, 10, 86, 20);
		txtIdCliente.setText("Cliente - ");
		txtIdCliente.setBackground(CyanClaro);
		txtIdCliente.setBorder(bottomBorder);
		pnlVenta.add(txtIdCliente);
		txtIdCliente.setColumns(10);

		btnBuscarCliente = new JButton("Buscar");
		btnBuscarCliente.setForeground(new Color(255, 255, 255));
		btnBuscarCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnBuscarCliente.setBackground(CyanMid);
		btnBuscarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			
				Cliente client= (Cliente) Tienda.getInstance().buscarPersonaId(txtIdCliente.getText());
				
				if(client==null)
				{
					btnBuscarCliente.setEnabled(false);
					RegistrarCliente reg = new RegistrarCliente(null);
					reg.setModal(true);
					reg.setVisible(true);
					txtIdCliente.setText("Cliente - " + (Tienda.getInstance().numCliente - 1));
				}
				else if(client!=null)
				{
					txtIdCliente.setText(client.getId());
					ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
					MensajeAlerta mensajito = new MensajeAlerta(iconito, "Busqueda exitosa.");
					mensajito.setModal(true);
					mensajito.setVisible(true);
					btnBuscarCliente.setEnabled(false);
				}
			}
		});
		btnBuscarCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnBuscarCliente.setBounds(185, 9, 89, 23);
		pnlVenta.add(btnBuscarCliente);

		JLabel lblNewLabel_6 = new JLabel("Empleado:");
		lblNewLabel_6.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_6.setBounds(365, 11, 73, 14);
		pnlVenta.add(lblNewLabel_6);

		txtEmpleado = new JTextField();
		txtEmpleado.setText(Tienda.getInstance().getLoginUser().getUserName());
		txtEmpleado.setEditable(false);
		txtEmpleado.setBounds(450, 10, 134, 20);

		txtEmpleado.setBackground(CyanClaro);
		txtEmpleado.setBorder(bottomBorder);
		pnlVenta.add(txtEmpleado);
		txtEmpleado.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("Hora:");
		lblNewLabel_7.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel_7.setBounds(446, 13, 46, 14);
		panel.add(lblNewLabel_7);

		txtHora = new JTextField();
		txtHora.setEditable(false);
		LocalDateTime ahora = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		txtHora.setText(ahora.format(formatter)); 
		txtHora.setBounds(502, 12, 153, 20);
		txtHora.setBackground(CyanClaro);
		txtHora.setBorder(bottomBorder);
		panel.add(txtHora);
		txtHora.setColumns(10);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnRegistrarFactura = new JButton("Registrar Factura");
		btnRegistrarFactura.setForeground(new Color(255, 255, 255));
		btnRegistrarFactura.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnRegistrarFactura.setBackground(CyanMid);
		btnRegistrarFactura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ArrayList<Producto> productos=Tienda.getInstance().getProductosSeleccionados();
				for (Producto prod : productos) {
					productosComprados.add(prod);
				}

				LocalDate hoy = LocalDate.now();

				if (esCV) {
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

				    if (productos.isEmpty()) {
				        ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
				        MensajeAlerta mensajito = new MensajeAlerta(iconito, "Debe agregar al menos un producto.");
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

				    clean();
				}
				else
				{
					if (txtID.getText().isEmpty() || txtIdCliente.getText().isEmpty() ) {
						ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
						MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de\nestar llenos!");
						mensajito.setModal(true);
						mensajito.setVisible(true);
						return;
					}	
					
				}
					
				ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
				MensajeAlerta mensajito = new MensajeAlerta(iconito, "Factura registrada correctamente.");
				mensajito.setModal(true);
				mensajito.setVisible(true);
				Tienda.getInstance().recargaSelecionado();
				cargaProCarritoDisponible();
				cargaProductoDisponible();
			}
		});
		btnRegistrarFactura.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnRegistrarFactura.setActionCommand("OK");
		buttonPane.add(btnRegistrarFactura);
		getRootPane().setDefaultButton(btnRegistrarFactura);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(250, 128, 114));
		btnCancelar.setForeground(new Color(255, 255, 255));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnCancelar.setActionCommand("Cancel");
		btnCancelar.setBackground(new Color(250, 128, 114));
		btnCancelar.setForeground(new Color(255, 255, 255));
		btnCancelar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		buttonPane.add(btnCancelar);
		if(esCV)
		{
			pnlVenta.setVisible(false);
			pnlCompra.setVisible(true);
			for (Producto pro : Tienda.getInstance().getListaProductos()) {
				pro.setEstado(true);	
			}
		
		}
	
		cargaProCarritoDisponible();
		cargaProductoDisponible();
		pnlComCarrito.setBackground(FondoClarito);
		pnlComDisponible.setBackground(FondoClarito);
		pnlCompra.setBackground(FondoClarito);
		
		btnBuscarProoveedor = new JButton("Buscar ");
		btnBuscarProoveedor.setBackground(CyanMid);
		btnBuscarProoveedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				Proveedor prove= (Proveedor) Tienda.getInstance().buscarPersonaId(txtProveedor.getText());
				
				if(prove==null)
				{
					btnBuscarProoveedor.setEnabled(false);
					RegistrarProveedor reg = new RegistrarProveedor(null);
					reg.setModal(true);
					reg.setVisible(true);
					txtProveedor.setText("Proveedor - " + (Tienda.getInstance().numProveedor - 1));
				}
				else if(prove!=null)
				{
					txtProveedor.setText(prove.getId());
					ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
					MensajeAlerta mensajito = new MensajeAlerta(iconito, "Busqueda exitosa.");
					mensajito.setModal(true);
					mensajito.setVisible(true);
					btnBuscarProoveedor.setEnabled(false);
				}
			}
		});
		btnBuscarProoveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnBuscarProoveedor.setBounds(267, 9, 89, 23);
		pnlCompra.add(btnBuscarProoveedor);
		
		txtProveedor = new JTextField();
		txtProveedor.setBounds(93, 10, 164, 20);
		txtProveedor.setText("Proveedor - ");
		txtProveedor.setBackground(CyanClaro);
		txtProveedor.setBorder(bottomBorder);
		pnlCompra.add(txtProveedor);
		txtProveedor.setColumns(10);
		pnlProCarrito.setBackground(FondoClarito);
		pnlProDisponible.setBackground(FondoClarito);
		pnlVenta.setBackground(FondoClarito);
	}

	public void cargaProductoDisponible() {
		modeloPro.setRowCount(0);
		dispProRows = new Object[tableProDisponible.getColumnCount()];
		
		for (Producto pro : Tienda.getInstance().getProductoNoSeleccionados()) {
			if (pro.isEstado()) {
				dispProRows[0] = pro.getId();
				dispProRows[1] = pro.getNumSerie();
				String tipo = null;
				if (pro instanceof MotherBoard) {
					tipo = "MotherBoard";
				} else if (pro instanceof Microprocesador) {
					tipo = "Microprocesador";
				} else if (pro instanceof DiscoDuro) {
					tipo = "DiscoDuro";
				} else {
					tipo = "Memoria Ram";
				}
				dispProRows[2] = tipo;
				dispProRows[3] = pro.getPrecio();
				modeloPro.addRow(dispProRows);
			}
		}
	}


	public void cargaProCarritoDisponible() {
		precioTotal = 0;
		modeloProCarri.setRowCount(0);
		caProRows = new Object[tableProCarrito.getColumnCount()];
		for (Producto pro : Tienda.getInstance().getProductosSeleccionados()) {
			if (pro.isEstado()) {
				precioTotal += pro.getPrecio();
				caProRows[0] = pro.getId();
				caProRows[1] = pro.getNumSerie();
				String tipo = null;
				if (pro instanceof MotherBoard) {
					tipo = "MotherBoard";
				} else if (pro instanceof Microprocesador) {
					tipo = "Microprocesador";
				} else if (pro instanceof DiscoDuro) {
					tipo = "DiscoDuro";
				} else {
					tipo = "Memoria Ram";
				}
				caProRows[2] = tipo;
				caProRows[3] = pro.getPrecio();
				modeloProCarri.addRow(caProRows);
			}
		}
		txtTotal.setText(String.format("%.2f", precioTotal));

	}
	public void clean() {

		txtID.setText("Factura - "+Tienda.getInstance().numFactura);
		LocalDate hoy= LocalDate.now();
		txtFecha.setText(hoy.toString());
		txtTotal.setText("0.0");
		txtDescuento.setText("0.0");
		txtIdCliente.setText("Cliente - ");
		btnBuscarCliente.setEnabled(true);

		txtEmpleado.setText(Tienda.getInstance().getLoginUser().getUserName());

		LocalDateTime ahora = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		txtHora.setText(ahora.format(formatter));		

		modeloPro.setRowCount(0);
		modeloProCarri.setRowCount(0);
		modeloCom.setRowCount(0);
		modeloComCarri.setRowCount(0);

		proveedoresRegistrados.removeAllElements();

		dispProRows = null;
		caProRows = null;
		dispComRows = null;
		caComRows = null;

		indexProCarrito = 0;
		indexProDisponible = 0;
		indexComCarrito = 0;
		indexComDisponible = 0;

		productosComprados.clear();

		btnAgregarPro.setEnabled(false);
		btnQuitarPro.setEnabled(false);
		btnProducto.setEnabled(false);

		btnProducto.setEnabled(false);
		pnlComCarrito.setVisible(false);
		pnlComDisponible.setVisible(false);
		pnlProCarrito.setVisible(true);
		pnlProDisponible.setVisible(true);


		txtProveedor.setText("Proveedor - ");
		
    	productosComprados.removeAll(productosComprados);
    	Tienda.getInstance().recargaSelecionado();
	}
}
