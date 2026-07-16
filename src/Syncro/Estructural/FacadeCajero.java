
package Syncro.Estructural;

import Syncro.Comportamiento.CmdDeposito;
import Syncro.Comportamiento.GestorCmd;
import Syncro.Comportamiento.Notificador;
import Syncro.Comportamiento.Auditoria;
import Syncro.Comportamiento.CmdRetiro;
import Syncro.Comportamiento.CmdTransferencia;
import Syncro.Creacional.BuilderCliente;
import Syncro.Creacional.FactoryCta;
import Syncro.Creacional.BaseDatos;
import Syncro.Modelos.Cliente;
import Syncro.Modelos.CtaBase;
import Syncro.Modelos.Transaccion;

public class FacadeCajero {
    private final BaseDatos db = BaseDatos.getInstance();
    private final GestorCmd gestor = GestorCmd.getInstance();

    public String registrarCliente(String dni, String nombre, String telefono, String correo) {
        try {
            Cliente nuevo = new BuilderCliente(dni).nombre(nombre).telefono(telefono).correo(correo).build();
            boolean ok = this.db.registrarCliente(nuevo);
            return ok ? "\u2713 Cliente '" + nombre + "' registrado con DNI: " + dni : "\u2717 Error: Ya existe un cliente con DNI: " + dni;
        }
        catch (IllegalStateException e) {
            return "\u2717 Error de validaci\u00f3n: " + e.getMessage();
        }
    }

    public String aperturarCuenta(String dniCliente, String tipoCuenta, double saldoInicial, boolean activarSMS, Syncro.Modelos.Moneda moneda) {
        Cliente cliente = this.db.buscarClientePorDni(dniCliente);
        if (cliente == null) {
            return "\u2717 Error: No existe cliente con DNI: " + dniCliente;
        }
        CtaBase nueva = FactoryCta.crearCuenta(tipoCuenta, saldoInicial, dniCliente, moneda);
        nueva.setNotificacionesSMS(activarSMS);
        this.db.registrarCuenta(nueva);
        Auditoria.getInstance().actualizar("APERTURA", nueva.getNumeroCuenta(), saldoInicial, "Apertura de cuenta " + tipoCuenta + " en " + moneda.name() + " para " + cliente.getNombre());
        if (activarSMS && !cliente.getTelefono().isEmpty()) {
            this.db.registrarSMS("SMS -> " + cliente.getTelefono() + ": Nueva cuenta " + tipoCuenta + " aperturada: " + nueva.getNumeroCuenta() + " | Saldo: " + moneda.getSimbolo() + String.format("%,.2f", saldoInicial));
        }
        return "\u2713 Cuenta " + tipoCuenta + " creada: " + nueva.getNumeroCuenta() + " | Saldo: " + moneda.getSimbolo() + String.format("%.2f", saldoInicial) + " | SMS: " + (activarSMS ? "Activado" : "Desactivado");
    }

    public String realizarDeposito(String numeroCuenta, double monto) {
        boolean ok;
        Cliente dueno;
        CtaBase cuenta = this.db.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            return "\u2717 Cuenta no encontrada: " + numeroCuenta;
        }

        if (this.db.isSeguroAntiFraude() && monto > 10000.0) {
            return "\u2717 Transacción rechazada por el Sistema Anti-Fraude: El monto supera el límite permitido ($10,000).";
        }

        CtaBase cuentaAProcesar = cuenta;
        if (cuenta.isNotificacionesSMS() && (dueno = this.db.buscarClientePorDni(cuenta.getDniDueno())) != null && !dueno.getTelefono().isEmpty()) {
            cuentaAProcesar = new DecoSMS(cuenta, dueno.getTelefono());
        }

        CmdDeposito cmd = new CmdDeposito(cuentaAProcesar, monto, "Dep\u00f3sito en ventanilla");
        if (cuenta.isNotificacionesSMS() && (dueno = this.db.buscarClientePorDni(cuenta.getDniDueno())) != null && !dueno.getTelefono().isEmpty()) {
            cmd.agregarObservador(new Notificador(dueno.getTelefono(), dueno.getNombre()));
        }
        if (ok = this.gestor.ejecutar(cmd)) {
            return "\u2713 Dep\u00f3sito de $" + String.format("%.2f", monto) + " exitoso. Nuevo saldo: $" + String.format("%.2f", cuenta.getSaldo());
        }
        return "\u2717 No se pudo realizar el dep\u00f3sito. Verifique el monto.";
    }

    public String realizarRetiro(String numeroCuenta, double monto) {
        boolean ok;
        Cliente dueno;
        CtaBase cuenta = this.db.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            return "\u2717 Cuenta no encontrada: " + numeroCuenta;
        }

        if (this.db.isSeguroAntiFraude() && monto > 10000.0) {
            return "\u2717 Transacción rechazada por el Sistema Anti-Fraude: El monto supera el límite permitido ($10,000).";
        }

        CtaBase cuentaAProcesar = cuenta;
        if (cuenta.isNotificacionesSMS() && (dueno = this.db.buscarClientePorDni(cuenta.getDniDueno())) != null && !dueno.getTelefono().isEmpty()) {
            cuentaAProcesar = new DecoSMS(cuenta, dueno.getTelefono());
        }

        CmdRetiro cmd = new CmdRetiro(cuentaAProcesar, monto, "Retiro en ventanilla");
        if (cuenta.isNotificacionesSMS() && (dueno = this.db.buscarClientePorDni(cuenta.getDniDueno())) != null && !dueno.getTelefono().isEmpty()) {
            cmd.agregarObservador(new Notificador(dueno.getTelefono(), dueno.getNombre()));
        }
        if (ok = this.gestor.ejecutar(cmd)) {
            return "\u2713 Retiro de $" + String.format("%.2f", monto) + " exitoso. Nuevo saldo: $" + String.format("%.2f", cuenta.getSaldo());
        }
        return "\u2717 Retiro rechazado: fondos insuficientes o cuenta inactiva.";
    }

    public String realizarTransferencia(String cuentaOrigen, String cuentaDestino, double monto) {
        CtaBase origen = this.db.buscarCuentaPorNumero(cuentaOrigen);
        CtaBase destino = this.db.buscarCuentaPorNumero(cuentaDestino);
        if (origen == null) {
            return "\u2717 Cuenta origen no encontrada: " + cuentaOrigen;
        }
        if (destino == null) {
            return "\u2717 Cuenta destino no encontrada: " + cuentaDestino;
        }

        if (this.db.isSeguroAntiFraude() && monto > 10000.0) {
            return "\u2717 Transacción rechazada por el Sistema Anti-Fraude: El monto supera el límite permitido ($10,000).";
        }

        CtaBase origenProcesar = origen;
        Cliente duenoOrigen = this.db.buscarClientePorDni(origen.getDniDueno());
        if (origen.isNotificacionesSMS() && duenoOrigen != null && !duenoOrigen.getTelefono().isEmpty()) {
            origenProcesar = new DecoSMS(origen, duenoOrigen.getTelefono());
        }

        CtaBase destinoProcesar = destino;
        Cliente duenoDestino = this.db.buscarClientePorDni(destino.getDniDueno());
        if (destino.isNotificacionesSMS() && duenoDestino != null && !duenoDestino.getTelefono().isEmpty()) {
            destinoProcesar = new DecoSMS(destino, duenoDestino.getTelefono());
        }

        CmdTransferencia cmd = new CmdTransferencia(origenProcesar, destinoProcesar, monto, "Transferencia a " + cuentaDestino);
        if (origen.isNotificacionesSMS() && duenoOrigen != null && !duenoOrigen.getTelefono().isEmpty()) {
            cmd.agregarObservador(new Notificador(duenoOrigen.getTelefono(), duenoOrigen.getNombre()));
        }
        if (destino.isNotificacionesSMS() && duenoDestino != null && !duenoDestino.getTelefono().isEmpty()) {
            cmd.agregarObservador(new Notificador(duenoDestino.getTelefono(), duenoDestino.getNombre()));
        }

        boolean ok = this.gestor.ejecutar(cmd);
        if (ok) {
            return "\u2713 Transferencia de $" + String.format("%.2f", monto) + " exitosa. Origen: $" + String.format("%.2f", origen.getSaldo()) + " | Destino: $" + String.format("%.2f", destino.getSaldo());
        }
        return "\u2717 Transferencia rechazada: fondos insuficientes, cuenta inactiva o error en destino.";
    }

    public BaseDatos getDatabase() {
        return this.db;
    }
}
