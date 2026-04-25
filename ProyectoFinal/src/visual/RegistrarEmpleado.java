//.
package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import database.TiendaComputos;
import logico.Empleado;
import logico.Persona;
import logico.Proveedor;
import logico.Tienda;
import logico.User;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import java.awt.Toolkit;
import javax.swing.JComboBox;

public class RegistrarEmpleado extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JTextField nombreField;
	private JTextField cedulaField;
	private JTextField correoField;
	private JSpinner edadSpinner;
	private JSpinner Comsionspinner;
	private JComboBox<User> cbxUser;
	private Empleado empleado;
	private User usuario;
	private String codigo = "";
    private DefaultComboBoxModel<User> usuariosRegistrados = new DefaultComboBoxModel<User>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegistrarEmpleado dialog = new RegistrarEmpleado(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegistrarEmpleado(Empleado empleado) {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegistrarEmpleado.class.getResource("/Imagenes/EmpleadoRegistrar2.png")));
		this.empleado = empleado; 

		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color FondoClarito = new Color(240, 255, 240);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);

		if (empleado != null) {
			setTitle("Actualizar Empleado");
			codigo = empleado.getId();
		} else {
			setTitle("Registrar Empleado");
		}
		setBounds(100, 100, 447, 355);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.setBackground(FondoClarito);
		setLocationRelativeTo(null); /*Poner en el centro*/



		{
			JLabel lblNewLabel = new JLabel("Ingrese los datos:");
			lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
			lblNewLabel.setBounds(132, 11, 182, 25);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel idTxt = new JLabel("ID: ");
			idTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			idTxt.setBounds(29, 82, 46, 14);
			contentPanel.add(idTxt);
		}
		{
			JLabel nombreTxt = new JLabel("Nombre:");
			nombreTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			nombreTxt.setBounds(29, 120, 57, 14);
			contentPanel.add(nombreTxt);
		}
		{
			JLabel cedulaTxt = new JLabel("Cedula:");
			cedulaTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			cedulaTxt.setBounds(29, 151, 57, 14);
			contentPanel.add(cedulaTxt);
		}
		{
			JLabel correoTxt = new JLabel("Correo:");
			correoTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			correoTxt.setBounds(29, 180, 57, 14);
			contentPanel.add(correoTxt);
		}
		{
			JLabel edadTxt = new JLabel("Edad:");
			edadTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			edadTxt.setBounds(235, 82, 46, 14);
			contentPanel.add(edadTxt);
		}

		idField = new JTextField();
		idField.setEditable(false);
		idField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		idField.setBounds(67, 78, 111, 20);
		contentPanel.add(idField);
		idField.setColumns(10);
		idField.setBorder(bottomBorder);
		idField.setBackground(CyanClaro);

		nombreField = new JTextField();
		nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		nombreField.setColumns(10);
		nombreField.setBounds(121, 114, 247, 20);
		nombreField.setBorder(bottomBorder);
		nombreField.setBackground(CyanClaro);
		contentPanel.add(nombreField);
		{
			cedulaField = new JTextField();
			cedulaField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			cedulaField.setColumns(10);
			cedulaField.setBounds(121, 145, 247, 20);
			cedulaField.setBackground(CyanClaro);
			cedulaField.setBorder(bottomBorder);
			contentPanel.add(cedulaField);
		}
		{
			correoField = new JTextField();
			correoField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			correoField.setColumns(10);
			correoField.setBounds(121, 174, 247, 20);
			correoField.setBackground(CyanClaro);
			correoField.setBorder(bottomBorder);
			contentPanel.add(correoField);
		}

		edadSpinner = new JSpinner(new SpinnerNumberModel(18, 18, 100, 1)); 
		edadSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		edadSpinner.setBounds(294, 78, 86, 20);
		edadSpinner.setBorder(bottomBorder);
		/*Nota: esta parte de aqui es para cambiar el Background del spinner*/
		JComponent editor = edadSpinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor) {
			JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
			spinnerEditor.getTextField().setBackground(CyanClaro);}
		contentPanel.add(edadSpinner);

		JLabel comisionTxt = new JLabel("Comision por venta:");
		comisionTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		comisionTxt.setBounds(54, 209, 149, 16);
		contentPanel.add(comisionTxt);

		Comsionspinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)); 
		Comsionspinner.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		Comsionspinner.setBounds(213, 205, 86, 20);
		Comsionspinner.setBorder(bottomBorder);
		/*Nota: esta parte de aqui es para cambiar el Background del spinner*/
		JComponent editor2 = Comsionspinner.getEditor();
		if (editor2 instanceof JSpinner.DefaultEditor) {
			JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor2;
			spinnerEditor.getTextField().setBackground(CyanClaro);
		}
		contentPanel.add(Comsionspinner);
		
		JLabel usuarioTxt = new JLabel("Usuario:");
		usuarioTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		usuarioTxt.setBounds(29, 240, 57, 14);
		contentPanel.add(usuarioTxt);
		
		cbxUser = new JComboBox();
        cbxUser.setBackground(CyanClaro);
        cbxUser.setBorder(bottomBorder);
		cbxUser.setBounds(121, 236, 247, 22);
		  for (User usuario : Tienda.getInstance().usuariosSinEmpleado()) {
                  usuariosRegistrados.addElement(usuario);
          }
        cbxUser.setModel(usuariosRegistrados);		
		contentPanel.add(cbxUser);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPane.setBackground(new Color(240, 255, 240));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Registrar");
				if (empleado != null) {
					okButton.setText("Actualizar");
				}
				okButton.setForeground(Color.WHITE);
				okButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				okButton.setBackground(CyanMid);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						if (nombreField.getText().isEmpty() || cedulaField.getText().isEmpty() || correoField.getText().isEmpty()) {
							ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
							MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de estar llenos!");
							mensajito.setModal(true);
							mensajito.setVisible(true);
							return;
						}

						String nombreApellido = nombreField.getText();
						String cedula = cedulaField.getText();
						String correo = correoField.getText();
						int edad = (int) edadSpinner.getValue();
						int comision = (int) Comsionspinner.getValue();
						usuario = Tienda.getInstance().buscarUsuarioPorUsername(cbxUser.getSelectedItem().toString());

						if (empleado ==  null) {
							Empleado newEmpleado = new Empleado(nombreApellido, edad, cedula, correo, (float) (comision / 100.0), usuario);
							//------- para SQL ---------
							boolean exito = TiendaComputos.getInstance().insertarEmpleado(newEmpleado);							
							//-----------------------------
							if (exito) {
								Tienda.getInstance().registrarPersona(newEmpleado);
								ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
								MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación satisfactoria.\nEmpleado registrado!");
								mensajito.setModal(true);
								mensajito.setVisible(true);
								clear();								
							}
						} else {
							empleado.setNombre(nombreApellido);
							empleado.setCedula(cedula);
							empleado.setCorreo(correo);
							empleado.setEdad(edad);
							empleado.setComisionVentas((float) (comision / 100.0));

							ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
							String texto = "¿Seguro desea modificar el empleado con código: "+ codigo +"?";
							VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
							ventanita.setModal(true);
							ventanita.setVisible(true);
							int option = ventanita.getResultado();

							if(option == JOptionPane.YES_OPTION){
								Tienda.getInstance().updatePersona(empleado);
								TiendaComputos.getInstance().modificarEmpleado(empleado);
								ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
								MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación satisfactoria.\nEmpleado modificado!");
								mensajito.setModal(true);
								mensajito.setVisible(true);
								dispose();
							}	

						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setForeground(Color.WHITE);
				cancelButton.setBackground(new Color(250, 128, 114));
				cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (empleado == null) {
							ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
							String texto = "¿Seguro desea cancelar el registro del empleado en curso?";
							VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
							ventanita.setModal(true);
							ventanita.setVisible(true);
							int option = ventanita.getResultado();			
							if(option == JOptionPane.YES_OPTION){
								dispose();
							}				
						}
						else {
							ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
							String texto = "¿Seguro desea cancelar la modificación del empleado con código: "+ codigo +"?";
							VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
							ventanita.setModal(true);
							ventanita.setVisible(true);
							int option = ventanita.getResultado();
							if(option == JOptionPane.YES_OPTION){
								dispose();
							}
						}
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		if (empleado != null) {
			cargarDatosCliente();
		} else {
			idField.setText("EMP-" + Tienda.getNumEmpleado());
		}
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {
				nombreField.requestFocusInWindow();
			}
		});
	}

	private void cargarDatosCliente() {
		idField.setText(empleado.getId());
		nombreField.setText(empleado.getNombre());
		cedulaField.setText(empleado.getCedula());
		correoField.setText(empleado.getCorreo());
		edadSpinner.setValue(empleado.getEdad());
		Comsionspinner.setValue((int) (empleado.getComisionVentas() * 100)); 
	}

	private void clear() { 
		idField.setText("EMP-" + Tienda.getNumEmpleado());
		nombreField.setText("");
		cedulaField.setText("");
		correoField.setText("");
		edadSpinner.setValue(18);
		Comsionspinner.setValue(0);
		usuariosRegistrados.removeAllElements();
		for (User usuario : Tienda.getInstance().usuariosSinEmpleado()) {
              usuariosRegistrados.addElement(usuario);
		}
		cbxUser.setModel(usuariosRegistrados);		
		contentPanel.add(cbxUser);
		nombreField.requestFocusInWindow();

	}
}
