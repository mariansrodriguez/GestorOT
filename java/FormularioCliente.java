import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class FormularioCliente extends Formulario {

    private JTextField txtId, txtNombre, txtTelefono, txtEmail, txtDireccion, txtCedula;
    private JTable tabla;
    

    public FormularioCliente() {
        super("Gestión de Clientes"); // llama al constructor de Formulario

        JPanel panelCampos = new JPanel(new GridLayout(6, 2, 5, 5));

        txtId = new JTextField();
        txtId.setEditable(false); // el id lo genera SQL Server (IDENTITY), no se escribe a mano
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

        // Cuando el usuario hace click en una fila de la tabla,
        // los datos de esa fila se cargan en los campos de texto (para poder modificar/eliminar)
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
    }

    @Override
    public void insertar() {
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

    @Override
    public void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la tabla primero.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este cliente?");
        if (confirmar != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM Clientes WHERE id_cliente=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
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
        txtTelefono.setText(tabla.getValueAt(fila, 2) != null ? tabla.getValueAt(fila, 2).toString() : "");
        txtEmail.setText(tabla.getValueAt(fila, 3) != null ? tabla.getValueAt(fila, 3).toString() : "");
        txtDireccion.setText(tabla.getValueAt(fila, 4) != null ? tabla.getValueAt(fila, 4).toString() : "");
        txtCedula.setText(tabla.getValueAt(fila, 5) != null ? tabla.getValueAt(fila, 5).toString() : "");
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

