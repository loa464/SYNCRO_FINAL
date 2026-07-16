
package Syncro.Estructural;

import Syncro.Modelos.CtaBase;

public abstract class DecoCuenta
extends CtaBase {
    protected CtaBase cuentaDecorada;

    public DecoCuenta(CtaBase cuentaDecorada) {
        super(cuentaDecorada.getNumeroCuenta(), cuentaDecorada.getSaldo(), cuentaDecorada.getDniDueno());
        this.cuentaDecorada = cuentaDecorada;
    }

    @Override
    public boolean depositar(double monto) {
        return this.cuentaDecorada.depositar(monto);
    }

    @Override
    public boolean retirar(double monto) {
        return this.cuentaDecorada.retirar(monto);
    }

    @Override
    public String getTipoCuenta() {
        return this.cuentaDecorada.getTipoCuenta();
    }

    @Override
    public double getComision() {
        return this.cuentaDecorada.getComision();
    }

    @Override
    public double getSaldo() {
        return this.cuentaDecorada.getSaldo();
    }

    @Override
    public String getNumeroCuenta() {
        return this.cuentaDecorada.getNumeroCuenta();
    }

    @Override
    public String getDniDueno() {
        return this.cuentaDecorada.getDniDueno();
    }

    @Override
    public boolean isActiva() {
        return this.cuentaDecorada.isActiva();
    }

    @Override
    public void setActiva(boolean activa) {
        this.cuentaDecorada.setActiva(activa);
    }

    @Override
    public boolean isNotificacionesSMS() {
        return this.cuentaDecorada.isNotificacionesSMS();
    }

    @Override
    public void setNotificacionesSMS(boolean notificacionesSMS) {
        this.cuentaDecorada.setNotificacionesSMS(notificacionesSMS);
    }

    @Override
    public void setNumeroCuenta(String numeroCuenta) {
        this.cuentaDecorada.setNumeroCuenta(numeroCuenta);
    }

    @Override
    public void setEstrategiaInteres(Syncro.Comportamiento.Interes estrategia) {
        this.cuentaDecorada.setEstrategiaInteres(estrategia);
    }

    @Override
    public Syncro.Comportamiento.Interes getEstrategiaInteres() {
        return this.cuentaDecorada.getEstrategiaInteres();
    }

    @Override
    public double calcularInteres() {
        return this.cuentaDecorada.calcularInteres();
    }

    @Override
    public String toString() {
        return this.cuentaDecorada.toString();
    }

    public CtaBase getCuentaDecorada() {
        return this.cuentaDecorada;
    }
}
