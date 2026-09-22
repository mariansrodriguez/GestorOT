package com.mycompany.gestorot.vista;

import com.mycompany.gestorot.vista.Formulario;
import com.mycompany.gestorot.conexion.Conexion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormularioTecnico extends Formulario {

    private JTextField txtId, txtNombre, txtEspecialidad, txtTelefono, txtCorreo, txtIdentificacion;
    private JTable tabla;

    public FormularioTecnico() {
        super("Gestión de Técnicos");

        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 5, 5));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNombre = new JTextField();
        txtEspecialidad = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtIdentificacion = new JTextField();

        panelCampos.add(new JLabel("ID:"));
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Especialidad:"));
        panelCampos.add(txtEspecialidad);
        panelCampos.add(new JLabel("Teléfono:"));
        panelCampos.add(txtTelefono);
        panelCampos.add(new JLabel("Correo:"));
        panelCampos.add(txtCorreo);
        panelCampos.add(new JLabel("Identificación:"));
        panelCampos.add(txtIdentificacion);

        add(panelCampos, BorderLayout.NORTH);

        tabla = new JTable();
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
    }

    @Override
    public void insertar() {
         if (!validarCampos()) return;
        String sql = "INSERT INTO Tecnicos (nombre, especialidad, telefono, correo, identificacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtEspecialidad.getText());
            ps.setString(3, txtTelefono.getText());
            ps.setString(4, txtCorreo.getText());
            ps.setString(5, txtIdentificacion.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Técnico insertado correctamente.");
            limpiarCampos();
            consultar();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e.getMessage());
        }
    }

    @Override
    public void consultar() {
        String sql = "SELECT * FROM Tecnicos";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Especialidad");
            modelo.addColumn("Teléfono");
            modelo.addColumn("Correo");
            modelo.addColumn("Identificación");

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_tecnicos"), // ojo: con "s", como está tu tabla real
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getString("identificacion")
                });
            }

            tabla.setModel(modelo);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al consultar: " + e.getMessage());
        }
    }

    @Override
    public void modificar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un técnico de la tabla primero.");
            return;
        }
         if (!validarCampos()) return;

        String sql = "UPDATE Tecnicos SET nombre=?, especialidad=?, telefono=?, correo=?, identificacion=? WHERE id_tecnicos=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtEspecialidad.getText());
            ps.setString(3, txtTelefono.getText());
            ps.setString(4, txtCorreo.getText());
            ps.setString(5, txtIdentificacion.getText());
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Técnico modificado correctamente.");
            limpiarCampos();
            consultar();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }
    private int contarOrdenesDelTecnico(int idTecnico) throws SQLException {
    String sql = "SELECT COUNT(*) FROM OrdenesTrabajo WHERE id_tecnico = ?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idTecnico);
        try (ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}

    @Override
    public void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un técnico de la tabla primero.");
            return;
        }
        int idTecnico = Integer.parseInt(txtId.getText());
        try {
        int ordenes = contarOrdenesDelTecnico(idTecnico);
        if (ordenes > 0) {
            JOptionPane.showMessageDialog(this,
                "No puedes eliminar este técnico: tiene " + ordenes + " orden(es) de trabajo asociada(s).");
            return;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al verificar órdenes: " + e.getMessage());
        return;
    }

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este técnico?");
        if (confirmar != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Tecnicos WHERE id_tecnicos=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Técnico eliminado correctamente.");
            limpiarCampos();
            consultar();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        txtId.setText(tabla.getValueAt(fila, 0).toString());
        txtNombre.setText(tabla.getValueAt(fila, 1).toString());
        txtEspecialidad.setText(tabla.getValueAt(fila, 2) != null ? tabla.getValueAt(fila, 2).toString() : "");
        txtTelefono.setText(tabla.getValueAt(fila, 3) != null ? tabla.getValueAt(fila, 3).toString() : "");
        txtCorreo.setText(tabla.getValueAt(fila, 4) != null ? tabla.getValueAt(fila, 4).toString() : "");
        txtIdentificacion.setText(tabla.getValueAt(fila, 5) != null ? tabla.getValueAt(fila, 5).toString() : "");
    }
    private boolean validarCampos() {
    if (txtNombre.getText().trim().isEmpty() ||
        txtEspecialidad.getText().trim().isEmpty() ||
        txtIdentificacion.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nombre, especialidad e identificación son obligatorios.");
        return false;
    }

    String correo = txtCorreo.getText().trim();
    if (!correo.isEmpty() && !correo.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
        JOptionPane.showMessageDialog(this, "El correo no tiene un formato válido.");
        return false;
    }

    return true;
}

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtEspecialidad.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtIdentificacion.setText("");
    }
}

