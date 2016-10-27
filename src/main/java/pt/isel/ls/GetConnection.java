package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import java.sql.Connection;

public class GetConnection {
    static SQLServerDataSource src;

    // Environment variables
    private final static String SERVER = System.getenv("ls_machine_name");
    private final static String PASSWORD = System.getenv("ls_password");
    private final static String USERNAME = System.getenv("ls_username");
    private static String DB_NAME;

    public static Connection connect(boolean debug) throws Exception {
        if (src == null || (debug && DB_NAME.equals(System.getenv("ls_db"))) || (!debug && DB_NAME.equals(System.getenv("ls_db_test")))) {
            DB_NAME = (debug) ? System.getenv("ls_db_test") : System.getenv("ls_db");
            if (SERVER == null || PASSWORD == null || USERNAME == null || DB_NAME == null)
                throw new Exception("ERROR: Environment variables not properly set. Please set them as follows:\n" +
                        "ls_machine_name: Your machine's name.\n" +
                        "ls_password: Your SQL password.\n" +
                        "ls_username: Your SQL username.\n" +
                        "ls_db: Your main SQL database.\n" +
                        "ls_db_test: Your test SQL database.\n");
            newDataSource();
        }
        return src.getConnection();
    }

    private static void newDataSource() {
        src = new SQLServerDataSource();
        src.setPassword(PASSWORD);
        src.setUser(USERNAME);
        src.setDatabaseName(DB_NAME);
        src.setServerName(SERVER);
    }
}
