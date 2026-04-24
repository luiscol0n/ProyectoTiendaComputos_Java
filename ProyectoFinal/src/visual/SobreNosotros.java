//.
package visual;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class SobreNosotros extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SobreNosotros frame = new SobreNosotros();
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
	public SobreNosotros() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(SobreNosotros.class.getResource("/Imagenes/desarrolladores.png")));
		setTitle("Sobre nosotros");
		
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 602, 491);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel fotopanel = new JPanel();
		fotopanel.setBorder(new LineBorder(CyanOscuro, 4, true));
		fotopanel.setBounds(87, 31, 410, 264);
		
		contentPane.add(fotopanel);
		fotopanel.setLayout(null);
		
		ImageIcon iconito = new ImageIcon(SobreNosotros.class.getResource("/Imagenes/foto.jpeg"));
		JLabel lblNewLabel = new JLabel(new ImageIcon(
				iconito.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH)));
		lblNewLabel.setBounds(0, 0, 410, 264);
		
		lblNewLabel.setBorder(new LineBorder(CyanOscuro, 3, true));
		fotopanel.add(lblNewLabel);

		JLabel names = new JLabel("Ambar Torres #10152701 - Melanie Pérez #10151906 - Luis Colón García #10152311");
		names.setHorizontalAlignment(SwingConstants.CENTER);
		names.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		names.setBounds(10, 340, 566, 42);
		contentPane.add(names);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(CyanMid);		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton.setBounds(250, 405, 89, 23);
		contentPane.add(btnNewButton);
		setLocationRelativeTo(null);
	}
}
