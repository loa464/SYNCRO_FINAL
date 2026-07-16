
package Syncro.Modelos;

import Syncro.Modelos.CtaBase;
import Syncro.Comportamiento.InteresImpl;
import Syncro.Creacional.BaseDatos;

public class CtaCorriente
extends CtaBase {
    private static final double LIMITE_SOBREGIRO = -500.0;
    private static final double COMISION_RETIRO = 1.5;

    public CtaCorriente(double saldoInicial, String dniDueno) {
        super(saldoInicial, dniDueno);
        this.setEstrategiaInteres(new InteresImpl.SinInteres());
    }

    public CtaCorriente(String numeroCuenta, double saldoInicial, String dniDueno) {
        super(numeroCuenta, saldoInicial, dniDueno);
        this.setEstrategiaInteres(new InteresImpl.SinInteres());
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= 0.0) {
            return false;
        }
        if (!this.isActiva()) {
            return false;
        }
        double limite = BaseDatos.getInstance().isSobregiroHabilitado() ? LIMITE_SOBREGIRO : 0.0;
        double saldoResultante = this.getSaldo() - monto - 1.5;
        if (saldoResultante < limite) {
            return false;
        }
        this.setSaldo(saldoResultante);
        return true;
    }

    @Override
    public String getTipoCuenta() {
        return "Corriente";
    }

    @Override
    public double getComision() {
        return 1.5;
    }

    public static double getLimiteSobregiro() {
        return -500.0;
    }
}
