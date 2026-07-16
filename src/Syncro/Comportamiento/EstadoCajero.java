package Syncro.Comportamiento;

import Syncro.Vista.Ventana;

public class EstadoCajero implements IEstadoSesion {

    @Override
    public void autenticar(ContextoSesion contexto, String usuario, String password) {
        // Ya está autenticado
    }

    @Override
    public void cerrarSesion(ContextoSesion contexto) {
        contexto.setEstado(new EstadoNoAutenticado(), null);
    }

    @Override
    public void configurarVentana(Ventana ventana) {
        // Cajero no tiene acceso a Auditoría ni Configuración
        ventana.setBotonAuditoriaVisible(false);
        // ventana.setBotonConfiguracionVisible(false);
        ventana.setRolUsuario("Cajero");
    }

    @Override
    public String getNombreRol() {
        return "Cajero";
    }
}
