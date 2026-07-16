
package Syncro.Comportamiento;

import Syncro.Comportamiento.Observador;

public interface Sujeto {
    public void agregarObservador(Observador var1);

    public void eliminarObservador(Observador var1);

    public void notificarObservadores(String var1, String var2, double var3, String var5);
}
