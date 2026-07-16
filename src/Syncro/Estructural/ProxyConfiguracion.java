package Syncro.Estructural;

import Syncro.Creacional.BaseDatos;
import Syncro.Comportamiento.ContextoSesion;

/**
 * Patrón de Diseño Proxy.
 * Protege la modificación de configuraciones del Core Bancario
 * interceptando las llamadas de cambio de reglas globales.
 */
public class ProxyConfiguracion {
    private final BaseDatos db = BaseDatos.getInstance();

    public boolean guardarConfiguracion(boolean sobregiro, boolean seguro) {
        String usuario = ContextoSesion.getInstance().getUsuarioActivo();
        String rol = ContextoSesion.getInstance().getEstado().getNombreRol();

        // Control de Acceso del Proxy de Seguridad
        if (!"Administrador".equalsIgnoreCase(rol)) {
            System.out.println("[PROXY ERROR] Intento de acceso no autorizado por el usuario: " + usuario + " (Rol: " + rol + ")");
            return false;
        }

        // Si se autoriza, se realiza la persistencia en base de datos
        db.setSobregiroHabilitado(sobregiro);
        db.setSeguroAntiFraude(seguro);
        
        System.out.println("[PROXY OK] Configuración del Core actualizada por Administrador: " + usuario);
        return true;
    }
}
