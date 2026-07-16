package Syncro.Modelos;

public enum Moneda {
    SOLES("S/."),
    DOLARES("$");

    private String simbolo;

    private Moneda(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return this.simbolo;
    }
}
