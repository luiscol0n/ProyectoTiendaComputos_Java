package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import database.TiendaComputos;
import logico.Empleado;
import logico.Persona;
import logico.Tienda;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.SystemColor;

public class ListaEmpleados extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton botonActualizar;
    private JButton btnEliminar;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            ListaEmpleados dialog = new ListaEmpleados();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public ListaEmpleados() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(ListaEmpleados.class.getResource("/Imagenes/listaClientes.png")));
        setTitle("Lista de Empleados");
        setBounds(100, 100, 919, 505);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(SystemColor.inactiveCaptionBorder);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null); /*Poner en el centro*/
        setResizable(false);
        
		Color CyanMid = new Color(80, 180, 152);
		Color Rojito = new Color(250, 128, 114);

        String[] columnas = {"ID", "Nombre", "Cédula", "Correo", "Comisión por Ventas", "Empleado del Mes"};
        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Bahnschrift", Font.PLAIN, 14));
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        table.setBackground(SystemColor.inactiveCaptionBorder);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);

        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    botonActualizar.setEnabled(true);
                    btnEliminar.setEnabled(true);
                } else {
                    botonActualizar.setEnabled(false);
                    btnEliminar.setEnabled(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        cargarDatosEmpleado();

        {
            JPanel buttonPane = new JPanel();
            buttonPane.setBackground(Color.LIGHT_GRAY);
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                botonActualizar = new JButton("Actualizar");
                botonActualizar.setForeground(SystemColor.desktop);
                botonActualizar.setEnabled(false);
                botonActualizar.setBackground(SystemColor.text);
                botonActualizar.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            String id = (String) tableModel.getValueAt(selectedRow, 0);
                            Empleado empleado = (Empleado) Tienda.getInstance().buscarPersonaId(id);
                            if (empleado != null) {
                                RegistrarEmpleado registrarEmpleadoDialog = new RegistrarEmpleado(empleado);
                                registrarEmpleadoDialog.setModal(true);
                                registrarEmpleadoDialog.setVisible(true);

                                actualizarTabla();
                            }
                        }
                    }
                });
                botonActualizar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                botonActualizar.setActionCommand("OK");
                buttonPane.add(botonActualizar);
                getRootPane().setDefaultButton(botonActualizar);
            }
            {
                JButton cancelButton = new JButton("Cancelar");
                cancelButton.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent arg0) {
                		dispose();
                	}
                });
                cancelButton.setForeground(SystemColor.desktop);
                cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                cancelButton.setActionCommand("Cancel");
                cancelButton.setBackground(SystemColor.text);
                
                {
                    btnEliminar = new JButton("Eliminar");
                    btnEliminar.setForeground(SystemColor.desktop);
                    btnEliminar.setEnabled(false);
                    btnEliminar.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                    btnEliminar.setBackground(SystemColor.text);
                    btnEliminar.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            eliminarEmpleadoSeleccionado();
                        }
                    });
                    buttonPane.add(btnEliminar);
                }
                buttonPane.add(cancelButton);
            }
        }
    }

    private void cargarDatosEmpleado() {
        ArrayList<Persona> listaPersonas = Tienda.getInstance().getListaPersonas();
        for (Persona persona : listaPersonas) {
            if (persona instanceof Empleado) {
                Empleado empleado = (Empleado) persona;
                Object[] row = {
                    empleado.getId(),
                    empleado.getNombre(),
                    empleado.getCedula(),
                    empleado.getCorreo(),
                    String.format("%.2f%%", empleado.getComisionVentas() * 100),
                    empleado.isEmpleadoMes() ? "Sí" : "No"
                };
                tableModel.addRow(row);
            }
        }
    }

    private void eliminarEmpleadoSeleccionado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String idEmpleado = (String) tableModel.getValueAt(selectedRow, 0);
            ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
            String texto = "¿Estás seguro de que deseas eliminar el empleado con código: "+idEmpleado+"?";
            VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
            ventanita.setModal(true);
            ventanita.setVisible(true);
			int confirmacion = ventanita.getResultado();
            if (confirmacion == JOptionPane.YES_OPTION) {
                Tienda.getInstance().eliminarPersona(idEmpleado);
                TiendaComputos.getInstance().eliminarPorCodigo(idEmpleado, "Empleado");
                tableModel.removeRow(selectedRow);
                ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
				MensajeAlerta mensajito = new MensajeAlerta(iconito, "Empleado eliminado correctamente.");
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
			MensajeAlerta mensajito = new MensajeAlerta(iconito, "Por favor, seleccione un empleado para eliminar.");
			mensajito.setModal(true);
			mensajito.setVisible(true);
        }
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0); 
        cargarDatosEmpleado();
    }
}
