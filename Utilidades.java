package gestor_contraseñas;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Utilidades {
    private static final String numeros = "0123456789";
    private static final String mayusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String minusculas = "abcdefghijklmnopqrstuvwxyz";
    private static final String caracteres = "!@#$%^&*()-_=+[]{}|;:,.?";
    private static final SecureRandom random = new SecureRandom();

    public static String generarContraseña(int length, String key) {
        return Encriptar(getCaracter(numeros + mayusculas + minusculas + caracteres, length), key);
    }

    public static String generarKey(int length) {
        return getCaracter(numeros + mayusculas + minusculas + caracteres, length);
    }

    public static String getCaracter(String caracteres, int length) {
        StringBuilder contraseña = new StringBuilder();
        for (int i = 0; i < length; i++) {
            contraseña.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return contraseña.toString();
    }

    public static SecretKeySpec CrearClave(String llave) {
        try {
            byte[] cadena = llave.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            cadena = md.digest(cadena);
            SecretKeySpec secretKeySpec = new SecretKeySpec(cadena, "AES");
            return secretKeySpec;
        } catch (Exception e) {
            return null;
        }
    }

    public static String Encriptar(String encriptar, String password) {
        try {
            SecretKeySpec secretKeySpec = CrearClave(password);
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            byte[] encriptada = cipher.doFinal(encriptar.getBytes("UTF-8"));
            byte[] ivMasEncriptada = new byte[16 + encriptada.length];
            System.arraycopy(iv, 0, ivMasEncriptada, 0, 16);
            System.arraycopy(encriptada, 0, ivMasEncriptada, 16, encriptada.length);

            return Base64.getEncoder().encodeToString(ivMasEncriptada);
        } catch (Exception e) {
            return "";
        }
    }
    public static String hashear(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return "";
        }
    }

    public static String Desencriptar(String desencriptar, String password) {
        try {
            byte[] ivMasEncriptada = Base64.getDecoder().decode(desencriptar);

            byte[] iv = Arrays.copyOfRange(ivMasEncriptada, 0, 16);
            byte[] encriptada = Arrays.copyOfRange(ivMasEncriptada, 16, ivMasEncriptada.length);

            SecretKeySpec secretKeySpec = CrearClave(password);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

            return new String(cipher.doFinal(encriptada));
        } catch (Exception e) {
            return "";
        }
    }
}