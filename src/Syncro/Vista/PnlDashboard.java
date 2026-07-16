
package Syncro.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Syncro.Creacional.BaseDatos;
import Syncro.Vista.PnlCurvo;
import Syncro.Vista.Colores;
import Syncro.Modelos.CtaBase;

public class PnlDashboard
extends JPanel {
    private final BaseDatos db = BaseDatos.getInstance();
    private JLabel lblCapitalTotal;
    private JLabel lblClientes;
    private JLabel lblCuentas;
    private JLabel lblTransacciones;
    private DefaultTableModel modeloCuentas;

    public PnlDashboard() {
        this.setBackground(Colores.BACKGROUND);
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(24, 24, 24, 24));
        this.inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, 1));
        contenido.setBackground(Colores.BACKGROUND);
        JPanel filaKPI = new JPanel(new GridLayout(1, 4, 16, 0));
        filaKPI.setBackground(Colores.BACKGROUND);
        filaKPI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        this.lblCapitalTotal = new JLabel("$0.00");
        this.lblClientes = new JLabel("0");
        this.lblCuentas = new JLabel("0");
        this.lblTransacciones = new JLabel("0");
        filaKPI.add(this.crearKPI("Capital Total", this.lblCapitalTotal, new Color(224, 242, 254), new Color(186, 230, 253), new Color(3, 105, 161), new Color(2, 132, 199), "\ud83d\udcb0"));
        filaKPI.add(this.crearKPI("Clientes Registrados", this.lblClientes, new Color(209, 250, 229), new Color(167, 243, 208), new Color(6, 95, 70), new Color(4, 120, 87), "\ud83d\udc64"));
        filaKPI.add(this.crearKPI("Cuentas Activas", this.lblCuentas, new Color(224, 231, 255), new Color(199, 210, 254), new Color(55, 48, 163), new Color(67, 56, 202), "\ud83c\udfe6"));
        filaKPI.add(this.crearKPI("Total Transacciones", this.lblTransacciones, new Color(255, 237, 213), new Color(253, 230, 138), new Color(154, 52, 18), new Color(194, 65, 12), "\ud83d\udd04"));
        contenido.add(filaKPI);
        contenido.add(Box.createVerticalStrut(20));
        contenido.add(this.crearTablaCuentas());
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        this.add((Component)scroll, "Center");
    }

    private PnlGradiente crearKPI(String titulo, JLabel valorLabel, Color colorStart, Color colorEnd, Color colorTitleText, Color colorValueText, String emoji) {
        PnlGradiente card = new PnlGradiente(12, colorStart, colorEnd);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(18, 20, 18, 20));
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", 0, 22));
        cuerpo.add((Component)lblEmoji, "West");
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, 1));
        textos.setOpaque(false);
        textos.setBorder(new EmptyBorder(0, 12, 0, 0));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Inter", 1, 12));
        lblTitulo.setForeground(colorTitleText);
        textos.add(lblTitulo);
        valorLabel.setFont(new Font("Inter", 1, 24));
        valorLabel.setForeground(colorValueText);
        textos.add(valorLabel);
        cuerpo.add((Component)textos, "Center");
        card.add((Component)cuerpo, "Center");
        return card;
    }

    private PnlCurvo crearTablaCuentas() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Colores.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colores.OUTLINE_LIGHT), new EmptyBorder(14, 20, 14, 20)));
        JLabel titulo = new JLabel("Resumen de Cuentas Activas");
        titulo.setFont(Colores.FONT_HEADLINE);
        titulo.setForeground(Colores.TEXT_PRIMARY);
        header.add((Component)titulo, "West");
        JLabel subLabel = new JLabel("Datos actualizados en tiempo real");
        subLabel.setFont(new Font("Inter", 2, 11));
        subLabel.setForeground(Colores.TEXT_HINT);
        header.add((Component)subLabel, "East");
        card.add((Component)header, "North");
        Object[] cols = new String[]{"N\u00famero de Cuenta", "Tipo", "Propietario (DNI)", "Saldo Actual", "Inter\u00e9s Estimado"};
        this.modeloCuentas = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return true;
            }
        };
        JTable tabla = new JTable(this.modeloCuentas);
        tabla.setRowHeight(44);
        tabla.setFont(Colores.FONT_BODY);
        tabla.setForeground(Colores.TEXT_PRIMARY);
        tabla.setSelectionBackground(Colores.ACCENT);
        tabla.setSelectionForeground(Colores.TEXT_WHITE);
        tabla.setGridColor(Colores.OUTLINE_LIGHT);
        
        ReadOnlyCellEditor cellEditor = new ReadOnlyCellEditor();
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellEditor(cellEditor);
        }

        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        JTableHeader th = tabla.getTableHeader();
        th.setFont(Colores.FONT_LABEL);
        th.setForeground(Colores.TEXT_SECONDARY);
        th.setBackground(Colores.SURFACE_LOW);
        th.setPreferredSize(new Dimension(0, 44));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colores.OUTLINE_LIGHT));
        DefaultTableCellRenderer der = new DefaultTableCellRenderer();
        der.setHorizontalAlignment(4);
        tabla.getColumnModel().getColumn(3).setCellRenderer(der);
        tabla.getColumnModel().getColumn(4).setCellRenderer(der);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(null);
        card.add((Component)sp, "Center");
        return card;
    }

    public void actualizarDatos() {
        this.lblCapitalTotal.setText("$" + String.format("%,.2f", this.db.getCapitalTotal()));
        this.lblClientes.setText(String.valueOf(this.db.getClientesActivos()));
        this.lblCuentas.setText(String.valueOf(this.db.getTotalCuentas()));
        this.lblTransacciones.setText(String.valueOf(this.db.getTransaccionesHoy()));
        this.modeloCuentas.setRowCount(0);
        List<CtaBase> cuentas = this.db.getCuentas();
        for (CtaBase c : cuentas) {
            double interes = c.calcularInteres();
            String interesStr = (c.getEstrategiaInteres() != null && interes > 0) ? 
                "$" + String.format("%,.2f", interes) + " (" + c.getEstrategiaInteres().getNombre() + ")" : "Sin interés";
            this.modeloCuentas.addRow(new Object[]{c.getNumeroCuenta(), c.getTipoCuenta(), c.getDniDueno(), "$" + String.format("%,.2f", c.getSaldo()), interesStr});
        }
    }
}
