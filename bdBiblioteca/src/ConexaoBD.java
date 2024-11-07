import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String
            URL = "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;"+
                  "databaseName=BD24137"+
            ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
    private static final String USER = "BD24137";
    private static final String PASSWORD = "Laracookiefub@";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
