package Syncro.Estructural;

import Syncro.Modelos.Moneda;

public class AdapterMoneda {
    // Tipo de cambio fijo para la demostración
    private static final double TIPO_CAMBIO_USD_PEN = 3.80;

    /**
     * Convierte un monto de una moneda origen a una moneda destino.
     */
    public static double convertir(double monto, Moneda origen, Moneda destino) {
        if (origen == destino) {
            return monto;
        }

        if (origen == Moneda.SOLES && destino == Moneda.DOLARES) {
            return monto / TIPO_CAMBIO_USD_PEN;
        } else if (origen == Moneda.DOLARES && destino == Moneda.SOLES) {
            return monto * TIPO_CAMBIO_USD_PEN;
        }

        return monto;
    }
}
