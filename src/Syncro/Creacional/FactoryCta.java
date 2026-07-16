
package Syncro.Creacional;

import Syncro.Modelos.CtaAhorro;
import Syncro.Modelos.CtaBase;
import Syncro.Modelos.CtaCorriente;

public class FactoryCta {
    public static final String TIPO_AHORRO = "Ahorros";
    public static final String TIPO_CORRIENTE = "Corriente";

    public static CtaBase crearCuenta(String tipo, double saldoInicial, String dniDueno, Syncro.Modelos.Moneda moneda) {
        CtaBase cuenta = null;
        switch (tipo) {
            case "Ahorros": {
                cuenta = new CtaAhorro(saldoInicial, dniDueno);
                break;
            }
            case "Corriente": {
                cuenta = new CtaCorriente(saldoInicial, dniDueno);
                break;
            }
            default:
                throw new IllegalArgumentException("Tipo de cuenta no reconocido: '" + tipo + "'. Use FactoryCta.TIPO_AHORRO o FactoryCta.TIPO_CORRIENTE.");
        }
        if (moneda != null) cuenta.setMoneda(moneda);
        return cuenta;
    }

    public static CtaBase crearCuentaExistente(String tipo, String numeroCuenta, double saldo, String dniDueno, Syncro.Modelos.Moneda moneda) {
        CtaBase cuenta = null;
        switch (tipo) {
            case "Ahorros": {
                cuenta = new CtaAhorro(numeroCuenta, saldo, dniDueno);
                break;
            }
            case "Corriente": {
                cuenta = new CtaCorriente(numeroCuenta, saldo, dniDueno);
                break;
            }
            default:
                throw new IllegalArgumentException("Tipo de cuenta no reconocido: '" + tipo + "'");
        }
        if (moneda != null) cuenta.setMoneda(moneda);
        return cuenta;
    }

    public static String[] getTiposDisponibles() {
        return new String[]{TIPO_AHORRO, TIPO_CORRIENTE};
    }
}
