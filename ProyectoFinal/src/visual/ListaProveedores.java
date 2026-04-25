//.
package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.TiendaComputos;
import logico.Persona;
import logico.Proveedor;
import logico.Tienda;
import java.awt.Dimension;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ListaProveedores extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton bottonActualizar;
    private JButton eliminarbotton;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ListaProveedores dialog = new ListaProveedores();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ListaProveedores() {
        setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        setTitle("Lista de Proveedores");
        setBounds(100, 100, 600, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 255, 240));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null); /*Poner en el centro*/
        setResizable(false);
        
		Color CyanMid = new Color(80, 180, 152);

        String[] columnas = {"ID", "Nombre", "Cedula", "Correo", "Empresa"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);
        table.setBackground(new Color(240, 255, 240));
        table.setBorder(null);
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setFont(new Font("Bahnschrift", Font.BOLD, 14));
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
                    bottonActualizar.setEnabled(true);
                    eliminarbotton.setEnabled(true);
                } else {
                    bottonActualizar.setEnabled(false);
                    eliminarbotton.setEnabled(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        cargarDatosProveedor();

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setBackground(new Color(240, 255, 240));
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                eliminarbotton = new JButton("Eliminar");
                eliminarbotton.setForeground(new Color(255, 255, 255));
                eliminarbotton.setEnabled(false);
                eliminarbotton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                eliminarbotton.setBackground(new Color(250, 128, 114));
                eliminarbotton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            eliminarProveedorSeleccionado();
                            actualizarTabla();
                        }
                    }
                });
                bottonActualizar = new JButton("Actualizar");
                bottonActualizar.setForeground(new Color(255, 255, 255));
                bottonActualizar.setEnabled(false);
                bottonActualizar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                bottonActualizar.setBackground(CyanMid);
                bottonActualizar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            String id = (String) tableModel.getValueAt(selectedRow, 0);
                            Proveedor proveedor = (Proveedor) Tienda.getInstance().buscarPersonaId(id);
                            if (proveedor != null) {
                                RegistrarProveedor registrarProveedorDialog = new RegistrarProveedor(proveedor);
                                registrarProveedorDialog.setModal(true);
                                registrarProveedorDialog.setVisible(true);
                                
                                actualizarTabla();
                            }
                        }
                    }
                });
                bottonActualizar.setActionCommand("OK");
                buttonPane.add(bottonActualizar);
                getRootPane().setDefaultButton(bottonActualizar);
                buttonPane.add(eliminarbotton);
            }
            {
                JButton cancelButton = new JButton("Cancelar");
                cancelButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		dispose();
                	}
                });
                cancelButton.setForeground(new Color(255, 255, 255));
                cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                cancelButton.setBackground(CyanMid);
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
    }

    private void cargarDatosProveedor() {
        ArrayList<Persona> listaPersonas = Tienda.getInstance().getListaPersonas();
        for (Persona persona : listaPersonas) {
            if (persona instanceof Proveedor) {
                Proveedor proveedor = (Proveedor) persona;
                Object[] row = {
                    proveedor.getId(),
                    proveedor.getNombre(),
                    proveedor.getCedula(),
                    proveedor.getCorreo(),
                    proveedor.getEmpresa()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0); 
        cargarDatosProveedor(); 
    }
    
    private void eliminarProveedorSeleccionado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String idProveedor = (String) tableModel.getValueAt(selectedRow, 0);
            //int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estas seguro de que deseas eliminar este proveedor?", "Confirmacion de eliminacion", JOptionPane.YES_NO_OPTION);
            ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
            String texto = "¿Estás seguro de que deseas eliminar el proveedor con código: " +idProveedor+ "?";
            VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
            ventanita.setModal(true);
            ventanita.setVisible(true);
			int confirmacion = ventanita.getResultado();
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                Tienda.getInstance().eliminarPersona(idProveedor);
                TiendaComputos.getInstance().eliminarPorCodigo(idProveedor, "Proveedor");
                tableModel.removeRow(selectedRow);
                //JOptionPane.showMessageDialog(this, "Proveedor eliminado correctamente.");
                ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
    			MensajeAlerta mensajito = new MensajeAlerta(iconito, "Proveedor eliminado correctamente.");
    			mensajito.setModal(true);
    			mensajito.setVisible(true);
            } else {
                //JOptionPane.showMessageDialog(this, "Eliminacion cancelada.");
            	ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
    			MensajeAlerta mensajito = new MensajeAlerta(iconito, "Eliminación cancelada.");
    			mensajito.setModal(true);
    			mensajito.setVisible(true);
            }
        } else {
            //JOptionPane.showMessageDialog(this, "Por favor, seleccione un proveedor para eliminar.");
        	ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/alert.png"));
			MensajeAlerta mensajito = new MensajeAlerta(iconito, "Por favor, seleccione un proveedor para eliminar.");
			mensajito.setModal(true);
			mensajito.setVisible(true);
        }
    }
}
