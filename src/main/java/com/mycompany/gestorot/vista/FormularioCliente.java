package com.mycompany.gestorot.vista;
import com.mycompany.gestorot.conexion.Conexion;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FormularioCliente extends Formulario {

    private JTextField txtId, txtNombre, txtTelefono, txtEmail, txtDireccion, txtCedula;
    private JTable tabla;
    

    public FormularioCliente() {
        super("Gestión de Clientes"); 

        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 5, 5));

        txtId = new JTextField();
        txtId.setEditable(false); 
        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();
        txtCedula = new JTextField();

        panelCampos.add(new JLabel("ID:"));
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Teléfono:"));
        panelCampos.add(txtTelefono);
        panelCampos.add(new JLabel("Email:"));
        panelCampos.add(txtEmail);
        panelCampos.add(new JLabel("Dirección:"));
        panelCampos.add(txtDireccion);
        panelCampos.add(new JLabel("Cédula/NIT:"));
        panelCampos.add(txtCedula);

        add(panelCampos, BorderLayout.NORTH);

        tabla = new JTable();
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
    }

    @Override
    public void insertar() {
        if (!validarCampos()) return;
        String sql = "INSERT INTO Clientes (nombre, telefono, email, direccion, cedula_nit) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtTelefono.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, txtDireccion.getText());
            ps.setString(5, txtCedula.getText());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente insertado correctamente.");
            limpiarCampos();
            consultar(); // refresca la tabla

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e.getMessage());
        }
    }

    @Override
    public void consultar() {
        String sql = "SELECT * FROM Clientes";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Teléfono");
            modelo.addColumn("Email");
            modelo.addColumn("Dirección");
            modelo.addColumn("Cédula/NIT");

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion"),
                    rs.getString("cedula_nit")
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
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla primero.");
            return;
        }
        if (!validarCampos()) return;


        String sql = "UPDATE Clientes SET nombre=?, telefono=?, email=?, direccion=?, cedula_nit=? WHERE id_cliente=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtTelefono.getText());
            ps.setString(3, txtEmail.getText());
            ps.setString(4, txtDireccion.getText());
            ps.setString(5, txtCedula.getText());
            ps.setInt(6, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente modificado correctamente.");
            limpiarCampos();
            consultar();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }
    private int contarOrdenesDelCliente(int idCliente) throws SQLException {
    String sql = "SELECT COUNT(*) FROM OrdenesTrabajo WHERE id_cliente = ?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idCliente);
        try (ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}

   @Override
public void eliminar() {
    if (txtId.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla primero.");
        return;
    }

    int idCliente = Integer.parseInt(txtId.getText());

    try {
        int ordenes = contarOrdenesDelCliente(idCliente);
        if (ordenes > 0) {
            JOptionPane.showMessageDialog(this,
                "No puedes eliminar este cliente: tiene " + ordenes + " orden(es) de trabajo asociada(s).");
            return;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al verificar órdenes: " + e.getMessage());
        return;
    }

    int confirmar = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este cliente?");
    if (confirmar != JOptionPane.YES_OPTION) return;


    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        txtId.setText(tabla.getValueAt(fila, 0).toString());
        txtNombre.setText(tabla.getValueAt(fila, 1).toString());
        txtTelefono.setText(tabla.getValueAt(fila, 2) != null ? tabla.getValueAt(fila, 2).toString() : "");
        txtEmail.setText(tabla.getValueAt(fila, 3) != null ? tabla.getValueAt(fila, 3).toString() : "");
        txtDireccion.setText(tabla.getValueAt(fila, 4) != null ? tabla.getValueAt(fila, 4).toString() : "");
        txtCedula.setText(tabla.getValueAt(fila, 5) != null ? tabla.getValueAt(fila, 5).toString() : "");
    }
    
    private boolean validarCampos() {
    if (txtNombre.getText().trim().isEmpty() ||
        txtTelefono.getText().trim().isEmpty() ||
        txtCedula.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nombre, teléfono y cédula/NIT son obligatorios.");
        return false;
    }

    String email = txtEmail.getText().trim();
    if (!email.isEmpty() && !email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
        JOptionPane.showMessageDialog(this, "El email no tiene un formato válido.");
        return false;
    }

    if (!txtTelefono.getText().trim().matches("\\d{7,10}")) {
        JOptionPane.showMessageDialog(this, "El teléfono debe tener entre 7 y 10 dígitos.");
        return false;
    }

    return true;
}

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        txtCedula.setText("");
    }
}

