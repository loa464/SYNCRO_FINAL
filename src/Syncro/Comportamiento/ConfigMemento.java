package Syncro.Comportamiento;

/**
 * Memento del patrón de diseño Memento.
 * Guarda de manera inmutable el estado de la configuración global.
 */
public class ConfigMemento {
    private final boolean sobregiroHabilitado;
    private final boolean seguroAntiFraude;

    public ConfigMemento(boolean sobregiroHabilitado, boolean seguroAntiFraude) {
        this.sobregiroHabilitado = sobregiroHabilitado;
        this.seguroAntiFraude = seguroAntiFraude;
    }

    public boolean isSobregiroHabilitado() {
        return sobregiroHabilitado;
    }

    public boolean isSeguroAntiFraude() {
        return seguroAntiFraude;
    }
}
