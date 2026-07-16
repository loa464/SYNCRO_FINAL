
package Syncro.Vista;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import Syncro.Vista.Colores;

public class PnlCurvo
extends JPanel {
    private int cornerRadius;
    private Color borderColor;
    private Color bgColor;

    public PnlCurvo(int radius, Color bgColor) {
        this.cornerRadius = radius;
        this.bgColor = bgColor;
        this.borderColor = Colores.OUTLINE_LIGHT;
        this.setOpaque(false);
    }

    public PnlCurvo(int radius, Color bgColor, Color borderColor) {
        this(radius, bgColor);
        this.borderColor = borderColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 15));
        g2.fillRoundRect(2, 2, this.getWidth() - 3, this.getHeight() - 3, this.cornerRadius, this.cornerRadius);
        g2.setColor(this.bgColor);
        g2.fillRoundRect(0, 0, this.getWidth() - 3, this.getHeight() - 3, this.cornerRadius, this.cornerRadius);
        g2.setColor(this.borderColor);
        g2.drawRoundRect(0, 0, this.getWidth() - 4, this.getHeight() - 4, this.cornerRadius, this.cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }
}
