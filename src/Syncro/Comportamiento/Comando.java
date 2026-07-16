
package Syncro.Comportamiento;

public interface Comando {
    public boolean ejecutar();

    public boolean deshacer();

    public String getDescripcion();
}
