
package Syncro.Estructural;

import Syncro.Creacional.BaseDatos;
import Syncro.Modelos.CtaBase;
import Syncro.Estructural.DecoCuenta;

public class DecoSMS
extends DecoCuenta {
    private String telefonoDestino;

    public DecoSMS(CtaBase cuentaDecorada, String telefonoDestino) {
        super(cuentaDecorada);
        this.telefonoDestino = telefonoDestino;
    }

    @Override
    public boolean retirar(double monto) {
        boolean exito = this.cuentaDecorada.retirar(monto);
        if (exito) {
            String mensajeSMS = "\"Banco Syncro: Su retiro de $" + String.format("%.2f", monto) + " fue exitoso. Saldo actual: $" + String.format("%.2f", this.cuentaDecorada.getSaldo()) + "\"";
            String logCompleto = "SMS enviado a: " + this.telefonoDestino + " \u2192 " + mensajeSMS;
            BaseDatos.getInstance().registrarSMS(logCompleto);
            System.out.println("[DECORATOR SMS] " + logCompleto);
        }
        return exito;
    }

    @Override
    public boolean depositar(double monto) {
        boolean exito = this.cuentaDecorada.depositar(monto);
        if (exito) {
            String mensajeSMS = "\"Banco Syncro: Dep\u00f3sito de $" + String.format("%.2f", monto) + " recibido. Saldo actual: $" + String.format("%.2f", this.cuentaDecorada.getSaldo()) + "\"";
            String logCompleto = "SMS enviado a: " + this.telefonoDestino + " \u2192 " + mensajeSMS;
            BaseDatos.getInstance().registrarSMS(logCompleto);
            System.out.println("[DECORATOR SMS] " + logCompleto);
        }
        return exito;
    }

    public String getTelefonoDestino() {
        return this.telefonoDestino;
    }
}
