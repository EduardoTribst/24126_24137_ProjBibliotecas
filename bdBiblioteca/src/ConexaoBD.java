import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static String URL;
    private static String USER;
    private static String PASSWORD;

    public ConexaoBD(String databaseName, String user, String password) {
        URL = "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;"+
                "databaseName="+ databaseName +
                ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
        USER = user;
        PASSWORD = password;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
