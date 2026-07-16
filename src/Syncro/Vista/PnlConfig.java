package Syncro.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import Syncro.Creacional.BaseDatos;
import Syncro.Vista.BtnCurvo;
import Syncro.Vista.PnlCurvo;
import Syncro.Vista.Colores;

public class PnlConfig
extends JPanel {
    private BaseDatos db = BaseDatos.getInstance();
    private JCheckBox toggleSobregiro;
    private JCheckBox toggleSeguro;
    private JPanel panelSMS;

    public PnlConfig() {
        this.setBackground(Colores.BACKGROUND);
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(24, 24, 24, 24));
        this.inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel contenido = new JPanel(new GridLayout(1, 2, 24, 0));
        contenido.setBackground(Colores.BACKGROUND);
        contenido.add(this.crearPanelReglas());
        contenido.add(this.crearPanelSMS());
        this.add((Component)contenido, "Center");
    }

    private PnlCurvo crearPanelReglas() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel header = this.crearHeaderCard("Reglas de Cuentas Globales", "\u2699");
        card.add((Component)header, "North");
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, 1));
        contenido.setOpaque(false);
        contenido.setBorder(new EmptyBorder(24, 24, 24, 24));
        this.toggleSobregiro = new JCheckBox();
        this.toggleSobregiro.setSelected(this.db.isSobregiroHabilitado());
        contenido.add(this.crearToggleCard("Permitir Sobregiros en Cuentas Corrientes", "Autoriza saldo negativo hasta el l\u00edmite establecido (-$500.00).", this.toggleSobregiro));
        contenido.add(Box.createVerticalStrut(16));
        this.toggleSeguro = new JCheckBox();
        this.toggleSeguro.setSelected(this.db.isSeguroAntiFraude());
        contenido.add(this.crearToggleCard("Habilitar Seguros Anti-Fraude", "Aplica retenci\u00f3n autom\u00e1tica en transacciones sospechosas (monto > $10,000).", this.toggleSeguro));
        contenido.add(Box.createVerticalStrut(16));
        JCheckBox toggleSMSGlobal = new JCheckBox();
        toggleSMSGlobal.setSelected(true);
        contenido.add(this.crearToggleCard("Notificaciones SMS Globales", "Habilita el env\u00edo de SMS para todas las cuentas con el servicio activo.", toggleSMSGlobal));
        contenido.add(Box.createVerticalStrut(24));
        JPanel filaBotones = new JPanel(new GridLayout(1, 2, 12, 0));
        filaBotones.setOpaque(false);
        filaBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        filaBotones.setAlignmentX(0.0f);

        BtnCurvo btnGuardar = new BtnCurvo("\ud83d\udcbe  Guardar", Colores.PRIMARY, Colores.PRIMARY_LIGHT, Colores.TEXT_WHITE);
        btnGuardar.addActionListener(e -> {
            // Guardar estado previo en Memento antes de modificar
            Syncro.Comportamiento.ConfigMemento memento = new Syncro.Comportamiento.ConfigMemento(
                this.db.isSobregiroHabilitado(),
                this.db.isSeguroAntiFraude()
            );
            Syncro.Comportamiento.ConfigCaretaker.getInstance().guardar(memento);

            // Intentar guardar a través de Proxy
            Syncro.Estructural.ProxyConfiguracion proxy = new Syncro.Estructural.ProxyConfiguracion();
            boolean ok = proxy.guardarConfiguracion(this.toggleSobregiro.isSelected(), this.toggleSeguro.isSelected());

            if (ok) {
                JOptionPane.showMessageDialog(this, "\u2713 Configuración guardada vía Proxy de Seguridad.\n\u2022 Sobregiro: " + (this.toggleSobregiro.isSelected() ? "Habilitado" : "Deshabilitado") + "\n\u2022 Anti-Fraude: " + (this.toggleSeguro.isSelected() ? "Habilitado" : "Deshabilitado"), "Proxy Configuración", 1);
            } else {
                JOptionPane.showMessageDialog(this, "\u2717 Acceso Denegado por el Proxy: Solo los administradores pueden alterar políticas globales.", "Error de Seguridad", 0);
                this.actualizarDatos();
            }
        });

        BtnCurvo btnRestaurar = new BtnCurvo("\u21b6  Deshacer Cambios", Colores.WARNING, new Color(217, 119, 6), Colores.TEXT_WHITE);
        btnRestaurar.addActionListener(e -> {
            Syncro.Comportamiento.ConfigMemento anterior = Syncro.Comportamiento.ConfigCaretaker.getInstance().deshacer();
            if (anterior != null) {
                this.db.setSobregiroHabilitado(anterior.isSobregiroHabilitado());
                this.db.setSeguroAntiFraude(anterior.isSeguroAntiFraude());
                this.actualizarDatos();
                JOptionPane.showMessageDialog(this, "\u2713 Configuración restaurada al estado anterior con Memento.", "Restaurado con éxito", 1);
            } else {
                JOptionPane.showMessageDialog(this, "⚠ No hay historial de configuraciones previas para restaurar.", "Sin Historial", 2);
            }
        });

        filaBotones.add(btnGuardar);
        filaBotones.add(btnRestaurar);
        contenido.add(filaBotones);
        card.add((Component)contenido, "Center");
        return card;
    }

    private PnlCurvo crearPanelSMS() {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        JPanel header = this.crearHeaderCard("Centro de Notificaciones SMS", "\ud83d\udcf1");
        card.add((Component)header, "North");
        this.panelSMS = new JPanel();
        this.panelSMS.setLayout(new BoxLayout(this.panelSMS, 1));
        this.panelSMS.setBackground(Colores.SURFACE);
        this.panelSMS.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel msgInicial = new JLabel("No hay notificaciones SMS registradas...");
        msgInicial.setFont(Colores.FONT_BODY);
        msgInicial.setForeground(Colores.TEXT_HINT);
        msgInicial.setAlignmentX(0.5f);
        this.panelSMS.add(msgInicial);
        JScrollPane scrollSMS = new JScrollPane(this.panelSMS);
        scrollSMS.setBorder(null);
        scrollSMS.getVerticalScrollBar().setUnitIncrement(10);
        card.add((Component)scrollSMS, "Center");
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

    private JPanel crearToggleCard(String titulo, String descripcion, JCheckBox toggle) {
        JPanel card = new JPanel(new BorderLayout(16, 0));
        card.setBackground(Colores.BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Colores.OUTLINE_LIGHT, 1, true), new EmptyBorder(16, 16, 16, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, 1));
        textos.setOpaque(false);
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Inter", 1, 14));
        lblTitulo.setForeground(Colores.TEXT_PRIMARY);
        textos.add(lblTitulo);
        JLabel lblDesc = new JLabel("<html>" + descripcion + "</html>");
        lblDesc.setFont(new Font("Inter", 0, 12));
        lblDesc.setForeground(Colores.TEXT_HINT);
        textos.add(Box.createVerticalStrut(4));
        textos.add(lblDesc);
        card.add((Component)textos, "Center");
        toggle.setOpaque(false);
        card.add((Component)toggle, "East");
        return card;
    }

    private JPanel crearSMSItem(String mensaje, int index) {
        JPanel item = new JPanel(new BorderLayout(12, 0));
        item.setBackground(Colores.BACKGROUND);
        item.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Colores.OUTLINE_LIGHT, 1, true), new EmptyBorder(12, 12, 12, 12)));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        JLabel icono = new JLabel("\ud83d\udcac");
        icono.setFont(new Font("Segoe UI Emoji", 0, 20));
        icono.setVerticalAlignment(1);
        item.add((Component)icono, "West");
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, 1));
        contenido.setOpaque(false);
        JLabel lblMensaje = new JLabel("<html><body style='width:250px'>" + mensaje + "</body></html>");
        lblMensaje.setFont(Colores.FONT_MONO);
        lblMensaje.setForeground(Colores.TEXT_PRIMARY);
        contenido.add(lblMensaje);
        JPanel estadoPanel = new JPanel(new FlowLayout(0, 0, 4));
        estadoPanel.setOpaque(false);
        JLabel lblEstado = new JLabel("\u2713\u2713 Entregado");
        lblEstado.setFont(Colores.FONT_LABEL);
        lblEstado.setForeground(Colores.SUCCESS);
        estadoPanel.add(lblEstado);
        contenido.add(estadoPanel);
        item.add((Component)contenido, "Center");
        return item;
    }

    public void actualizarDatos() {
        this.toggleSobregiro.setSelected(this.db.isSobregiroHabilitado());
        this.toggleSeguro.setSelected(this.db.isSeguroAntiFraude());
        this.panelSMS.removeAll();
        List<String> sms = this.db.getLogsSMS();
        if (sms.isEmpty()) {
            JLabel msg = new JLabel("No hay notificaciones SMS registradas...");
            msg.setFont(Colores.FONT_BODY);
            msg.setForeground(Colores.TEXT_HINT);
            msg.setAlignmentX(0.5f);
            this.panelSMS.add(msg);
        } else {
            for (int i = sms.size() - 1; i >= 0; --i) {
                this.panelSMS.add(this.crearSMSItem(sms.get(i), i));
                this.panelSMS.add(Box.createVerticalStrut(8));
            }
        }
        this.panelSMS.revalidate();
        this.panelSMS.repaint();
    }
}
