package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.Commands.GetChecklists;
import pt.isel.ls.Commands.PostChecklist;
import pt.isel.ls.DTO.Checklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;

public class SQLTest {
    Connection con = null;
    static SQLServerDataSource src;

    private final static int user = 0;
    private final static String serverPass[] = {"zaxscdvfbgnhmj", "sa"};
    private final static String serverName[] = {"WIN-773BLA1UH43", "user-PC"};

    private final String TESTNAME = "SQLTest";
    private final String TESTDESC = "DESCRIPTION";
    private final String TESTDATE = "06-10-2016";

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
        String s1 = "insert into checklist(Cl_name,  Cl_desc, Cl_duedate) values (?, ?, CAST(? as datetime))";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, TESTNAME);
        ps.setString(2, TESTDESC);
        ps.setString(3, TESTDATE);

        ps.execute();

    }

    @Test
    public void testPostChecklist() throws SQLException {
        try {

            HashMap<String, String> map = new HashMap<>();
            map.put("name", TESTNAME);
            map.put("description", TESTDESC);
            map.put("dueDate",TESTDATE);

            con = src.getConnection();
            int result = (int) new PostChecklist().execute(map, con);
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

            LinkedList result = (LinkedList) new GetChecklists().execute(null, con);

            assertEquals(1, result.size());
            assertEquals(true, ((Checklist)result.get(0)).getName().equals(TESTNAME));
            assertEquals(true, ((Checklist)result.get(0)).getDescription().equals(TESTDESC));
            assertEquals(true, ((Checklist)result.get(0)).getDueDate().equals(TESTDATE));

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
