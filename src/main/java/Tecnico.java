
public class Tecnico {
    private int idTecnico;
    private String nombre;
    private String especialidad;
    private String telefono;
    private String correo;
    private String identificacion;

    public Tecnico() {
    }

    public Tecnico(int idTecnico, String nombre, String especialidad,
                    String telefono, String correo, String identificacion) {
        this.idTecnico = idTecnico;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.correo = correo;
        this.identificacion = identificacion;
    }

    public int getIdTecnico() { return idTecnico; }
    public void setIdTecnico(int idTecnico) { this.idTecnico = idTecnico; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
}