package gestor_contraseñas;

import javax.swing.WindowConstants;

public class Gestor_Contraseñas {
    public static void main(String[] args) {
        Interfaz b = new Interfaz();
        b.login();
        b.setVisible(true);
        b.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}