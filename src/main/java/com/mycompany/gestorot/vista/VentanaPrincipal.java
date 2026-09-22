package com.mycompany.gestorot.vista;

import com.mycompany.gestorot.vista.FormularioTecnico;
import com.mycompany.gestorot.vista.FormularioOrdenTrabajo;
import com.mycompany.gestorot.vista.FormularioCliente;
import javax.swing.*;
import java.awt.*;
 
public class VentanaPrincipal extends JFrame {
 
    public VentanaPrincipal() {
        setTitle("GestorOT - Menú Principal");
        setSize(400, 300);
        setLocationRelativeTo(null); // centra la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // aquí sí cerramos toda la app
        setLayout(new GridLayout(3, 1, 10, 10));
 
        JButton btnClientes = new JButton("Gestión de Clientes");
        JButton btnTecnicos = new JButton("Gestión de Técnicos");
        JButton btnOrdenes = new JButton("Gestión de Órdenes de Trabajo");
 
        // Al hacer click, se abre el formulario correspondiente y se carga su tabla
        btnClientes.addActionListener(e -> {
            FormularioCliente f = new FormularioCliente();
            f.setVisible(true);
            f.consultar();
        });
 
        btnTecnicos.addActionListener(e -> {
            FormularioTecnico f = new FormularioTecnico();
            f.setVisible(true);
            f.consultar();
        });
 
        btnOrdenes.addActionListener(e -> {
            FormularioOrdenTrabajo f = new FormularioOrdenTrabajo();
            f.setVisible(true);
            f.consultar();
        });
 
        add(btnClientes);
        add(btnTecnicos);
        add(btnOrdenes);
    }
}
 