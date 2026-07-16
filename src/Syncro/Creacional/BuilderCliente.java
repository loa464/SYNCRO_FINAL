
package Syncro.Creacional;

import Syncro.Modelos.Cliente;

public class BuilderCliente {
    private String dni;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;

    public BuilderCliente(String dni) {
        this.dni = dni;
        this.nombre = "";
        this.telefono = "";
        this.correo = "";
        this.direccion = "";
    }

    public BuilderCliente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public BuilderCliente telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public BuilderCliente correo(String correo) {
        this.correo = correo;
        return this;
    }

    public BuilderCliente direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public Cliente build() {
        if (this.nombre == null || this.nombre.trim().isEmpty()) {
            throw new IllegalStateException("El nombre completo es obligatorio.");
        }
        if (this.dni == null || !this.dni.trim().matches("\\d{8}")) {
            throw new IllegalStateException("El DNI debe tener exactamente 8 dígitos numéricos.");
        }
        String telLimpio = this.telefono != null ? this.telefono.trim().replace(" ", "").replace("-", "") : "";
        if (telLimpio.isEmpty() || !telLimpio.matches("^(?:\\+?51)?9\\d{8}$")) {
            throw new IllegalStateException("El teléfono debe ser un celular peruano válido (9 dígitos, ej: 9XXXXXXXX).");
        }
        String correoLimpio = this.correo != null ? this.correo.trim() : "";
        if (correoLimpio.isEmpty() || !correoLimpio.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalStateException("El correo electrónico debe tener un formato válido con '@' (ej: usuario@correo.com).");
        }
        return new Cliente(this.dni.trim(), this.nombre.trim(), telLimpio, correoLimpio, this.direccion.trim());
    }

}
