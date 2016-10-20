package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;

public class GetConnection {
    static SQLServerDataSource src;

    private final static int user = 0;
    private final static String serverPass[] = {"zaxscdvfbgnhmj", "sa", "sa"};
    private final static String serverName[] = {"WIN-773BLA1UH43", "user-PC", "ASUSTRON"};

    public static Connection connect() throws SQLServerException {
        if(src == null) newDataSource();
        return src.getConnection();
    }

    private static void newDataSource() {
        src = new SQLServerDataSource();
        src.setPassword(serverPass[user]);
        src.setUser("sa");
        src.setDatabaseName("LS");
        src.setServerName(serverName[user]);
    }
}
