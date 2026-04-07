package gestor_contraseñas;
import java.util.ArrayList;
import java.util.Scanner;

public class Usuarios {
    protected String nombre;
    protected String contraseña;
    protected static ArrayList<Contraseñas> contraseñas;
    static Scanner input = new Scanner(System.in);
    
    public Usuarios(String nombre, String contraseña){
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.contraseñas = new ArrayList<>();
    }

    public String gerNombre(){
        return this.nombre;
    }
    public String getContraseña(){
        return this.contraseña;
    }
    public ArrayList<Contraseñas> getContraseñas(){
        return this.contraseñas;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setContraseña(String contraseña){
        this.contraseña = contraseña;
    }

    public void añadirContraseña(Contraseñas contraseña){
        this.contraseñas.add(contraseña);
    }
    public void borrarContraseña(int posicionContraseña){
        this.contraseñas.remove(posicionContraseña);
    }
    
    public static void crearContraseña(){
        System.out.print("Nombre: ");
        String nombre = input.nextLine();
        contraseñas.add(new Contraseñas(nombre, Utilidades.generarContraseña(32)));
    }

    @Override
    public String toString() {
        return  "Nombre:" + nombre;
    }
    
}
