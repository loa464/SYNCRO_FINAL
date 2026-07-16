package Syncro.Comportamiento;

import Syncro.Vista.Ventana;
import javax.swing.JOptionPane;

public class EstadoAdmin implements IEstadoSesion {

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
        // Admin tiene acceso a todo
        ventana.setBotonAuditoriaVisible(true);
        // ventana.setBotonConfiguracionVisible(true);  // asumiendo que agregaremos este método
        ventana.setRolUsuario("Administrador");
    }

    @Override
    public String getNombreRol() {
        return "Administrador";
    }
}
