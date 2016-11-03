package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;

public class GetConnection {
    static SQLServerDataSource src;

    // Environment variables
    private static String server;
    private static String database;
    private static String user;
    private static String password;

    public static Connection connect(boolean debug) throws Exception {
        setEnvironmentVariables(debug);
        if (src == null) {
            if (server == null || password == null || user == null || database == null)
                throw new Exception("ERROR: Environment variables not properly set. Please set them as follows:\n" +
                        "LS_DBCONN_APP_SQLSRV - properties for the application database - SQL Server.\n" +
                        "LS_DBCONN_TEST_SQLSRV - properties for the test database - SQL Server.\n" +
                        "\n" +
                        "The properties are represented as a sequence of name value pairs (name=pair) separated by ';'. E.g. server=myServerAddress;database=myDataBase;user=myUsername;password=myPassword;");
            newDataSource();
        }
        return src.getConnection();
    }

    private static void setEnvironmentVariables(boolean debug) {
        String EnvVars = debug ? System.getenv("LS_DBCONN_TEST_SQLSRV") : System.getenv("LS_DBCONN_APP_SQLSRV");
        String[] EnvVarsArray = EnvVars.split(";");
        server = EnvVarsArray[0].split("=")[1];
        database = EnvVarsArray[1].split("=")[1];
        user = EnvVarsArray[2].split("=")[1];
        password = EnvVarsArray[3].split("=")[1];
    }

    private static void newDataSource() {
        src = new SQLServerDataSource();
        src.setPassword(password);
        src.setUser(user);
        src.setDatabaseName(database);
        src.setServerName(server);
    }
}
