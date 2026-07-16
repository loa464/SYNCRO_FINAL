
package Syncro.Modelos;

import Syncro.Comportamiento.Interes;

public abstract class CtaBase {
    private String numeroCuenta;
    private double saldo;
    private String dniDueno;
    private boolean activa;
    private boolean notificacionesSMS;
    private static int contadorCuentas = 1000;
    private Interes estrategiaInteres;
    private Moneda moneda;

    public CtaBase(double saldoInicial, String dniDueno) {
        this.numeroCuenta = "SYN-" + String.format("%06d", ++contadorCuentas);
        this.saldo = saldoInicial;
        this.dniDueno = dniDueno;
        this.activa = true;
        this.notificacionesSMS = false;
        this.moneda = Moneda.SOLES; // Default
    }

    public CtaBase(String numeroCuenta, double saldoInicial, String dniDueno) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
        this.dniDueno = dniDueno;
        this.activa = true;
        this.notificacionesSMS = false;
        this.moneda = Moneda.SOLES; // Default
    }

    public abstract boolean retirar(double var1);

    public abstract String getTipoCuenta();

    public abstract double getComision();

    public boolean depositar(double monto) {
        if (monto <= 0.0) {
            return false;
        }
        if (!this.activa) {
            return false;
        }
        this.saldo += monto;
        return true;
    }

    public String getNumeroCuenta() {
        return this.numeroCuenta;
    }

    public double getSaldo() {
        return this.saldo;
    }

    protected void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getDniDueno() {
        return this.dniDueno;
    }

    public boolean isActiva() {
        return this.activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isNotificacionesSMS() {
        return this.notificacionesSMS;
    }

    public void setNotificacionesSMS(boolean notificacionesSMS) {
        this.notificacionesSMS = notificacionesSMS;
    }

    
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void setEstrategiaInteres(Interes estrategia) {
        this.estrategiaInteres = estrategia;
    }

    public Interes getEstrategiaInteres() {
        return this.estrategiaInteres;
    }

    public double calcularInteres() {
        if (this.estrategiaInteres != null) {
            return this.estrategiaInteres.calcular(this.saldo);
        }
        return 0.0;
    }

    public static void resetContador() {
        contadorCuentas = 1000;
    }

    public static void setContador(int valor) {
        contadorCuentas = valor;
    }

    public Moneda getMoneda() {
        return this.moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public String toString() {
        return "Cuenta{" + this.numeroCuenta + ", saldo=" + this.moneda.getSimbolo() + String.format("%.2f", this.saldo) + ", tipo=" + this.getTipoCuenta() + "}";
    }
}
