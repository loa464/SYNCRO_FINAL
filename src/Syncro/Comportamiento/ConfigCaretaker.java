package Syncro.Comportamiento;

import java.util.Stack;

/**
 * Conserje (Caretaker) del patrón de diseño Memento.
 * Administra el historial de mementos guardados en memoria.
 */
public class ConfigCaretaker {
    private static ConfigCaretaker instancia = null;
    private final Stack<ConfigMemento> historial = new Stack<>();

    private ConfigCaretaker() {}

    public static synchronized ConfigCaretaker getInstance() {
        if (instancia == null) {
            instancia = new ConfigCaretaker();
        }
        return instancia;
    }

    public void guardar(ConfigMemento memento) {
        historial.push(memento);
    }

    public ConfigMemento deshacer() {
        if (historial.isEmpty()) {
            return null;
        }
        return historial.pop();
    }

    public boolean puedeDeshacer() {
        return !historial.isEmpty();
    }
}
