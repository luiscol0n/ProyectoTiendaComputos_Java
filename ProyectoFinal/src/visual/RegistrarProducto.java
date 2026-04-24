//.
package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import logico.DiscoDuro;
import logico.FacturaCompra;
import logico.MemoriaRam;
import logico.Microprocesador;
import logico.MotherBoard;
import logico.Persona;
import logico.Producto;
import logico.Proveedor;
import logico.Tienda;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.SpinnerNumberModel;
import javax.swing.JRadioButton;
import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.Toolkit;

public class RegistrarProducto extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtMarca;
    private JTextField txtNumSerie;
    private JComboBox cbxProveedor;
    private DefaultComboBoxModel<Proveedor> proveedoresRegistrados = new DefaultComboBoxModel<Proveedor>();
    private JSpinner spnCantidad;
    private JRadioButton rbtnMotherBoard;
    private JRadioButton rbtnMemoriaRAM;
    private JRadioButton rbtnMicroprocesador;
    private JRadioButton rbtnDiscoDuro;
    private JPanel pnlMotherBoard;
    private JTextField txtMBModelo;
    private JTextField txtMBSocket;
    private JCheckBox[] chkDiscosAceptados;
    private JPanel pnlMemoriaRAM;
    private JSpinner spnMRCantidad;
    private JComboBox<String> cbxMRTipo;
    private JTextField txtMPModelo;
    private JTextField txtMPSocket;
    private JPanel pnlMicroprocesador;
    private JSpinner spnMPVelocidadProcesamiento;
    private JTextField txtDDModelo;
    private JPanel pnlDiscoDuro;
    private JSpinner spnPrecio;
    private JComboBox cbxMBTipoRam;
    private JComboBox cbxDDTipoConexion;
    private JSpinner spnDDCapacidadAlmacenamiento;
    private String codigo = "";

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            RegistrarProducto dialog = new RegistrarProducto(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * @wbp.parser.constructor
     */
    public RegistrarProducto(Producto producto) {
        this(producto, false);
    }

    
    public RegistrarProducto(Producto producto, boolean soloLectura) {
        setIconImage(Toolkit.getDefaultToolkit().getImage(RegistrarProducto.class.getResource("/Imagenes/registerproduct.png")));

        Color CyanOscuro = new Color(70, 133, 133);
        Color CyanMid = new Color(80, 180, 152);
        Color CyanClaro = new Color(222, 249, 196);
        Color FondoClarito = new Color(240, 255, 240);
        Color Rojito = new Color(250, 128, 114);
        MatteBorder bottomBorder = new MatteBorder(0, 0, 2, 0, CyanOscuro);

        if (soloLectura) {
            setTitle("Detalle del Producto");
        } else if (producto != null) {
            setTitle("Actualizar Producto");
            codigo = producto.getId();
        } else {
            setTitle("Registrar Producto");
        }

        setBounds(100, 100, 570, 400);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(240, 255, 240));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        {
            JPanel panel = new JPanel();
            panel.setBackground(new Color(240, 255, 240));
            panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            contentPanel.add(panel, BorderLayout.CENTER);
            panel.setLayout(null);

            JPanel panel_1 = new JPanel();
            panel_1.setBackground(new Color(240, 255, 240));
            panel_1.setBorder(new TitledBorder(null, "Información General", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            panel_1.setBounds(4, 4, 534, 127);
            panel_1.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            panel.add(panel_1);
            panel_1.setLayout(null);

            JLabel lblId = new JLabel("ID:");
            lblId.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblId.setBounds(15, 28, 56, 16);
            panel_1.add(lblId);

            txtId = new JTextField();
            txtId.setEditable(false);
            txtId.setBorder(bottomBorder);
            txtId.setBackground(CyanClaro);
            txtId.setBounds(85, 25, 140, 22);
            panel_1.add(txtId);
            txtId.setColumns(10);

            JLabel lblMarca = new JLabel("Marca:");
            lblMarca.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMarca.setBounds(15, 60, 56, 16);
            panel_1.add(lblMarca);

            txtMarca = new JTextField();
            txtMarca.setBackground(CyanClaro);
            txtMarca.setBorder(bottomBorder);
            txtMarca.setBounds(85, 57, 140, 22);
            panel_1.add(txtMarca);
            txtMarca.setColumns(10);

            JLabel lblCantidad = new JLabel("Cantidad:");
            lblCantidad.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblCantidad.setBounds(15, 92, 61, 16);
            panel_1.add(lblCantidad);

            spnCantidad = new JSpinner();
            spnCantidad.setForeground(new Color(255, 255, 255));
            spnCantidad.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
            spnCantidad.setBounds(85, 89, 140, 22);
            spnCantidad.setBackground(CyanClaro);
            spnCantidad.setBorder(bottomBorder);
            panel_1.add(spnCantidad);

            JLabel lblNumSerie = new JLabel("Num. Serie:");
            lblNumSerie.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblNumSerie.setBounds(238, 28, 79, 16);
            panel_1.add(lblNumSerie);

            txtNumSerie = new JTextField();
            txtNumSerie.setBounds(325, 24, 197, 22);
            txtNumSerie.setBackground(CyanClaro);
            txtNumSerie.setBorder(bottomBorder);
            panel_1.add(txtNumSerie);
            txtNumSerie.setColumns(10);

            JLabel lblProveedor = new JLabel("Proveedor:");
            lblProveedor.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblProveedor.setBounds(238, 60, 79, 16);
            panel_1.add(lblProveedor);

            cbxProveedor = new JComboBox();
            cbxProveedor.setBackground(CyanClaro);
            cbxProveedor.setBorder(bottomBorder);
            cbxProveedor.setBounds(325, 56, 197, 22);

            for (Persona persona : Tienda.getInstance().getListaPersonas()) {
                if (persona instanceof Proveedor) {
                    proveedoresRegistrados.addElement((Proveedor) persona);
                }
            }
            cbxProveedor.setModel(proveedoresRegistrados);
            panel_1.add(cbxProveedor);

            JLabel lblPrecio = new JLabel("Precio:");
            lblPrecio.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblPrecio.setBounds(238, 92, 56, 16);
            panel_1.add(lblPrecio);

            spnPrecio = new JSpinner();
            spnPrecio.setModel(new SpinnerNumberModel(new Float(0), null, null, new Float(1)));
            spnPrecio.setBounds(325, 88, 197, 22);
            spnPrecio.setBackground(CyanClaro);
            spnPrecio.setBorder(bottomBorder);
            panel_1.add(spnPrecio);

            JPanel panel_2 = new JPanel();
            panel_2.setBackground(new Color(240, 255, 240));
            panel_2.setBorder(new TitledBorder(null, "Tipo Producto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            panel_2.setBounds(4, 135, 534, 63);
            panel.add(panel_2);
            panel_2.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            panel_2.setLayout(null);

            // Solo validar proveedor si NO estamos en modo soloLectura y NO hay producto cargado
            if (!soloLectura) {
                Proveedor proveedor = null;
                if (cbxProveedor.getSelectedItem() != null) {
                    proveedor = (Proveedor) Tienda.getInstance().buscarPersonaId(cbxProveedor.getSelectedItem().toString());
                }
                if (proveedor == null) {
                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Debe seleccionar un proveedor.");
                    mensajito.setModal(true);
                    mensajito.setVisible(true);
                    return;
                }
            }

            rbtnMotherBoard = new JRadioButton("MotherBoard");
            rbtnMotherBoard.setBackground(FondoClarito);
            rbtnMotherBoard.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    rbtnMotherBoard.setSelected(true);
                    rbtnMemoriaRAM.setSelected(false);
                    rbtnMicroprocesador.setSelected(false);
                    rbtnDiscoDuro.setSelected(false);
                    pnlMotherBoard.setVisible(true);
                    pnlMemoriaRAM.setVisible(false);
                    spnMRCantidad.setVisible(false);
                    pnlMicroprocesador.setVisible(false);
                    spnMPVelocidadProcesamiento.setVisible(false);
                    pnlDiscoDuro.setVisible(false);
                    spnDDCapacidadAlmacenamiento.setVisible(false);
                }
            });
            rbtnMotherBoard.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            rbtnMotherBoard.setBounds(12, 23, 119, 25);
            rbtnMotherBoard.setSelected(true);
            panel_2.add(rbtnMotherBoard);

            rbtnMemoriaRAM = new JRadioButton("Memoria RAM");
            rbtnMemoriaRAM.setBackground(FondoClarito);
            rbtnMemoriaRAM.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rbtnMotherBoard.setSelected(false);
                    rbtnMemoriaRAM.setSelected(true);
                    rbtnMicroprocesador.setSelected(false);
                    rbtnDiscoDuro.setSelected(false);
                    pnlMotherBoard.setVisible(false);
                    pnlMemoriaRAM.setVisible(true);
                    spnMRCantidad.setVisible(true);
                    pnlMicroprocesador.setVisible(false);
                    spnMPVelocidadProcesamiento.setVisible(false);
                    pnlDiscoDuro.setVisible(false);
                    spnDDCapacidadAlmacenamiento.setVisible(false);
                }
            });
            rbtnMemoriaRAM.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            rbtnMemoriaRAM.setBounds(144, 23, 130, 25);
            panel_2.add(rbtnMemoriaRAM);

            rbtnMicroprocesador = new JRadioButton("Microprocesador");
            rbtnMicroprocesador.setBackground(FondoClarito);
            rbtnMicroprocesador.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rbtnMotherBoard.setSelected(false);
                    rbtnMemoriaRAM.setSelected(false);
                    rbtnMicroprocesador.setSelected(true);
                    rbtnDiscoDuro.setSelected(false);
                    pnlMotherBoard.setVisible(false);
                    pnlMemoriaRAM.setVisible(false);
                    spnMRCantidad.setVisible(false);
                    pnlMicroprocesador.setVisible(true);
                    spnMPVelocidadProcesamiento.setVisible(true);
                    pnlDiscoDuro.setVisible(false);
                    spnDDCapacidadAlmacenamiento.setVisible(false);
                }
            });
            rbtnMicroprocesador.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            rbtnMicroprocesador.setBounds(278, 23, 141, 25);
            panel_2.add(rbtnMicroprocesador);

            rbtnDiscoDuro = new JRadioButton("Disco Duro");
            rbtnDiscoDuro.setBackground(FondoClarito);
            rbtnDiscoDuro.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rbtnMotherBoard.setSelected(false);
                    rbtnMemoriaRAM.setSelected(false);
                    rbtnMicroprocesador.setSelected(false);
                    rbtnDiscoDuro.setSelected(true);
                    pnlMotherBoard.setVisible(false);
                    pnlMemoriaRAM.setVisible(false);
                    spnMRCantidad.setVisible(false);
                    pnlMicroprocesador.setVisible(false);
                    spnMPVelocidadProcesamiento.setVisible(false);
                    pnlDiscoDuro.setVisible(true);
                    spnDDCapacidadAlmacenamiento.setVisible(true);
                }
            });
            rbtnDiscoDuro.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            rbtnDiscoDuro.setBounds(425, 23, 101, 25);
            panel_2.add(rbtnDiscoDuro);

            pnlMotherBoard = new JPanel();
            pnlMotherBoard.setBackground(FondoClarito);
            pnlMotherBoard.setBorder(new TitledBorder(null, "Mother Board", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            pnlMotherBoard.setBounds(4, 204, 534, 93);
            panel.add(pnlMotherBoard);
            pnlMotherBoard.setLayout(null);

            JLabel lblMBModelo = new JLabel("Modelo:");
            lblMBModelo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMBModelo.setBounds(15, 28, 56, 16);
            pnlMotherBoard.add(lblMBModelo);

            txtMBModelo = new JTextField();
            txtMBModelo.setBackground(CyanClaro);
            txtMBModelo.setBorder(bottomBorder);
            txtMBModelo.setColumns(10);
            txtMBModelo.setBounds(90, 25, 178, 22);
            pnlMotherBoard.add(txtMBModelo);

            JLabel lblMBSocket = new JLabel("S\u00F3cket:");
            lblMBSocket.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMBSocket.setBounds(288, 28, 70, 16);
            pnlMotherBoard.add(lblMBSocket);

            txtMBSocket = new JTextField();
            txtMBSocket.setColumns(10);
            txtMBSocket.setBounds(348, 25, 174, 22);
            txtMBSocket.setBackground(CyanClaro);
            txtMBSocket.setBorder(bottomBorder);
            pnlMotherBoard.add(txtMBSocket);

            JLabel lblMBTipoRam = new JLabel("Tipo RAM:");
            lblMBTipoRam.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMBTipoRam.setBounds(15, 60, 81, 16);
            pnlMotherBoard.add(lblMBTipoRam);

            JLabel lblDiscosAceptados = new JLabel("Discos");
            lblDiscosAceptados.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblDiscosAceptados.setBounds(215, 58, 116, 16);
            pnlMotherBoard.add(lblDiscosAceptados);

            cbxMBTipoRam = new JComboBox();
            cbxMBTipoRam.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            cbxMBTipoRam.setBackground(CyanClaro);
            cbxMBTipoRam.setBorder(bottomBorder);
            cbxMBTipoRam.setModel(new DefaultComboBoxModel(new String[]{"<Seleccione uno>", "DDR3", "DDR4", "DDR5"}));
            cbxMBTipoRam.setBounds(90, 58, 116, 22);
            pnlMotherBoard.add(cbxMBTipoRam);

            String[] tiposDiscos = {"PATA", "SATA", "SCSI", "SSD", "NVMe"};
            chkDiscosAceptados = new JCheckBox[tiposDiscos.length];
            int chkX = 262;
            for (int i = 0; i < tiposDiscos.length; i++) {
                chkDiscosAceptados[i] = new JCheckBox(tiposDiscos[i]);
                chkDiscosAceptados[i].setFont(new Font("Bahnschrift", Font.PLAIN, 11));
                chkDiscosAceptados[i].setBackground(CyanClaro);
                chkDiscosAceptados[i].setBounds(chkX, 55, 54, 22);
                pnlMotherBoard.add(chkDiscosAceptados[i]);
                chkX += 54;
            }


            pnlMemoriaRAM = new JPanel();
            pnlMemoriaRAM.setBackground(new Color(240, 255, 240));
            pnlMemoriaRAM.setLayout(null);
            pnlMemoriaRAM.setBorder(new TitledBorder(null, "Memoria RAM", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            pnlMemoriaRAM.setBounds(4, 204, 534, 63);
            panel.add(pnlMemoriaRAM);
            pnlMemoriaRAM.setVisible(false);

            JLabel lblMRCantidad = new JLabel("Cantidad Memoria:");
            lblMRCantidad.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMRCantidad.setBounds(15, 28, 119, 16);
            pnlMemoriaRAM.add(lblMRCantidad);

            JLabel lblMRTipo = new JLabel("Tipo:");
            lblMRTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMRTipo.setBounds(310, 28, 48, 16);
            pnlMemoriaRAM.add(lblMRTipo);

            spnMRCantidad = new JSpinner();
            spnMRCantidad.setBackground(CyanClaro);
            spnMRCantidad.setBorder(bottomBorder);
            spnMRCantidad.setBounds(148, 25, 140, 22);
            pnlMemoriaRAM.add(spnMRCantidad);

            cbxMRTipo = new JComboBox();
            cbxMRTipo.setBackground(CyanClaro);
            cbxMRTipo.setBorder(bottomBorder);
            cbxMRTipo.setModel(new DefaultComboBoxModel(new String[]{"<Seleccione uno>", "DDR3", "DDR4", "DDR5"}));
            cbxMRTipo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            cbxMRTipo.setBounds(355, 25, 167, 22);
            pnlMemoriaRAM.add(cbxMRTipo);

            pnlMicroprocesador = new JPanel();
            pnlMicroprocesador.setBackground(new Color(240, 255, 240));
            pnlMicroprocesador.setLayout(null);
            pnlMicroprocesador.setBorder(new TitledBorder(null, "Microprocesador", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            pnlMicroprocesador.setBounds(4, 204, 534, 93);
            panel.add(pnlMicroprocesador);
            pnlMicroprocesador.setVisible(false);

            JLabel lblMPModelo = new JLabel("Modelo:");
            lblMPModelo.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMPModelo.setBounds(15, 28, 56, 16);
            pnlMicroprocesador.add(lblMPModelo);

            txtMPModelo = new JTextField();
            txtMPModelo.setBackground(CyanClaro);
            txtMPModelo.setBorder(bottomBorder);
            txtMPModelo.setColumns(10);
            txtMPModelo.setBounds(90, 25, 178, 22);
            pnlMicroprocesador.add(txtMPModelo);

            JLabel lblMPSocket = new JLabel("S\u00F3cket:");
            lblMPSocket.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMPSocket.setBounds(288, 28, 70, 16);
            pnlMicroprocesador.add(lblMPSocket);

            txtMPSocket = new JTextField();
            txtMPSocket.setBackground(CyanClaro);
            txtMPSocket.setBorder(bottomBorder);
            txtMPSocket.setColumns(10);
            txtMPSocket.setBounds(348, 25, 174, 22);
            pnlMicroprocesador.add(txtMPSocket);

            JLabel lblMPVelocidadProcesamiento = new JLabel("Velocidad Procesamiento:");
            lblMPVelocidadProcesamiento.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblMPVelocidadProcesamiento.setBounds(15, 60, 178, 16);
            pnlMicroprocesador.add(lblMPVelocidadProcesamiento);

            spnMPVelocidadProcesamiento = new JSpinner();
            spnMPVelocidadProcesamiento.setBackground(CyanClaro);
            spnMPVelocidadProcesamiento.setBorder(bottomBorder);
            spnMPVelocidadProcesamiento.setBounds(195, 56, 206, 22);
            pnlMicroprocesador.add(spnMPVelocidadProcesamiento);

            pnlDiscoDuro = new JPanel();
            pnlDiscoDuro.setBackground(new Color(240, 255, 240));
            pnlDiscoDuro.setLayout(null);
            pnlDiscoDuro.setBorder(new TitledBorder(null, "Disco Duro", TitledBorder.LEADING, TitledBorder.TOP, null, null));
            pnlDiscoDuro.setBounds(4, 204, 534, 93);
            panel.add(pnlDiscoDuro);
            pnlDiscoDuro.setVisible(false);

            JLabel label = new JLabel("Modelo:");
            label.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            label.setBounds(15, 28, 61, 16);
            pnlDiscoDuro.add(label);

            txtDDModelo = new JTextField();
            txtDDModelo.setBackground(CyanClaro);
            txtDDModelo.setBorder(bottomBorder);
            txtDDModelo.setColumns(10);
            txtDDModelo.setBounds(80, 25, 178, 22);
            pnlDiscoDuro.add(txtDDModelo);

            JLabel lblDDTipoConexion = new JLabel("Tipo Conexi\u00F3n:");
            lblDDTipoConexion.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblDDTipoConexion.setBounds(278, 28, 102, 16);
            pnlDiscoDuro.add(lblDDTipoConexion);

            JLabel lblDDCapacidadAlmacenamiento = new JLabel("Capacidad Almacenamiento:");
            lblDDCapacidadAlmacenamiento.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            lblDDCapacidadAlmacenamiento.setBounds(15, 60, 187, 16);
            pnlDiscoDuro.add(lblDDCapacidadAlmacenamiento);

            spnDDCapacidadAlmacenamiento = new JSpinner();
            spnDDCapacidadAlmacenamiento.setBackground(CyanClaro);
            spnDDCapacidadAlmacenamiento.setBorder(bottomBorder);
            spnDDCapacidadAlmacenamiento.setBounds(207, 57, 206, 22);
            pnlDiscoDuro.add(spnDDCapacidadAlmacenamiento);

            cbxDDTipoConexion = new JComboBox();
            cbxDDTipoConexion.setBackground(CyanClaro);
            cbxDDTipoConexion.setBorder(bottomBorder);
            cbxDDTipoConexion.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
            cbxDDTipoConexion.setModel(new DefaultComboBoxModel(new String[]{"<Seleccione uno>", "IDE", "SCSI", "SAS", "PCI-e"}));
            cbxDDTipoConexion.setBounds(383, 25, 139, 22);
            pnlDiscoDuro.add(cbxDDTipoConexion);
        }


        {
            JPanel buttonPane = new JPanel();
            buttonPane.setBackground(new Color(240, 255, 240));
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);

            if (soloLectura) {
                // boton "Cerrar" en modo soloLectura
                JButton cerrarButton = new JButton("Cerrar");
                cerrarButton.setForeground(Color.WHITE);
                cerrarButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                cerrarButton.setBackground(new Color(70, 133, 133));
                cerrarButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                buttonPane.add(cerrarButton);
                getRootPane().setDefaultButton(cerrarButton);

            } else {
                JButton okButton = new JButton("Registrar");
                okButton.setForeground(Color.WHITE);
                okButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                if (producto != null) {
                    okButton.setText("Actualizar");
                }
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        String id = txtId.getText();
                        String numSerie = txtNumSerie.getText();
                        String marca = txtMarca.getText();

                        int cantidad = new Integer(spnCantidad.getValue().toString());
                        float precio = new Float(spnPrecio.getValue().toString());

                        Proveedor proveedor = null;
                        if (cbxProveedor.getSelectedItem() != null) {
                            proveedor = (Proveedor) Tienda.getInstance().buscarPersonaId(cbxProveedor.getSelectedItem().toString());
                        }

                        if (producto == null) {
                            Producto nuevoProd = null;

                            if (id.isEmpty() || numSerie.isEmpty() || cantidad == 0) {
                                ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
                                MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de estar llenos!");
                                mensajito.setModal(true);
                                mensajito.setVisible(true);
                                return;
                            }

                            if (rbtnMotherBoard.isSelected()) {
                                if (txtMBModelo.getText().isEmpty() || txtMBSocket.getText().isEmpty() || "<Seleccione uno>".equalsIgnoreCase(cbxMBTipoRam.getSelectedItem().toString())) {
                                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
                                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de estar llenos!");
                                    mensajito.setModal(true);
                                    mensajito.setVisible(true);
                                    return;
                                } else {
                                    String modelo = txtMBModelo.getText();
                                    String socket = txtMBSocket.getText();
                                    String tipoRAM = cbxMBTipoRam.getSelectedItem().toString();
                                    ArrayList<String> aux = new ArrayList<>();
                                    for (JCheckBox chk : chkDiscosAceptados) {
                                        if (chk.isSelected()) aux.add(chk.getText());
                                    }
                                    nuevoProd = new MotherBoard(numSerie, cantidad, proveedor, marca, precio, modelo, socket, tipoRAM, aux);
                                }
                            }
                            if (rbtnMemoriaRAM.isSelected()) {
                                if ((int) spnMRCantidad.getValue() == 0 || "<Seleccione uno>".equalsIgnoreCase(cbxMRTipo.getSelectedItem().toString())) {
                                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
                                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de estar llenos!");
                                    mensajito.setModal(true);
                                    mensajito.setVisible(true);
                                    return;
                                } else {
                                    int cantidadRam = new Integer(spnMRCantidad.getValue().toString());
                                    String tipoMem = cbxMRTipo.getSelectedItem().toString();
                                    nuevoProd = new MemoriaRam(numSerie, cantidad, proveedor, marca, precio, cantidadRam, tipoMem);
                                }
                            }
                            if (rbtnMicroprocesador.isSelected()) {
                                if (txtMPModelo.getText().isEmpty() || txtMPSocket.getText().isEmpty() || (int) spnMPVelocidadProcesamiento.getValue() == 0) {
                                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
                                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de estar llenos!");
                                    mensajito.setModal(true);
                                    mensajito.setVisible(true);
                                    return;
                                } else {
                                    String modelo = txtMPModelo.getText();
                                    String socket = txtMPSocket.getText();
                                    int velocidadProcesamiento = new Integer(spnMPVelocidadProcesamiento.getValue().toString());
                                    nuevoProd = new Microprocesador(numSerie, cantidad, proveedor, marca, precio, modelo, socket, velocidadProcesamiento);
                                }
                            }
                            if (rbtnDiscoDuro.isSelected()) {
                                if (txtDDModelo.getText().isEmpty() || "<Seleccione uno>".equalsIgnoreCase(cbxDDTipoConexion.getSelectedItem().toString()) || spnDDCapacidadAlmacenamiento.getValue().equals(0)) {
                                    ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/cancel.png"));
                                    MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación errónea.\nTodos los campos deben de estar llenos!");
                                    mensajito.setModal(true);
                                    mensajito.setVisible(true);
                                    return;
                                } else {
                                    String modelo = txtDDModelo.getText();
                                    String tipoConex = cbxDDTipoConexion.getSelectedItem().toString();
                                    int cantAlm = new Integer(spnDDCapacidadAlmacenamiento.getValue().toString());
                                    nuevoProd = new DiscoDuro(numSerie, cantidad, proveedor, marca, precio, modelo, cantAlm, tipoConex);
                                }
                            }
                            Tienda.getInstance().registrarProducto(nuevoProd);
                            ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
                            MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación satisfactoria.\nProducto registrado!");
                            mensajito.setModal(true);
                            mensajito.setVisible(true);
                            clean();

                        } else {
                            producto.setNumSerie(numSerie);
                            producto.setMarca(marca);
                            producto.setProveedor(proveedor);
                            producto.setCantDisponible(cantidad);
                            producto.setPrecio(precio);

                            if (producto instanceof MotherBoard) {
                                ((MotherBoard) producto).setModelo(txtMBModelo.getText());
                                ((MotherBoard) producto).setTipoSocket(txtMBSocket.getText());
                                ((MotherBoard) producto).setTipoRam(cbxMBTipoRam.getSelectedItem().toString());
                                ArrayList<String> aux = new ArrayList<>();
                                for (JCheckBox chk : chkDiscosAceptados) {
                                    if (chk.isSelected()) aux.add(chk.getText());
                                }
                                ((MotherBoard) producto).setListaDiscoDuroAceptados(aux);
                            }
                            if (producto instanceof MemoriaRam) {
                                ((MemoriaRam) producto).setCantMemoria(new Integer(spnMRCantidad.getValue().toString()));
                                ((MemoriaRam) producto).setTipoMemoria(cbxMRTipo.getSelectedItem().toString());
                            }
                            if (producto instanceof Microprocesador) {
                                ((Microprocesador) producto).setModelo(txtMPModelo.getText());
                                ((Microprocesador) producto).setSocket(txtMPSocket.getText());
                                ((Microprocesador) producto).setVelocidadProcesamiento(new Integer(spnMPVelocidadProcesamiento.getValue().toString()));
                            }
                            if (producto instanceof DiscoDuro) {
                                ((DiscoDuro) producto).setModelo(txtDDModelo.getText());
                                ((DiscoDuro) producto).setTipoConexion(cbxDDTipoConexion.getSelectedItem().toString());
                                ((DiscoDuro) producto).setCapacidad(new Integer(spnDDCapacidadAlmacenamiento.getValue().toString()));
                            }

                            ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
                            String texto = "¿Seguro desea modificar el producto con código: " + codigo + "?";
                            VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
                            ventanita.setModal(true);
                            ventanita.setVisible(true);
                            int option = ventanita.getResultado();

                            if (option == JOptionPane.YES_OPTION) {
                                Tienda.getInstance().updateProducto(producto);
                                ImageIcon iconito = new ImageIcon(MensajeAlerta.class.getResource("/Imagenes/check.png"));
                                MensajeAlerta mensajito = new MensajeAlerta(iconito, "Operación satisfactoria.\nProducto modificado!");
                                mensajito.setModal(true);
                                mensajito.setVisible(true);
                            }

                            LocalDate hoy = LocalDate.now();
                            ArrayList<Producto> pro = new ArrayList<Producto>();
                            String proveedorIndex = new String(cbxProveedor.getSelectedItem().toString());
                            Proveedor provee = (Proveedor) Tienda.getInstance().buscarPersonaId(proveedorIndex);
                            int cantidadfact = (int) spnCantidad.getValue();
                            double preciofact = (float) spnPrecio.getValue();
                            pro.add(producto);
                            FacturaCompra fact = new FacturaCompra("Factura - " + Tienda.getNumFactura(), hoy, pro, proveedor, cantidadfact, ((preciofact * cantidadfact) - ((preciofact * cantidadfact) * 0.25)));
                            Tienda.getInstance().registrarFactura(fact);
                            dispose();
                        }
                    }
                });
                okButton.setActionCommand("OK");
                okButton.setBackground(new Color(80, 180, 152));
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);

                JButton cancelButton = new JButton("Cancelar");
                cancelButton.setForeground(Color.WHITE);
                cancelButton.setFont(new Font("Bahnschrift", Font.PLAIN, 14));
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        if (producto == null) {
                            ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
                            String texto = "¿Seguro desea cancelar el registro del producto en curso?";
                            VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
                            ventanita.setModal(true);
                            ventanita.setVisible(true);
                            int option = ventanita.getResultado();
                            if (option == JOptionPane.YES_OPTION) {
                                dispose();
                            }
                        } else {
                            ImageIcon icono = new ImageIcon(VentanaOpcion.class.getResource("/Imagenes/alert.png"));
                            String texto = "¿Seguro desea cancelar la modificación del producto con código: " + codigo + "?";
                            VentanaOpcion ventanita = new VentanaOpcion(icono, texto);
                            ventanita.setModal(true);
                            ventanita.setVisible(true);
                            int option = ventanita.getResultado();
                            if (option == JOptionPane.YES_OPTION) {
                                dispose();
                            }
                        }
                    }
                });
                cancelButton.setActionCommand("Cancel");
                cancelButton.setBackground(new Color(250, 128, 114));
                cancelButton.setForeground(Color.WHITE);
                buttonPane.add(cancelButton);
            }
        }

        loadProducto(producto);

        // si es soloLectura, deshabilitar todos los campos editables despues de cargar los datos
        if (soloLectura) {
            aplicarModoSoloLectura();
        }
    }

    // modo lectura
    private void aplicarModoSoloLectura() {
        txtMarca.setEditable(false);
        txtNumSerie.setEditable(false);
        spnCantidad.setEnabled(false);
        spnPrecio.setEnabled(false);
        cbxProveedor.setEnabled(false);

        rbtnMotherBoard.setEnabled(false);
        rbtnMemoriaRAM.setEnabled(false);
        rbtnMicroprocesador.setEnabled(false);
        rbtnDiscoDuro.setEnabled(false);

        txtMBModelo.setEditable(false);
        txtMBSocket.setEditable(false);
        cbxMBTipoRam.setEnabled(false);
        for (JCheckBox chk : chkDiscosAceptados) {
            chk.setEnabled(false);
        }

        spnMRCantidad.setEnabled(false);
        cbxMRTipo.setEnabled(false);

        txtMPModelo.setEditable(false);
        txtMPSocket.setEditable(false);
        spnMPVelocidadProcesamiento.setEnabled(false);

        txtDDModelo.setEditable(false);
        cbxDDTipoConexion.setEnabled(false);
        spnDDCapacidadAlmacenamiento.setEnabled(false);
    }

    private void loadProducto(Producto producto) {
        if (producto != null) {
            txtId.setText(producto.getId());
            txtNumSerie.setText(producto.getNumSerie());
            txtMarca.setText(producto.getMarca());
            spnCantidad.setValue(producto.getCantDisponible());
            spnPrecio.setValue(producto.getPrecio());

            if (producto instanceof MotherBoard) {
                rbtnMotherBoard.setSelected(true);
                rbtnMemoriaRAM.setSelected(false);
                rbtnMicroprocesador.setSelected(false);
                rbtnDiscoDuro.setSelected(false);
                pnlMotherBoard.setVisible(true);
                pnlMemoriaRAM.setVisible(false);
                spnMRCantidad.setVisible(false);
                pnlMicroprocesador.setVisible(false);
                spnMPVelocidadProcesamiento.setVisible(false);
                pnlDiscoDuro.setVisible(false);
                spnDDCapacidadAlmacenamiento.setVisible(false);
                txtMBModelo.setText(((MotherBoard) producto).getModelo());
                txtMBSocket.setText(((MotherBoard) producto).getTipoSocket());
                cbxMBTipoRam.setSelectedIndex(buscarIndiceSeleccionado(cbxMBTipoRam, ((MotherBoard) producto).getTipoRam()));

                // ---- CORRECCIÓN: Restaurar discos seleccionados con checkboxes ----
                ArrayList<String> discosGuardados = ((MotherBoard) producto).getListaDiscoDuroAceptados();
                if (discosGuardados != null) {
                    for (JCheckBox chk : chkDiscosAceptados) {
                        chk.setSelected(false);
                        for (String disco : discosGuardados) {
                            if (disco.equalsIgnoreCase(chk.getText())) {
                                chk.setSelected(true);
                                break;
                            }
                        }
                    }
                }
            }

            if (producto instanceof MemoriaRam) {
                rbtnMotherBoard.setSelected(false);
                rbtnMemoriaRAM.setSelected(true);
                rbtnMicroprocesador.setSelected(false);
                rbtnDiscoDuro.setSelected(false);
                pnlMotherBoard.setVisible(false);
                pnlMemoriaRAM.setVisible(true);
                spnMRCantidad.setVisible(true);
                pnlMicroprocesador.setVisible(false);
                spnMPVelocidadProcesamiento.setVisible(false);
                pnlDiscoDuro.setVisible(false);
                spnDDCapacidadAlmacenamiento.setVisible(false);
                spnMRCantidad.setValue(((MemoriaRam) producto).getCantMemoria());
                cbxMRTipo.setSelectedIndex(buscarIndiceSeleccionado(cbxMRTipo, ((MemoriaRam) producto).getTipoMemoria()));
            }

            if (producto instanceof Microprocesador) {
                rbtnMotherBoard.setSelected(false);
                rbtnMemoriaRAM.setSelected(false);
                rbtnMicroprocesador.setSelected(true);
                rbtnDiscoDuro.setSelected(false);
                pnlMotherBoard.setVisible(false);
                pnlMemoriaRAM.setVisible(false);
                spnMRCantidad.setVisible(false);
                pnlMicroprocesador.setVisible(true);
                spnMPVelocidadProcesamiento.setVisible(true);
                pnlDiscoDuro.setVisible(false);
                spnDDCapacidadAlmacenamiento.setVisible(false);
                txtMPModelo.setText(((Microprocesador) producto).getModelo());
                txtMPSocket.setText(((Microprocesador) producto).getSocket());
                spnMPVelocidadProcesamiento.setValue(((Microprocesador) producto).getVelocidadProcesamiento());
            }

            if (producto instanceof DiscoDuro) {
                rbtnMotherBoard.setSelected(false);
                rbtnMemoriaRAM.setSelected(false);
                rbtnMicroprocesador.setSelected(false);
                rbtnDiscoDuro.setSelected(true);
                pnlMotherBoard.setVisible(false);
                pnlMemoriaRAM.setVisible(false);
                spnMRCantidad.setVisible(false);
                pnlMicroprocesador.setVisible(false);
                spnMPVelocidadProcesamiento.setVisible(false);
                pnlDiscoDuro.setVisible(true);
                spnDDCapacidadAlmacenamiento.setVisible(true);
                txtDDModelo.setText(((DiscoDuro) producto).getModelo());
                cbxDDTipoConexion.setSelectedIndex(buscarIndiceSeleccionado(cbxDDTipoConexion, ((DiscoDuro) producto).getTipoConexion()));
                spnDDCapacidadAlmacenamiento.setValue(((DiscoDuro) producto).getCapacidad());
            }

        } else {
            txtId.setText("PDT-" + Tienda.getInstance().numProducto);
        }
    }

    public void clean() {
        txtId.setText("PDT-" + Tienda.getInstance().numProducto);
        txtNumSerie.setText("");
        txtMarca.setText("");
        int cantProveedores = 0;
        for (Persona persona : Tienda.getInstance().getListaPersonas()) {
            if (persona instanceof Proveedor) {
                cantProveedores++;
            }
        }
        if (cantProveedores > 0) {
            cbxProveedor.setSelectedIndex(0);
        }
        spnCantidad.setValue(new Integer(1));
        spnPrecio.setValue(new Float(0));
        rbtnMotherBoard.setSelected(true);
        rbtnMemoriaRAM.setSelected(false);
        rbtnMicroprocesador.setSelected(false);
        rbtnDiscoDuro.setSelected(false);
        pnlMotherBoard.setVisible(true);
        pnlMemoriaRAM.setVisible(false);
        pnlMicroprocesador.setVisible(false);
        pnlDiscoDuro.setVisible(false);
        txtMBModelo.setText("");
        txtMBSocket.setText("");
        cbxMBTipoRam.setSelectedIndex(0);
        for (JCheckBox chk : chkDiscosAceptados) { chk.setSelected(false); } // ---- CORRECCIÓN: limpiar checkboxes ----
        spnMRCantidad.setValue(new Integer(0));
        cbxMRTipo.setSelectedIndex(0);
        txtMPModelo.setText("");
        txtMPSocket.setText("");
        spnMPVelocidadProcesamiento.setValue(new Integer(0));
        txtDDModelo.setText("");
        cbxDDTipoConexion.setSelectedIndex(0);
        spnDDCapacidadAlmacenamiento.setValue(new Integer(0));
    }

    private int buscarIndiceSeleccionado(JComboBox<String> aux, String item) {
        int i = 0;
        while (i < aux.getItemCount()) {
            if (item.equalsIgnoreCase(aux.getItemAt(i))) {
                return i;
            }
            i++;
        }
        return 0;
    }
}