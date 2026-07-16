
package Syncro.Vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import Syncro.Vista.PnlAuditoria;
import Syncro.Vista.PnlCajero;
import Syncro.Vista.PnlClientes;
import Syncro.Vista.PnlConfig;
import Syncro.Vista.PnlDashboard;
import Syncro.Vista.Colores;

public class Ventana
extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContenido;
    private PnlDashboard panelDashboard;
    private PnlClientes panelClientes;
    private PnlCajero panelCajero;
    private PnlAuditoria panelAuditoria;
    private PnlConfig panelConfiguracion;
    private SidebarButton btnDashboard;
    private SidebarButton btnClientes;
    private SidebarButton btnCajero;
    private SidebarButton btnAuditoria;
    private SidebarButton btnConfiguracion;
    private SidebarButton botonActivo = null;
    private JLabel lblRol;

    public Ventana() {
        this.setTitle("Syncro - Sistema de Gesti\u00f3n Bancaria");
        this.setDefaultCloseOperation(3);
        this.setSize(1280, 820);
        this.setMinimumSize(new Dimension(1100, 700));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.inicializarComponentes();
    }

    private void inicializarComponentes() {
        this.add((Component)this.crearSidebar(), "West");
        JPanel areaDerecha = new JPanel(new BorderLayout());
        areaDerecha.setBackground(Colores.BACKGROUND);
        areaDerecha.add((Component)this.crearHeader(), "North");
        this.cardLayout = new CardLayout();
        this.panelContenido = new JPanel(this.cardLayout);
        this.panelContenido.setBackground(Colores.BACKGROUND);
        this.panelDashboard = new PnlDashboard();
        this.panelClientes = new PnlClientes();
        this.panelCajero = new PnlCajero();
        this.panelAuditoria = new PnlAuditoria();
        this.panelConfiguracion = new PnlConfig();
        this.panelContenido.add((Component)this.panelDashboard, "DASHBOARD");
        this.panelContenido.add((Component)this.panelClientes, "CLIENTES");
        this.panelContenido.add((Component)this.panelCajero, "CAJERO");
        this.panelContenido.add((Component)this.panelAuditoria, "AUDITORIA");
        this.panelContenido.add((Component)this.panelConfiguracion, "CONFIGURACION");
        areaDerecha.add((Component)this.panelContenido, "Center");
        this.add((Component)areaDerecha, "Center");
        this.navegarA("DASHBOARD", this.btnDashboard);
    }

    private JPanel crearSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, new Color(10, 22, 40), 0, getHeight(), new Color(24, 37, 60));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, 1));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(16, 0, 16, 0));
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        brand.setOpaque(false);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel iconoLogo = new JLabel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, 40, 40, 10, 10);
                g2.setColor(new Color(10, 22, 40));
                g2.setFont(new Font("Inter", 1, 15));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("SC", (40 - fm.stringWidth("SC")) / 2, 26);
            }
        };
        iconoLogo.setPreferredSize(new Dimension(40, 40));
        brand.add(iconoLogo);
        JPanel textoLogo = new JPanel();
        textoLogo.setLayout(new BoxLayout(textoLogo, 1));
        textoLogo.setOpaque(false);
        JLabel lblNombre = new JLabel("Syncro");
        lblNombre.setFont(new Font("Inter", 1, 18));
        lblNombre.setForeground(Color.WHITE);
        textoLogo.add(lblNombre);
        JLabel lblSub = new JLabel("Core Banking System");
        lblSub.setFont(new Font("Inter", 0, 10));
        lblSub.setForeground(Colores.TEXT_SIDEBAR);
        textoLogo.add(lblSub);
        brand.add(textoLogo);
        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(8));
        JSeparator divLogo = new JSeparator();
        divLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divLogo.setForeground(new Color(255, 255, 255, 20));
        sidebar.add(divLogo);
        sidebar.add(Box.createVerticalStrut(8));
        this.btnDashboard = new SidebarButton("Dashboard", "\ud83d\udcca ");
        this.btnClientes = new SidebarButton("Gesti\u00f3n de Clientes", "\ud83d\udc65 ");
        this.btnCajero = new SidebarButton("Cajero / Operaciones", "\ud83d\udcb3 ");
        this.btnAuditoria = new SidebarButton("Reg. de Auditor\u00eda", "\ud83d\udccb ");
        this.btnDashboard.addActionListener(e -> this.navegarA("DASHBOARD", this.btnDashboard));
        this.btnClientes.addActionListener(e -> this.navegarA("CLIENTES", this.btnClientes));
        this.btnCajero.addActionListener(e -> this.navegarA("CAJERO", this.btnCajero));
        this.btnAuditoria.addActionListener(e -> this.navegarA("AUDITORIA", this.btnAuditoria));
        sidebar.add(this.btnDashboard);
        sidebar.add(this.btnClientes);
        sidebar.add(this.btnCajero);
        sidebar.add(this.btnAuditoria);
        sidebar.add(Box.createVerticalGlue());
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(new Color(255, 255, 255, 20));
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(6));
        
        this.btnConfiguracion = new SidebarButton("Configuraci\u00f3n", "\u2699 ");
        this.btnConfiguracion.addActionListener(e -> this.navegarA("CONFIGURACION", this.btnConfiguracion));
        sidebar.add(this.btnConfiguracion);
        
        sidebar.add(Box.createVerticalStrut(4));
        SidebarButton btnCerrarSesion = new SidebarButton("Cerrar Sesi\u00f3n", "\ud83d\udeaa ");
        btnCerrarSesion.addActionListener(e -> {
            Syncro.Comportamiento.ContextoSesion.getInstance().cerrarSesion();
            new Syncro.Vista.VentanaLogin().setVisible(true);
            this.dispose();
        });
        sidebar.add(btnCerrarSesion);
        
        return sidebar;
    }

    private JLabel crearEtiquetaSeccion(String texto) {
        JLabel lbl = new JLabel("  " + texto);
        lbl.setFont(new Font("Inter", 1, 10));
        lbl.setForeground(new Color(100, 116, 139));
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return lbl;
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 64));
        header.setBackground(Colores.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colores.OUTLINE_LIGHT), new EmptyBorder(0, 24, 0, 24)));
        JLabel lblTitulo = new JLabel("Sistema de Gesti\u00f3n Bancaria  \u2014  Syncro");
        lblTitulo.setFont(Colores.FONT_HEADLINE);
        lblTitulo.setForeground(Colores.TEXT_PRIMARY);
        header.add((Component)lblTitulo, "West");
        JPanel adminPanel = new JPanel(new FlowLayout(2, 10, 13));
        adminPanel.setOpaque(false);
        JLabel avatar = new JLabel("U"){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Colores.ACCENT);
                g2.fillOval(0, 0, 36, 36);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Inter", 1, 15));
                g2.drawString(lblRol != null ? lblRol.getText().substring(0, 1).toUpperCase() : "U", 11, 24);
            }
        };
        avatar.setPreferredSize(new Dimension(36, 36));
        adminPanel.add(avatar);
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, 1));
        info.setOpaque(false);
        this.lblRol = new JLabel("Cargando...");
        this.lblRol.setFont(Colores.FONT_LABEL);
        this.lblRol.setForeground(Colores.TEXT_PRIMARY);
        info.add(this.lblRol);
        JLabel rol = new JLabel("Core Banking User");
        rol.setFont(new Font("Inter", 0, 10));
        rol.setForeground(Colores.TEXT_HINT);
        info.add(rol);
        adminPanel.add(info);
        header.add((Component)adminPanel, "East");
        return header;
    }

    public void setBotonAuditoriaVisible(boolean visible) {
        if (this.btnAuditoria != null) {
            this.btnAuditoria.setVisible(visible);
        }
    }

    public void setBotonConfiguracionVisible(boolean visible) {
        if (this.btnConfiguracion != null) {
            this.btnConfiguracion.setVisible(visible);
        }
    }

    public void setRolUsuario(String rol) {
        if (this.lblRol != null) {
            this.lblRol.setText(rol);
        }
    }

    private void navegarA(String panelName, SidebarButton botonSeleccionado) {
        this.cardLayout.show(this.panelContenido, panelName);
        if (this.botonActivo != null) {
            this.botonActivo.setActivo(false);
        }
        this.botonActivo = botonSeleccionado;
        if (this.botonActivo != null) {
            this.botonActivo.setActivo(true);
        }
        switch (panelName) {
            case "DASHBOARD": {
                this.panelDashboard.actualizarDatos();
                break;
            }
            case "CLIENTES": {
                this.panelClientes.actualizarTabla();
                break;
            }
            case "CAJERO": {
                this.panelCajero.actualizarDatos();
                break;
            }
            case "AUDITORIA": {
                this.panelAuditoria.actualizarDatos();
                break;
            }
            case "CONFIGURACION": {
                this.panelConfiguracion.actualizarDatos();
                break;
            }
        }
    }

    private static class SidebarButton
    extends JButton {
        private boolean activo = false;
        private static final Color COLOR_ACTIVO_START = new Color(37, 99, 235, 230);
        private static final Color COLOR_ACTIVO_END = new Color(29, 78, 216, 140);
        private static final Color COLOR_HOVER = new Color(255, 255, 255, 18);
        private static final Color COLOR_NORMAL = new Color(0, 0, 0, 0);
        private Color colorActual = COLOR_NORMAL;

        private static final int PADDING_IZQUIERDO = 24;

        public SidebarButton(String texto, String icono) {
            super(icono + texto);
            this.setFont(new Font("Inter", 0, 14));
            this.setForeground(Colores.TEXT_SIDEBAR);
            this.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            this.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            this.setMargin(new Insets(0, 0, 0, 0));
            this.putClientProperty("JButton.buttonType", "toolBarButton");
            this.putClientProperty("FlatLaf.style", "margin: 0,0,0,0");
            this.setFocusPainted(false);
            this.setBorderPainted(false);
            this.setContentAreaFilled(false);
            this.setOpaque(false);
            this.setCursor(new Cursor(12));
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
            this.setPreferredSize(new Dimension(260, 52));
            this.setBorder(new EmptyBorder(0, 0, 0, 8));
            this.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!activo) {
                        colorActual = COLOR_HOVER;
                        repaint();
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!activo) {
                        colorActual = COLOR_NORMAL;
                        repaint();
                    }
                }
            });
        }

        public void setActivo(boolean valor) {
            this.activo = valor;
            if (valor) {
                this.setForeground(Color.WHITE);
                this.setFont(new Font("Inter", 1, 14));
            } else {
                this.colorActual = COLOR_NORMAL;
                this.setForeground(Colores.TEXT_SIDEBAR);
                this.setFont(new Font("Inter", 0, 14));
            }
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int indent = PADDING_IZQUIERDO;
            
            if (this.activo) {
                // Rounded active background with gradient
                java.awt.GradientPaint gp = new java.awt.GradientPaint(12, 0, COLOR_ACTIVO_START, this.getWidth() - 24, 0, COLOR_ACTIVO_END);
                g2.setPaint(gp);
                g2.fillRoundRect(12, 4, this.getWidth() - 24, this.getHeight() - 8, 10, 10);
                
                // Neon blue selector indicator
                g2.setColor(new Color(96, 165, 250));
                g2.fillRoundRect(16, 10, 4, this.getHeight() - 20, 2, 2);
                
                indent = PADDING_IZQUIERDO + 8;
            } else if (colorActual == COLOR_HOVER) {
                g2.setColor(COLOR_HOVER);
                g2.fillRoundRect(12, 4, this.getWidth() - 24, this.getHeight() - 8, 10, 10);
            }
            
            g2.setFont(this.getFont());
            g2.setColor(this.getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int textY = (this.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(this.getText(), indent, textY);
            g2.dispose();
        }
    }
}
