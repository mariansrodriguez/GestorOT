

public class Cliente {
    private int idCliente;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String cedulaNit;
    private java.sql.Date fechaRegistro;

    // Constructor vacío (necesario para crear el objeto antes de llenarlo)
    public Cliente() {
    }

    // Constructor con todos los campos (útil al traer datos desde SQL)
    public Cliente(int idCliente, String nombre, String telefono, String email,
                   String direccion, String cedulaNit, java.sql.Date fechaRegistro) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.cedulaNit = cedulaNit;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCedulaNit() { return cedulaNit; }
    public void setCedulaNit(String cedulaNit) { this.cedulaNit = cedulaNit; }

    public java.sql.Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(java.sql.Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}