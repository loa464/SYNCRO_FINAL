
package Syncro.Vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.JButton;
import Syncro.Vista.Colores;

public class BtnCurvo
extends JButton {
    private Color bgColor;
    private Color hoverColor;
    private Color currentBg;
    private int radius;

    public BtnCurvo(String text, final Color bgColor, final Color hoverColor, Color textColor) {
        super(text);
        this.bgColor = bgColor;
        this.hoverColor = hoverColor;
        this.currentBg = bgColor;
        this.radius = 10;
        this.setForeground(textColor);
        this.setFont(Colores.FONT_LABEL);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.setCursor(new Cursor(12));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                currentBg = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                currentBg = bgColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.currentBg);
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), this.radius, this.radius);
        g2.dispose();
        super.paintComponent(g);
    }

    public void setColors(Color bg, Color hover) {
        this.bgColor = bg;
        this.hoverColor = hover;
        this.currentBg = bg;
        this.repaint();
    }
}
