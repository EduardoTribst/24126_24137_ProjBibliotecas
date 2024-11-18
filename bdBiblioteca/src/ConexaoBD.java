import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    public static Connection getConnection(String nomeServidor, String nomeBD, String usuario, char[] senha) throws SQLException {
        String URL = "jdbc:sqlserver://" + nomeServidor + ":1433;"+
                "databaseName="+ nomeBD +
                ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        String USER = usuario;
        String PASSWORD = String.valueOf(senha);


        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
