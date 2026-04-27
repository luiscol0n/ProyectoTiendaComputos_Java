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
import java.awt.SystemColor;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage(SobreNosotros.class.getResource("/Imagenes/desarrollo.png")));
		setTitle("Desarrolladores");
		
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 618, 610);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaptionBorder);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ImageIcon iconito = new ImageIcon(SobreNosotros.class.getResource("/Imagenes/foto.jpeg"));

		JLabel names = new JLabel("Triana Liranzo #10149874 - Melanie P\u00E9rez #10151906 - Luis Col\u00F3n Garc\u00EDa #10152311");
		names.setHorizontalAlignment(SwingConstants.CENTER);
		names.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		names.setBounds(12, 445, 566, 42);
		contentPane.add(names);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnNewButton.setForeground(SystemColor.desktop);
		btnNewButton.setBackground(SystemColor.text);		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton.setBounds(254, 500, 89, 23);
		contentPane.add(btnNewButton);
		JLabel lblNewLabel = new JLabel(new ImageIcon(SobreNosotros.class.getResource("/Imagenes/desarrolladores.jpg")));
		lblNewLabel.setBounds(111, 13, 353, 419);
		contentPane.add(lblNewLabel);
		
		lblNewLabel.setBorder(new LineBorder(new Color(0, 0, 139), 3, true));
		setLocationRelativeTo(null);
	}
}
