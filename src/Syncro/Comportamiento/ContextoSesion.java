package Syncro.Comportamiento;

import Syncro.Vista.Ventana;

public class ContextoSesion {
    private static ContextoSesion instancia;
    private IEstadoSesion estadoActual;
    private String usuarioActivo;

    private ContextoSesion() {
        this.estadoActual = new EstadoNoAutenticado();
    }

    public static synchronized ContextoSesion getInstance() {
        if (instancia == null) {
            instancia = new ContextoSesion();
        }
        return instancia;
    }

    public void setEstado(IEstadoSesion estado, String usuario) {
        this.estadoActual = estado;
        this.usuarioActivo = usuario;
    }

    public IEstadoSesion getEstado() {
        return this.estadoActual;
    }

    public String getUsuarioActivo() {
        return this.usuarioActivo;
    }

    public void autenticar(String usuario, String password) {
        this.estadoActual.autenticar(this, usuario, password);
    }

    public void cerrarSesion() {
        this.estadoActual.cerrarSesion(this);
    }
}
