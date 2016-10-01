package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;


public class SQLTests {
    Connection con = null;
    static SQLServerDataSource src;

    private final static int user = 1;
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


    private void setup() throws SQLException {

        con = src.getConnection();

        String s1 = "insert into student(nome, numero)\n" +
                "values\n" +
                "(?, ?)";

        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, TESTNAME);
        ps.setInt(2, TESTNUMBER);
        ps.execute();

    }

    @Test
    public void selectTest() throws SQLException {
        try {
            setup();

            ResultSet rs;
            String s1 = "select * from student where nome = '" + TESTNAME + "' and numero = " + TESTNUMBER;

            PreparedStatement ps = con.prepareStatement(s1);
            rs = ps.executeQuery();

            /* Revert the database to its pre-test state, before asserting the obtained result set.
             This way, the database will remain untouched, regardless of the assert tests. */
            ps = con.prepareStatement("delete from student where numero = " + TESTNUMBER);
            ps.execute();

            rs.next();
            assertEquals(TESTNAME, rs.getString(1));
            assertEquals(TESTNUMBER, rs.getInt(2));

        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Test
    public void insertTest() throws SQLException {
        try {
            setup();
            con = src.getConnection();

            String s1 = "select * from student where nome = '" + TESTNAME + "' and numero = " + TESTNUMBER;

            PreparedStatement ps = con.prepareStatement(s1);

            ResultSet rs = ps.executeQuery();

            /* Revert the database to its pre-test state, before asserting the obtained result set.
             This way, the database will remain untouched, regardless of the assert tests. */
            ps = con.prepareStatement("delete from student where numero = " + TESTNUMBER);
            ps.execute();

            rs.next();

            assertEquals(TESTNAME, rs.getString(1));
            assertEquals(TESTNUMBER, rs.getInt(2));

        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Test
    public void updateTest() throws SQLException {
        try {
            setup();

            String s1 = "update student set nome = 'SQLUPDATETEST' where numero = " + TESTNUMBER;
            String s2 = "select * from student where numero = " + TESTNUMBER;

            PreparedStatement ps = con.prepareStatement(s1);
            ps.executeUpdate();

            ps = con.prepareStatement(s2);
            ResultSet rs = ps.executeQuery();

            /* Revert the database to its pre-test state, before asserting the obtained result set.
             This way, the database will remain untouched, regardless of the assert tests. */
            ps = con.prepareStatement("delete from student where numero = " + TESTNUMBER);
            ps.execute();

            rs.next();
            assertEquals("SQLUPDATETEST", rs.getString(1));

        }finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Test
    public void deleteTest() throws SQLException {
        try {
            setup();

            String s1 = "delete from student where numero = " + TESTNUMBER;
            String s2 = "select * from student where numero = " + TESTNUMBER;

            PreparedStatement ps = con.prepareStatement(s1);
            ps.execute();

            ps = con.prepareStatement(s2);
            ResultSet rs = ps.executeQuery();
            assertEquals(false, rs.isBeforeFirst());
        }
        finally {
            if (con != null) {
                con.close();
            }
        }
    }
}