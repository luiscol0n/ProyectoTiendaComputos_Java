package visual;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import logico.DiscoDuro;
import logico.MemoriaRam;
import logico.Microprocesador;
import logico.MotherBoard;
import logico.Producto;
import logico.Tienda;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.Icon;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;

public class Favoritos extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Producto producto;
	ImageIcon iconito;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Favoritos dialog = new Favoritos();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Favoritos() {
		
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color Rojito = new Color(250, 128, 114);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);
		producto = Tienda.getInstance().productoFavorito();
		String texto;	
		
		if (producto instanceof Microprocesador) {
			texto = producto.getId() + "\n" + "Tipo: Microprosesador\n Precio: "+ producto.getPrecio() ;
			iconito = new ImageIcon(Favoritos.class.getResource("/Imagenes/microprocesador.png"));
		}else if(producto instanceof MotherBoard) {
			texto = producto.getId() + "\n" + "Tipo: Motherboard\n Precio: "+ producto.getPrecio() ;
			iconito = new ImageIcon(Favoritos.class.getResource("/Imagenes/tarjeta-madre.png"));
		}else if (producto instanceof MemoriaRam) {
			texto = producto.getId() + "\n" + "Tipo: Ram\n Precio: "+ producto.getPrecio() ;
			iconito = new ImageIcon(Favoritos.class.getResource("/Imagenes/memoria-ram.png"));
		}else if (producto instanceof DiscoDuro) {
			texto = producto.getId() + "\n" + "Tipo: Disco de Almacenamiento\n Precio: "+ producto.getPrecio() ;
			iconito = new ImageIcon(Favoritos.class.getResource("/Imagenes/disco-ssd.png"));
		}else {
			texto = "No hay producto\nfavorito aun!";
			iconito = new ImageIcon(Favoritos.class.getResource("/Imagenes/signo-de-interrogacion.png"));
		}
		
		
		
		setTitle("Producto Favorito");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Favoritos.class.getResource("/Imagenes/favorito.png")));
		setBounds(100, 100, 404, 256);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.inactiveCaptionBorder);
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 139), 4, true));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("El producto favorito actualmente es: ");
		lblNewLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		lblNewLabel.setBounds(69, 23, 236, 25);
		contentPanel.add(lblNewLabel);
		
		/**/
		
		JLabel productoFav = new JLabel(new ImageIcon(
	            iconito.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
		productoFav.setBounds(45, 80, 94, 84);
		contentPanel.add(productoFav);
		
		JLabel productoInfo = new JLabel("<html>" + texto.replace("\n", "<br>") + "</html>");
		productoInfo.setHorizontalAlignment(SwingConstants.CENTER);
		productoInfo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		productoInfo.setBackground(SystemColor.inactiveCaptionBorder);
		productoInfo.setBorder(new MatteBorder(0, 0, 2, 0, (Color) SystemColor.activeCaption));
		productoInfo.setBounds(176, 80, 168, 84);
		contentPanel.add(productoInfo);
		setLocationRelativeTo(null);
	}
}
