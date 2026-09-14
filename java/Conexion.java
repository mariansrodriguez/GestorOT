

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
        "jdbc:sqlserver://localhost:1433;databaseName=GestorOT;encrypt=true;trustServerCertificate=true;";
    private static final String USUARIO = "gestorot_user";
    private static final String CONTRASENA = "TuPasswordSegura123!"; // la que pusiste al crear el login

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}