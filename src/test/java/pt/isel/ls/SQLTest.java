package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLTest {
    Connection con = null;
    static SQLServerDataSource src;

    @Test
    public void testPostChecklist() throws SQLException {
        src = new SQLServerDataSource();
        src.setPassword("sa");
        src.setUser("sa");
        src.setDatabaseName("LS");
        src.setServerName("user-PC");
        con = src.getConnection();
        PostChecklist.postChecklist("SQLTEST", "06-10-2016", "TEST", con);
    }
}
