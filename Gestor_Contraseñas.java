package gestor_contraseñas;

import java.util.ArrayList;
import java.util.Scanner;

public class Gestor_Contraseñas {
    static ArrayList<Usuarios> usuarios = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        String encriptada = "";
        String aEnccriptar = input.nextLine();
        
        encriptada = Utilidades.Encriptar(aEnccriptar);
        
        System.out.println(encriptada);
        System.out.println(Utilidades.Desencriptar(encriptada));
        
        
        
        usuarios.add(new Usuarios("Test", "asdktasd"));
        usuarios.get(0).crearContraseña();
        System.out.println(usuarios.get(0).getContraseñas().get(0).toString());
    }
    
}
