package gestor_contraseñas;

import java.util.Scanner;

public class Gestor_Contraseñas {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Database.inicializar();
        
        System.out.println("1. Registrarse");
        System.out.println("2. Login");
        int opcion = Integer.parseInt(input.nextLine());

        if (opcion == 1) {
            registrar();
        } else {
            login();
        }
    }

    public static void registrar() {
        System.out.print("Nombre Usuario: ");
        String nombre = input.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = input.nextLine();
        System.out.print("Repite la contraseña: ");
        String validacion = input.nextLine();

        if (!contraseña.equals(validacion)) {
            System.out.println("Las contraseñas no coinciden");
            return;
        }

        boolean ok = Database.registrarUsuario(nombre, Utilidades.hashear(contraseña));
        System.out.println(ok ? "Usuario registrado" : "El usuario ya existe");
    }

    public static void login() {
        System.out.print("Usuario: ");
        String nombre = input.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = input.nextLine();

        String[] datos = Database.login(nombre);
        if (datos == null || !datos[1].equals(Utilidades.hashear(contraseña))) {
            System.out.println("Usuario o contraseña incorrectos");
            return;
        }

        System.out.println("Login correcto");
        int idUsuario = Integer.parseInt(datos[0]);
        menu(idUsuario, contraseña);
    }

    public static void menu(int idUsuario, String passwordMaestra) {
        int opcion = 0;
        do {
            System.out.println("1. Nueva contraseña");
            System.out.println("2. Ver contraseñas");
            System.out.println("3. Salir");
            opcion = Integer.parseInt(input.nextLine());

            if (opcion == 1) {
                System.out.print("Nombre del servicio: ");
                String servicio = input.nextLine();
                String enc = Utilidades.generarContraseña(32, passwordMaestra);
                Database.guardarContraseña(idUsuario, servicio, enc);
                System.out.println("Contraseña guardada");

            } else if (opcion == 2) {
                try {
                    java.sql.ResultSet rs = Database.getContraseñas(idUsuario);
                    while (rs.next()) {
                        String servicio = rs.getString("nombre_servicio");
                        String enc = rs.getString("contraseña");
                        System.out.println("Servicio: " + servicio);
                        System.out.println("Contraseña: " + Utilidades.Desencriptar(enc, passwordMaestra));
                    }
                } catch (Exception e) {
                    System.out.println("Error cargando contraseñas");
                }
            }
        } while (opcion != 3);
    }
}