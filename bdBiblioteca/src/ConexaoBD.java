import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    public static Connection getConnection(String nomeServidor, String nomeBD, String usuario, String senha) throws SQLException {
        URL = "jdbc:sqlserver://" + nomeServidor + ":1433;"+
                "databaseName="+ nomeBD +
                ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        USER = usuario;
        PASSWORD = senha;


        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
