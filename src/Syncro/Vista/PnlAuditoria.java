
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import Syncro.Comportamiento.GestorCmd;
import Syncro.Comportamiento.Auditoria;
import Syncro.Creacional.BaseDatos;
import Syncro.Vista.PnlCurvo;
import Syncro.Vista.Colores;

public class PnlAuditoria
extends JPanel {
    private JPanel panelLogs;
    private JLabel lblTotalEventos;
    private JLabel lblTotalComandos;
    private JLabel lblTotalSMS;

    public PnlAuditoria() {
        this.setBackground(Colores.BACKGROUND);
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(24, 24, 24, 24));
        this.inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, 1));
        contenido.setBackground(Colores.BACKGROUND);
        JPanel filaStats = new JPanel(new GridLayout(1, 3, 16, 0));
        filaStats.setBackground(Colores.BACKGROUND);
        filaStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        this.lblTotalEventos = new JLabel("0");
        this.lblTotalComandos = new JLabel("0");
        this.lblTotalSMS = new JLabel("0");
        filaStats.add(this.crearStatCard("Eventos Registrados", this.lblTotalEventos, Colores.ACCENT));
        filaStats.add(this.crearStatCard("Operaciones en Historial", this.lblTotalComandos, Colores.SUCCESS));
        filaStats.add(this.crearStatCard("SMS Enviados", this.lblTotalSMS, Colores.WARNING));
        contenido.add(filaStats);
        contenido.add(Box.createVerticalStrut(20));
        PnlCurvo cardLog = new PnlCurvo(12, Colores.SURFACE);
        cardLog.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Colores.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colores.OUTLINE_LIGHT), new EmptyBorder(14, 20, 14, 20)));
        JLabel titulo = new JLabel("Log de Auditor\u00eda del Sistema");
        titulo.setFont(Colores.FONT_HEADLINE);
        titulo.setForeground(Colores.TEXT_PRIMARY);
        header.add((Component)titulo, "West");
        JLabel subtitulo = new JLabel("Registro de transacciones y eventos de seguridad");
        subtitulo.setFont(new Font("Inter", 2, 12));
        subtitulo.setForeground(Colores.TEXT_HINT);
        header.add((Component)subtitulo, "East");
        cardLog.add((Component)header, "North");
        this.panelLogs = new JPanel();
        this.panelLogs.setLayout(new BoxLayout(this.panelLogs, 1));
        this.panelLogs.setBackground(Colores.SURFACE);
        this.panelLogs.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel msgVacio = new JLabel("No hay eventos registrados. Realice una transacci\u00f3n en el Cajero.");
        msgVacio.setFont(Colores.FONT_BODY);
        msgVacio.setForeground(Colores.TEXT_HINT);
        this.panelLogs.add(msgVacio);
        JScrollPane scroll = new JScrollPane(this.panelLogs);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        cardLog.add((Component)scroll, "Center");
        contenido.add(cardLog);
        JScrollPane scrollPrincipal = new JScrollPane(contenido);
        scrollPrincipal.setBorder(null);
        this.add((Component)scrollPrincipal, "Center");
    }

    private PnlCurvo crearStatCard(String titulo, JLabel valorLabel, Color color) {
        PnlCurvo card = new PnlCurvo(12, Colores.SURFACE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 20, 16, 20));
        JPanel barra = new JPanel();
        barra.setPreferredSize(new Dimension(0, 3));
        barra.setBackground(color);
        card.add((Component)barra, "North");
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Inter", 0, 11));
        lbl.setForeground(Colores.TEXT_HINT);
        card.add((Component)lbl, "Center");
        valorLabel.setFont(new Font("Inter", 1, 28));
        valorLabel.setForeground(color);
        card.add((Component)valorLabel, "South");
        return card;
    }

    private JPanel crearFilaLog(String texto, int indice) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, indice % 2 == 0 ? Colores.ACCENT : Colores.SUCCESS), new EmptyBorder(10, 12, 10, 8)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel numLabel = new JLabel(String.format("#%03d", indice + 1));
        numLabel.setFont(new Font("Consolas", 1, 11));
        numLabel.setForeground(Colores.TEXT_HINT);
        numLabel.setPreferredSize(new Dimension(45, 20));
        fila.add((Component)numLabel, "West");
        JLabel content = new JLabel("<html>" + texto.replace("|", "<span style='color:#9CA3AF'> | </span>") + "</html>");
        content.setFont(new Font("Consolas", 0, 12));
        content.setForeground(Colores.TEXT_PRIMARY);
        fila.add((Component)content, "Center");
        return fila;
    }

    public void actualizarDatos() {
        List<String> registros = Auditoria.getInstance().getRegistros();
        List<String> sms = BaseDatos.getInstance().getLogsSMS();
        this.lblTotalEventos.setText(String.valueOf(registros.size()));
        this.lblTotalComandos.setText(String.valueOf(GestorCmd.getInstance().getTotalComandos()));
        this.lblTotalSMS.setText(String.valueOf(sms.size()));
        this.panelLogs.removeAll();
        if (registros.isEmpty()) {
            JLabel msg = new JLabel("No hay eventos de auditor\u00eda. Ejecute transacciones en el Cajero.");
            msg.setFont(Colores.FONT_BODY);
            msg.setForeground(Colores.TEXT_HINT);
            this.panelLogs.add(msg);
        } else {
            for (int i = registros.size() - 1; i >= 0; --i) {
                this.panelLogs.add(this.crearFilaLog(registros.get(i), i));
                this.panelLogs.add(Box.createVerticalStrut(4));
            }
        }
        this.panelLogs.revalidate();
        this.panelLogs.repaint();
    }
}
