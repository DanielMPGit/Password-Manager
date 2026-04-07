package gestor_contraseñas;

import java.util.ArrayList;

public class Gestor_Contraseñas {
    static ArrayList<Usuarios> usuarios = new ArrayList<>();
    public static void main(String[] args) {
        
        usuarios.add(new Usuarios("Test", "asdktasd"));
        usuarios.get(0).crearContraseña();
        System.out.println(usuarios.get(0).getContraseñas().get(0).toString());
    }
    
}
