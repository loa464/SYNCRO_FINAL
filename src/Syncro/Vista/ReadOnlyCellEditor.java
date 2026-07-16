package Syncro.Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableCellRenderer;

public class ReadOnlyCellEditor extends DefaultCellEditor {
    public ReadOnlyCellEditor() {
        super(new JTextField());
        JTextField tf = (JTextField) getComponent();
        tf.setEditable(false);
        tf.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        
        // 2 clicks to edit ensures standard row selection and dragging are unaffected,
        // while double-clicking a cell opens the text field to select/copy sub-text.
        setClickCountToStart(2);
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem copyItem = new JMenuItem("Copiar");
        copyItem.addActionListener(e -> tf.copy());
        popup.add(copyItem);
        
        tf.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mostrarPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mostrarPopup(e);
            }

            private void mostrarPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JTextField tf = (JTextField) getComponent();
        tf.setText(value != null ? value.toString() : "");
        tf.setFont(table.getFont());
        
        // Match JTable selection visual styles
        tf.setBackground(table.getSelectionBackground());
        tf.setForeground(table.getSelectionForeground());
        tf.setSelectedTextColor(table.getSelectionForeground());
        tf.setSelectionColor(new Color(255, 255, 255, 100)); // semi-transparent white highlight
        
        // Dynamically align text matching the custom column renderer
        int align = JTextField.LEFT;
        var renderer = table.getCellRenderer(row, column);
        if (renderer instanceof DefaultTableCellRenderer) {
            align = ((DefaultTableCellRenderer) renderer).getHorizontalAlignment();
        }
        tf.setHorizontalAlignment(align);
        
        return tf;
    }
}
