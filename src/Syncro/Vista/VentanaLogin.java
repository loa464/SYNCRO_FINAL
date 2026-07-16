package Syncro.Vista;

import Syncro.Comportamiento.ContextoSesion;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public VentanaLogin() {
        setTitle("Syncro - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Colores.BACKGROUND);
        
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(new BoxLayout(pnlForm, BoxLayout.Y_AXIS));
        pnlForm.setBackground(Colores.SURFACE);
        pnlForm.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JLabel lblTitulo = new JLabel("Bienvenido a Syncro");
        lblTitulo.setFont(new Font("Inter", Font.BOLD, 24));
        lblTitulo.setForeground(Colores.TEXT_PRIMARY);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSub = new JLabel("Ingresa tus credenciales");
        lblSub.setFont(Colores.FONT_BODY);
        lblSub.setForeground(Colores.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pnlForm.add(lblTitulo);
        pnlForm.add(Box.createVerticalStrut(8));
        pnlForm.add(lblSub);
        pnlForm.add(Box.createVerticalStrut(40));
        
        JLabel lblUser = new JLabel("Usuario");
        lblUser.setFont(Colores.FONT_LABEL);
        lblUser.setForeground(Colores.TEXT_SECONDARY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsuario.setFont(Colores.FONT_BODY);
        EstiloInput.aplicarFocoDinamico(txtUsuario);
        
        pnlForm.add(lblUser);
        pnlForm.add(Box.createVerticalStrut(8));
        pnlForm.add(txtUsuario);
        pnlForm.add(Box.createVerticalStrut(20));
        
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(Colores.FONT_LABEL);
        lblPass.setForeground(Colores.TEXT_SECONDARY);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setFont(Colores.FONT_BODY);
        EstiloInput.aplicarFocoDinamico(txtPassword);
        
        pnlForm.add(lblPass);
        pnlForm.add(Box.createVerticalStrut(8));
        pnlForm.add(txtPassword);
        pnlForm.add(Box.createVerticalStrut(40));
        
        BtnCurvo btnIngresar = new BtnCurvo("Ingresar", Colores.PRIMARY, Colores.PRIMARY_LIGHT, Colores.TEXT_WHITE);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.addActionListener(e -> autenticar());
        
        pnlForm.add(btnIngresar);
        
        pnlMain.add(pnlForm, BorderLayout.CENTER);
        add(pnlMain);
    }

    private void autenticar() {
        String user = txtUsuario.getText();
        String pass = new String(txtPassword.getPassword());
        
        try {
            ContextoSesion.getInstance().autenticar(user, pass);
            
            Ventana mainVentana = new Ventana();
            ContextoSesion.getInstance().getEstado().configurarVentana(mainVentana);
            
            mainVentana.setVisible(true);
            this.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
