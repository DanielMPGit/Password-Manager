package gestor_contraseñas;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:sqlite:gestor.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            return null;
        }
    }

    public static void inicializar() {
        try (Connection con = conectar(); Statement st = con.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT UNIQUE NOT NULL,
                    hash_contraseña TEXT NOT NULL
                )
            """);
            st.execute("""
                CREATE TABLE IF NOT EXISTS contraseñas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_usuario INTEGER NOT NULL,
                    nombre_servicio TEXT NOT NULL,
                    contraseña TEXT NOT NULL,
                    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
                )
            """);
        } catch (Exception e) {
            System.out.println("Error inicializando BD: " + e.getMessage());
        }
    }

    public static boolean registrarUsuario(String nombre, String hashContraseña) {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO usuarios (nombre, hash_contraseña) VALUES (?, ?)")) {
            ps.setString(1, nombre);
            ps.setString(2, hashContraseña);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] login(String nombre) {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(
                "SELECT id, hash_contraseña FROM usuarios WHERE nombre = ?")) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{rs.getString("id"), rs.getString("hash_contraseña")};
            }
        } catch (Exception e) {}
        return null;
    }

    public static void guardarContraseña(int idUsuario, String nombreServicio, String contraseñaEncriptada) {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO contraseñas (id_usuario, nombre_servicio, contraseña) VALUES (?, ?, ?)")) {
            ps.setInt(1, idUsuario);
            ps.setString(2, nombreServicio);
            ps.setString(3, contraseñaEncriptada);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error guardando contraseña: " + e.getMessage());
        }
    }

    public static ResultSet getContraseñas(int idUsuario) {
        try {
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement(
                "SELECT nombre_servicio, contraseña FROM contraseñas WHERE id_usuario = ?");
            ps.setInt(1, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) {
            return null;
        }
    }

    public static void borrarContraseña(int idUsuario, String nombreServicio) {
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(
                "DELETE FROM contraseñas WHERE id_usuario = ? AND nombre_servicio = ?")) {
            ps.setInt(1, idUsuario);
            ps.setString(2, nombreServicio);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error borrando contraseña: " + e.getMessage());
        }
    }
}