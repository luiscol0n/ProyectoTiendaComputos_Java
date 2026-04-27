package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Cliente;
import logico.Persona;
import logico.Proveedor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;


import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;



public class InformacionPersona extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private String codigo = "";
	private JTextField txtIdProveedor;
	private JTextField txtNombreProveedor;
	private JTextField txtCedulaProveedor;
	private JTextField txtCorreoProveedor;
	private JPanel pnlProveedores;
	private JTextField txtEdadProveedor;
	private JTextField txtEmpresaProveedor;
	private JTextField txtIdCliente;
	private JTextField txtNombreCliente;
	private JTextField txtCorreoCliente;
	private JTextField txtCedulaCliente;
	private JTextField txtEdadCliente;
	private JTextField txtClasificacionCliente;
	private JTextField txtComprasCliente;
	private JPanel pnlClientes;
	private JLabel lblDatosProveedor;
	private JLabel lblDatosCliente;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InformacionPersona dialog = new InformacionPersona(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InformacionPersona(Persona persona) {

		setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color FondoClarito = new Color(240, 255, 240);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);
		setBounds(100, 100, 514, 264);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setBackground(FondoClarito);
		contentPanel.setLayout(null);

		{
			pnlProveedores = new JPanel();
			pnlProveedores.setBounds(5, 5, 498, 180);
			pnlProveedores.setLayout(null);
			pnlProveedores.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnlProveedores.setBackground(new Color(240, 255, 240));
			contentPanel.add(pnlProveedores);
			{

				lblDatosProveedor = new JLabel();
				lblDatosProveedor.setText("Informaci\u00F3n Proveedores: Proveedor - 1");
				if (persona != null && persona instanceof Proveedor)
				{
					lblDatosProveedor.setText("Informaci n Proveedores: "+persona.getId());			
				}
				lblDatosProveedor.setForeground(Color.BLACK);
				lblDatosProveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
				lblDatosProveedor.setBounds(105, 10, 325, 25);
				pnlProveedores.add(lblDatosProveedor);
			}
			{
				JLabel label = new JLabel("ID: ");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(15, 51, 46, 14);
				pnlProveedores.add(label);
			}
			{
				JLabel label = new JLabel("Nombre:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(15, 85, 57, 14);
				pnlProveedores.add(label);
			}
			{
				JLabel label = new JLabel("Cedula:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(15, 119, 57, 14);
				pnlProveedores.add(label);
			}
			{
				JLabel label = new JLabel("Correo:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(260, 119, 57, 14);
				pnlProveedores.add(label);
			}
			{
				JLabel label = new JLabel("Edad:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(190, 51, 46, 14);
				pnlProveedores.add(label);
			}
			{
				txtIdProveedor = new JTextField();
				txtIdProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtIdProveedor.setEditable(false);
				txtIdProveedor.setColumns(10);
				txtIdProveedor.setBackground(new Color(222, 249, 196));
				txtIdProveedor.setBounds(55, 48, 115, 20);
				pnlProveedores.add(txtIdProveedor);
			}
			{
				txtNombreProveedor = new JTextField();
				txtNombreProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtNombreProveedor.setEditable(false);
				txtNombreProveedor.setColumns(10);
				txtNombreProveedor.setBackground(new Color(222, 249, 196));
				txtNombreProveedor.setBounds(90, 82, 334, 20);
				pnlProveedores.add(txtNombreProveedor);
			}
			{
				txtCedulaProveedor = new JTextField();
				txtCedulaProveedor.setEditable(false);
				txtCedulaProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtCedulaProveedor.setColumns(10);
				txtCedulaProveedor.setBackground(new Color(222, 249, 196));
				txtCedulaProveedor.setBounds(90, 116, 155, 20);
				pnlProveedores.add(txtCedulaProveedor);
			}
			{
				txtCorreoProveedor = new JTextField();
				txtCorreoProveedor.setEditable(false);
				txtCorreoProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtCorreoProveedor.setColumns(10);
				txtCorreoProveedor.setBackground(new Color(222, 249, 196));
				txtCorreoProveedor.setBounds(322, 116, 159, 20);
				pnlProveedores.add(txtCorreoProveedor);
			}
			{
				txtEdadProveedor = new JTextField();
				txtEdadProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtEdadProveedor.setEditable(false);
				txtEdadProveedor.setColumns(10);
				txtEdadProveedor.setBackground(new Color(222, 249, 196));
				txtEdadProveedor.setBounds(239, 48, 85, 20);
				pnlProveedores.add(txtEdadProveedor);
			}
			{
				JLabel lblEmpresa = new JLabel("Empresa:");
				lblEmpresa.setForeground(Color.BLACK);
				lblEmpresa.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblEmpresa.setBounds(15, 151, 71, 14);
				pnlProveedores.add(lblEmpresa);
			}
			{
				txtEmpresaProveedor = new JTextField();
				txtEmpresaProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtEmpresaProveedor.setEditable(false);
				txtEmpresaProveedor.setColumns(10);
				txtEmpresaProveedor.setBackground(new Color(222, 249, 196));
				txtEmpresaProveedor.setBounds(90, 148, 334, 20);
				pnlProveedores.add(txtEmpresaProveedor);
			}
		}
		{
			pnlClientes = new JPanel();
			pnlClientes.setBounds(5, 5, 498, 180);
			pnlClientes.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnlClientes.setBackground(new Color(240, 255, 240));
			contentPanel.add(pnlClientes);
			pnlClientes.setLayout(null);
			{
				lblDatosCliente = new JLabel();
				lblDatosCliente.setText("Informaci\u00F3n Clientes: Cliente - 1");
				if (persona != null && persona instanceof Cliente)
				{
					lblDatosCliente.setText("Informaci n Clientes: "+persona.getId());				
				}
				lblDatosCliente.setForeground(Color.BLACK);
				lblDatosCliente.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
				lblDatosCliente.setBounds(130, 10, 236, 25);
				pnlClientes.add(lblDatosCliente);
			}
			{
				JLabel label = new JLabel("ID: ");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(15, 51, 46, 14);
				pnlClientes.add(label);
			}
			{
				JLabel label = new JLabel("Nombre:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(15, 85, 57, 14);
				pnlClientes.add(label);
			}
			{
				JLabel label = new JLabel("Cedula:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(15, 119, 57, 14);
				pnlClientes.add(label);
			}
			{
				JLabel label = new JLabel("Correo:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(260, 119, 57, 14);
				pnlClientes.add(label);
			}
			{
				JLabel label = new JLabel("Edad:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(190, 51, 46, 14);
				pnlClientes.add(label);
			}
			{
				txtIdCliente = new JTextField();
				txtIdCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtIdCliente.setEditable(false);
				txtIdCliente.setColumns(10);
				txtIdCliente.setBackground(new Color(222, 249, 196));
				txtIdCliente.setBounds(55, 48, 115, 20);
				pnlClientes.add(txtIdCliente);
			}
			{
				txtNombreCliente = new JTextField();
				txtNombreCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtNombreCliente.setEditable(false);
				txtNombreCliente.setColumns(10);
				txtNombreCliente.setBackground(new Color(222, 249, 196));
				txtNombreCliente.setBounds(90, 82, 230, 20);
				pnlClientes.add(txtNombreCliente);
			}
			{
				txtCorreoCliente = new JTextField();
				txtCorreoCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtCorreoCliente.setEditable(false);
				txtCorreoCliente.setColumns(10);
				txtCorreoCliente.setBackground(new Color(222, 249, 196));
				txtCorreoCliente.setBounds(322, 116, 145, 20);
				pnlClientes.add(txtCorreoCliente);
			}
			{
				txtCedulaCliente = new JTextField();
				txtCedulaCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtCedulaCliente.setEditable(false);
				txtCedulaCliente.setColumns(10);
				txtCedulaCliente.setBackground(new Color(222, 249, 196));
				txtCedulaCliente.setBounds(90, 116, 159, 20);
				pnlClientes.add(txtCedulaCliente);
			}
			{
				txtEdadCliente = new JTextField();
				txtEdadCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtEdadCliente.setEditable(false);
				txtEdadCliente.setColumns(10);
				txtEdadCliente.setBackground(new Color(222, 249, 196));
				txtEdadCliente.setBounds(235, 48, 85, 20);
				pnlClientes.add(txtEdadCliente);
			}
			{
				JLabel label = new JLabel("Clasificaci\u00F3n:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(340, 51, 95, 14);
				pnlClientes.add(label);
			}
			{
				txtClasificacionCliente = new JTextField();
				txtClasificacionCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtClasificacionCliente.setEditable(false);
				txtClasificacionCliente.setColumns(10);
				txtClasificacionCliente.setBackground(new Color(222, 249, 196));
				txtClasificacionCliente.setBounds(435, 48, 46, 20);
				pnlClientes.add(txtClasificacionCliente);
			}
			{
				JLabel label = new JLabel("Compras:");
				label.setForeground(Color.BLACK);
				label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				label.setBounds(340, 85, 78, 14);
				pnlClientes.add(label);
			}
			{
				txtComprasCliente = new JTextField();
				txtComprasCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
				txtComprasCliente.setEditable(false);
				txtComprasCliente.setColumns(10);
				txtComprasCliente.setBackground(new Color(222, 249, 196));
				txtComprasCliente.setBounds(415, 82, 66, 20);
				pnlClientes.add(txtComprasCliente);
			}
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setBackground(new Color(240, 255, 240));
		{
			JButton cancelButton = new JButton("Salir");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setBackground(new Color(250, 128, 114));
			cancelButton.setForeground(new Color(255, 255, 255));
			cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		cargarDatos(persona);
	}

	public void cargarDatos(Persona persona) {
		if (persona instanceof Cliente) {
			txtIdCliente.setText(persona.getId());
			txtNombreCliente.setText(persona.getNombre());
			txtCedulaCliente.setText(persona.getCedula());
			txtCorreoCliente.setText(persona.getCorreo());
			txtEdadCliente.setText(String.valueOf(persona.getEdad()));
			txtClasificacionCliente.setText(Character.toString(((Cliente) persona).getClasificacion()));
			txtComprasCliente.setText(String.valueOf(((Cliente) persona).getCantVentas()));

			txtIdProveedor.setText("");
			txtNombreProveedor.setText("");
			txtCedulaProveedor.setText("");
			txtCorreoProveedor.setText("");
			txtEdadProveedor.setText("");
			txtEmpresaProveedor.setText("");
			
			pnlProveedores.setVisible(false);
			pnlClientes.setVisible(true);
		}

		if (persona instanceof Proveedor) {
			txtIdProveedor.setText(persona.getId());
			txtNombreProveedor.setText(persona.getNombre());
			txtCedulaProveedor.setText(persona.getCedula());
			txtCorreoProveedor.setText(persona.getCorreo());
			txtEdadProveedor.setText(String.valueOf(((Proveedor) persona).getEdad()));
			txtEmpresaProveedor.setText(((Proveedor) persona).getEmpresa());

			txtIdCliente.setText("");
			txtNombreCliente.setText("");
			txtCedulaCliente.setText("");
			txtCorreoCliente.setText("");
			txtEdadCliente.setText("");
			txtClasificacionCliente.setText("");
			txtComprasCliente.setText("");
			
			pnlProveedores.setVisible(true);
			pnlClientes.setVisible(false);
		}
	}
}