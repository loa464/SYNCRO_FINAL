package Syncro.Comportamiento;

import Syncro.Vista.Ventana;
import javax.swing.JOptionPane;

public class EstadoNoAutenticado implements IEstadoSesion {

    @Override
    public void autenticar(ContextoSesion contexto, String usuario, String password) {
        if ("admin".equals(usuario) && "admin".equals(password)) {
            contexto.setEstado(new EstadoAdmin(), usuario);
        } else if ("cajero".equals(usuario) && "cajero".equals(password)) {
            contexto.setEstado(new EstadoCajero(), usuario);
        } else {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }
    }

    @Override
    public void cerrarSesion(ContextoSesion contexto) {
        // Ya está cerrado
    }

    @Override
    public void configurarVentana(Ventana ventana) {
        // En este estado la ventana principal no debería mostrarse, 
        // o al menos todos los módulos deben estar ocultos.
        ventana.setVisible(false);
    }

    @Override
    public String getNombreRol() {
        return "NoAutenticado";
    }
}
