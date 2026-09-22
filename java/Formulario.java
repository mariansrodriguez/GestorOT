


import javax.swing.*;
import java.awt.*;

public abstract class Formulario extends JFrame {

    protected JButton btnInsertar;
    protected JButton btnConsultar;
    protected JButton btnModificar;
    protected JButton btnEliminar;
    protected JPanel panelBotones;

    public Formulario(String titulo) {
        setTitle(titulo);
        setSize(500, 400);
        setLocationRelativeTo(null); // centra la ventana en la pantalla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        inicializarBotones();
    }

    private void inicializarBotones() {
        btnInsertar = new JButton("Insertar");
        btnConsultar = new JButton("Consultar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        panelBotones = new JPanel();
        panelBotones.add(btnInsertar);
        panelBotones.add(btnConsultar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);

        // Conecta cada botón con su método abstracto correspondiente.
        // Como estos métodos son abstractos, aquí no importa qué hacen —
        // eso lo define cada formulario hijo (Cliente, Tecnico, OrdenTrabajo).
        btnInsertar.addActionListener(e -> insertar());
        btnConsultar.addActionListener(e -> consultar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
    }

    // Métodos abstractos: cada formulario hijo ESTÁ OBLIGADO a escribir su propia versión
    public abstract void insertar();
    public abstract void consultar();
    public abstract void modificar();
    public abstract void eliminar();
}
