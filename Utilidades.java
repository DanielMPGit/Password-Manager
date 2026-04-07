package gestor_contraseñas;

public class Utilidades {
    private static String numeros = "0123456789";
    private static String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String minusculas = "abcdefghijklmnopqrstuvwxyz";
    private static final String caracteres = "!@#$%^&*()-_=+[]{}|;:,.?@$";
    
    public static String generarContraseña(int length) {
            return getCaracter(numeros + mayusculas + minusculas + caracteres, length);
    }
    
    public static String getCaracter(String caracteres, int length) {
            String contraseña = "";

            for (int i = 0; i < length; i++) {
                    contraseña+=(caracteres.charAt((int)(Math.random() * caracteres.length())));
            }

            return contraseña;
    }
}
