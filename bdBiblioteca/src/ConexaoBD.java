import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String
            URL = "jdbc:sqlserver://regulus.cotuca.unicamp.br:1433;"+
                  "databaseName=chico"+
            ";integratedSecurity=false;encrypt=true;trustServerCertificate=true";
    private static final String USER = "chico";
    private static final String PASSWORD = "82Eridani";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
