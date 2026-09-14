

public class OrdenTrabajo {
    private int idOrden;
    private int idCliente;
    private int idTecnico;
    private String descripcionServicio;
    private java.sql.Date fechaIngreso;
    private java.sql.Date fechaEntregaEstimada;
    private String estado;
    private double costo;

    public OrdenTrabajo() {
    }

    public OrdenTrabajo(int idOrden, int idCliente, int idTecnico, String descripcionServicio,
                         java.sql.Date fechaIngreso, java.sql.Date fechaEntregaEstimada,
                         String estado, double costo) {
        this.idOrden = idOrden;
        this.idCliente = idCliente;
        this.idTecnico = idTecnico;
        this.descripcionServicio = descripcionServicio;
        this.fechaIngreso = fechaIngreso;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estado = estado;
        this.costo = costo;
    }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdTecnico() { return idTecnico; }
    public void setIdTecnico(int idTecnico) { this.idTecnico = idTecnico; }

    public String getDescripcionServicio() { return descripcionServicio; }
    public void setDescripcionServicio(String descripcionServicio) { this.descripcionServicio = descripcionServicio; }

    public java.sql.Date getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(java.sql.Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public java.sql.Date getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(java.sql.Date fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
}