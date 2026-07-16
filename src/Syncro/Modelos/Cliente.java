
package Syncro.Modelos;

import java.util.ArrayList;
import java.util.List;
import Syncro.Modelos.CtaBase;

public class Cliente {
    private String dni;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private List<CtaBase> cuentas;

    public Cliente(String dni, String nombre, String telefono, String correo, String direccion) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.cuentas = new ArrayList<CtaBase>();
    }

    public void agregarCuenta(CtaBase cuenta) {
        this.cuentas.add(cuenta);
    }

    public int getTotalCuentas() {
        return this.cuentas.size();
    }

    public double getSaldoTotal() {
        double total = 0.0;
        for (CtaBase cuenta : this.cuentas) {
            total += cuenta.getSaldo();
        }
        return total;
    }

    public String getDni() {
        return this.dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<CtaBase> getCuentas() {
        return this.cuentas;
    }

    public String toString() {
        return "Cliente{dni=" + this.dni + ", nombre=" + this.nombre + "}";
    }
}
