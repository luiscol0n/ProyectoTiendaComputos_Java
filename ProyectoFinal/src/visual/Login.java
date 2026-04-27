package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import database.TiendaComputos;
import logico.Tienda;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.JPasswordField;
import java.awt.SystemColor;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				FileInputStream empresa;
				FileOutputStream empresa2;
				ObjectInputStream empresaRead;
				ObjectOutputStream empresaWrite;
				try {
					empresa = new FileInputStream ("tienda.dat");
					empresaRead = new ObjectInputStream(empresa);
					Tienda temp = (Tienda)empresaRead.readObject();
					Tienda.setMiTienda(temp);
					Tienda.getInstance().generarIds();
					empresa.close();
					empresaRead.close();
				} catch (FileNotFoundException e) {
					TiendaComputos.getInstance().limpiarTablasTotal();
					try {
						empresa2 = new  FileOutputStream("tienda.dat");
						empresaWrite = new ObjectOutputStream(empresa2);
						User aux = new User("Administrador", "Admin", "Admin");
						TiendaComputos.getInstance().insertarUsuario("Administrador", "Admin", "Admin");
						Tienda.getInstance().RegistrarUser(aux);
						empresaWrite.writeObject(Tienda.getInstance());
						empresa2.close();
						empresaWrite.close();
					} catch (FileNotFoundException e1) {
					} catch (IOException e1) {
						// TODO Auto-generated catch block
					}
				} catch (IOException e) {
					
					
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					Login frame = new Login();
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
	public Login() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Imagenes/login.png")));
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color Rojito = new Color (250, 128, 114);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);
		
		setTitle("Bienvenido a TLM Tech");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 434, 283);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.textHighlight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setToolTipText("hhh");
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblUsuario = new JLabel("    Usuario:");
		lblUsuario.setIcon(new ImageIcon(Login.class.getResource("/Imagenes/usuarioo.png")));
		lblUsuario.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblUsuario.setBounds(22, 92, 105, 34);
		panel.add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setIcon(new ImageIcon(Login.class.getResource("/Imagenes/password.png")));
		lblContrasea.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblContrasea.setBounds(22, 139, 105, 24);
		panel.add(lblContrasea);
		
		textField = new JTextField();
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setBounds(139, 102, 196, 20);
		panel.add(textField);
		textField.setColumns(10);
		textField.setBorder(bottomBorder);
		textField.setBackground(SystemColor.control);
		
		JButton btnLogin = new JButton("Iniciar sesi\u00F3n");
		btnLogin.setForeground(SystemColor.desktop);
		btnLogin.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnLogin.setActionCommand("OK");
		btnLogin.setBackground(SystemColor.text);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Tienda.getInstance().confirmLogin(textField.getText(),new String (passwordField.getPassword()))){
					Principal frame = new Principal();
					dispose();
					frame.setVisible(true);
				}
				else {
					ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
	                MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nLos datos ingresados no son válidos.");
	                mensajito.setModal(true);
	                mensajito.setVisible(true);
					textField.setText("");
					passwordField.setText("");
				};
				
			}
		});
		btnLogin.setBounds(80, 192, 133, 24);
		panel.add(btnLogin);
		
		JLabel iconoP = new JLabel(new ImageIcon(Login.class.getResource("/Imagenes/Login TLM.png"))
			);
		iconoP.setBounds(101, 15, 216, 74);
		panel.add(iconoP);
		
		JButton cancelbutton = new JButton("Salir");
		cancelbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		cancelbutton.setForeground(SystemColor.desktop);
		cancelbutton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		cancelbutton.setActionCommand("OK");
		cancelbutton.setBackground(SystemColor.text);
		cancelbutton.setBounds(247, 192, 105, 24);
		
		panel.add(cancelbutton);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(SystemColor.control);
		passwordField.setBorder(bottomBorder);
		
		passwordField.setBounds(139, 141, 196, 20);
		panel.add(passwordField);
		this.getRootPane().setDefaultButton(btnLogin);
	}
}