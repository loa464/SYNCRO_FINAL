
package Syncro.Comportamiento;

import java.util.ArrayList;
import java.util.List;
import Syncro.Comportamiento.Comando;
import Syncro.Comportamiento.Observador;
import Syncro.Comportamiento.Auditoria;
import Syncro.Comportamiento.Sujeto;
import Syncro.Creacional.BaseDatos;
import Syncro.Modelos.CtaBase;
import Syncro.Modelos.Transaccion;

public class CmdDeposito
implements Comando,
Sujeto {
    private final CtaBase cuenta;
    private final double monto;
    private final String descripcion;
    private boolean ejecutado = false;
    private final List<Observador> observadores = new ArrayList<Observador>();

    public CmdDeposito(CtaBase cuenta, double monto, String descripcion) {
        this.cuenta = cuenta;
        this.monto = monto;
        this.descripcion = descripcion;
        this.agregarObservador(Auditoria.getInstance());
    }

    @Override
    public boolean ejecutar() {
        boolean exito = this.cuenta.depositar(this.monto);
        if (exito) {
            this.ejecutado = true;
            Transaccion tx = new Transaccion(Transaccion.TipoOperacion.DEPOSITO, this.monto, this.cuenta.getNumeroCuenta(), true, this.descripcion);
            BaseDatos.getInstance().registrarTransaccion(tx);
            BaseDatos.getInstance().actualizarSaldoCuenta(this.cuenta);
            this.notificarObservadores("DEPOSITO", this.cuenta.getNumeroCuenta(), this.monto, this.descripcion);
        }
        return exito;
    }

    @Override
    public boolean deshacer() {
        if (!this.ejecutado) {
            return false;
        }
        boolean exito = this.cuenta.retirar(this.monto);
        if (exito) {
            this.ejecutado = false;
            Transaccion tx = new Transaccion(Transaccion.TipoOperacion.RETIRO, this.monto, this.cuenta.getNumeroCuenta(), true, "REVERSO: " + this.descripcion);
            BaseDatos.getInstance().registrarTransaccion(tx);
            BaseDatos.getInstance().actualizarSaldoCuenta(this.cuenta);
            this.notificarObservadores("REVERSO_DEPOSITO", this.cuenta.getNumeroCuenta(), this.monto, "Reverso de: " + this.descripcion);
        }
        return exito;
    }

    @Override
    public String getDescripcion() {
        return "Dep\u00f3sito de $" + String.format("%,.2f", this.monto) + " en " + this.cuenta.getNumeroCuenta();
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
    public void notificarObservadores(String evento, String numCuenta, double monto, String msg) {
        for (Observador o : this.observadores) {
            o.actualizar(evento, numCuenta, monto, msg);
        }
    }
}
