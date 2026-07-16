package Syncro;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import Syncro.Creacional.BuilderCliente;
import Syncro.Creacional.FactoryCta;
import Syncro.Creacional.BaseDatos;
import Syncro.Vista.Ventana;
import Syncro.Modelos.Cliente;
import Syncro.Modelos.CtaBase;
import Syncro.Modelos.Transaccion;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Component.arc", 8);
            UIManager.put("Button.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("Component.focusWidth", 3);
            UIManager.put("Component.focusColor", new Color(37, 99, 235, 60));
            UIManager.put("Component.focusedBorderColor", new Color(37, 99, 235));
            UIManager.put("Component.borderColor", new Color(210, 214, 219));
            UIManager.put("Component.hoverBorderColor", new Color(156, 163, 175));
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.showButtons", false);
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("TitlePane.unifiedBackground", true);
        }
        catch (Exception ex) {
            System.err.println("No se pudo inicializar FlatLaf. Usando Look & Feel por defecto.");
            ex.printStackTrace();
        }
        
        boolean conectado = BaseDatos.getInstance().cargarDesdePostgres();
        if (!conectado) {
            Main.cargarDatosDemo();
        }
        
        SwingUtilities.invokeLater(() -> {
            Syncro.Vista.VentanaLogin login = new Syncro.Vista.VentanaLogin();
            login.setVisible(true);
        });
    }

    private static void cargarDatosDemo() {
        BaseDatos db = BaseDatos.getInstance();
        Cliente cliente1 = new BuilderCliente("45928102").nombre("Juan Pérez").telefono("955501920").correo("juan.perez@mail.com").build();
        Cliente cliente2 = new BuilderCliente("38741256").nombre("María García").telefono("955588210").correo("maria.garcia@mail.com").build();
        Cliente cliente3 = new BuilderCliente("52103987").nombre("Carlos Ruiz").telefono("955533470").correo("carlos.ruiz@mail.com").build();
        Cliente cliente4 = new BuilderCliente("61287430").nombre("Elena Torres").telefono("955555900").correo("elena.torres@mail.com").build();
        db.registrarCliente(cliente1);
        db.registrarCliente(cliente2);
        db.registrarCliente(cliente3);
        db.registrarCliente(cliente4);
        
        CtaBase cuenta1 = FactoryCta.crearCuenta("Ahorros", 1500.0, "45928102", Syncro.Modelos.Moneda.SOLES);
        cuenta1.setNotificacionesSMS(true);
        db.registrarCuenta(cuenta1);
        
        CtaBase cuenta2 = FactoryCta.crearCuenta("Corriente", 2500.0, "38741256", Syncro.Modelos.Moneda.SOLES);
        cuenta2.setNotificacionesSMS(true);
        db.registrarCuenta(cuenta2);
        
        CtaBase cuenta3 = FactoryCta.crearCuenta("Ahorros", 850.5, "52103987", Syncro.Modelos.Moneda.SOLES);
        db.registrarCuenta(cuenta3);
        
        CtaBase cuenta4 = FactoryCta.crearCuenta("Corriente", 12400.0, "61287430", Syncro.Modelos.Moneda.SOLES);
        cuenta4.setNotificacionesSMS(true);
        db.registrarCuenta(cuenta4);
        
        db.registrarTransaccion(new Transaccion(Transaccion.TipoOperacion.DEPOSITO, 150000.0, cuenta1.getNumeroCuenta(), true, "Depósito Internacional"));
        db.registrarTransaccion(new Transaccion(Transaccion.TipoOperacion.RETIRO, 2500.0, cuenta2.getNumeroCuenta(), true, "Retiro Efectivo ATM"));
        db.registrarTransaccion(new Transaccion(Transaccion.TipoOperacion.DEPOSITO, 3500.0, cuenta4.getNumeroCuenta(), true, "Transferencia Recibida - Nómina"));
        db.registrarSMS("SMS enviado a: 955501920 → \"Banco Syncro: Depósito de $150,000.00 exitoso. Saldo actual: $151,500.00\"");
        db.registrarSMS("SMS enviado a: 955588210 → \"Banco Syncro: Retiro de $2,500.00 procesado. Saldo actual: $0.00\"");
        System.out.println("✓ Datos de demostración cargados exitosamente.");
    }
}
