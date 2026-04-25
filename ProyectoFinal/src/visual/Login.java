//.
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
		setIconImage(Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/Imagenes/bienvenido-de-nuevo.png")));
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color Rojito = new Color (250, 128, 114);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);
		
		setTitle("Bienvenido");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 253);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(32, 178, 170));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 255, 240));
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblUsuario.setBounds(37, 28, 72, 14);
		panel.add(lblUsuario);
		
		JLabel lblContrasea = new JLabel("Contrase\u00F1a:");
		lblContrasea.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblContrasea.setBounds(37, 87, 105, 14);
		panel.add(lblContrasea);
		
		textField = new JTextField();
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setBounds(37, 53, 191, 20);
		panel.add(textField);
		textField.setColumns(10);
		textField.setBorder(bottomBorder);
		textField.setBackground(CyanClaro);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setForeground(new Color(255, 255, 255));
		btnLogin.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnLogin.setActionCommand("OK");
		btnLogin.setBackground(CyanMid);
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
		btnLogin.setBounds(94, 158, 89, 34);
		panel.add(btnLogin);
		
		JLabel iconoP = new JLabel(new ImageIcon(
			    new ImageIcon(getClass().getResource("/Imagenes/loginprincipal.png")).getImage()
			    .getScaledInstance(90, 90, Image.SCALE_SMOOTH))
			);
		iconoP.setBounds(252, 44, 119, 103);
		panel.add(iconoP);
		
		JButton cancelbutton = new JButton("Cancelar");
		cancelbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		cancelbutton.setForeground(new Color(255, 255, 255));
		cancelbutton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		cancelbutton.setActionCommand("OK");
		cancelbutton.setBackground(Rojito);
		cancelbutton.setBounds(220, 158, 98, 34);
		
		panel.add(cancelbutton);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(CyanClaro);
		passwordField.setBorder(bottomBorder);
		
		passwordField.setBounds(37, 113, 191, 20);
		panel.add(passwordField);
		this.getRootPane().setDefaultButton(btnLogin);
	}
}