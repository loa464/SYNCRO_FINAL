
package Syncro.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import Syncro.Creacional.BaseDatos;
import Syncro.Vista.BtnCurvo;
import Syncro.Vista.PnlCurvo;
import Syncro.Vista.Colores;
import Syncro.Modelos.Cliente;
import Syncro.Modelos.CtaBase;
import Syncro.Modelos.Transaccion;
import Syncro.Estructural.FacadeCajero;

public class PnlCajero
extends JPanel {
    private FacadeCajero facade = new FacadeCajero();
    private BaseDatos db = BaseDatos.getInstance();
    private JTextField txtDniBuscar;
    private JTextField txtMonto;
    private JTextField txtCuentaDestino;
    private JComboBox<String> cmbCuentaOrigen;
    private ButtonGroup grupoOperacion;
    private JRadioButton rbDeposito;
    private JRadioButton rbRetiro;
    private JRadioButton rbTransferencia;
    private JPanel panelTimeline;
    private JLabel lblInfoCuenta;
    private JPanel panelCuentaDestino;

    public PnlCajero() {
        this.setBackground(Colores.BACKGROUND);
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(24, 24, 24, 24));
        this.inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel contenido = new JPanel(new GridLayout(1, 2, 24, 0));
        contenido.setBackground(Colores.BACKGROUND);
        contenido.add(this.crearFormularioTransaccion());
        contenido.add(this.crearPanelHistorial());
        this.add((Component)contenido, "Center");
    }

    private PnlCurvo crearFormularioTransaccion() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel header = this.crearHeaderCard("Nueva Transacci\u00f3n", "\ud83d\udcb8");
        card.add((Component)header, "North");
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, 1));
        form.setOpaque(false);
        form.setBorder(new EmptyBorder(20, 24, 24, 24));
        JPanel panelBuscar = new JPanel(new BorderLayout(8, 0));
        panelBuscar.setOpaque(false);
        panelBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JPanel labelBuscar = new JPanel();
        labelBuscar.setLayout(new BoxLayout(labelBuscar, 1));
        labelBuscar.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar Cliente (DNI)");
        lblBuscar.setFont(Colores.FONT_LABEL);
        lblBuscar.setForeground(Colores.TEXT_SECONDARY);
        labelBuscar.add(lblBuscar);
        labelBuscar.add(Box.createVerticalStrut(6));
        this.txtDniBuscar = new JTextField();
        EstiloInput.limitarSoloNumerosMaxLongitud(this.txtDniBuscar, 8);
        this.txtDniBuscar.setFont(Colores.FONT_BODY);
        this.txtDniBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        EstiloInput.aplicarFocoDinamico(this.txtDniBuscar);
        labelBuscar.add(this.txtDniBuscar);
        panelBuscar.add((Component)labelBuscar, "Center");
        BtnCurvo btnBuscar = new BtnCurvo("Buscar", Colores.ACCENT, Colores.PRIMARY, Colores.TEXT_WHITE);
        btnBuscar.setPreferredSize(new Dimension(90, 36));
        btnBuscar.addActionListener(e -> this.accionBuscarCliente());
        JPanel panelBtnBuscar = new JPanel(new FlowLayout(2, 0, 22));
        panelBtnBuscar.setOpaque(false);
        panelBtnBuscar.add(btnBuscar);
        panelBuscar.add((Component)panelBtnBuscar, "East");
        form.add(panelBuscar);
        form.add(Box.createVerticalStrut(8));
        this.lblInfoCuenta = new JLabel("Ingrese un DNI y presione Buscar...");
        this.lblInfoCuenta.setFont(new Font("Inter", 2, 12));
        this.lblInfoCuenta.setForeground(Colores.TEXT_HINT);
        this.lblInfoCuenta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        this.lblInfoCuenta.setAlignmentX(0.0f);
        form.add(this.lblInfoCuenta);
        form.add(Box.createVerticalStrut(12));
        JPanel panelSelector = new JPanel();
        panelSelector.setLayout(new BoxLayout(panelSelector, 1));
        panelSelector.setOpaque(false);
        panelSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel lblSelector = new JLabel("Seleccionar Cuenta");
        lblSelector.setFont(Colores.FONT_LABEL);
        lblSelector.setForeground(Colores.TEXT_SECONDARY);
        panelSelector.add(lblSelector);
        panelSelector.add(Box.createVerticalStrut(6));
        this.cmbCuentaOrigen = new JComboBox<String>();
        this.cmbCuentaOrigen.setFont(Colores.FONT_BODY);
        this.cmbCuentaOrigen.addActionListener(e -> this.actualizarHistorial());
        EstiloInput.aplicarFocoDinamico(this.cmbCuentaOrigen);
        panelSelector.add(this.cmbCuentaOrigen);
        form.add(panelSelector);
        form.add(Box.createVerticalStrut(16));
        JLabel lblTipo = new JLabel("Tipo de Operaci\u00f3n");
        lblTipo.setFont(Colores.FONT_LABEL);
        lblTipo.setForeground(Colores.TEXT_SECONDARY);
        lblTipo.setAlignmentX(0.0f);
        form.add(lblTipo);
        form.add(Box.createVerticalStrut(8));
        JPanel panelRadios = new JPanel(new GridLayout(1, 3, 8, 0));
        panelRadios.setOpaque(false);
        panelRadios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        this.grupoOperacion = new ButtonGroup();
        this.rbDeposito = this.crearRadioEstilizado("Dep\u00f3sito");
        this.rbRetiro = this.crearRadioEstilizado("Retiro");
        this.rbTransferencia = this.crearRadioEstilizado("Transferencia");
        this.rbDeposito.setSelected(true);
        this.rbTransferencia.addActionListener(e -> this.panelCuentaDestino.setVisible(true));
        this.rbDeposito.addActionListener(e -> this.panelCuentaDestino.setVisible(false));
        this.rbRetiro.addActionListener(e -> this.panelCuentaDestino.setVisible(false));
        this.grupoOperacion.add(this.rbDeposito);
        this.grupoOperacion.add(this.rbRetiro);
        this.grupoOperacion.add(this.rbTransferencia);
        panelRadios.add(this.rbDeposito);
        panelRadios.add(this.rbRetiro);
        panelRadios.add(this.rbTransferencia);
        form.add(panelRadios);
        form.add(Box.createVerticalStrut(12));
        this.panelCuentaDestino = new JPanel();
        this.panelCuentaDestino.setLayout(new BoxLayout(this.panelCuentaDestino, 1));
        this.panelCuentaDestino.setOpaque(false);
        this.panelCuentaDestino.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        this.panelCuentaDestino.setVisible(false);
        JLabel lblDestino = new JLabel("Cuenta Destino");
        lblDestino.setFont(Colores.FONT_LABEL);
        lblDestino.setForeground(Colores.TEXT_SECONDARY);
        this.panelCuentaDestino.add(lblDestino);
        this.panelCuentaDestino.add(Box.createVerticalStrut(6));
        this.txtCuentaDestino = new JTextField();
        this.txtCuentaDestino.setFont(Colores.FONT_BODY);
        this.txtCuentaDestino.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        EstiloInput.aplicarFocoDinamico(this.txtCuentaDestino);
        this.panelCuentaDestino.add(this.txtCuentaDestino);
        form.add(this.panelCuentaDestino);
        form.add(Box.createVerticalStrut(12));
        JPanel panelMonto = new JPanel();
        panelMonto.setLayout(new BoxLayout(panelMonto, 1));
        panelMonto.setOpaque(false);
        panelMonto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        JLabel lblMonto = new JLabel("Monto");
        lblMonto.setFont(Colores.FONT_LABEL);
        lblMonto.setForeground(Colores.TEXT_SECONDARY);
        panelMonto.add(lblMonto);
        panelMonto.add(Box.createVerticalStrut(6));
        this.txtMonto = new JTextField();
        this.txtMonto.setFont(Colores.FONT_MONO_BIG);
        this.txtMonto.setHorizontalAlignment(4);
        this.txtMonto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        this.txtMonto.setForeground(Colores.PRIMARY);
        EstiloInput.aplicarFocoDinamico(this.txtMonto);
        panelMonto.add(this.txtMonto);
        form.add(panelMonto);
        form.add(Box.createVerticalStrut(20));
        BtnCurvo btnEjecutar = new BtnCurvo("\u2713  Ejecutar Transacci\u00f3n", Colores.SUCCESS, new Color(33, 139, 58), Colores.TEXT_WHITE);
        btnEjecutar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnEjecutar.setFont(new Font("Inter", 1, 14));
        btnEjecutar.addActionListener(e -> this.accionEjecutarTransaccion());
        form.add(btnEjecutar);
        form.add(Box.createVerticalStrut(10));
        BtnCurvo btnDeshacer = new BtnCurvo("\u21b6  Deshacer \u00daltima Transacci\u00f3n", Colores.WARNING, new Color(217, 119, 6), Colores.TEXT_WHITE);
        btnDeshacer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnDeshacer.setFont(new Font("Inter", 1, 13));
        btnDeshacer.addActionListener(e -> this.accionDeshacerTransaccion());
        form.add(btnDeshacer);
        card.add((Component)form, "Center");
        return card;
    }

    private PnlCurvo crearPanelHistorial() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel header = this.crearHeaderCard("Historial de Cuenta", "\ud83d\udcdc");
        card.add((Component)header, "North");
        this.panelTimeline = new JPanel();
        this.panelTimeline.setLayout(new BoxLayout(this.panelTimeline, 1));
        this.panelTimeline.setBackground(Colores.SURFACE);
        this.panelTimeline.setBorder(new EmptyBorder(20, 30, 20, 24));
        JLabel msgInicial = new JLabel("Seleccione una cuenta para ver su historial...");
        msgInicial.setFont(Colores.FONT_BODY);
        msgInicial.setForeground(Colores.TEXT_HINT);
        this.panelTimeline.add(msgInicial);
        JScrollPane scrollTimeline = new JScrollPane(this.panelTimeline);
        scrollTimeline.setBorder(null);
        scrollTimeline.getVerticalScrollBar().setUnitIncrement(10);
        card.add((Component)scrollTimeline, "Center");
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

    private JRadioButton crearRadioEstilizado(String texto) {
        JRadioButton rb = new JRadioButton(texto);
        rb.setFont(Colores.FONT_BODY);
        rb.setForeground(Colores.TEXT_PRIMARY);
        rb.setOpaque(false);
        rb.setFocusPainted(false);
        return rb;
    }

    private void accionBuscarCliente() {
        String dni = this.txtDniBuscar.getText().trim();
        if (dni.isEmpty()) {
            this.lblInfoCuenta.setText("\u26a0 Ingrese un DNI v\u00e1lido.");
            this.lblInfoCuenta.setForeground(Colores.ERROR);
            return;
        }
        Cliente cliente = this.db.buscarClientePorDni(dni);
        if (cliente == null) {
            this.lblInfoCuenta.setText("\u2717 Cliente no encontrado con DNI: " + dni);
            this.lblInfoCuenta.setForeground(Colores.ERROR);
            this.cmbCuentaOrigen.removeAllItems();
            return;
        }
        this.lblInfoCuenta.setText("\u2713 Cliente: " + cliente.getNombre() + " | Cuentas: " + cliente.getTotalCuentas());
        this.lblInfoCuenta.setForeground(Colores.SUCCESS);
        this.cmbCuentaOrigen.removeAllItems();
        List<CtaBase> cuentas = this.db.obtenerCuentasPorDni(dni);
        for (CtaBase c : cuentas) {
            this.cmbCuentaOrigen.addItem(c.getNumeroCuenta() + " (" + c.getTipoCuenta() + ") - " + c.getMoneda().getSimbolo() + String.format("%,.2f", c.getSaldo()));
        }
    }

    private void accionEjecutarTransaccion() {
        double monto;
        if (this.cmbCuentaOrigen.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Primero busque un cliente y seleccione una cuenta.", "Sin Cuenta", 2);
            return;
        }
        String montoTexto = this.txtMonto.getText().trim();
        if (montoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido.", "Monto Vacío", 2);
            return;
        }
        try {
            monto = Double.parseDouble(montoTexto);
        }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.", "Error", 0);
            return;
        }
        String seleccion = (String)this.cmbCuentaOrigen.getSelectedItem();
        String numCuenta = seleccion.split(" ")[0];

        final double finalMonto = monto;
        new javax.swing.SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                if (rbDeposito.isSelected()) {
                    return facade.realizarDeposito(numCuenta, finalMonto);
                } else if (rbRetiro.isSelected()) {
                    return facade.realizarRetiro(numCuenta, finalMonto);
                } else {
                    String cuentaDest = txtCuentaDestino.getText().trim();
                    if (cuentaDest.isEmpty()) {
                        return "ERROR:Ingrese la cuenta destino para la transferencia.";
                    }
                    return facade.realizarTransferencia(numCuenta, cuentaDest, finalMonto);
                }
            }

            @Override
            protected void done() {
                try {
                    String resultado = get();
                    if (resultado.startsWith("ERROR:")) {
                        JOptionPane.showMessageDialog(PnlCajero.this, resultado.substring(6), "Datos Incompletos", 2);
                        return;
                    }
                    if (resultado.startsWith("✓")) {
                        JOptionPane.showMessageDialog(PnlCajero.this, resultado, "Transacción Exitosa", 1);
                        txtMonto.setText("");
                        txtCuentaDestino.setText("");
                        accionBuscarCliente();
                        actualizarHistorial();
                    } else {
                        JOptionPane.showMessageDialog(PnlCajero.this, resultado, "Transacción Fallida", 0);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PnlCajero.this, "Error al ejecutar transacción: " + e.getMessage(), "Error", 0);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void accionDeshacerTransaccion() {
        new javax.swing.SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return Syncro.Comportamiento.GestorCmd.getInstance().deshacerUltimo();
            }

            @Override
            protected void done() {
                try {
                    String resultado = get();
                    if (resultado != null) {
                        JOptionPane.showMessageDialog(PnlCajero.this, "✓ Transacción deshecha exitosamente:\n" + resultado, "Reverso Exitoso", 1);
                        accionBuscarCliente();
                        actualizarHistorial();
                    } else {
                        JOptionPane.showMessageDialog(PnlCajero.this, "⚠ No hay transacciones en el historial para deshacer.", "Error al Deshacer", 2);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PnlCajero.this, "Error al deshacer transacción: " + e.getMessage(), "Error", 0);
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void actualizarHistorial() {
        this.panelTimeline.removeAll();
        if (this.cmbCuentaOrigen.getSelectedItem() == null) {
            JLabel msg = new JLabel("Seleccione una cuenta...");
            msg.setFont(Colores.FONT_BODY);
            msg.setForeground(Colores.TEXT_HINT);
            this.panelTimeline.add(msg);
            this.panelTimeline.revalidate();
            this.panelTimeline.repaint();
            return;
        }
        String seleccion = (String)this.cmbCuentaOrigen.getSelectedItem();
        String numCuenta = seleccion.split(" ")[0];
        List<Transaccion> historial = this.db.obtenerHistorialCuenta(numCuenta);
        if (historial.isEmpty()) {
            JLabel msg = new JLabel("Sin transacciones registradas para esta cuenta.");
            msg.setFont(Colores.FONT_BODY);
            msg.setForeground(Colores.TEXT_HINT);
            this.panelTimeline.add(msg);
        } else {
            for (int i = historial.size() - 1; i >= 0; --i) {
                this.panelTimeline.add(this.crearTimelineItem(historial.get(i), numCuenta));
            }
        }
        this.panelTimeline.revalidate();
        this.panelTimeline.repaint();
    }

    private JPanel crearTimelineItem(Transaccion tx, String numCuentaActual) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        boolean esIngreso = tx.getTipo() == Transaccion.TipoOperacion.DEPOSITO || 
                           (tx.getTipo() == Transaccion.TipoOperacion.TRANSFERENCIA && numCuentaActual.equals(tx.getNumeroCuentaDestino()));
        item.setOpaque(false);
        
        // Custom circle icon panel with connecting vertical line
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw timeline connecting line
                g2.setColor(new Color(210, 214, 219));
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.drawLine(20, 0, 20, getHeight());
                
                // Color theme
                Color bgCircle = esIngreso ? Colores.SUCCESS_LIGHT : Colores.ERROR_LIGHT;
                Color fgIcon = esIngreso ? Colores.SUCCESS : Colores.ERROR;
                if (tx.getTipo() == Transaccion.TipoOperacion.TRANSFERENCIA) {
                    if (esIngreso) {
                        bgCircle = new Color(209, 250, 229);
                        fgIcon = new Color(4, 120, 87);
                    } else {
                        bgCircle = new Color(224, 231, 255);
                        fgIcon = new Color(67, 56, 202);
                    }
                }
                
                // Fill circle on top of the line
                g2.setColor(bgCircle);
                g2.fillOval(2, (getHeight() - 36) / 2, 36, 36);
                
                // Draw border for circle
                g2.setColor(fgIcon);
                g2.setStroke(new java.awt.BasicStroke(1));
                g2.drawOval(2, (getHeight() - 36) / 2, 36, 36);
                
                // Draw text icon
                g2.setColor(fgIcon);
                g2.setFont(new Font("Inter", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                
                String symbol = "↓";
                if (tx.getTipo() == Transaccion.TipoOperacion.DEPOSITO) {
                    symbol = "↓";
                } else if (tx.getTipo() == Transaccion.TipoOperacion.RETIRO) {
                    symbol = "↑";
                } else { // Transferencia
                    symbol = esIngreso ? "←" : "→";
                }
                
                int x = (40 - fm.stringWidth(symbol)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(symbol, x, y);
                g2.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(40, 65));
        iconPanel.setOpaque(false);
        
        item.setBorder(new EmptyBorder(8, 0, 8, 12));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        item.add((Component)iconPanel, "West");
        
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, 1));
        textos.setOpaque(false);
        
        String desc = tx.getDescripcion();
        if (tx.getTipo() == Transaccion.TipoOperacion.TRANSFERENCIA) {
            if (numCuentaActual.equals(tx.getNumeroCuentaDestino())) {
                desc = "Transf. Recibida desde " + tx.getNumeroCuenta();
            } else {
                desc = "Transf. Enviada a " + tx.getNumeroCuentaDestino();
            }
        }
        
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Inter", 1, 13));
        lblDesc.setForeground(Colores.TEXT_PRIMARY);
        textos.add(lblDesc);
        JLabel lblFecha = new JLabel(tx.getFechaFormateada());
        lblFecha.setFont(new Font("Inter", 0, 11));
        lblFecha.setForeground(Colores.TEXT_HINT);
        textos.add(lblFecha);
        item.add((Component)textos, "Center");
        String signo = esIngreso ? "+" : "-";
        CtaBase cta = BaseDatos.getInstance().buscarCuentaPorNumero(numCuentaActual);
        String simbolo = cta != null ? cta.getMoneda().getSimbolo() : "$";
        JLabel lblMonto = new JLabel(signo + simbolo + String.format("%,.2f", tx.getMonto()));
        lblMonto.setFont(new Font("Consolas", 1, 14));
        lblMonto.setForeground(esIngreso ? Colores.SUCCESS : Colores.ERROR);
        item.add((Component)lblMonto, "East");
        return item;
    }

    public void actualizarDatos() {
        if (!this.txtDniBuscar.getText().trim().isEmpty()) {
            this.accionBuscarCliente();
        }
    }
}
