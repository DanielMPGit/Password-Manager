package gestor_contraseñas;
import java.util.ArrayList;
import java.util.Scanner;

public class Usuarios {
    protected String nombre;
    protected String contraseña;
    protected String hashContraseña;
    protected static ArrayList<Contraseñas> contraseñas;
    static Scanner input = new Scanner(System.in);
    
    public Usuarios(String nombre, String contraseña){
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.hashContraseña = Utilidades.hashear(contraseña);
        this.contraseñas = new ArrayList<>();
    }

    public String getNombre(){
        return this.nombre;
    }
    public String getContraseña(){
        return this.contraseña;
    }
    public ArrayList<Contraseñas> getContraseñas(){
        return this.contraseñas;
    }
    
    public Contraseñas getContraseñaArray(int num){
        return contraseñas.get(num);
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
    
    public static void crearContraseña(String passwordMaestra) {
        System.out.print("Nombre: ");
        String nombre = input.nextLine();
        contraseñas.add(new Contraseñas(nombre, Utilidades.generarContraseña(32, passwordMaestra)));
    }

    @Override
    public String toString() {
        return  "Nombre:" + nombre;
    }
    
}
