package visual;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;

/**
 * Informes.java TLM Tech
 * JDialog con JTabbedPane. Una pestana por reporte.
 * Invocar desde el panel principal:
 *     new Informes(frameParent).setVisible(true);
 *
 * Ajusta getConnection() con tus credenciales.
 */
public class PanelReportes extends JDialog {

    private static final Color VERDE_CAMPO = new Color(204, 255, 204);
    private static final Color AZUL_HEADER = new Color(44,  95,  158);

    private Connection conn;

    private JTextField txtDesde, txtHasta;
    private JTextField txtResTotal, txtResCant, txtResProm;
    private DefaultTableModel mdlIngresos;

    private DefaultTableModel mdlTop;

    private DefaultTableModel mdlVend;

    private JTextField txtBusqCliente;
    private DefaultTableModel mdlLtv;

    private JSpinner spnUmbral;
    private DefaultTableModel mdlStock;

    private DefaultTableModel mdlCat;
    private JTextField txtCatLider;

    public PanelReportes(Frame parent) {
        super(parent, "TLM Tech - Panel de Reportes", true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(PanelReportes.class.getResource("/Imagenes/reportes.png")));
        setTitle("Panel de Reportes");
        setSize(780, 540);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        try { conn = getConnection(); }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error de conexion:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Bahnschrift", Font.PLAIN, 13));
        tabs.addTab("Ingresos",      tabIngresos());
        tabs.addTab("Top Productos", tabTopProductos());
        tabs.addTab("Vendedores",    tabVendedores());
        tabs.addTab("Clientes",   tabLtv());
        tabs.addTab("Stock",    tabStock());
        tabs.addTab("Por Categoria", tabCategoria());

        getContentPane().add(tabs, BorderLayout.CENTER);
        getContentPane().add(barraInferior(), BorderLayout.SOUTH);
    }

    private Connection getConnection() throws Exception {
        String url  = "jdbc:sqlserver://localhost:1433;databaseName=TiendaComputos;trustServerCertificate=true";
        String user = "proyectotiendabd";
        String pass = "proyectotiendabd";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, user, pass);
    }

    // =========================================================================
    //  TAB 1 INGRESOS POR RANGO DE FECHAS
    // =========================================================================
    private JPanel tabIngresos() {
        JPanel p = panelTab();

        JPanel gFiltro = grupo("Filtro de Fechas");
		try {
		    MaskFormatter mascara = new MaskFormatter("####-##-##");
		    mascara.setPlaceholderCharacter(' '); // Para que se vean los espacios vacíos
		    txtDesde = new JFormattedTextField(mascara);
		    txtHasta = new JFormattedTextField(mascara);
		    
		 // --- AQUÍ EL CAMBIO PARA EL ANCHO ---
	        Dimension tamanoFecha = new Dimension(100, 25); // Ancho: 100, Alto: 25
	        txtDesde.setPreferredSize(tamanoFecha);
	        txtHasta.setPreferredSize(tamanoFecha);
	        // ------------------------------------
	        
	        // Estilo visual igual a tus otros campos
	        txtDesde.setBackground(VERDE_CAMPO);
	        txtHasta.setBackground(VERDE_CAMPO);
	        txtDesde.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	        txtHasta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		} catch (ParseException e) {
		    txtDesde = new JFormattedTextField(); // Fallback por si falla la máscara
		    txtHasta = new JFormattedTextField();
		}

        JButton btnGen = boton("Generar", new Color(90, 158, 111));
        btnGen.addActionListener(e -> cargarIngresos());
        gFiltro.add(fila("Desde:", txtDesde, "Hasta:", txtHasta, btnGen));
        p.add(gFiltro);

        JPanel gRes = grupo("Resumen del Periodo");
        txtResTotal = campoSoloLectura(180);
        txtResCant  = campoSoloLectura(60);
        txtResProm  = campoSoloLectura(180);
        gRes.add(fila("Total Ingresos:", txtResTotal, "Facturas:", txtResCant));
        gRes.add(Box.createVerticalStrut(4));
        gRes.add(fila("Ticket Promedio:", txtResProm));
        p.add(gRes);

        JPanel gTabla = grupo("Detalle de Facturas");
        mdlIngresos = modelo("Cod. Venta", "Fecha", "Empleado", "Cliente", "Monto (RD$)", "% Ganancia");
        gTabla.add(new JScrollPane(tabla(mdlIngresos)));
        p.add(gTabla);
        return p;
    }

    private void cargarIngresos() {
        if (conn == null) return;
        mdlIngresos.setRowCount(0);
        double total = 0; int cant = 0;
        String sql =
            "SELECT fv.CodVenta, f.FechaFactura, pe.Nombre AS Emp, pc.Nombre AS Cli, " +
            "       f.MontoTotal, fv.PorcentajeGanancia " +
            "FROM FacturaVenta fv " +
            "JOIN Factura f  ON fv.Id_Factura    = f.Id_Factura " +
            "JOIN Empleado e ON fv.Id_P_Empleado = e.Id_Persona " +
            "JOIN Persona pe ON e.Id_Persona     = pe.Id_Persona " +
            "JOIN Cliente c  ON fv.Id_P_Cliente  = c.Id_Persona " +
            "JOIN Persona pc ON c.Id_Persona     = pc.Id_Persona " +
            "WHERE f.FechaFactura BETWEEN ? AND ? " +
            "ORDER BY f.FechaFactura DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, txtDesde.getText().trim());
            ps.setString(2, txtHasta.getText().trim());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double m = rs.getDouble("MontoTotal");
                total += m; cant++;
                mdlIngresos.addRow(new Object[]{
                    rs.getString("CodVenta"), rs.getDate("FechaFactura"),
                    rs.getString("Emp"), rs.getString("Cli"),
                    String.format("%,.2f", m), rs.getDouble("PorcentajeGanancia") + "%"
                });
            }
            txtResTotal.setText(String.format("RD$ %,.2f", total));
            txtResCant.setText(String.valueOf(cant));
            txtResProm.setText(cant > 0 ? String.format("RD$ %,.2f", total / cant) : "â€”");
        } catch (SQLException ex) { error(ex); }
    }

    // =========================================================================
    //  TAB 2 TOP 5 PRODUCTOS MAS VENDIDOS
    // =========================================================================
    private JPanel tabTopProductos() {
        JPanel p = panelTab();
        JPanel gCtrl = grupo("Opciones");
        JButton btn = boton("Actualizar", AZUL_HEADER);
        btn.addActionListener(e -> cargarTop());
        gCtrl.add(fila(btn));
        p.add(gCtrl);

        JPanel gTabla = grupo("Ranking de Productos");
        mdlTop = modelo("#", "Num. Serie", "Marca", "Tipo", "Unidades Vendidas", "Ingresos (RD$)");
        gTabla.add(new JScrollPane(tabla(mdlTop)));
        p.add(gTabla);
        return p;
    }

    private void cargarTop() {
        if (conn == null) return;
        mdlTop.setRowCount(0);
        String sql =
            "SELECT TOP 5 pr.NumSerie, pr.Marca, pr.TipoProducto, " +
            "       SUM(dfv.CantProducto) AS Unid, SUM(dfv.SubTotal) AS Ing " +
            "FROM DetalleFacturaVenta dfv " +
            "JOIN Producto pr ON dfv.Id_Producto = pr.Id_Producto " +
            "GROUP BY pr.NumSerie, pr.Marca, pr.TipoProducto " +
            "ORDER BY Unid DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            int r = 1;
            while (rs.next()) {
                mdlTop.addRow(new Object[]{
                    r++, rs.getString("NumSerie"), rs.getString("Marca"),
                    rs.getString("TipoProducto"), rs.getInt("Unid"),
                    String.format("%,.2f", rs.getDouble("Ing"))
                });
            }
        } catch (SQLException ex) { error(ex); }
    }

    // =========================================================================
    //  TAB 3 RENDIMIENTO DE VENDEDORES
    // =========================================================================
    private JPanel tabVendedores() {
        JPanel p = panelTab();
        JPanel gCtrl = grupo("Opciones");
        JButton btnAct = boton("Actualizar", AZUL_HEADER);
        JButton btnMes = boton("Marcar Empleado del Mes", new Color(90, 158, 111));
        btnAct.addActionListener(e -> cargarVendedores());
        btnMes.addActionListener(e -> marcarEmpleadoMes());
        gCtrl.add(fila(btnAct, btnMes));
        p.add(gCtrl);

        JPanel gTabla = grupo("Vendedores y Comisiones");
        mdlVend = modelo("Cod. Empleado", "Nombre", "Tipo Usuario", "Ventas", "Comision (RD$)", "Emp. Mes");
        gTabla.add(new JScrollPane(tabla(mdlVend)));
        p.add(gTabla);
        return p;
    }

    private void cargarVendedores() {
        if (conn == null) return;
        mdlVend.setRowCount(0);
        String sql =
            "SELECT e.CodEmpleado, pe.Nombre, u.Tipo, e.CantVentas, e.ComisionVentas, " +
            "       CASE WHEN e.EmpleadoMes=1 THEN 'Si' ELSE 'No' END AS EsMes " +
            "FROM Empleado e " +
            "JOIN Persona pe ON e.Id_Persona = pe.Id_Persona " +
            "JOIN Usuario u  ON e.Id_Usuario = u.Id_Usuario " +
            "ORDER BY e.ComisionVentas DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mdlVend.addRow(new Object[]{
                    rs.getString("CodEmpleado"), rs.getString("Nombre"), rs.getString("Tipo"),
                    rs.getInt("CantVentas"), String.format("%,.2f", rs.getDouble("ComisionVentas")),
                    rs.getString("EsMes")
                });
            }
        } catch (SQLException ex) { error(ex); }
    }

    private void marcarEmpleadoMes() {
        JTable t = buscarTabla(getContentPane(), mdlVend);
        if (t == null || t.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado de la lista.");
            return;
        }
        String cod = (String) mdlVend.getValueAt(t.getSelectedRow(), 0);
        try {
            conn.prepareStatement("UPDATE Empleado SET EmpleadoMes = 0").executeUpdate();
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE Empleado SET EmpleadoMes = 1 WHERE CodEmpleado = ?");
            ps.setString(1, cod);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Empleado del Mes actualizado.");
            cargarVendedores();
        } catch (SQLException ex) { error(ex); }
    }

    // =========================================================================
    //  TAB 4 VALOR DE VIDA DEL CLIENTE (LTV)
    // =========================================================================
    private JPanel tabLtv() {
        JPanel p = panelTab();
        JPanel gFiltro = grupo("Buscar Cliente");
        txtBusqCliente = campoTexto("", 180);
        JButton btnBuscar = boton("Buscar", AZUL_HEADER);
        JButton btnTodos  = boton("Ver Todos", new Color(120, 120, 120));
        btnBuscar.addActionListener(e -> cargarLtv(txtBusqCliente.getText().trim()));
        btnTodos.addActionListener(e -> { txtBusqCliente.setText(""); cargarLtv(""); });
        gFiltro.add(fila("Nombre / Cedula:", txtBusqCliente, btnBuscar, btnTodos));
        p.add(gFiltro);

        JPanel gTabla = grupo("Historial de Clientes");
        mdlLtv = modelo("Cod. Cliente", "Nombre", "Cedula", "Registro", "Compras", "Total Gastado (RD$)", "Clasificacion");
        gTabla.add(new JScrollPane(tabla(mdlLtv)));
        p.add(gTabla);
        return p;
    }
    
    private void cargarLtv(String filtro) {
        if (conn == null) return;
        mdlLtv.setRowCount(0);
        String sql =
            "SELECT c.CodCliente, pe.Nombre, pe.Cedula, pe.FechaRegistro, " +
            "       COUNT(fv.Id_Factura) AS Compras, " +
            "       COALESCE(SUM(f.MontoTotal), 0) AS Total, c.Clasificacion " +
            "FROM Cliente c " +
            "JOIN Persona pe ON c.Id_Persona = pe.Id_Persona " +
            "LEFT JOIN FacturaVenta fv ON fv.Id_P_Cliente = c.Id_Persona " +
            "LEFT JOIN Factura f       ON fv.Id_Factura   = f.Id_Factura " +
            "WHERE pe.Nombre LIKE ? OR pe.Cedula LIKE ? " +
            "GROUP BY c.CodCliente, pe.Nombre, pe.Cedula, pe.FechaRegistro, c.Clasificacion " +
            "ORDER BY Total DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + filtro + "%";
            ps.setString(1, like); ps.setString(2, like);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mdlLtv.addRow(new Object[]{
                    rs.getString("CodCliente"), rs.getString("Nombre"), rs.getString("Cedula"),
                    rs.getDate("FechaRegistro"), rs.getInt("Compras"),
                    String.format("%,.2f", rs.getDouble("Total")), rs.getString("Clasificacion")
                });
            }
        } catch (SQLException ex) { error(ex); }
    }

    
    // =========================================================================
    //  TAB 5 PRODUCTOS CON BAJO STOCK
    // =========================================================================
    private JPanel tabStock() {
        JPanel p = panelTab();
        JPanel gFiltro = grupo("Umbral de Alerta");
        spnUmbral = new JSpinner(new SpinnerNumberModel(3, 0, 999, 1));
        spnUmbral.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        spnUmbral.setPreferredSize(new Dimension(65, 22));
        JButton btnGen = boton("Generar", new Color(180, 60, 60));
        btnGen.addActionListener(e -> cargarStock((int) spnUmbral.getValue()));
        gFiltro.add(fila("Minimo de unidades:", spnUmbral, btnGen));
        p.add(gFiltro);

        JPanel gTabla = grupo("Productos por debajo del limite");
        mdlStock = modelo("ID", "Num. Serie", "Marca", "Tipo", "Cant. Disponible", "Precio (RD$)", "Estado");
        JTable tbl = tabla(mdlStock);
        tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    String st = String.valueOf(mdlStock.getValueAt(row, 6));
                    setBackground("CRITICO".equals(st)
                        ? new Color(255, 220, 220) : new Color(255, 248, 220));
                }
                return this;
            }
        });
        gTabla.add(new JScrollPane(tbl));
        p.add(gTabla);
        return p;
    }

    private void cargarStock(int umbral) {
        if (conn == null) return;
        mdlStock.setRowCount(0);
        String sql =
            "SELECT Id_Producto, NumSerie, Marca, TipoProducto, CantDisponible, Precio " +
            "FROM Producto WHERE CantDisponible < ? ORDER BY CantDisponible ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, umbral);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int cant = rs.getInt("CantDisponible");
                mdlStock.addRow(new Object[]{
                    rs.getInt("Id_Producto"), rs.getString("NumSerie"), rs.getString("Marca"),
                    rs.getString("TipoProducto"), cant,
                    String.format("%,.2f", rs.getDouble("Precio")),
                    cant == 0 ? "CRITICO" : "BAJO"
                });
            }
        } catch (SQLException ex) { error(ex); }
    }

    // =========================================================================
    //  TAB 6 VENTAS POR CATEGORIA
    // =========================================================================
    private JPanel tabCategoria() {
        JPanel p = panelTab();
        JPanel gCtrl = grupo("Opciones");
        txtCatLider = campoSoloLectura(200);
        JButton btn = boton("Actualizar", AZUL_HEADER);
        btn.addActionListener(e -> cargarCategoria());
        gCtrl.add(fila("Categoria lider:", txtCatLider, btn));
        p.add(gCtrl);

        JPanel gTabla = grupo("Ventas agrupadas por tipo de producto");
        mdlCat = modelo("Tipo Producto", "Unidades Vendidas", "Ingresos (RD$)", "% del Total", "Facturas");
        gTabla.add(new JScrollPane(tabla(mdlCat)));
        p.add(gTabla);
        return p;
    }

    private void cargarCategoria() {
        if (conn == null) return;
        mdlCat.setRowCount(0);
        String sql =
            "SELECT pr.TipoProducto, SUM(dfv.CantProducto) AS Unid, " +
            "       SUM(dfv.SubTotal) AS Ing, COUNT(DISTINCT dfv.Id_F_Venta) AS Fact " +
            "FROM DetalleFacturaVenta dfv " +
            "JOIN Producto pr ON dfv.Id_Producto = pr.Id_Producto " +
            "GROUP BY pr.TipoProducto ORDER BY Ing DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            java.util.List<Object[]> filas = new java.util.ArrayList<>();
            double totalIng = 0;
            while (rs.next()) {
                double ing = rs.getDouble("Ing");
                totalIng += ing;
                filas.add(new Object[]{ rs.getString("TipoProducto"), rs.getInt("Unid"), ing, rs.getInt("Fact") });
            }
            String lider = "â€”";
            for (int i = 0; i < filas.size(); i++) {
                Object[] f = filas.get(i);
                double ing = (double) f[2];
                double pct = totalIng > 0 ? (ing / totalIng) * 100 : 0;
                mdlCat.addRow(new Object[]{
                    f[0], f[1], String.format("%,.2f", ing),
                    String.format("%.1f%%", pct), f[3]
                });
                if (i == 0) lider = (String) f[0];
            }
            txtCatLider.setText(lider);
        } catch (SQLException ex) { error(ex); }
    }

    // =========================================================================
    //  BARRA INFERIOR
    // =========================================================================
    private JPanel barraInferior() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        p.setBackground(new Color(240, 240, 240));
        p.setBorder(new MatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        JButton btnCerrar = boton("Cerrar Panel", new Color(255, 255, 255));
        btnCerrar.addActionListener(e -> dispose());
        p.add(btnCerrar);
        return p;
    }

    // =========================================================================
    //  UTILIDADES UI
    // =========================================================================
    private JPanel panelTab() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(240, 240, 240));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return p;
    }

    private JPanel grupo(String titulo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(240, 240, 240));
        p.setBorder(new CompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(170, 170, 170)),
                titulo, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 11)),
            BorderFactory.createEmptyBorder(4, 6, 6, 6)));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return p;
    }

    private JPanel fila(Object... items) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        p.setOpaque(false);
        
        for (Object it : items) {
            // Chequeo para Strings
            if (it instanceof String) {
                String s = (String) it; // Casting manual
                JLabel lbl = new JLabel(s);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                p.add(lbl);
            } 
            // Chequeo para Componentes (Botones, TextFields, etc.)
            else if (it instanceof java.awt.Component) {
                java.awt.Component c = (java.awt.Component) it; // Casting manual
                p.add(c);
            }
        }
        return p;
    }

    private JTextField campoTexto(String valor, int ancho) {
        JTextField tf = new JTextField(valor);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBackground(VERDE_CAMPO);
        tf.setPreferredSize(new Dimension(ancho, 22));
        return tf;
    }

    private JTextField campoSoloLectura(int ancho) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBackground(new Color(232, 245, 232));
        tf.setEditable(false);
        tf.setPreferredSize(new Dimension(ancho, 22));
        return tf;
    }
    
    private JTable tabla(DefaultTableModel mdl) {
        JTable t = new JTable(mdl);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setRowHeight(25);
        t.setGridColor(new Color(210, 210, 210));
        t.setFillsViewportHeight(true);
        
        // Colores de selección forzados
        t.setSelectionBackground(new Color(198, 225, 255));
        t.setSelectionForeground(Color.BLACK);

        // CONFIGURACIÓN DEL ENCABEZADO (HEADERS)
        JTableHeader header = t.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setOpaque(true); // CRUCIAL
        header.setBackground(AZUL_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // RENDERER MEJORADO PARA LAS CELDAS
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Forzamos el color del texto a negro siempre
                c.setForeground(Color.BLACK);
                
                if (isSelected) {
                    c.setBackground(new Color(198, 225, 255));
                } else {
                    // Alternamos blanco y gris muy claro para que se vea la diferencia
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(242, 242, 242));
                }
                
                // Si es un JLabel (que casi siempre lo es), quitamos bordes internos raros
                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(new EmptyBorder(0, 5, 0, 5));
                }
                
                return c;
            }
        });

        return t;
    }

    private DefaultTableModel modelo(String... cols) {
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JButton boton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setBackground(bg);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
    }

    private JTable buscarTabla(Container c, DefaultTableModel mdl) {
        for (Component comp : c.getComponents()) {
            // 1. Verificamos si es un JScrollPane
            if (comp instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) comp; // Casting manual
                
                // 2. Verificamos si lo que hay dentro del ScrollPane es una JTable
                if (sp.getViewport().getView() instanceof JTable) {
                    JTable t = (JTable) sp.getViewport().getView(); // Casting manual
                    
                    // Si el modelo coincide, retornamos la tabla
                    if (t.getModel() == mdl) {
                        return t;
                    }
                }
            }
            
            // 3. Búsqueda recursiva: si el componente es un contenedor (como un JPanel)
            if (comp instanceof Container) {
                Container cont = (Container) comp; // Casting manual
                JTable found = buscarTabla(cont, mdl);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    private void error(Exception ex) {
        JOptionPane.showMessageDialog(this,
            "Error:\n" + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { 
                // Prueba con CrossPlatform si el System sigue dando problemas de colores
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); 
            } 
            catch (Exception ignored) {}
            new PanelReportes(null).setVisible(true);
        });
    }
}