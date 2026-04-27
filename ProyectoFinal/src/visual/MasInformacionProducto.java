package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import logico.DiscoDuro;
import logico.MemoriaRam;
import logico.Microprocesador;
import logico.MotherBoard;
import logico.Producto;


import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class MasInformacionProducto extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtID;
	private JTextField txtNumSerie;
	private JTextField txtMarca;
	private JTextField txtProovedor;
	private JTextField txtCantidad;
	private JTextField txtPrecio;
	private JTextField txtModeloMicro;
	private JTextField txtSocketMicro;
	private JTextField txtVeProMicro;
	private JTextField txtModeloMother;
	private JTextField txtTipoRamMother;
	private JTextField txtSocketMother;
	private JTextField txtDiscoMother;
	private JPanel pnlMicro;
	private JPanel pnlMother;
	private JTextField txtCantRam;
	private JTextField txtTipoRam;
	private JTextField txtConexionDisco;
	private JTextField txtModeloDisco;
	private JTextField txtCapAlmDisco;
	private JPanel pnlDisco;
	private JPanel pnlRam;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MasInformacionProducto dialog = new MasInformacionProducto(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 * @param producto 
	 */
	public MasInformacionProducto(Producto producto) {
		Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color Rojito = new Color(250, 128, 114);
		MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);
		
		
		if (producto != null) {
			setTitle("Información: "+producto.getId());			
		}
		setBounds(100, 100, 498, 306);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 255, 240));
		contentPanel.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		contentPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBackground(new Color(240, 255, 240));
			panel.setBorder(new TitledBorder(null, "Información General", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.setBounds(4, 4, 474, 120);
			contentPanel.add(panel);
			panel.setLayout(null);
			{
				JLabel lblNewLabel_1 = new JLabel("ID:");
				lblNewLabel_1.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_1.setBounds(15, 28, 46, 22);
				panel.add(lblNewLabel_1);
			}
			{
				txtID = new JTextField();
				txtID.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtID.setEditable(false);
				txtID.setBounds(80, 25, 148, 20);
				txtID.setBackground(CyanClaro);
				txtID.setBorder(bottomBorder);
				panel.add(txtID);
				txtID.setColumns(10);
			}
			{
				JLabel lblNewLabel_2 = new JLabel("Num. Serie:");
				lblNewLabel_2.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_2.setBounds(247, 24, 97, 22);
				panel.add(lblNewLabel_2);
			}
			{
				txtNumSerie = new JTextField();
				txtNumSerie.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtNumSerie.setEditable(false);
				txtNumSerie.setBounds(330, 25, 132, 20);
				txtNumSerie.setBackground(CyanClaro);
				txtNumSerie.setBorder(bottomBorder);
				panel.add(txtNumSerie);
				txtNumSerie.setColumns(10);
			}
			{
				JLabel lblNewLabel_3 = new JLabel("Marca:");
				lblNewLabel_3.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_3.setBounds(15, 60, 46, 22);
				panel.add(lblNewLabel_3);
			}
			{
				txtMarca = new JTextField();
				txtMarca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtMarca.setEditable(false);
				txtMarca.setBounds(80, 57, 148, 20);
				txtMarca.setBackground(CyanClaro);
				txtMarca.setBorder(bottomBorder);
				panel.add(txtMarca);
				txtMarca.setColumns(10);
			}
			{
				JLabel lblNewLabel_4 = new JLabel("Proveedor:");
				lblNewLabel_4.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_4.setBounds(247, 56, 87, 22);
				panel.add(lblNewLabel_4);
			}
			{
				txtProovedor = new JTextField();
				txtProovedor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtProovedor.setEditable(false);
				txtProovedor.setBounds(330, 57, 132, 20);
				txtProovedor.setBorder(bottomBorder);
				txtProovedor.setBackground(CyanClaro);
				panel.add(txtProovedor);
				txtProovedor.setColumns(10);
			}
			{
				JLabel lblNewLabel_5 = new JLabel("Cantidad:");
				lblNewLabel_5.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_5.setBounds(15, 88, 69, 22);
				panel.add(lblNewLabel_5);
			}
			{
				txtCantidad = new JTextField();
				txtCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtCantidad.setEditable(false);
				txtCantidad.setBounds(80, 89, 148, 20);
				txtCantidad.setBackground(CyanClaro);
				txtCantidad.setBorder(bottomBorder);
				panel.add(txtCantidad);
				txtCantidad.setColumns(10);
			}
			{
				JLabel lblNewLabel_6 = new JLabel("Precio:");
				lblNewLabel_6.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_6.setBounds(247, 88, 59, 22);
				panel.add(lblNewLabel_6);
			}
			{
				txtPrecio = new JTextField();
				txtPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtPrecio.setEditable(false);
				txtPrecio.setBounds(329, 89, 132, 20);
				txtPrecio.setBorder(bottomBorder);
				txtPrecio.setBackground(CyanClaro);
				panel.add(txtPrecio);
				txtPrecio.setColumns(10);
			}
		}
		{
			pnlMicro = new JPanel();
			pnlMicro.setBackground(new Color(240, 255, 240));
			pnlMicro.setBorder(new TitledBorder(null, "Microprocesador", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlMicro.setBounds(4, 125, 474, 90);
			contentPanel.add(pnlMicro);
			pnlMicro.setLayout(null);
			{
				JLabel lblNewLabel_8 = new JLabel("Modelo:");
				lblNewLabel_8.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_8.setBounds(15, 29, 60, 14);
				pnlMicro.add(lblNewLabel_8);
			}
			{
				txtModeloMicro = new JTextField();
				txtModeloMicro.setEditable(false);
				txtModeloMicro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtModeloMicro.setBounds(80, 25, 145, 20);
				txtModeloMicro.setBackground(CyanClaro);
				txtModeloMicro.setBorder(bottomBorder);
				pnlMicro.add(txtModeloMicro);
				txtModeloMicro.setColumns(10);
			}
			{
				JLabel lblNewLabel_9 = new JLabel("Socket:");
				lblNewLabel_9.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_9.setBounds(260, 29, 60, 14);
				pnlMicro.add(lblNewLabel_9);
			}
			{
				txtSocketMicro = new JTextField();
				txtSocketMicro.setEditable(false);
				txtSocketMicro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtSocketMicro.setBounds(317, 25, 145, 20);
				txtSocketMicro.setBackground(CyanClaro);
				txtSocketMicro.setBorder(bottomBorder);
				pnlMicro.add(txtSocketMicro);
				txtSocketMicro.setColumns(10);
			}
			{
				JLabel lblNewLabel_10 = new JLabel("Velocidad de Procesamiento:");
				lblNewLabel_10.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_10.setBounds(15, 60, 200, 14);
				pnlMicro.add(lblNewLabel_10);
			}
			{
				txtVeProMicro = new JTextField();
				txtVeProMicro.setEditable(false);
				txtVeProMicro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
				txtVeProMicro.setBounds(215, 57, 187, 20);
				txtVeProMicro.setBackground(CyanClaro);
				txtVeProMicro.setBorder(bottomBorder);
				pnlMicro.add(txtVeProMicro);
				txtVeProMicro.setColumns(10);
			}
		}
		{
			pnlMother = new JPanel();
			pnlMother.setBackground(new Color(240, 255, 240));
			pnlMother.setBorder(new TitledBorder(null, "MotherBoard", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlMother.setBounds(4, 125, 474, 90);
			contentPanel.add(pnlMother);
			pnlMother.setLayout(null);
			{
				JLabel lblNewLabel_12 = new JLabel("Modelo:");
				lblNewLabel_12.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_12.setBounds(15, 28, 60, 14);
				pnlMother.add(lblNewLabel_12);
			}
			{
				txtModeloMother = new JTextField();
				txtModeloMother.setEditable(false);
				txtModeloMother.setBounds(87, 25, 141, 20);
				txtModeloMother.setBackground(CyanClaro);
				txtModeloMother.setBorder(bottomBorder);
				pnlMother.add(txtModeloMother);
				txtModeloMother.setColumns(10);
			}
			{
				JLabel lblNewLabel_13 = new JLabel("Tipo Ram:");
				lblNewLabel_13.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_13.setBounds(15, 60, 86, 14);
				pnlMother.add(lblNewLabel_13);
			}
			{
				txtTipoRamMother = new JTextField();
				txtTipoRamMother.setEditable(false);
				txtTipoRamMother.setBounds(87, 57, 105, 20);
				txtTipoRamMother.setBackground(CyanClaro);
				txtTipoRamMother.setBorder(bottomBorder);
				pnlMother.add(txtTipoRamMother);
				txtTipoRamMother.setColumns(10);
			}
			{
				JLabel lblNewLabel_14 = new JLabel("Socket:");
				lblNewLabel_14.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_14.setBounds(260, 28, 60, 14);
				pnlMother.add(lblNewLabel_14);
			}
			{
				txtSocketMother = new JTextField();
				txtSocketMother.setEditable(false);
				txtSocketMother.setBounds(317, 25, 145, 20);
				txtSocketMother.setBackground(CyanClaro);
				txtSocketMother.setBorder(bottomBorder);
				pnlMother.add(txtSocketMother);
				txtSocketMother.setColumns(10);
			}
			{
				JLabel lblNewLabel_15 = new JLabel("Discos Aceptados:");
				lblNewLabel_15.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_15.setBounds(215, 60, 118, 14);
				pnlMother.add(lblNewLabel_15);
			}
			{
				txtDiscoMother = new JTextField();
				txtDiscoMother.setEditable(false);
				txtDiscoMother.setBounds(344, 57, 118, 20);
				txtDiscoMother.setBackground(CyanClaro);
				txtDiscoMother.setBorder(bottomBorder);
				pnlMother.add(txtDiscoMother);
				txtDiscoMother.setColumns(10);
			}
		}
		{
			pnlRam = new JPanel();
			pnlRam.setBackground(new Color(240, 255, 240));
			pnlRam.setBorder(new TitledBorder(null, "Memoria RAM", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlRam.setBounds(4, 125, 474, 60);
			contentPanel.add(pnlRam);
			pnlRam.setLayout(null);
			{
				JLabel lblNewLabel_17 = new JLabel("Cantidad:");
				lblNewLabel_17.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_17.setBounds(10, 28, 65, 14);
				pnlRam.add(lblNewLabel_17);
			}
			{
				txtCantRam = new JTextField();
				txtCantRam.setEditable(false);
				txtCantRam.setBounds(85, 25, 146, 20);
				txtCantRam.setBackground(CyanClaro);
				txtCantRam.setBorder(bottomBorder);
				pnlRam.add(txtCantRam);
				txtCantRam.setColumns(10);
			}
			{
				JLabel lblNewLabel_18 = new JLabel("Tipo:");
				lblNewLabel_18.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_18.setBounds(255, 28, 46, 14);
				pnlRam.add(lblNewLabel_18);
			}
			{
				txtTipoRam = new JTextField();
				txtTipoRam.setEditable(false);
				txtTipoRam.setBounds(299, 25, 163, 20);
				txtTipoRam.setBackground(CyanClaro);
				txtTipoRam.setBorder(bottomBorder);
				pnlRam.add(txtTipoRam);
				txtTipoRam.setColumns(10);
			}
		}
		{
			pnlDisco = new JPanel();
			pnlDisco.setBackground(new Color(240, 255, 240));
			pnlDisco.setBorder(new TitledBorder(null, "Disco Duro", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlDisco.setBounds(4, 125, 474, 90);
			contentPanel.add(pnlDisco);
			pnlDisco.setLayout(null);
			{
				JLabel lblNewLabel_20 = new JLabel("Modelo:");
				lblNewLabel_20.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_20.setBounds(10, 28, 55, 14);
				pnlDisco.add(lblNewLabel_20);
			}
			{
				JLabel lblNewLabel_21 = new JLabel("Tipo Conexion:");
				lblNewLabel_21.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_21.setBounds(245, 28, 100, 14);
				pnlDisco.add(lblNewLabel_21);
			}
			{
				JLabel lblNewLabel_22 = new JLabel("Capacidad de Almacenamiento:");
				lblNewLabel_22.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				lblNewLabel_22.setBounds(10, 60, 200, 14);
				pnlDisco.add(lblNewLabel_22);
			}
			{
				txtConexionDisco = new JTextField();
				txtConexionDisco.setEditable(false);
				txtConexionDisco.setBounds(350, 24, 112, 20);
				txtConexionDisco.setBackground(CyanClaro);
				txtConexionDisco.setBorder(bottomBorder);
				pnlDisco.add(txtConexionDisco);
				txtConexionDisco.setColumns(10);
			}
			{
				txtModeloDisco = new JTextField();
				txtModeloDisco.setEditable(false);
				txtModeloDisco.setBounds(77, 25, 149, 20);
				txtModeloDisco.setBackground(CyanClaro);
				txtModeloDisco.setBorder(bottomBorder);
				pnlDisco.add(txtModeloDisco);
				txtModeloDisco.setColumns(10);
			}
			{
				txtCapAlmDisco = new JTextField();
				txtCapAlmDisco.setEditable(false);
				txtCapAlmDisco.setBounds(225, 57, 237, 20);
				txtCapAlmDisco.setBackground(CyanClaro);
				txtCapAlmDisco.setBorder(bottomBorder);
				pnlDisco.add(txtCapAlmDisco);
				txtCapAlmDisco.setColumns(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(240, 255, 240));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnVisualizar = new JButton("Visualizar el Producto");
				btnVisualizar.setForeground(new Color(255, 255, 255));
				btnVisualizar.setBackground(CyanMid);
				btnVisualizar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						VisualizacionProducto visualiza =new VisualizacionProducto(producto);
						visualiza.setVisible(true);
					}
				});
				btnVisualizar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				btnVisualizar.setActionCommand("OK");
				buttonPane.add(btnVisualizar);
				getRootPane().setDefaultButton(btnVisualizar);
			}
			{
				JButton btnCancelar = new JButton("Cancelar");
				btnCancelar.setForeground(new Color(255, 255, 255));
				btnCancelar.setBackground(Rojito);
				btnCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCancelar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
				btnCancelar.setActionCommand("Cancel");
				buttonPane.add(btnCancelar);
			}
		}
		load(producto);
	}
	private void load(Producto producto) {
	    if (producto != null) {
	        
	        txtID.setText(producto.getId());
	        txtNumSerie.setText(producto.getNumSerie());
	        txtMarca.setText(producto.getMarca());
	        txtProovedor.setText(String.valueOf(producto.getProveedor()));
	        txtCantidad.setText(String.valueOf(producto.getCantDisponible()));
	        txtPrecio.setText(String.valueOf(producto.getPrecio()));
	        
	        if (producto instanceof Microprocesador) {
	            Microprocesador micro = (Microprocesador) producto;
	            pnlDisco.setEnabled(false);
	            pnlMicro.setEnabled(true);
	            pnlMother.setEnabled(false);
	            pnlRam.setEnabled(false);
	            txtModeloMicro.setText(micro.getModelo());
	            txtSocketMicro.setText(micro.getSocket());
	            txtVeProMicro.setText(String.valueOf(micro.getVelocidadProcesamiento()));
	        } else if (producto instanceof MotherBoard) {
	        	MotherBoard mother = (MotherBoard) producto;
	            pnlDisco.setVisible(false);
	            pnlMicro.setVisible(false);
	            pnlMother.setVisible(true);
	            pnlRam.setVisible(false);
	            txtModeloMother.setText(mother.getModelo());
	            txtTipoRamMother.setText(mother.getTipoRam());
	            txtSocketMother.setText(mother.getTipoSocket());
	            String discosAceptados = String.join(", ", mother.getListaDiscoDuroAceptados());
	            txtDiscoMother.setText(discosAceptados);
	        } else if (producto instanceof MemoriaRam) {
	            MemoriaRam ram = (MemoriaRam) producto;
	            pnlDisco.setVisible(false);
	            pnlMicro.setVisible(false);
	            pnlMother.setVisible(false);
	            pnlRam.setVisible(true);
	            txtCantRam.setText(String.valueOf(ram.getCantMemoria()));
	            txtTipoRam.setText(ram.getTipoMemoria());
	        } else if (producto instanceof DiscoDuro) {
	            DiscoDuro disco = (DiscoDuro) producto;
	            pnlDisco.setVisible(true);
	            pnlMicro.setVisible(false);
	            pnlMother.setVisible(false);
	            pnlRam.setVisible(false);
	            txtConexionDisco.setText(disco.getTipoConexion());
	            txtModeloDisco.setText(disco.getModelo());
	            txtCapAlmDisco.setText(String.valueOf(disco.getCapacidad()));
	        }
	    
	    }
	}
}

