
package Syncro.Comportamiento;

import Syncro.Comportamiento.Interes;

public class InteresImpl {

    public static class SinInteres
    implements Interes {
        @Override
        public double calcular(double saldo) {
            return 0.0;
        }

        @Override
        public String getNombre() {
            return "Sin Inter\u00e9s (N\u00f3mina / Corriente)";
        }
    }

    public static class InteresVIP
    implements Interes {
        private static final double TASA = 0.04;

        @Override
        public double calcular(double saldo) {
            return saldo * 0.04;
        }

        @Override
        public String getNombre() {
            return "Inter\u00e9s VIP (4% anual)";
        }
    }

    public static class InteresEstandar
    implements Interes {
        private static final double TASA = 0.02;

        @Override
        public double calcular(double saldo) {
            return saldo * 0.02;
        }

        @Override
        public String getNombre() {
            return "Inter\u00e9s Est\u00e1ndar (2% anual)";
        }
    }
}
