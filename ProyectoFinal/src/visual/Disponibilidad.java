
//.
package visual;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import logico.Tienda;
import java.awt.Toolkit;

public class Disponibilidad extends JDialog {
	private JTextField textField;
	private JTextField textField_1;
	private JLabel iconlabel; 
	ImageIcon icono;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Disponibilidad dialog = new Disponibilidad();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Disponibilidad() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Disponibilidad.class.getResource("/Imagenes/convenience.png")));
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
		panel.setBackground(FondoClarito);
		panel.setBounds(0, 0, 363, 216);
		getContentPane().add(panel);
		setLocationRelativeTo(null);

		JLabel label = new JLabel("Ingrese el id del producto: ");
		label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		label.setBounds(10, 22, 184, 22);
		panel.add(label);

		textField = new JTextField();
		textField.setText("Producto - ");
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setColumns(10);
		textField.setBackground(CyanClaro);
		textField.setBounds(204, 24, 126, 20);
		textField.setBorder(bottomBorder);
		panel.add(textField);

		JButton button = new JButton("Buscar...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean hay = Tienda.getInstance().disponibleProducto(textField.getText());
				textField_1.setVisible(true);

				if (hay) {
					textField_1.setText("Está Disponible");
					icono = new ImageIcon(
							new ImageIcon(getClass().getResource("/Imagenes/check.png")).getImage()
									.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
				} else {
					textField_1.setText("No está disponible");
					icono = new ImageIcon(
							new ImageIcon(getClass().getResource("/Imagenes/alert.png")).getImage()
									.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
				}
				
				
				iconlabel.setIcon(icono);
			}
		});
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		button.setBackground(CyanMid);
		button.setBounds(129, 71, 95, 30);
		panel.add(button);

		textField_1 = new JTextField();
		textField_1.setVisible(false);
		textField_1.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBackground(CyanClaro);
		textField_1.setBounds(68, 164, 126, 20);
		textField_1.setBorder(bottomBorder);
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
