
package Syncro.Vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Syncro.Creacional.FactoryCta;
import Syncro.Creacional.BaseDatos;
import Syncro.Vista.BtnCurvo;
import Syncro.Vista.PnlCurvo;
import Syncro.Vista.Colores;
import Syncro.Modelos.Cliente;
import Syncro.Modelos.CtaBase;
import Syncro.Estructural.FacadeCajero;

public class PnlClientes
extends JPanel {
    private FacadeCajero facade = new FacadeCajero();
    private BaseDatos db = BaseDatos.getInstance();
    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDniBuscar;
    private JTextField txtSaldoInicial;
    private JComboBox<String> cmbTipoCuenta;
    private JComboBox<String> cmbMoneda;
    private JCheckBox chkSMS;
    private DefaultTableModel modeloTabla;
    private JTable tablaGlobal;

    public PnlClientes() {
        this.setBackground(Colores.BACKGROUND);
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(24, 24, 24, 24));
        this.inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, 1));
        contenido.setBackground(Colores.BACKGROUND);
        JPanel filaSuperior = new JPanel(new GridLayout(1, 2, 16, 0));
        filaSuperior.setBackground(Colores.BACKGROUND);
        filaSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));
        filaSuperior.add(this.crearCardCliente());
        filaSuperior.add(this.crearCardCuenta());
        contenido.add(filaSuperior);
        contenido.add(Box.createVerticalStrut(24));
        contenido.add(this.crearCardTabla());
        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        this.add((Component)scroll, "Center");
    }

    private PnlCurvo crearCardCliente() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel headerPanel = this.crearHeaderCard("Registrar Nuevo Cliente", "\ud83d\udc64");
        card.add((Component)headerPanel, "North");
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, 1));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(20, 24, 24, 24));
        this.txtNombre = new JTextField();
        EstiloInput.bloquearNumeros(this.txtNombre);
        form.add(this.crearCampo("Nombre Completo", this.txtNombre, "Ej. Juan Carlos Pérez"));
        form.add(Box.createVerticalStrut(12));
        JPanel filaDniTel = new JPanel(new GridLayout(1, 2, 12, 0));
        filaDniTel.setOpaque(false);
        filaDniTel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        filaDniTel.setAlignmentX(0.0f);
        this.txtDni = new JTextField();
        EstiloInput.limitarSoloNumerosMaxLongitud(this.txtDni, 8);
        filaDniTel.add(this.crearCampo("DNI / Documento", this.txtDni, "8 dígitos numéricos"));
        this.txtTelefono = new JTextField();
        EstiloInput.limitarSoloNumerosMaxLongitud(this.txtTelefono, 9);
        filaDniTel.add(this.crearCampo("Teléfono", this.txtTelefono, "Celular de 9 dígitos (ej: 9XXXXXXXX)"));
        form.add(filaDniTel);
        form.add(Box.createVerticalStrut(12));
        this.txtCorreo = new JTextField();
        form.add(this.crearCampo("Correo Electrónico", this.txtCorreo, "usuario@ejemplo.com"));
        form.add(Box.createVerticalStrut(20));
        BtnCurvo btnCrear = new BtnCurvo("\u2713  Crear Cliente", Colores.PRIMARY, Colores.PRIMARY_LIGHT, Colores.TEXT_WHITE);
        btnCrear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnCrear.setAlignmentX(0.0f);
        btnCrear.addActionListener(e -> this.accionCrearCliente());
        form.add(btnCrear);
        card.add((Component)form, "Center");
        return card;
    }

    private PnlCurvo crearCardCuenta() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel headerPanel = this.crearHeaderCard("Apertura de Cuentas", "\ud83d\udcb3");
        card.add((Component)headerPanel, "North");
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, 1));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(20, 24, 24, 24));
        this.txtDniBuscar = new JTextField();
        EstiloInput.limitarSoloNumerosMaxLongitud(this.txtDniBuscar, 8);
        form.add(this.crearCampo("Vincular a Cliente Existente (DNI)", this.txtDniBuscar, "Buscar cliente..."));
        form.add(Box.createVerticalStrut(12));
        JPanel filaTipoSaldo = new JPanel(new GridLayout(1, 3, 12, 0));
        filaTipoSaldo.setOpaque(false);
        filaTipoSaldo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        filaTipoSaldo.setAlignmentX(0.0f);
        
        JPanel panelTipo = new JPanel();
        panelTipo.setLayout(new BoxLayout(panelTipo, 1));
        panelTipo.setOpaque(false);
        panelTipo.setAlignmentX(0.0f);
        JLabel lblTipo = new JLabel("Tipo de Cuenta");
        lblTipo.setFont(Colores.FONT_LABEL);
        lblTipo.setForeground(Colores.TEXT_SECONDARY);
        panelTipo.add(lblTipo);
        panelTipo.add(Box.createVerticalStrut(6));
        this.cmbTipoCuenta = new JComboBox<String>(FactoryCta.getTiposDisponibles());
        this.cmbTipoCuenta.setFont(Colores.FONT_BODY);
        EstiloInput.aplicarFocoDinamico(this.cmbTipoCuenta);
        panelTipo.add(this.cmbTipoCuenta);
        filaTipoSaldo.add(panelTipo);
        
        JPanel panelMoneda = new JPanel();
        panelMoneda.setLayout(new BoxLayout(panelMoneda, 1));
        panelMoneda.setOpaque(false);
        panelMoneda.setAlignmentX(0.0f);
        JLabel lblMoneda = new JLabel("Moneda");
        lblMoneda.setFont(Colores.FONT_LABEL);
        lblMoneda.setForeground(Colores.TEXT_SECONDARY);
        panelMoneda.add(lblMoneda);
        panelMoneda.add(Box.createVerticalStrut(6));
        this.cmbMoneda = new JComboBox<String>(new String[]{"SOLES", "DOLARES"});
        this.cmbMoneda.setFont(Colores.FONT_BODY);
        EstiloInput.aplicarFocoDinamico(this.cmbMoneda);
        panelMoneda.add(this.cmbMoneda);
        filaTipoSaldo.add(panelMoneda);
        
        this.txtSaldoInicial = new JTextField();
        filaTipoSaldo.add(this.crearCampo("Saldo Inicial", this.txtSaldoInicial, "0.00"));
        form.add(filaTipoSaldo);
        form.add(Box.createVerticalStrut(14));
        this.chkSMS = new JCheckBox("Activar Notificaciones SMS");
        this.chkSMS.setFont(Colores.FONT_BODY);
        this.chkSMS.setForeground(Colores.TEXT_PRIMARY);
        this.chkSMS.setOpaque(false);
        this.chkSMS.setSelected(true);
        this.chkSMS.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        this.chkSMS.setAlignmentX(0.0f);
        form.add(this.chkSMS);
        form.add(Box.createVerticalStrut(20));
        BtnCurvo btnAperturar = new BtnCurvo("\ud83d\udcb3  Aperturar Cuenta", Colores.PRIMARY, Colores.PRIMARY_LIGHT, Colores.TEXT_WHITE);
        btnAperturar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnAperturar.setAlignmentX(0.0f);
        btnAperturar.addActionListener(e -> this.accionAperturarCuenta());
        form.add(btnAperturar);
        card.add((Component)form, "Center");
        return card;
    }

    private PnlCurvo crearCardTabla() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel headerPanel = this.crearHeaderCard("Registro Global del Sistema", "\ud83d\udccb");
        card.add((Component)headerPanel, "North");
        Object[] columnas = new String[]{"ID Cliente", "Nombre", "Tipo de Cuenta", "Saldo Actual", "Estado"};
        this.modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        this.tablaGlobal = new JTable(this.modeloTabla);
        this.tablaGlobal.setRowHeight(44);
        this.tablaGlobal.setFont(Colores.FONT_BODY);
        this.tablaGlobal.setForeground(Colores.TEXT_PRIMARY);
        this.tablaGlobal.setSelectionBackground(Colores.ACCENT);
        this.tablaGlobal.setSelectionForeground(Colores.TEXT_WHITE);
        this.tablaGlobal.setGridColor(Colores.OUTLINE_LIGHT);
        
        ReadOnlyCellEditor cellEditor = new ReadOnlyCellEditor();
        for (int i = 0; i < this.tablaGlobal.getColumnCount(); i++) {
            this.tablaGlobal.getColumnModel().getColumn(i).setCellEditor(cellEditor);
        }

        this.tablaGlobal.setShowHorizontalLines(true);
        this.tablaGlobal.setShowVerticalLines(false);
        this.tablaGlobal.setIntercellSpacing(new Dimension(0, 1));
        JTableHeader header = this.tablaGlobal.getTableHeader();
        header.setFont(Colores.FONT_LABEL);
        header.setForeground(Colores.TEXT_SECONDARY);
        header.setBackground(Colores.SURFACE_LOW);
        header.setPreferredSize(new Dimension(0, 44));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colores.OUTLINE_LIGHT));
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(0);
        this.tablaGlobal.getColumnModel().getColumn(0).setCellRenderer(centrado);
        this.tablaGlobal.getColumnModel().getColumn(4).setCellRenderer(centrado);
        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(4);
        this.tablaGlobal.getColumnModel().getColumn(3).setCellRenderer(derecha);
        JScrollPane scrollTabla = new JScrollPane(this.tablaGlobal);
        scrollTabla.setBorder(null);
        scrollTabla.setPreferredSize(new Dimension(0, 250));
        card.add((Component)scrollTabla, "Center");
        return card;
    }

    private JPanel crearHeaderCard(String titulo, String icono) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Colores.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colores.OUTLINE_LIGHT), new EmptyBorder(16, 24, 16, 24)));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(Colores.FONT_HEADLINE);
        lblTitulo.setForeground(Colores.TEXT_PRIMARY);
        header.add((Component)lblTitulo, "West");
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Emoji", 0, 20));
        header.add((Component)lblIcono, "East");
        return header;
    }

    private JPanel crearCampo(String etiqueta, JTextField campo, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, 1));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        panel.setAlignmentX(0.0f);
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(Colores.FONT_LABEL);
        lbl.setForeground(Colores.TEXT_SECONDARY);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
        campo.setFont(Colores.FONT_BODY);
        campo.setToolTipText(placeholder);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        EstiloInput.aplicarFocoDinamico(campo);
        panel.add(campo);
        return panel;
    }

    private void accionCrearCliente() {
        String nombre = this.txtNombre.getText().trim();
        String dni = this.txtDni.getText().trim();
        String telefono = this.txtTelefono.getText().trim();
        String correo = this.txtCorreo.getText().trim();
        
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre completo es obligatorio.", "Datos Incompletos", 2);
            return;
        }
        if (!dni.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener exactamente 8 dígitos numéricos.", "Formato Inválido", 2);
            return;
        }
        String telLimpio = telefono.replace(" ", "").replace("-", "");
        if (!telLimpio.matches("^(?:\\+?51)?9\\d{8}$")) {
            JOptionPane.showMessageDialog(this, "El teléfono debe ser un celular peruano válido (9 dígitos, ej: 9XXXXXXXX).", "Formato Inválido", 2);
            return;
        }
        if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "El correo electrónico debe tener un formato válido con '@' (ej: usuario@correo.com).", "Formato Inválido", 2);
            return;
        }


        new javax.swing.SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return facade.registrarCliente(dni, nombre, telefono, correo);
            }

            @Override
            protected void done() {
                try {
                    String resultado = get();
                    if (resultado.startsWith("✓")) {
                        JOptionPane.showMessageDialog(PnlClientes.this, resultado, "Éxito", 1);
                        limpiarFormularioCliente();
                        actualizarTabla();
                    } else {
                        JOptionPane.showMessageDialog(PnlClientes.this, resultado, "Error", 0);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PnlClientes.this, "Error al registrar cliente: " + e.getMessage(), "Error", 0);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void accionAperturarCuenta() {
        double saldoInicial;
        String dniCliente = this.txtDniBuscar.getText().trim();
        String tipoCuenta = (String)this.cmbTipoCuenta.getSelectedItem();
        String monedaStr = (String)this.cmbMoneda.getSelectedItem();
        Syncro.Modelos.Moneda monedaEnum = Syncro.Modelos.Moneda.valueOf(monedaStr);
        String saldoTexto = this.txtSaldoInicial.getText().trim();
        boolean activarSMS = this.chkSMS.isSelected();
        if (dniCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el DNI del cliente propietario.", "Datos Incompletos", 2);
            return;
        }
        try {
            saldoInicial = saldoTexto.isEmpty() ? 0.0 : Double.parseDouble(saldoTexto);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El saldo inicial debe ser un número válido.", "Error de Formato", 0);
            return;
        }

        final double finalSaldoInicial = saldoInicial;
        new javax.swing.SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return facade.aperturarCuenta(dniCliente, tipoCuenta, finalSaldoInicial, activarSMS, monedaEnum);
            }

            @Override
            protected void done() {
                try {
                    String resultado = get();
                    if (resultado.startsWith("✓")) {
                        JOptionPane.showMessageDialog(PnlClientes.this, resultado, "Éxito", 1);
                        limpiarFormularioCuenta();
                        actualizarTabla();
                    } else {
                        JOptionPane.showMessageDialog(PnlClientes.this, resultado, "Error", 0);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PnlClientes.this, "Error al aperturar cuenta: " + e.getMessage(), "Error", 0);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void actualizarTabla() {
        this.modeloTabla.setRowCount(0);
        List<Cliente> clientes = this.db.getClientes();
        for (Cliente cliente : clientes) {
            List<CtaBase> cuentas = cliente.getCuentas();
            if (cuentas.isEmpty()) {
                this.modeloTabla.addRow(new Object[]{cliente.getDni(), cliente.getNombre(), "Sin cuenta", "$0.00", "Pendiente"});
                continue;
            }
            for (CtaBase cuenta : cuentas) {
                this.modeloTabla.addRow(new Object[]{cliente.getDni(), cliente.getNombre(), cuenta.getTipoCuenta(), cuenta.getMoneda().getSimbolo() + String.format("%,.2f", cuenta.getSaldo()), cuenta.isActiva() ? "Activo" : "Inactivo"});
            }
        }
    }

    private void limpiarFormularioCliente() {
        this.txtNombre.setText("");
        this.txtDni.setText("");
        this.txtTelefono.setText("");
        this.txtCorreo.setText("");
    }

    private void limpiarFormularioCuenta() {
        this.txtDniBuscar.setText("");
        this.txtSaldoInicial.setText("");
        this.cmbTipoCuenta.setSelectedIndex(0);
        this.chkSMS.setSelected(true);
    }
}
