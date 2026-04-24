//.
package visual;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TipoFactura extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnCompra;
	private JButton btnVenta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TipoFactura dialog = new TipoFactura();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TipoFactura() {
		setUndecorated(true);
		Color CyanOscuro = new Color(70, 133, 133);
        Color CyanMid = new Color(80, 180, 152);
        
		setBounds(100, 100, 282, 141);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBackground(CyanOscuro);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(240, 255, 240));
		panel.setBounds(10, 11, 262, 119);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Seleccione el tipo de factura: ");
		lblNewLabel.setBounds(22, 35, 215, 20);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 16));
		
		btnCompra = new JButton("Compra");
		btnCompra.setBounds(32, 66, 89, 23);
		panel.add(btnCompra);
		btnCompra.setForeground(Color.WHITE);
		btnCompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrarFactura reg= new RegistrarFactura(true);
				reg.setModal(true);
				reg.setVisible(true);
				dispose();
			}
		});
		btnCompra.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		
		
		btnCompra.setBackground(new Color(250, 128, 114));
		
		btnVenta = new JButton("Venta");
		btnVenta.setBounds(131, 66, 89, 23);
		panel.add(btnVenta);
		btnVenta.setForeground(Color.WHITE);
		btnVenta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegistrarFactura reg= new RegistrarFactura(false);
				reg.setModal(true);
				reg.setVisible(true);
				dispose();
			}
		});
		btnVenta.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnVenta.setBackground(CyanMid);
		
		JButton btnCancel = new JButton("X");
		btnCancel.setBackground(Color.RED);
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setFocusable(false);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(214, 0, 48, 25);
		panel.add(btnCancel);
		
	}
}
