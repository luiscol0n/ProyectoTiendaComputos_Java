package visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import logico.Empleado;
import logico.Tienda;
import java.awt.SystemColor;

public class PagoSueldo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JLabel iconlabel; 
	ImageIcon icono;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PagoSueldo dialog = new PagoSueldo();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PagoSueldo() {
		setTitle("Sueldo Empleado");
		setIconImage(Toolkit.getDefaultToolkit().getImage(PagoSueldo.class.getResource("/Imagenes/wallet.png")));
		setBounds(100, 100, 379, 255);
		getContentPane().setLayout(null);

		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro = new Color(222, 249, 196);
		Color FondoClarito = new Color(240, 255, 240);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		panel.setBounds(0, 0, 363, 216);
		getContentPane().add(panel);
		setLocationRelativeTo(null);

		JLabel label = new JLabel("Ingrese el id del Empleado: ");
		label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		label.setBounds(10, 22, 184, 22);
		panel.add(label);

		textField = new JTextField();
		textField.setText("Empleado - ");
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setColumns(10);
		textField.setBackground(SystemColor.text);
		textField.setBounds(204, 24, 126, 20);
		textField.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));
		panel.add(textField);

		JButton button = new JButton("Buscar...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Empleado hay = (Empleado)Tienda.getInstance().buscarPersonaId(textField.getText());
				textField_1.setVisible(true);

				if (hay!=null) {
					float pago=Tienda.getInstance().calcularSalarioEmpleado(hay);
					textField_1.setText("El pago a realizar en un total de: "+pago);
					icono = new ImageIcon(
							new ImageIcon(getClass().getResource("/Imagenes/check.png")).getImage()
									.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
				} else {
					textField_1.setText("Error al calcular el pago.");
					icono = new ImageIcon(
							new ImageIcon(getClass().getResource("/Imagenes/alert.png")).getImage()
									.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
				}
				
				
				iconlabel.setIcon(icono);
			}
		});
		button.setForeground(SystemColor.text);
		button.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		button.setBackground(new Color(0, 0, 139));
		button.setBounds(129, 71, 95, 30);
		panel.add(button);

		textField_1 = new JTextField();
		textField_1.setVisible(false);
		textField_1.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBackground(SystemColor.text);
		textField_1.setBounds(22, 164, 172, 20);
		textField_1.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));
		panel.add(textField_1);

		JSeparator separator = new JSeparator();
		separator.setForeground(CyanOscuro);
		separator.setBounds(0, 125, 372, 2);
		panel.add(separator);

		iconlabel = new JLabel();
		iconlabel.setBounds(225, 138, 95, 67);
		panel.add(iconlabel);
	}

}
