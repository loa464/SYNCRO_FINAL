package Syncro.Creacional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Syncro.Modelos.*;
import Syncro.Comportamiento.Auditoria;

public class BaseDatos {
    private static BaseDatos instanciaUnica = null;
    private List<Cliente> clientes = new ArrayList<>();
    private List<CtaBase> cuentas = new ArrayList<>();
    private List<Transaccion> transacciones = new ArrayList<>();
    private List<String> logsSMS = new ArrayList<>();
    private boolean sobregiroHabilitado = true;
    private boolean seguroAntiFraude = false;

    private BaseDatos() {}

    public static synchronized BaseDatos getInstance() {
        if (instanciaUnica == null) {
            instanciaUnica = new BaseDatos();
        }
        return instanciaUnica;
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Ignore
        }
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/syncro_db", "postgres", "Loa");
    }

    public boolean cargarDesdePostgres() {
        try (Connection conn = getConnection()) {
            clientes.clear();
            cuentas.clear();
            transacciones.clear();
            logsSMS.clear();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT sobregiro_habilitado, seguro_anti_fraude FROM syncro.configuracion_sistema WHERE id = 1")) {
                if (rs.next()) {
                    this.sobregiroHabilitado = rs.getBoolean("sobregiro_habilitado");
                    this.seguroAntiFraude = rs.getBoolean("seguro_anti_fraude");
                }
            } catch (SQLException e) {
                // Ignore config table failures
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT dni, nombre, telefono, correo, direccion FROM syncro.clientes")) {
                while (rs.next()) {
                    Cliente c = new Cliente(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion")
                    );
                    clientes.add(c);
                }
            }

            int maxContador = 1000;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT numero_cuenta, tipo_cuenta, saldo, dni_dueno, activa, notificaciones_sms, moneda FROM syncro.cuentas")) {
                while (rs.next()) {
                    String numeroCuenta = rs.getString("numero_cuenta");
                    if (numeroCuenta != null && numeroCuenta.startsWith("SYN-")) {
                        try {
                            int num = Integer.parseInt(numeroCuenta.substring(4));
                            if (num > maxContador) {
                                maxContador = num;
                            }
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                    }
                    String tipo = rs.getString("tipo_cuenta");
                    double saldo = rs.getDouble("saldo");
                    String dni = rs.getString("dni_dueno");
                    
                    String monedaStr = rs.getString("moneda");
                    Moneda moneda = (monedaStr != null) ? Moneda.valueOf(monedaStr) : Moneda.SOLES;
                    
                    CtaBase cuenta = FactoryCta.crearCuentaExistente(tipo, numeroCuenta, saldo, dni, moneda);
                    cuenta.setActiva(rs.getBoolean("activa"));
                    boolean notifSms = rs.getBoolean("notificaciones_sms");
                    cuenta.setNotificacionesSMS(notifSms);
                    
                    Cliente owner = this.buscarClientePorDni(dni);
                    CtaBase cuentaGuardar = cuenta;
                    if (notifSms) {
                        String telefono = owner != null ? owner.getTelefono() : "";
                        cuentaGuardar = new Syncro.Estructural.DecoSMS(cuenta, telefono);
                    }
                    
                    cuentas.add(cuentaGuardar);
                    if (owner != null) {
                        owner.agregarCuenta(cuentaGuardar);
                    }
                }
            }
            CtaBase.setContador(maxContador);

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, tipo_operacion, monto, numero_cuenta, numero_cuenta_destino, exitosa, descripcion, fecha_hora FROM syncro.transacciones ORDER BY id")) {
                while (rs.next()) {
                    String tipoStr = rs.getString("tipo_operacion");
                    Transaccion.TipoOperacion tipo = Transaccion.TipoOperacion.valueOf(tipoStr);
                    double monto = rs.getDouble("monto");
                    String cta = rs.getString("numero_cuenta");
                    boolean exito = rs.getBoolean("exitosa");
                    String desc = rs.getString("descripcion");
                    
                    Transaccion t = new Transaccion(tipo, monto, cta, exito, desc);
                    t.setId(rs.getInt("id"));
                    t.setNumeroCuentaDestino(rs.getString("numero_cuenta_destino"));
                    t.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                    
                    transacciones.add(t);
                }
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT mensaje FROM syncro.notificaciones_sms ORDER BY id")) {
                while (rs.next()) {
                    logsSMS.add(rs.getString("mensaje"));
                }
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT evento, numero_cuenta, monto, descripcion, fecha_hora FROM syncro.auditoria ORDER BY id")) {
                List<String> registros = Auditoria.getInstance().getRegistros();
                registros.clear();
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                while (rs.next()) {
                    String evento = rs.getString("evento");
                    String numCuenta = rs.getString("numero_cuenta");
                    double monto = rs.getDouble("monto");
                    String desc = rs.getString("descripcion");
                    Timestamp ts = rs.getTimestamp("fecha_hora");
                    String timestamp = ts != null ? ts.toLocalDateTime().format(formatter) : java.time.LocalDateTime.now().format(formatter);
                    
                    String entrada = "[" + timestamp + "] EVENTO: " + evento + " | CUENTA: " + numCuenta + " | MONTO: $" + String.format("%,.2f", monto) + " | DETALLE: " + desc;
                    registros.add(entrada);
                }
            } catch (SQLException e) {
                // Ignore audit table failure if not created or empty
            }

            return true;
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al cargar datos desde PostgreSQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarCliente(Cliente cliente) {
        for (Cliente c : this.clientes) {
            if (c.getDni().equals(cliente.getDni())) {
                return false;
            }
        }
        this.clientes.add(cliente);
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO syncro.clientes (dni, nombre, telefono, correo, direccion, fecha_registro) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)")) {
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al registrar cliente: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    public Cliente buscarClientePorDni(String dni) {
        for (Cliente c : this.clientes) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        return null;
    }

    public void registrarCuenta(CtaBase cuenta) {
        this.cuentas.add(cuenta);
        Cliente dueno = this.buscarClientePorDni(cuenta.getDniDueno());
        if (dueno != null) {
            dueno.agregarCuenta(cuenta);
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO syncro.cuentas (numero_cuenta, tipo_cuenta, saldo, dni_dueno, activa, notificaciones_sms, moneda, fecha_apertura) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)")) {
            ps.setString(1, cuenta.getNumeroCuenta());
            ps.setString(2, cuenta.getTipoCuenta());
            ps.setDouble(3, cuenta.getSaldo());
            ps.setString(4, cuenta.getDniDueno());
            ps.setBoolean(5, cuenta.isActiva());
            ps.setBoolean(6, cuenta.isNotificacionesSMS());
            ps.setString(7, cuenta.getMoneda() != null ? cuenta.getMoneda().name() : Moneda.SOLES.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al registrar cuenta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public CtaBase buscarCuentaPorNumero(String numeroCuenta) {
        for (CtaBase c : this.cuentas) {
            if (c.getNumeroCuenta().equals(numeroCuenta)) {
                return c;
            }
        }
        return null;
    }

    public List<CtaBase> obtenerCuentasPorDni(String dni) {
        List<CtaBase> resultado = new ArrayList<>();
        for (CtaBase c : this.cuentas) {
            if (c.getDniDueno().equals(dni)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public void registrarTransaccion(Transaccion transaccion) {
        this.transacciones.add(transaccion);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO syncro.transacciones (tipo_operacion, monto, numero_cuenta, numero_cuenta_destino, exitosa, descripcion, fecha_hora) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)")) {
            ps.setString(1, transaccion.getTipo().name());
            ps.setDouble(2, transaccion.getMonto());
            ps.setString(3, transaccion.getNumeroCuenta());
            ps.setString(4, transaccion.getNumeroCuentaDestino());
            ps.setBoolean(5, transaccion.isExitosa());
            ps.setString(6, transaccion.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al registrar transaccion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void registrarAuditoria(String evento, String numeroCuenta, double monto, String descripcion) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO syncro.auditoria (evento, numero_cuenta, monto, descripcion, fecha_hora) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)")) {
            ps.setString(1, evento);
            ps.setString(2, numeroCuenta);
            ps.setDouble(3, monto);
            ps.setString(4, descripcion);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al registrar auditoria: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarSaldoCuenta(CtaBase cuenta) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE syncro.cuentas SET saldo = ? WHERE numero_cuenta = ?")) {
            ps.setDouble(1, cuenta.getSaldo());
            ps.setString(2, cuenta.getNumeroCuenta());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al actualizar saldo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void vaciarLogs() {
        this.transacciones.clear();
        this.logsSMS.clear();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM syncro.transacciones");
            stmt.executeUpdate("DELETE FROM syncro.notificaciones_sms");
            stmt.executeUpdate("DELETE FROM syncro.auditoria");
            System.out.println("[BaseDatos OK] Log de transacciones y auditoría vaciado físicamente de MySQL.");
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al vaciar logs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarNotificacionesSMS(CtaBase cuenta) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE syncro.cuentas SET notificaciones_sms = ? WHERE numero_cuenta = ?")) {
            ps.setBoolean(1, cuenta.isNotificacionesSMS());
            ps.setString(2, cuenta.getNumeroCuenta());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al actualizar notificaciones SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Transaccion> obtenerUltimasTransacciones(int cantidad) {
        int total = this.transacciones.size();
        int desde = Math.max(0, total - cantidad);
        List<Transaccion> ultimas = new ArrayList<>();
        for (int i = total - 1; i >= desde; --i) {
            ultimas.add(this.transacciones.get(i));
        }
        return ultimas;
    }

    public List<Transaccion> obtenerHistorialCuenta(String numeroCuenta) {
        List<Transaccion> historial = new ArrayList<>();
        for (Transaccion t : this.transacciones) {
            if (t.getNumeroCuenta().equals(numeroCuenta) || (t.getNumeroCuentaDestino() != null && t.getNumeroCuentaDestino().equals(numeroCuenta))) {
                historial.add(t);
            }
        }
        return historial;
    }

    public void registrarSMS(String mensaje) {
        this.logsSMS.add(mensaje);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO syncro.notificaciones_sms (mensaje, fecha_envio) VALUES (?, CURRENT_TIMESTAMP)")) {
            ps.setString(1, mensaje);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al registrar log SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getCapitalTotal() {
        double total = 0.0;
        for (CtaBase c : this.cuentas) {
            total += c.getSaldo();
        }
        return total;
    }

    public int getClientesActivos() {
        return this.clientes.size();
    }

    public int getTotalCuentas() {
        return this.cuentas.size();
    }

    public int getTransaccionesHoy() {
        return Auditoria.getInstance().getTotalRegistros();
    }

    public List<Cliente> getClientes() { return this.clientes; }
    public List<CtaBase> getCuentas() { return this.cuentas; }
    public List<Transaccion> getTransacciones() { return this.transacciones; }
    public List<String> getLogsSMS() { return this.logsSMS; }

    public boolean isSobregiroHabilitado() { return this.sobregiroHabilitado; }
    public void setSobregiroHabilitado(boolean valor) {
        this.sobregiroHabilitado = valor;
        actualizarConfiguracion();
    }

    public boolean isSeguroAntiFraude() { return this.seguroAntiFraude; }
    public void setSeguroAntiFraude(boolean valor) {
        this.seguroAntiFraude = valor;
        actualizarConfiguracion();
    }

    private void actualizarConfiguracion() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE syncro.configuracion_sistema SET sobregiro_habilitado = ?, seguro_anti_fraude = ? WHERE id = 1")) {
            ps.setBoolean(1, this.sobregiroHabilitado);
            ps.setBoolean(2, this.seguroAntiFraude);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("INSERT INTO syncro.configuracion_sistema (id, sobregiro_habilitado, seguro_anti_fraude, sms_global_activo) VALUES (1, " + this.sobregiroHabilitado + ", " + this.seguroAntiFraude + ", true)");
                }
            }
        } catch (SQLException e) {
            System.err.println("[BaseDatos ERROR] Error al actualizar configuracion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
