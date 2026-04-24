//.
package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import logico.Producto;
import logico.Tienda;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CantidadProducto extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField productoField;
	private JTextField cantField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CantidadProducto dialog = new CantidadProducto();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CantidadProducto() {
		setTitle("Cantidad Disponible");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CantidadProducto.class.getResource("/Imagenes/boxes.png")));
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color FondoClarito = new Color(240, 255, 240);
		Color Rojito = new Color (250, 128, 114);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);
		
		setBackground(FondoClarito);
		setResizable(false);
		setBounds(100, 100, 370, 236);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 255, 240));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		setLocationRelativeTo(null);
		contentPanel.setLayout(null);
		{
			JLabel idTxt = new JLabel("Ingrese el id del producto: ");
			idTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
			idTxt.setBounds(10, 22, 184, 22);
			contentPanel.add(idTxt);
		}
		
		productoField = new JTextField();
		productoField.setText("Producto - ");
		productoField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		productoField.setBounds(204, 24, 126, 20);
		productoField.setBorder(bottomBorder);
		productoField.setBackground(CyanClaro);
		contentPanel.add(productoField);
		productoField.setColumns(10);
		
		JButton okButton = new JButton("Buscar...");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idProducto = productoField.getText();
				Producto pro=Tienda.getInstance().buscarProductoId(idProducto);
             	
             	  if(pro!=null) {
             		 cantField.setText(String.valueOf(pro.getCantDisponible()));
             	  }      		  
             	  else {
             		  //JOptionPane.showMessageDialog(null, "El producto no existe", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
             		 ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
             		 MensajeAlerta mensajito = new MensajeAlerta(iconito, "El producto no existe");
             		 mensajito.setModal(true);
             		 mensajito.setVisible(true);
                  }
			}
		});
		
		
		okButton.setForeground(new Color(255, 255, 255));
		okButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		okButton.setBounds(129, 71, 95, 30);
		okButton.setBackground(CyanMid);
		contentPanel.add(okButton);
		
		JLabel cantTxt = new JLabel("Quedan: ");
		cantTxt.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		cantTxt.setBounds(85, 154, 64, 22);
		contentPanel.add(cantTxt);
		
		cantField = new JTextField();
		cantField.setEditable(false);
		cantField.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		cantField.setBounds(159, 157, 126, 20);
		cantField.setBorder(bottomBorder);
		cantField.setBackground(CyanClaro);
		contentPanel.add(cantField);
		cantField.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(CyanOscuro);
		separator.setBounds(0, 125, 372, 2);
		contentPanel.add(separator);
	}
	

}
