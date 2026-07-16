
package Syncro.Modelos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaccion {
    private static int contadorId = 0;
    private int id = ++contadorId;
    private TipoOperacion tipo;
    private double monto;
    private String numeroCuenta;
    private String numeroCuentaDestino;
    private LocalDateTime fechaHora;
    private boolean exitosa;
    private String descripcion;

    public Transaccion(TipoOperacion tipo, double monto, String numeroCuenta, boolean exitosa, String descripcion) {
        this.tipo = tipo;
        this.monto = monto;
        this.numeroCuenta = numeroCuenta;
        this.numeroCuentaDestino = null;
        this.fechaHora = LocalDateTime.now();
        this.exitosa = exitosa;
        this.descripcion = descripcion;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.fechaHora.format(formatter);
    }

    public String getHoraCorta() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return this.fechaHora.format(formatter);
    }

    public int getId() {
        return this.id;
    }

    public TipoOperacion getTipo() {
        return this.tipo;
    }

    public double getMonto() {
        return this.monto;
    }

    public String getNumeroCuenta() {
        return this.numeroCuenta;
    }

    public String getNumeroCuentaDestino() {
        return this.numeroCuentaDestino;
    }

    public void setNumeroCuentaDestino(String numeroCuentaDestino) {
        this.numeroCuentaDestino = numeroCuentaDestino;
    }

    public LocalDateTime getFechaHora() {
        return this.fechaHora;
    }

    public boolean isExitosa() {
        return this.exitosa;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public String toString() {
        return "Transaccion{id=" + this.id + ", tipo=" + this.tipo.getDescripcion() + ", monto=$" + String.format("%.2f", this.monto) + "}";
    }

    public static enum TipoOperacion {
        DEPOSITO("Dep\u00f3sito"),
        RETIRO("Retiro"),
        TRANSFERENCIA("Transferencia");

        private final String descripcion;

        private TipoOperacion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return this.descripcion;
        }
    }
}
