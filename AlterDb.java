import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class AlterDb {
    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/syncro_db", "postgres", "Loa");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("ALTER TABLE syncro.cuentas ADD COLUMN moneda VARCHAR(20) NOT NULL DEFAULT 'SOLES';");
            System.out.println("Columna agregada exitosamente.");
            conn.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
