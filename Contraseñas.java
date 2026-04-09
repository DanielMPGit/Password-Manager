package gestor_contraseñas;
public class Contraseñas {
    protected String nombre_servicio;
    protected  String contraseña;

    public Contraseñas(String nombre_servicio, String contraseña) {
        this.nombre_servicio = nombre_servicio;
        this.contraseña = contraseña;
    }
    
    public String getNombre_servicio() {
        return nombre_servicio;
    }

    public void setNombre_servicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String toString(String passwordMaestra) {
        return "Nombre: " + nombre_servicio + "\nContraseña: " + Utilidades.Desencriptar(contraseña, passwordMaestra);
    }
    
    
}
