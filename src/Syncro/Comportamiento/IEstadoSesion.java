package Syncro.Comportamiento;

import Syncro.Vista.Ventana;

public interface IEstadoSesion {
    void autenticar(ContextoSesion contexto, String usuario, String password);
    void cerrarSesion(ContextoSesion contexto);
    void configurarVentana(Ventana ventana);
    String getNombreRol();
}
