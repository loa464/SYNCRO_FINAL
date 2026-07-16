
package Syncro.Comportamiento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Syncro.Comportamiento.Observador;
import Syncro.Creacional.BaseDatos;

public class Auditoria
implements Observador {
    private static final List<String> registros = new ArrayList<String>();
    private static Auditoria instancia = null;

    private Auditoria() {
    }

    public static Auditoria getInstance() {
        if (instancia == null) {
            instancia = new Auditoria();
        }
        return instancia;
    }

    @Override
    public void actualizar(String evento, String numeroCuenta, double monto, String mensaje) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String entrada = "[" + timestamp + "] EVENTO: " + evento + " | CUENTA: " + numeroCuenta + " | MONTO: $" + String.format("%,.2f", monto) + " | DETALLE: " + mensaje;
        registros.add(entrada);
        System.out.println("[Auditoria] " + entrada);
        BaseDatos.getInstance().registrarAuditoria(evento, numeroCuenta, monto, mensaje);
    }

    public List<String> getRegistros() {
        return registros;
    }

    public int getTotalRegistros() {
        return registros.size();
    }

    public void vaciarRegistros() {
        registros.clear();
    }
}
