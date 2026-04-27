package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logico.Cliente;
import logico.Tienda;

import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.border.MatteBorder;
import javax.swing.text.MaskFormatter;

import database.TiendaComputos;

import java.awt.Toolkit;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;

public class RegistrarCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField idField;
	private JTextField nombreField;
	private JTextField cedulaField;
	private JTextField correoField;
	private JSpinner edadSpinner; 
	private Cliente cliente; 
	private String codigo = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegistrarCliente dialog = new RegistrarCliente(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegistrarCliente(Cliente cliente) {
		setTitle("Registrar Cliente");
		setResizable(false);
		setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		setIconImage(Toolkit.getDefaultToolkit().getImage(RegistrarCliente.class.getResource("/Imagenes/ClienteRegistrar1.png")));
		this.cliente = cliente; 

		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color FondoClarito = new Color(240, 255, 240);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);

		if (cliente != null) {
			setTitle("Actualizar Cliente");
			codigo = cliente.getId();
		} else {
			setTitle("Registrar Cliente");			
		}
		setBounds(100, 100, 447, 264);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null); /*Poner en el centro*/
		contentPanel.setBackground(new Color(245, 245, 245));

		{
			JLabel lblNewLabel = new JLabel("Ingrese los datos:");
			lblNewLabel.setForeground(new Color(0, 0, 0));
			lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
			lblNewLabel.setBounds(156, 13, 144, 25);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel idTxt = new JLabel("ID: ");
			idTxt.setForeground(new Color(0, 0, 0));
			idTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			idTxt.setBounds(35, 51, 46, 14);
			contentPanel.add(idTxt);
		}
		{
			JLabel nombreTxt = new JLabel("Nombre:");
			nombreTxt.setForeground(new Color(0, 0, 0));
			nombreTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			nombreTxt.setBounds(35, 89, 57, 14);
			contentPanel.add(nombreTxt);
		}
		{
			JLabel cedulaTxt = new JLabel("Cedula:");
			cedulaTxt.setForeground(new Color(0, 0, 0));
			cedulaTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			cedulaTxt.setBounds(35, 120, 57, 14);
			contentPanel.add(cedulaTxt);
		}
		{
			JLabel correoTxt = new JLabel("Correo:");
			correoTxt.setForeground(new Color(0, 0, 0));
			correoTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			correoTxt.setBounds(35, 149, 57, 14);
			contentPanel.add(correoTxt);
		}
		{
			JLabel edadTxt = new JLabel("Edad:");
			edadTxt.setForeground(new Color(0, 0, 0));
			edadTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			edadTxt.setBounds(287, 51, 46, 14);
			contentPanel.add(edadTxt);
		}

		idField = new JTextField();
		idField.setEditable(false);
		idField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		idField.setBounds(78, 47, 86, 20);
		contentPanel.add(idField);
		idField.setColumns(10);
		idField.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));
		idField.setBackground(Color.WHITE);

		nombreField = new JTextField();
		nombreField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		nombreField.setColumns(10);
		nombreField.setBounds(127, 83, 247, 20);
		nombreField.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));
		nombreField.setBackground(Color.WHITE);
		contentPanel.add(nombreField);
		
		{
			try {
			    MaskFormatter mascara = new MaskFormatter("###-#######-#");
			    mascara.setPlaceholderCharacter(' '); // Para que se vean los espacios vacíos
			    cedulaField = new JFormattedTextField(mascara);
			} catch (ParseException e) {
			    cedulaField = new JFormattedTextField(); // Fallback por si falla la máscara
			}

			cedulaField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			cedulaField.setColumns(10);
			cedulaField.setBounds(127, 114, 247, 20);
			cedulaField.setBackground(Color.WHITE);
			cedulaField.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));
			contentPanel.add(cedulaField);
			/*cedulaField = new JTextField();
			cedulaField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			cedulaField.setColumns(10);
			cedulaField.setBounds(127, 114, 247, 20);
			cedulaField.setBackground(CyanClaro);
			cedulaField.setBorder(bottomBorder);
			contentPanel.add(cedulaField);*/
		}
		{
			correoField = new JTextField();
			correoField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
			correoField.setColumns(10);
			correoField.setBounds(127, 143, 247, 20);
			correoField.setBackground(Color.WHITE);
			correoField.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));
			contentPanel.add(correoField);

		}

		edadSpinner = new JSpinner(new SpinnerNumberModel(16, 16, 100, 1)); 
		edadSpinner.setForeground(Color.BLACK);
		edadSpinner.setBackground(Color.WHITE);
		edadSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		edadSpinner.setBounds(328, 46, 86, 20);
		edadSpinner.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));

		JComponent editor = edadSpinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor) {
		    JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor) editor;
		    spinnerEditor.getTextField().setBackground(SystemColor.WHITE); 
		    spinnerEditor.getTextField().setEditable(true);
		}
		
		contentPanel.add(edadSpinner);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new LineBorder(SystemColor.activeCaption));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setBackground(Color.LIGHT_GRAY);
			{
				JButton okButton = new JButton("Registrar");
				if (cliente != null) {
					okButton.setText("Actualizar");
				}
				okButton.setForeground(SystemColor.desktop);
				okButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				okButton.setBackground(SystemColor.text);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						if (nombreField.getText().isEmpty() || cedulaField.getText().equalsIgnoreCase("   -       - ") || correoField.getText().isEmpty()) {
							//JOptionPane.showMessageDialog(null, "Operación errónea. Todos los campos deben de estar llenos!", "Error", JOptionPane.WARNING_MESSAGE);
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


						if (cliente ==  null) {
							Cliente newCliente = new Cliente(nombreApellido, edad, cedula, correo);
							//------- para SQL --------
							boolean exito = TiendaComputos.getInstance().insertarCliente(newCliente);
							//-----------------------------
							if (exito) {
								Tienda.getInstance().registrarPersona(newCliente);
								ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
								MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación satisfactoria.\nCliente registrado!");
								mensajito.setModal(true);
								mensajito.setVisible(true);
								clear();
							}	
							
						} else {
							cliente.setNombre(nombreApellido);
							cliente.setCedula(cedula);
							cliente.setCorreo(correo);
							cliente.setEdad(edad);
							
							ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
							String texto = "¿Seguro desea modificar el cliente con código: "+ codigo +"?";
				            VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
				            ventanita.setModal(true);
				            ventanita.setVisible(true);
							int option = ventanita.getResultado();

							if(option == JOptionPane.YES_OPTION){
								Tienda.getInstance().updatePersona(cliente);
								boolean exito = TiendaComputos.getInstance().modificarCliente(cliente);
								ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
				                MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación satisfactoria.\nCliente modificado!");
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
				cancelButton.setBackground(SystemColor.text);
				cancelButton.setForeground(SystemColor.desktop);
				cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (cliente == null) {
							ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
							String texto = "¿Seguro desea cancelar el registro del cliente en curso?";
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
							String texto = "¿Seguro desea cancelar la modificación del cliente con código: "+codigo+"?";
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

		if (cliente != null) {
			cargarDatosCliente();
		} else {
			idField.setText("CLI-" + Tienda.numCliente);
		}
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {
				nombreField.requestFocusInWindow();
			}
		});
	}

	private void cargarDatosCliente() {
		idField.setText(cliente.getId());
		nombreField.setText(cliente.getNombre());
		cedulaField.setText(cliente.getCedula());
		correoField.setText(cliente.getCorreo());
		edadSpinner.setValue(cliente.getEdad());
	}

	private void clear() { 
		idField.setText("CLI-" + Tienda.numCliente);
		nombreField.setText("");
		cedulaField.setText("");
		correoField.setText("");
		edadSpinner.setValue(16);
		
		nombreField.requestFocusInWindow();
	}
}
