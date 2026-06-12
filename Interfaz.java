package gestor_contraseñas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Interfaz extends javax.swing.JFrame {
    static ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
    public void login(){
        Database.inicializar();
        setTitle("Login");
        setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        setContentPane(panel);

        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField tfUsuario = new JTextField(20);
        JLabel lblPass = new JLabel("Contraseña:");
        JTextField tfPass = new JTextField(20);
        JButton btnLogin = new JButton("Login");
        JButton btnRegistrar = new JButton("Registrar");

        panel.add(lblUsuario);  
        panel.add(tfUsuario);
        panel.add(lblPass);     
        panel.add(tfPass);
        panel.add(btnLogin);   
        panel.add(btnRegistrar);
        
        
        btnRegistrar.addActionListener(e -> {

        });
        btnLogin.addActionListener(e -> {
            String nombre = tfUsuario.getText().trim();
            String pass = tfPass.getText().trim();

            String[] datos = Database.login(nombre);
            if (datos == null || !datos[1].equals(Utilidades.hashear(pass))) {
                JOptionPane.showMessageDialog(panel, "Usuario o contraseña incorrectos");
            } else {
                Usuarios u = new Usuarios(nombre, pass);
                try {
                    ResultSet rs = Database.getContraseñas(Integer.parseInt(datos[0]));
                    while (rs.next()) {
                        u.añadirContraseña(new Contraseñas(rs.getString("nombre_servicio"), rs.getString("contraseña")));
                    }
                } catch (Exception ex) {
                    System.out.println("Error cargando contraseñas: " + ex.getMessage());
                }
                listaUsuarios.add(u);
                dispose();
                Gestor(nombre);
            }
        }); 
        btnRegistrar.addActionListener(e -> {
            Registrar();
        });  
        revalidate();
        repaint();
    }
    public void Registrar(){
        setTitle("Register");
        setSize(400, 200);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        setContentPane(panel);

        JLabel lblUsuario = new JLabel("Usuario:");
        JTextField tfUsuario = new JTextField(20);
        JLabel lblPass = new JLabel("Contraseña:");
        JTextField tfPass = new JTextField(20);
        JLabel lblPassRepeat = new JLabel("Repetir Contraseña:");
        JTextField tfPassrepeat = new JTextField(20);
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnlogin = new JButton("Login");

        panel.add(lblUsuario);  
        panel.add(tfUsuario);
        panel.add(lblPass);     
        panel.add(tfPass);
        panel.add(lblPassRepeat);  
        panel.add(tfPassrepeat);
        panel.add(btnRegistrar);
        panel.add(btnlogin);
        
        btnlogin.addActionListener(e -> {
            login();
        });
        btnRegistrar.addActionListener(e -> {
            String nombre = tfUsuario.getText().trim();
            String pass = tfPass.getText().trim();
            String validacion = tfPass.getText().trim();
            if (nombre.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Rellena todos los campos");
                return;
            }
            if (existeUsuario(nombre)) {
                JOptionPane.showMessageDialog(panel, "Usuario ya existente");
                return;
            }
            if(!pass.equals(validacion)){
                JOptionPane.showMessageDialog(panel, "Las contraseñas no coinciden");
            }
            boolean ok = Database.registrarUsuario(nombre, Utilidades.hashear(pass));
            JOptionPane.showMessageDialog(panel, ok ? "Usuario registrado" : "El usuario ya existe");
            login();
        });
        revalidate();
        repaint();
 
    }public void Gestor(String nombreUsuario) {
        JFrame frame = new JFrame("Biblioteca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        String[] columnas = {"Servicio", "Contraseña"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnBorrar = new JButton("Borrar");
        JButton btnAñadir = new JButton("Añadir");
        panelBotones.add(btnBorrar);
        panelBotones.add(btnAñadir);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(scroll, BorderLayout.CENTER);
        panelCentro.add(panelBotones, BorderLayout.EAST);
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBuscar = new JLabel("Buscar:");
        JTextField txtBuscar = new JTextField(30);
        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtBuscar);
        frame.add(panelCentro, BorderLayout.CENTER);
        frame.add(panelBuscar, BorderLayout.SOUTH);
        Usuarios nombre = returnUsuario(nombreUsuario);
        Runnable actualizarTabla = () -> {
            modelo.setRowCount(0);
            String filtro = txtBuscar.getText().toLowerCase();
            nombre.getContraseñas().sort(Comparator.comparing(Contraseñas::getNombre_servicio));
            for (Contraseñas l : nombre.getContraseñas()) {
                if (l.getNombre_servicio() == null || l.getNombre_servicio().equals("null")) {
                    continue;
                }
                if (!filtro.isEmpty() && !l.getNombre_servicio().toLowerCase().contains(filtro) && !l.getContraseña().toLowerCase().contains(filtro)) {
                    continue;
                }
                modelo.addRow(new Object[]{l.getNombre_servicio(), Utilidades.Desencriptar(l.getContraseña(), nombreUsuario)});
            }
        };
        btnBorrar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(frame, "Selecciona una Contraseña");
                return;
            }
            String servicio = (String) modelo.getValueAt(fila, 0);
            String[] datos = Database.login(nombreUsuario);
            Database.borrarContraseña(Integer.parseInt(datos[0]), servicio);
            nombre.getContraseñas().removeIf(c -> c.getNombre_servicio().equals(servicio));
            actualizarTabla.run();
        });
        btnAñadir.addActionListener(e -> {
            String servicio = JOptionPane.showInputDialog(frame, "Nombre del servicio:");
            if (servicio == null || servicio.trim().isEmpty()) return;
            boolean existe = nombre.getContraseñas().stream().anyMatch(c -> c.getNombre_servicio() != null && c.getNombre_servicio().equals(servicio));
            if (existe) {
                JOptionPane.showMessageDialog(frame, "Ya existe un servicio con ese nombre");
                return;
            }
            String[] datos = Database.login(nombreUsuario);
            String contraseñaGenerada = Utilidades.generarContraseña(32, nombreUsuario);
            Database.guardarContraseña(Integer.parseInt(datos[0]), servicio, contraseñaGenerada);
            nombre.añadirContraseña(new Contraseñas(servicio, contraseñaGenerada));
            actualizarTabla.run();
        });

        tabla.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
                    int fila = tabla.getSelectedRow();
                    if (fila == -1) return;
                    String contraseña = (String) modelo.getValueAt(fila, 1);
                    StringSelection ss = new StringSelection(contraseña);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
                    e.consume();
                }
            }
        });

        actualizarTabla.run();
        frame.setVisible(true);
    }
    
    public static boolean existeUsuario(String nombre) {
        for (Usuarios u : listaUsuarios) {
            if (u.getNombre().equals(nombre)) return true;
        }
        return false;
    }
    public static Usuarios returnUsuario(String nombre) {
        for (Usuarios u : listaUsuarios) {
            if (u.getNombre().equals(nombre)) return u;
        }
        return null;
    }
}