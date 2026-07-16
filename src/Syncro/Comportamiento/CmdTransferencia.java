package Syncro.Comportamiento;

import java.util.ArrayList;
import java.util.List;
import Syncro.Creacional.BaseDatos;
import Syncro.Modelos.CtaBase;
import Syncro.Modelos.Transaccion;

public class CmdTransferencia
implements Comando,
Sujeto {
    private final CtaBase origen;
    private final CtaBase destino;
    private final double monto;
    private final String descripcion;
    private boolean ejecutado = false;
    private final List<Observador> observadores = new ArrayList<Observador>();

    public CmdTransferencia(CtaBase origen, CtaBase destino, double monto, String descripcion) {
        this.origen = origen;
        this.destino = destino;
        this.monto = monto;
        this.descripcion = descripcion;
        this.agregarObservador(Auditoria.getInstance());
    }

    @Override
    public boolean ejecutar() {
        // Withdraw from origin
        boolean okRetiro = this.origen.retirar(this.monto);
        if (!okRetiro) {
            return false;
        }

        // Convert amount to destination currency using Adapter
        double montoDestino = Syncro.Estructural.AdapterMoneda.convertir(this.monto, this.origen.getMoneda(), this.destino.getMoneda());

        // Deposit into destination
        boolean okDeposito = this.destino.depositar(montoDestino);
        if (!okDeposito) {
            // Rollback withdrawal
            this.origen.depositar(this.monto);
            return false;
        }

        this.ejecutado = true;

        // Register single TRANSFERENCIA transaction log
        Transaccion tx = new Transaccion(Transaccion.TipoOperacion.TRANSFERENCIA, this.monto, this.origen.getNumeroCuenta(), true, this.descripcion);
        tx.setNumeroCuentaDestino(this.destino.getNumeroCuenta());
        BaseDatos.getInstance().registrarTransaccion(tx);

        // Persist updated balances to PostgreSQL
        BaseDatos.getInstance().actualizarSaldoCuenta(this.origen);
        BaseDatos.getInstance().actualizarSaldoCuenta(this.destino);

        this.notificarObservadores("TRANSFERENCIA", this.origen.getNumeroCuenta(), this.monto, this.descripcion);
        return true;
    }

    @Override
    public boolean deshacer() {
        if (!this.ejecutado) {
            return false;
        }

        // Convert amount to destination currency using Adapter
        double montoDestino = Syncro.Estructural.AdapterMoneda.convertir(this.monto, this.origen.getMoneda(), this.destino.getMoneda());

        // To undo, we withdraw from destination
        boolean okRetiro = this.destino.retirar(montoDestino);
        if (!okRetiro) {
            return false;
        }

        // And deposit back to origin
        boolean okDeposito = this.origen.depositar(this.monto);
        if (!okDeposito) {
            // Rollback withdrawal from destination
            this.destino.depositar(montoDestino);
            return false;
        }

        this.ejecutado = false;

        // Register a reverse TRANSFERENCIA transaction
        Transaccion tx = new Transaccion(Transaccion.TipoOperacion.TRANSFERENCIA, this.monto, this.destino.getNumeroCuenta(), true, "REVERSO: " + this.descripcion);
        tx.setNumeroCuentaDestino(this.origen.getNumeroCuenta());
        BaseDatos.getInstance().registrarTransaccion(tx);

        // Persist updated balances
        BaseDatos.getInstance().actualizarSaldoCuenta(this.origen);
        BaseDatos.getInstance().actualizarSaldoCuenta(this.destino);

        this.notificarObservadores("REVERSO_TRANSFERENCIA", this.origen.getNumeroCuenta(), this.monto, "Reverso de: " + this.descripcion);
        return true;
    }

    @Override
    public String getDescripcion() {
        return "Transferencia de $" + String.format("%,.2f", this.monto) + " de " + this.origen.getNumeroCuenta() + " a " + this.destino.getNumeroCuenta();
    }

    @Override
    public void agregarObservador(Observador o) {
        this.observadores.add(o);
    }

    @Override
    public void eliminarObservador(Observador o) {
        this.observadores.remove(o);
    }

    @Override
    public void notificarObservadores(String e, String nc, double m, String msg) {
        for (Observador o : this.observadores) {
            o.actualizar(e, nc, m, msg);
        }
    }
}
