
package Syncro.Comportamiento;

import java.util.ArrayDeque;
import java.util.Deque;
import Syncro.Comportamiento.Comando;

public class GestorCmd {
    private static GestorCmd instancia = null;
    private final Deque<Comando> historial = new ArrayDeque<Comando>();

    private GestorCmd() {
    }

    public static GestorCmd getInstance() {
        if (instancia == null) {
            instancia = new GestorCmd();
        }
        return instancia;
    }

    public boolean ejecutar(Comando comando) {
        boolean exito = comando.ejecutar();
        if (exito) {
            this.historial.push(comando);
        }
        return exito;
    }

    public String deshacerUltimo() {
        if (this.historial.isEmpty()) {
            return null;
        }
        Comando ultimo = this.historial.pop();
        boolean exito = ultimo.deshacer();
        if (exito) {
            return "Deshecho: " + ultimo.getDescripcion();
        }
        return "No se pudo deshacer: " + ultimo.getDescripcion();
    }

    public boolean puedeDeshacer() {
        return !this.historial.isEmpty();
    }

    public int getTotalComandos() {
        return this.historial.size();
    }

    public String getUltimoComando() {
        if (this.historial.isEmpty()) {
            return "Sin operaciones recientes";
        }
        return this.historial.peek().getDescripcion();
    }
}
