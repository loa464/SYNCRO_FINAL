
package Syncro.Comportamiento;

import Syncro.Comportamiento.Observador;
import Syncro.Creacional.BaseDatos;

public class Notificador
implements Observador {
    private String telefonoDestino;
    private String nombreCliente;

    public Notificador(String telefonoDestino, String nombreCliente) {
        this.telefonoDestino = telefonoDestino;
        this.nombreCliente = nombreCliente;
    }

    @Override
    public void actualizar(String evento, String numeroCuenta, double monto, String mensaje) {
        String sms = "Banco Syncro | " + this.nombreCliente + ": " + mensaje + " Cuenta: " + numeroCuenta + " | Monto: $" + String.format("%,.2f", monto);
        BaseDatos.getInstance().registrarSMS("SMS -> " + this.telefonoDestino + ": " + sms);
        System.out.println("[Observer SMS] Enviado a " + this.telefonoDestino + ": " + sms);
    }

    public String getTelefonoDestino() {
        return this.telefonoDestino;
    }

    public String getNombreCliente() {
        return this.nombreCliente;
    }
}
