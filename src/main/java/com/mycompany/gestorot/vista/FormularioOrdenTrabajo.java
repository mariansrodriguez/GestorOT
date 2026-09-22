package com.mycompany.gestorot.vista;

import com.mycompany.gestorot.vista.Formulario;
import com.mycompany.gestorot.conexion.Conexion;
import com.mycompany.gestorot.modelo.ItemCombo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FormularioOrdenTrabajo extends Formulario {

    private JTextField txtId, txtDescripcion, txtFechaEntrega, txtCosto;
    private JComboBox<ItemCombo> comboCliente, comboTecnico;
    private JComboBox<String> comboEstado;
    private JTable tabla;

    public FormularioOrdenTrabajo() {
        super("Gestión de Órdenes de Trabajo");

        JPanel panelCampos = new JPanel(new GridLayout(7, 2, 5, 5));

        txtId = new JTextField();
        txtId.setEditable(false);
        comboCliente = new JComboBox<>();
        comboTecnico = new JComboBox<>();
        txtDescripcion = new JTextField();
        txtFechaEntrega = new JTextField(); // formato: 2026-09-20
        comboEstado = new JComboBox<>(new String[]{"pendiente", "en proceso", "terminado"});
        txtCosto = new JTextField();

        panelCampos.add(new JLabel("ID:"));
        panelCampos.add(txtId);
        panelCampos.add(new JLabel("Cliente:"));
        panelCampos.add(comboCliente);
        panelCampos.add(new JLabel("Técnico:"));
        panelCampos.add(comboTecnico);
        panelCampos.add(new JLabel("Descripción:"));
        panelCampos.add(txtDescripcion);
        panelCampos.add(new JLabel("Fecha entrega (AAAA-MM-DD):"));
        panelCampos.add(txtFechaEntrega);
        panelCampos.add(new JLabel("Estado:"));
        panelCampos.add(comboEstado);
        panelCampos.add(new JLabel("Costo:"));
        panelCampos.add(txtCosto);

        add(panelCampos, BorderLayout.NORTH);

        tabla = new JTable();
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());

        cargarCombos(); // llena los combos con los clientes/técnicos existentes al abrir
    }

    // Llena comboCliente y comboTecnico consultando las tablas Clientes y Tecnicos
    private void cargarCombos() {
        comboCliente.removeAllItems();
        comboTecnico.removeAllItems();

        try (Connection con = Conexion.getConexion()) {

            try (PreparedStatement ps = con.prepareStatement("SELECT id_cliente, nombre FROM Clientes");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comboCliente.addItem(new ItemCombo(rs.getInt("id_cliente"), rs.getString("nombre")));
                }
            }

            try (PreparedStatement ps = con.prepareStatement("SELECT id_tecnicos, nombre FROM Tecnicos");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    comboTecnico.addItem(new ItemCombo(rs.getInt("id_tecnicos"), rs.getString("nombre")));
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar combos: " + e.getMessage());
        }
    }

    @Override
    public void insertar() {
        
        ItemCombo cliente = (ItemCombo) comboCliente.getSelectedItem();
        ItemCombo tecnico = (ItemCombo) comboTecnico.getSelectedItem();

        if (cliente == null || tecnico == null) {
            JOptionPane.showMessageDialog(this, "Debes tener al menos un cliente y un técnico creados.");
            return;
        }
        if (!validarCampos()) return;

        String sql = "INSERT INTO OrdenesTrabajo (id_cliente, id_tecnico, descripcion_servicio, fecha_entrega_estimada, estado, costo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cliente.getId());
            ps.setInt(2, tecnico.getId());
            ps.setString(3, txtDescripcion.getText());
            ps.setDate(4, java.sql.Date.valueOf(txtFechaEntrega.getText())); // requiere formato AAAA-MM-DD
            ps.setString(5, (String) comboEstado.getSelectedItem());
            ps.setDouble(6, txtCosto.getText().isEmpty() ? 0.0 : Double.parseDouble(txtCosto.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Orden insertada correctamente.");
            limpiarCampos();
            consultar();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa el formato AAAA-MM-DD (ej: 2026-09-20).");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e.getMessage());
        }
    }

    @Override
    public void consultar() {
        // JOIN con Clientes y Tecnicos para mostrar nombres en vez de solo ids
        String sql = "SELECT o.id_orden, c.nombre AS cliente, t.nombre AS tecnico, " +
                     "o.descripcion_servicio, o.fecha_ingreso, o.fecha_entrega_estimada, o.estado, o.costo, " +
                     "o.id_cliente, o.id_tecnico " +
                     "FROM OrdenesTrabajo o " +
                     "JOIN Clientes c ON o.id_cliente = c.id_cliente " +
                     "JOIN Tecnicos t ON o.id_tecnico = t.id_tecnicos";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Cliente");
            modelo.addColumn("Técnico");
            modelo.addColumn("Descripción");
            modelo.addColumn("Fecha ingreso");
            modelo.addColumn("Fecha entrega");
            modelo.addColumn("Estado");
            modelo.addColumn("Costo");
            modelo.addColumn("id_cliente");  // columnas ocultas, las usamos internamente
            modelo.addColumn("id_tecnico");  // (para poder editar sin perder el id real)

            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_orden"),
                    rs.getString("cliente"),
                    rs.getString("tecnico"),
                    rs.getString("descripcion_servicio"),
                    rs.getDate("fecha_ingreso"),
                    rs.getDate("fecha_entrega_estimada"),
                    rs.getString("estado"),
                    rs.getDouble("costo"),
                    rs.getInt("id_cliente"),
                    rs.getInt("id_tecnico")
                });
            }

            tabla.setModel(modelo);
            // Ocultamos las dos últimas columnas (id_cliente, id_tecnico) - son solo para uso interno
            tabla.getColumnModel().getColumn(8).setMinWidth(0);
            tabla.getColumnModel().getColumn(8).setMaxWidth(0);
            tabla.getColumnModel().getColumn(9).setMinWidth(0);
            tabla.getColumnModel().getColumn(9).setMaxWidth(0);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al consultar: " + e.getMessage());
        }
    }

    @Override
    public void modificar() {
        if (!validarCampos()) return;
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona una orden de la tabla primero.");
            return;
        }

        ItemCombo cliente = (ItemCombo) comboCliente.getSelectedItem();
        ItemCombo tecnico = (ItemCombo) comboTecnico.getSelectedItem();

        String sql = "UPDATE OrdenesTrabajo SET id_cliente=?, id_tecnico=?, descripcion_servicio=?, " +
                     "fecha_entrega_estimada=?, estado=?, costo=? WHERE id_orden=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cliente.getId());
            ps.setInt(2, tecnico.getId());
            ps.setString(3, txtDescripcion.getText());
            ps.setDate(4, java.sql.Date.valueOf(txtFechaEntrega.getText()));
            ps.setString(5, (String) comboEstado.getSelectedItem());
            ps.setDouble(6, txtCosto.getText().isEmpty() ? 0.0 : Double.parseDouble(txtCosto.getText()));
            ps.setInt(7, Integer.parseInt(txtId.getText()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Orden modificada correctamente.");
            limpiarCampos();
            consultar();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa el formato AAAA-MM-DD.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }

    @Override
    public void eliminar() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona una orden de la tabla primero.");
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar esta orden?");
        if (confirmar != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM OrdenesTrabajo WHERE id_orden=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(txtId.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Orden eliminada correctamente.");
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
        txtDescripcion.setText(tabla.getValueAt(fila, 3) != null ? tabla.getValueAt(fila, 3).toString() : "");
        txtFechaEntrega.setText(tabla.getValueAt(fila, 5) != null ? tabla.getValueAt(fila, 5).toString() : "");
        comboEstado.setSelectedItem(tabla.getValueAt(fila, 6).toString());
        txtCosto.setText(tabla.getValueAt(fila, 7).toString());

        int idCliente = (int) tabla.getValueAt(fila, 8);
        int idTecnico = (int) tabla.getValueAt(fila, 9);

        // Selecciona en el combo el item cuyo id coincida con el guardado
        for (int i = 0; i < comboCliente.getItemCount(); i++) {
            if (comboCliente.getItemAt(i).getId() == idCliente) {
                comboCliente.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < comboTecnico.getItemCount(); i++) {
            if (comboTecnico.getItemAt(i).getId() == idTecnico) {
                comboTecnico.setSelectedIndex(i);
                break;
            }
        }
    }
    private boolean validarCampos() {
    if (comboCliente.getSelectedItem() == null || comboTecnico.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Debes seleccionar un cliente y un técnico.");
        return false;
    }

    if (txtDescripcion.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "La descripción del servicio es obligatoria.");
        return false;
    }

    if (!txtFechaEntrega.getText().trim().matches("\\d{4}-\\d{2}-\\d{2}")) {
        JOptionPane.showMessageDialog(this, "La fecha debe tener el formato AAAA-MM-DD.");
        return false;
    }

    String costo = txtCosto.getText().trim();
    if (!costo.isEmpty() && !costo.matches("\\d+(\\.\\d{1,2})?")) {
        JOptionPane.showMessageDialog(this, "El costo debe ser un número válido (ej: 80000 o 80000.50).");
        return false;
    }

    return true;
}

    private void limpiarCampos() {
        txtId.setText("");
        txtDescripcion.setText("");
        txtFechaEntrega.setText("");
        txtCosto.setText("");
        if (comboCliente.getItemCount() > 0) comboCliente.setSelectedIndex(0);
        if (comboTecnico.getItemCount() > 0) comboTecnico.setSelectedIndex(0);
    }
}

