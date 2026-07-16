package Syncro.Vista;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class PnlGradiente extends JPanel {
    private final int cornerRadius;
    private final Color colorStart;
    private final Color colorEnd;
    private boolean hovered = false;

    public PnlGradiente(int radius, Color colorStart, Color colorEnd) {
        this.cornerRadius = radius;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.setOpaque(false);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offset = hovered ? 4 : 0;
        int shadowAlpha = hovered ? 35 : 15;

        // Draw shadow
        g2.setColor(new Color(0, 0, 0, shadowAlpha));
        g2.fillRoundRect(3, 6, getWidth() - 7, getHeight() - 7, cornerRadius, cornerRadius);

        // Draw background
        g2.translate(0, -offset);
        GradientPaint gp = new GradientPaint(0, 0, colorStart, getWidth(), getHeight(), colorEnd);
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, cornerRadius, cornerRadius);

        // Draw border
        g2.setColor(new Color(255, 255, 255, 60));
        g2.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, cornerRadius, cornerRadius);

        // Draw children
        super.paint(g2);

        g2.dispose();
    }
}
