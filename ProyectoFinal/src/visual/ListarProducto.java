//.
package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logico.DiscoDuro;
import logico.MemoriaRam;
import logico.Microprocesador;
import logico.MotherBoard;
import logico.Producto;
import logico.Tienda;

public class ListarProducto extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel tableModel;
	private JButton btnVerMas;
	private JButton botonActualizar;
	private JButton botonEliminar;
	private String codigo = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ListarProducto dialog = new ListarProducto();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListarProducto() {
		Color CyanMid = new Color(80, 180, 152);
		Color Rojito = new Color(250, 128, 114);

		setIconImage(Toolkit.getDefaultToolkit().getImage(ListarProducto.class.getResource("/Imagenes/to-do-list.png")));
		setTitle("Lista de Productos");
		setBounds(100, 100, 1050, 505);
		setLocationRelativeTo(null);
		setResizable(false);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setBackground(new Color(240, 255, 240));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		String[] columnas = {"ID", "NO. Serie", "Tipo", "Cantidad", "Proveedor", "Precio"};
		tableModel = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);
		table.setBackground(new Color(240, 255, 240));
		table.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		table.getTableHeader().setFont(new Font("Bahnschrift", Font.PLAIN, 14));

		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
		}

		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = table.getSelectedRow();
				if (index >= 0) {
					codigo = table.getValueAt(index, 0).toString();
					btnVerMas.setEnabled(true);
					botonEliminar.setEnabled(true);
					botonActualizar.setEnabled(true);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 255, 240));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		botonActualizar = new JButton("Actualizar");
		botonActualizar.setForeground(new Color(255, 255, 255));
		botonActualizar.setBackground(CyanMid);
		botonActualizar.setEnabled(false);
		botonActualizar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		botonActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!codigo.isEmpty()) {
					Producto aux = Tienda.getInstance().buscarProductoId(codigo);
					if (aux != null) {
						RegistrarProducto updatePublicacion = new RegistrarProducto(aux);
						updatePublicacion.setModal(true);
						updatePublicacion.setVisible(true);

						cargarProducto();
						table.clearSelection();
						table.revalidate();
						table.repaint();

						btnVerMas.setEnabled(false);
						botonActualizar.setEnabled(false);
						botonEliminar.setEnabled(false);
						codigo = "";
					}
				}
			}
		});

		btnVerMas = new JButton("Mas Informacion");
		btnVerMas.setForeground(new Color(255, 255, 255));
		btnVerMas.setBackground(CyanMid);
		btnVerMas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String id = (String) tableModel.getValueAt(selectedRow, 0);
					Producto pro = Tienda.getInstance().buscarProductoId(id);
					if (pro != null) {
						MasInformacionProducto mas = new MasInformacionProducto(pro);
						mas.setModal(true);
						mas.setVisible(true);
					}
				}
			}
		});
		btnVerMas.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		btnVerMas.setEnabled(false);
		buttonPane.add(btnVerMas);

		botonActualizar.setActionCommand("OK");
		buttonPane.add(botonActualizar);
		getRootPane().setDefaultButton(botonActualizar);

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.setForeground(new Color(255, 255, 255));
		cancelButton.setBackground(CyanMid);
		cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> dispose());

		botonEliminar = new JButton("Eliminar");
		botonEliminar.setForeground(new Color(255, 255, 255));
		botonEliminar.setBackground(Rojito);
		botonEliminar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
		botonEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!codigo.isEmpty()) {
					ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
					String texto = "¿Seguro desea eliminar el producto con código: " + codigo + "?";
					VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
					ventanita.setModal(true);
					ventanita.setVisible(true);
					int option = ventanita.getResultado();

					if (option == JOptionPane.YES_OPTION) {
						Tienda.getInstance().eliminarProducto(codigo);

						cargarProducto();
						table.clearSelection();
						table.revalidate();
						table.repaint();

						btnVerMas.setEnabled(false);
						botonEliminar.setEnabled(false);
						botonActualizar.setEnabled(false);
						codigo = "";

						ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
						MensajeAlerta mensajito = new MensajeAlerta(iconito, "Producto eliminado correctamente.");
						mensajito.setModal(true);
						mensajito.setVisible(true);
					} else {
						ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
						MensajeAlerta mensajito = new MensajeAlerta(iconito, "Eliminación cancelada.");
						mensajito.setModal(true);
						mensajito.setVisible(true);
					}
				}
			}
		});
		botonEliminar.setEnabled(false);
		buttonPane.add(botonEliminar);
		buttonPane.add(cancelButton);

		cargarProducto();
	}

	public void cargarProducto() {
		tableModel.setRowCount(0);

		for (Producto producto : Tienda.getInstance().getListaProductos()) {
			Object[] row = new Object[tableModel.getColumnCount()];

			row[0] = producto.getId();
			row[1] = producto.getNumSerie();

			if (producto instanceof MotherBoard) {
				row[2] = "MotherBoard";
			} else if (producto instanceof MemoriaRam) {
				row[2] = "Memoria RAM";
			} else if (producto instanceof Microprocesador) {
				row[2] = "Microprocesador";
			} else if (producto instanceof DiscoDuro) {
				row[2] = "Disco Duro";
			} else {
				row[2] = "Producto";
			}

			row[3] = producto.getCantDisponible();

			if (producto.getProveedor() != null) {
				row[4] = producto.getProveedor().getId();
			} else {
				row[4] = "Vacío";
			}

			row[5] = producto.getPrecio();

	
			tableModel.addRow(row);
		}

		tableModel.fireTableDataChanged();
	}
}