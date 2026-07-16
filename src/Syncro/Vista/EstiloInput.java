package Syncro.Vista;

import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class EstiloInput {

    public static void aplicarFocoDinamico(JTextField field) {
        // Configure native FlatLaf margin for text fields (keeps it comfortable and spacious)
        field.putClientProperty("JTextField.margin", new Insets(6, 10, 6, 10));
    }

    public static void aplicarFocoDinamico(JComboBox<?> combo) {
        // Configure native FlatLaf padding for combo boxes (keeps it comfortable and spacious)
        combo.putClientProperty("JComboBox.padding", new Insets(6, 10, 6, 10));
    }

    public static void limitarSoloNumerosMaxLongitud(JTextField campo, int maxLongitud) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < string.length(); i++) {
                    char c = string.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                String filtered = sb.toString();
                int currentLength = fb.getDocument().getLength();
                if (currentLength + filtered.length() <= maxLongitud) {
                    super.insertString(fb, offset, filtered, attr);
                } else {
                    int disponible = maxLongitud - currentLength;
                    if (disponible > 0) {
                        super.insertString(fb, offset, filtered.substring(0, disponible), attr);
                    }
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, null, attrs);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                String filtered = sb.toString();
                int currentLength = fb.getDocument().getLength();
                int nextLength = currentLength - length + filtered.length();
                if (nextLength <= maxLongitud) {
                    super.replace(fb, offset, length, filtered, attrs);
                } else {
                    int disponible = maxLongitud - (currentLength - length);
                    if (disponible > 0) {
                        super.replace(fb, offset, length, filtered.substring(0, disponible), attrs);
                    }
                }
            }
        });
    }

    public static void bloquearNumeros(JTextField campo) {
        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < string.length(); i++) {
                    char c = string.charAt(i);
                    if (!Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                super.insertString(fb, offset, sb.toString(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, null, attrs);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (!Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                super.replace(fb, offset, length, sb.toString(), attrs);
            }
        });
    }
}
