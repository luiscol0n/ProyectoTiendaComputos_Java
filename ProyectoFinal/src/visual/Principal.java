//.
package visual;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import logico.Persona;
import logico.Proveedor;
import logico.Tienda;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.Toolkit;
import javax.swing.JLabel;
import java.awt.SystemColor;

public class Principal extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal frame = new Principal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Principal() {
		
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanClaro =  new Color (222, 249, 196);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/Imagenes/logotype.png")));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				FileOutputStream empresa2;
				ObjectOutputStream empresaWrite;
				try {
					empresa2 = new FileOutputStream("tienda.dat");
					empresaWrite = new ObjectOutputStream(empresa2);
					empresaWrite.writeObject(Tienda.getInstance());
					empresa2.close();
					empresaWrite.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("TLM Tech");

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 0, 139));
		setJMenuBar(menuBar);

		JMenu clientesMenu = new JMenu("          Clientes          ");
		clientesMenu.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		clientesMenu.setForeground(Color.WHITE);
		menuBar.add(clientesMenu);

		JMenuItem buttonRegistrarCliente = new JMenuItem("Registrar");
		buttonRegistrarCliente.setBackground(Color.WHITE);
		buttonRegistrarCliente.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/ClienteRegistrar1.png")));
		buttonRegistrarCliente.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		buttonRegistrarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrarCliente ventanita = new RegistrarCliente(null);
				ventanita.setModal(true);
				ventanita.setVisible(true);
			}
		});
		clientesMenu.add(buttonRegistrarCliente);

		JMenuItem muenoListaClientes = new JMenuItem("Lista Clientes");
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			muenoListaClientes.setVisible(false);
		}
		muenoListaClientes.setBackground(Color.WHITE);
		muenoListaClientes.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/lista.png")));
		muenoListaClientes.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		muenoListaClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListaClientes ventanita = new ListaClientes();
				ventanita.setModal(true);
				ventanita.setVisible(true);
			}
		});
		clientesMenu.add(muenoListaClientes);

		JMenu menuEmpleado = new JMenu("          Empleados          ");
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			menuEmpleado.setVisible(false);
		}
		menuEmpleado.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		menuEmpleado.setForeground(Color.WHITE);
		menuBar.add(menuEmpleado);

		JMenuItem menubuttonRegistrarEmpleado = new JMenuItem("Registrar");
		menubuttonRegistrarEmpleado.setBackground(Color.WHITE);
		menubuttonRegistrarEmpleado.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/ClienteRegistrar1.png")));
		menubuttonRegistrarEmpleado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Tienda.getInstance().usuariosSinEmpleado().size() == 0) {
					ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
					MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nAsegúrese de tener al menos 1 usuario registrado!");
					mensajito.setModal(true);
					mensajito.setVisible(true);
					return;
				} else {
					RegistrarEmpleado ventanita = new RegistrarEmpleado(null);
					ventanita.setModal(true);
					ventanita.setVisible(true);					
				}
			}
		});
		menubuttonRegistrarEmpleado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuEmpleado.add(menubuttonRegistrarEmpleado);

		JMenuItem menubuttonListaEMpleado = new JMenuItem("Lista Empleados");
		menubuttonListaEMpleado.setBackground(Color.WHITE);
		menubuttonListaEMpleado.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/lista.png")));
		menubuttonListaEMpleado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListaEmpleados ventanita = new ListaEmpleados();
				ventanita.setModal(true);
				ventanita.setVisible(true);
			}
		});
		menubuttonListaEMpleado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuEmpleado.add(menubuttonListaEMpleado);

		JMenu menuProveedor = new JMenu("          Proveedores          ");
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			menuProveedor.setVisible(false);
		}
		menuProveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		menuProveedor.setForeground(Color.WHITE);
		menuBar.add(menuProveedor);

		JMenuItem menuRegistrarProveedor = new JMenuItem("Registrar");
		menuRegistrarProveedor.setBackground(Color.WHITE);
		menuRegistrarProveedor.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/proveedor.png")));
		menuRegistrarProveedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistrarProveedor ventanita = new RegistrarProveedor(null);
				ventanita.setModal(true);
				ventanita.setVisible(true);
			}
		});
		menuRegistrarProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuProveedor.add(menuRegistrarProveedor);

		JMenuItem menuListaProveedor = new JMenuItem("Lista Proveedores");
		menuListaProveedor.setBackground(Color.WHITE);
		menuListaProveedor.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/lista.png")));
		menuListaProveedor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListaProveedores ventanita = new ListaProveedores();
				ventanita.setModal(true);
				ventanita.setVisible(true);
			}
		});
		menuListaProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuProveedor.add(menuListaProveedor);

		JMenu menuProducto = new JMenu("          Productos          ");
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador"))
		{
			menuProducto.setVisible(false);
		}
		menuProducto.setForeground(Color.WHITE);
		menuProducto.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		menuBar.add(menuProducto);

		JMenuItem mntmNewMenuItem = new JMenuItem("Registrar");
		mntmNewMenuItem.setBackground(Color.WHITE);
		mntmNewMenuItem.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/producto.png")));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int cantProveedores = 0;	
				for (Persona persona : Tienda.getInstance().getListaPersonas()) {
					if (persona instanceof Proveedor) {
						cantProveedores++;
					}
				}
				if (cantProveedores == 0) {
					ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
					MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nDebe haber por lo menos un proveedor registrado!");
					mensajito.setModal(true);
					mensajito.setVisible(true);
					return;
				} else {
					RegistrarProducto registrarProducto = new RegistrarProducto(null);
					registrarProducto.setVisible(true);
				}

			}
		});
		mntmNewMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuProducto.add(mntmNewMenuItem);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Lista");
		mntmNewMenuItem_1.setBackground(Color.WHITE);
		mntmNewMenuItem_1.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/lista.png")));
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListarProducto lista= new ListarProducto();
				lista.setModal(true);
				lista.setVisible(true);
			}
		});
		mntmNewMenuItem_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuProducto.add(mntmNewMenuItem_1);

		JMenu menuFactura = new JMenu("          Facturas          ");
		menuFactura.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		menuFactura.setForeground(Color.WHITE);
		menuBar.add(menuFactura);

		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Registrar");
		mntmNewMenuItem_7.setBackground(Color.WHITE);
		mntmNewMenuItem_7.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/factura.png")));
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TipoFactura tipo= new TipoFactura();
				tipo.setModal(true);
				tipo.setVisible(true);
			}
		});
		mntmNewMenuItem_7.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuFactura.add(mntmNewMenuItem_7);

		JMenuItem mntmNewMenuItem_8 = new JMenuItem("Lista");
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListadoFacturas listadoFacturas = new ListadoFacturas();
				listadoFacturas.setVisible(true);
			}
		});
		mntmNewMenuItem_8.setBackground(Color.WHITE);
		mntmNewMenuItem_8.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/lista.png")));
		mntmNewMenuItem_8.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuFactura.add(mntmNewMenuItem_8);

		JMenu menuAdministracion = new JMenu("          Administraci\u00F3n          ");
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			menuAdministracion.setText("Inventario");
		}
		menuAdministracion.setForeground(Color.WHITE);
		menuAdministracion.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
		menuBar.add(menuAdministracion);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Desarrolladores");
		mntmNewMenuItem_2.setBackground(Color.WHITE);
		mntmNewMenuItem_2.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/desarrollo.png")));
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SobreNosotros tipo= new SobreNosotros();
				tipo.setVisible(true);

			}
		});

		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Producto Favorito");
		mntmNewMenuItem_5.setBackground(Color.WHITE);
		mntmNewMenuItem_5.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/favorito.png")));
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Favoritos ventanita = new Favoritos();
				ventanita.setModal(true);
				ventanita.setVisible(true);
			}
		});

		mntmNewMenuItem_5.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_5);

		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Disponibilidad");
		mntmNewMenuItem_3.setBackground(Color.WHITE);
		mntmNewMenuItem_3.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/disponibilidad.png")));
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Disponibilidad ventanita = new Disponibilidad();
				ventanita.setModal(true);
				ventanita.setVisible(true);

			}
		});
		mntmNewMenuItem_3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_3);

		JMenuItem mntmNewMenuItem_6 = new JMenuItem("Cantidad de un producto");
		mntmNewMenuItem_6.setBackground(Color.WHITE);
		mntmNewMenuItem_6.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/cantidad.png")));
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				CantidadProducto ventanita = new CantidadProducto();
				ventanita.setModal(true);
				ventanita.setVisible(true);

			}
		});
		mntmNewMenuItem_6.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_6);

		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Inventario general");
		mntmNewMenuItem_4.setBackground(Color.WHITE);
		mntmNewMenuItem_4.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/inventario.png")));
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				InventarioGeneral ventanita = new InventarioGeneral();

				ventanita.setVisible(true);


			}
		});
		mntmNewMenuItem_4.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_4);
		mntmNewMenuItem_2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_2);

		JMenu mnNewMenu_3 = new JMenu("Usuarios");
		mnNewMenu_3.setBackground(Color.WHITE);
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			mnNewMenu_3.setVisible(false);
		}
		
		JMenuItem mntmNewMenuItem_13 = new JMenuItem("Calcular sueldo de un empleado");
		mntmNewMenuItem_13.setBackground(Color.WHITE);
		mntmNewMenuItem_13.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/salario.png")));
		mntmNewMenuItem_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PagoSueldo pago= new PagoSueldo();
				pago.setVisible(true);	
			}
		});
		mntmNewMenuItem_13.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_13);
		mnNewMenu_3.setBackground(CyanClaro);
		mnNewMenu_3.setForeground(Color.BLACK);
		mnNewMenu_3.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/EmpleadoRegistrar.png")));
		mnNewMenu_3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mnNewMenu_3);
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			mntmNewMenuItem_13.setVisible(false);
		}

		
		
		JMenuItem mntmNewMenuItem_10 = new JMenuItem("Registro");
		mntmNewMenuItem_10.setBackground(Color.WHITE);
		mntmNewMenuItem_10.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/ClienteRegistrar1.png")));
		mntmNewMenuItem_10.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmNewMenuItem_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegUser regUser = new RegUser(null);
				regUser.setVisible(true);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_10);

		JMenuItem mntmNewMenuItem_9 = new JMenuItem("Listado");
		mntmNewMenuItem_9.setBackground(Color.WHITE);
		mntmNewMenuItem_9.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/lista.png")));
		mntmNewMenuItem_9.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		mntmNewMenuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ListadoUsuarios listadoUsuarios = new ListadoUsuarios();
				listadoUsuarios.setVisible(true);
			}
		});
		mnNewMenu_3.add(mntmNewMenuItem_9);

		JMenuItem mntmNewMenuItem_11 = new JMenuItem("Backup");
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			mntmNewMenuItem_11.setVisible(false);
		}
		mntmNewMenuItem_11.setBackground(Color.WHITE);
		mntmNewMenuItem_11.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/backup.png")));
		mntmNewMenuItem_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
				String texto = "¿Estás seguro de que desea realizar un backup de la información actual?";
				VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
				ventanita.setModal(true);
				ventanita.setVisible(true);
				int confirmacion = ventanita.getResultado();

				if (confirmacion == JOptionPane.YES_OPTION) {
					try {
						Socket socket = new Socket("127.0.0.1", 7000);
						DataOutputStream flujoSalida = new DataOutputStream(socket.getOutputStream());
						File file = new File("tienda.dat");
						FileInputStream fileInputStream = new FileInputStream(file);

							byte[] buffer = new byte[4096];
							int bytesRead;
							while ((bytesRead = fileInputStream.read(buffer)) != -1) {
								flujoSalida.write(buffer, 0, bytesRead);
							}
							flujoSalida.flush();
							fileInputStream.close();
							flujoSalida.close();
							socket.close();
							
						ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
						MensajeAlerta mensajito = new MensajeAlerta(iconito, "Backup realizado correctamente.");
						mensajito.setModal(true);
						mensajito.setVisible(true);
					}
					catch (UnknownHostException uhe)
					{
						ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
						MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea. No se pudo acceder al servidor!");
						mensajito.setModal(true);
						mensajito.setVisible(true);
						return;
					}
					catch (IOException ex) {
						ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
						MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea. Comunicación rechazada!");
						mensajito.setModal(true);
						mensajito.setVisible(true);
						return;
					}

				} else {
					ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
					MensajeAlerta mensajito = new MensajeAlerta(iconito, "Backup cancelado.");
					mensajito.setModal(true);
					mensajito.setVisible(true);
				}
			}
		});
		mntmNewMenuItem_11.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_11);
		
		JMenuItem mntmNewMenuItem_12 = new JMenuItem("Panel de Reportes");
		mntmNewMenuItem_12.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/reportes.png")));
		mntmNewMenuItem_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PanelReportes panelReportes = new PanelReportes(null);
				panelReportes.setVisible(true);
			}
		});
		mntmNewMenuItem_12.setBackground(Color.WHITE);
		mntmNewMenuItem_12.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		menuAdministracion.add(mntmNewMenuItem_12);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		if (!Tienda.getLoginUser().getTipo().equalsIgnoreCase("Administrador")) {
			mntmNewMenuItem_12.setVisible(false);
		}
		
		JLabel fondo = new JLabel("");
		fondo.setIcon(new ImageIcon(Principal.class.getResource("/Imagenes/Background  Tienda Computos.png")));
		fondo.setBounds(0, 0, 1921, 1028);
		contentPane.add(fondo);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

	}
}
