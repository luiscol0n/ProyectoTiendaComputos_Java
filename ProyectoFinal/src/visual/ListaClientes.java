//.
package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logico.Cliente;
import logico.Persona;
import logico.Tienda;
import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Color;
import java.awt.Toolkit;

public class ListaClientes extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton botonActualizar;
    private JButton botonEliminar;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ListaClientes dialog = new ListaClientes();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ListaClientes() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(ListaClientes.class.getResource("/Imagenes/to-do-list.png")));
        setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        setTitle("Lista de Clientes");
        setBounds(100, 100, 600, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 255, 240));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null); /*Poner en el centro*/
        setResizable(false);
        
        Color CyanOscuro = new Color(70, 133, 133);
		Color CyanMid = new Color(80, 180, 152);
		Color CyanClaro =  new Color (222, 249, 196);
		Color FondoClarito = new Color(240, 255, 240);

        String[] columnas = {"ID", "Nombre", "Cedula", "Correo", "Clasificacion"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);
        table.setBackground(new Color(240, 255, 240));
        table.setBorder(null);
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        table.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        table.setPreferredScrollableViewportSize(new Dimension(500, 300));
        table.setFillsViewportHeight(true);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    botonActualizar.setEnabled(true);
                    botonEliminar.setEnabled(true);
                } else {
                    botonActualizar.setEnabled(false);
                    botonEliminar.setEnabled(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        cargarDatosCliente();

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setBackground(new Color(240, 255, 240));
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                botonEliminar = new JButton("Eliminar");
                botonEliminar.setForeground(new Color(255, 255, 255));
                botonEliminar.setEnabled(false);
                botonEliminar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                botonEliminar.setBackground(new Color(250, 128, 114));
                botonEliminar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            eliminarClienteSeleccionado();
                            actualizarTabla();
                        }
                    }
                });
                botonActualizar = new JButton("Actualizar");
                botonActualizar.setForeground(new Color(255, 255, 255));
                botonActualizar.setEnabled(false);
                botonActualizar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                botonActualizar.setBackground(CyanMid);
                botonActualizar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            String id = (String) tableModel.getValueAt(selectedRow, 0);
                            Cliente cliente = (Cliente) Tienda.getInstance().buscarPersonaId(id);
                            if (cliente != null) {
                                RegistrarCliente registrarClienteDialog = new RegistrarCliente(cliente);
                                registrarClienteDialog.setModal(true);
                                registrarClienteDialog.setVisible(true);
                                actualizarTabla();
                            }
                        }
                    }
                });
                botonActualizar.setActionCommand("OK");
                buttonPane.add(botonActualizar);
                getRootPane().setDefaultButton(botonActualizar);
                buttonPane.add(botonEliminar);
            }
            {
                JButton cancelButton = new JButton("Cancelar");
                cancelButton.setForeground(new Color(255, 255, 255));
                cancelButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent arg0) {
                		dispose();
                	}
                });
                cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                cancelButton.setActionCommand("Cancel");
                cancelButton.setBackground(CyanMid);
                buttonPane.add(cancelButton);
            }
        }
    }

    private void cargarDatosCliente() {
        ArrayList<Persona> listaPersonas = Tienda.getInstance().getListaPersonas();
        for (Persona persona : listaPersonas) {
            if (persona instanceof Cliente) {
                Cliente cliente = (Cliente) persona;
                Object[] row = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getCedula(),
                    cliente.getCorreo(),
                    cliente.getClasificacion()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0); 
        cargarDatosCliente(); 
    }
    
    private void eliminarClienteSeleccionado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String idCliente = (String) tableModel.getValueAt(selectedRow, 0);
            ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
            String texto = "¿Estás seguro de que deseas eliminar el cliente con código: "+idCliente+"?";
            VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
            ventanita.setModal(true);
            ventanita.setVisible(true);
			int confirmacion = ventanita.getResultado();
            if (confirmacion == JOptionPane.YES_OPTION) {
                Tienda.getInstance().eliminarPersona(idCliente);
                tableModel.removeRow(selectedRow);
                ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
				MensajeAlerta mensajito = new MensajeAlerta(iconito, "Cliente eliminado correctamente.");
				mensajito.setModal(true);
				mensajito.setVisible(true);
            } else {
            	ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
				MensajeAlerta mensajito = new MensajeAlerta(iconito, "Eliminación cancelada.");
				mensajito.setModal(true);
				mensajito.setVisible(true);
            }
        } else {
            ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
			MensajeAlerta mensajito = new MensajeAlerta(iconito, "Por favor, seleccione un cliente para eliminar.");
			mensajito.setModal(true);
			mensajito.setVisible(true);
        
        }
    }
}
