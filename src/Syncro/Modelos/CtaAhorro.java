
package Syncro.Modelos;

import Syncro.Modelos.CtaBase;
import Syncro.Comportamiento.InteresImpl;

public class CtaAhorro
extends CtaBase {
    public CtaAhorro(double saldoInicial, String dniDueno) {
        super(saldoInicial, dniDueno);
        inicializarEstrategia(saldoInicial);
    }

    public CtaAhorro(String numeroCuenta, double saldoInicial, String dniDueno) {
        super(numeroCuenta, saldoInicial, dniDueno);
        inicializarEstrategia(saldoInicial);
    }

    private void inicializarEstrategia(double saldo) {
        if (saldo >= 10000.0) {
            this.setEstrategiaInteres(new InteresImpl.InteresVIP());
        } else {
            this.setEstrategiaInteres(new InteresImpl.InteresEstandar());
        }
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= 0.0) {
            return false;
        }
        if (!this.isActiva()) {
            return false;
        }
        if (monto > this.getSaldo()) {
            return false;
        }
        this.setSaldo(this.getSaldo() - monto);
        return true;
    }

    @Override
    public String getTipoCuenta() {
        return "Ahorros";
    }

    @Override
    public double getComision() {
        return 0.0;
    }
}
