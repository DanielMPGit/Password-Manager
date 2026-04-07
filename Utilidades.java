package gestor_contraseñas;

import java.util.Base64;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
        
public class Utilidades {
    private static String numeros = "0123456789";
    private static String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String minusculas = "abcdefghijklmnopqrstuvwxyz";
    private static final String caracteres = "!@#$%^&*()-_=+[]{}|;:,.?@$";
    private static String key = generarContraseña(32);
    
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
    
    
    public static SecretKeySpec CrearCalve(String llave) {
        try {
            byte[] cadena = llave.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            cadena = md.digest(cadena);
            cadena = Arrays.copyOf(cadena, 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(cadena, "AES");
            return secretKeySpec;
        } catch (Exception e) {
            return null;
        }

    }
    
    public static String Encriptar(String encriptar){
     
        try {
            SecretKeySpec secretKeySpec = CrearCalve(key);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            
            byte [] cadena = encriptar.getBytes("UTF-8");
            byte [] encriptada = cipher.doFinal(cadena);
            String cadena_encriptada = Base64.getEncoder().encodeToString(encriptada);
            return cadena_encriptada;
            
            
            
        } catch (Exception e) {
            return "";
        }
    }
    public static String Desencriptar(String desencriptar) {
     
        try {
            SecretKeySpec secretKeySpec = CrearCalve(key);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            
            byte [] cadena =Base64.getDecoder().decode(desencriptar);
            byte [] desencriptacioon = cipher.doFinal(cadena);
            String cadena_desencriptada = new String(desencriptacioon);
            return cadena_desencriptada;
            
        } catch (Exception e) {
            return "";
        }
    }
}
