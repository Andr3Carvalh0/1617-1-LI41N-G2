package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;

public class SQLTest {
    Connection con = null;
    static SQLServerDataSource src;

    private final static int user = 0;
    private final static String serverPass[] = {"zaxscdvfbgnhmj", "sa"};
    private final static String serverName[] = {"WIN-773BLA1UH43", "user-PC"};

    private final String TESTNAME = "SQLTest";
    private final int TESTNUMBER = 11111;

    @Test
    public void connectionTest() throws SQLException {
        try {
            con = src.getConnection();
            assertEquals(true, con != null);
        } finally {
            assert con != null;
            if (con != null) {
                con.close();
            }
        }
    }

    @BeforeClass
    public static void newDataSource() {
        src = new SQLServerDataSource();
        src.setPassword(serverPass[user]);
        src.setUser("sa");
        src.setDatabaseName("LS");
        src.setServerName(serverName[user]);
    }

    private int getMaxID(Connection con) throws SQLException {
        String s0 = "select max(Cl_id) from checklist";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);

    }

    private void setup(Connection con) throws SQLException {
        String s1 = "insert into checklist(Cl_name, Cl_duedate, Cl_desc) values (?, CAST(? as datetime), ?)";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, "SQLTEST");
        ps.setString(2, "06-10-2016");
        ps.setString(3, "TEST");

        ps.execute();

    }

    @Test
    public void testPostChecklist() throws SQLException {
        try {
            con = src.getConnection();
            int result = PostChecklist.postChecklist("SQLTEST", "06-10-2016", "TEST", con);
            assertEquals(getMaxID(con), result);

        }finally {
            if(con != null){

                int id = getMaxID(con);

                //Delete the test entry
                String s1 = "delete from checklist where Cl_id = ?";
                PreparedStatement ps = con.prepareStatement(s1);
                ps.setInt(1, id);

                ps.execute();
                con.close();
            }
        }
    }

    @Test
    public void testGetChecklist() throws SQLException {
        try {
            con = src.getConnection();
            setup(con);

            LinkedList result = GetChecklist.getChecklists(con);

            assertEquals(1, result.size());

        }finally {
            if(con != null){
                int id = getMaxID(con);

                //Delete the test entry
                String s1 = "delete from checklist where Cl_id = ?";
                PreparedStatement ps = con.prepareStatement(s1);
                ps.setInt(1, id);
                ps.execute();
                con.close();
            }
        }
    }
}
