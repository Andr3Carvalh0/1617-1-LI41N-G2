package pt.isel.ls;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;


public class SQLTests {
    Connection con = null;
    static SQLServerDataSource src;

    private final static int user = 0;
    private final static String serverPass[] = {"zaxscdvfbgnhmj", "sa"};
    private final static String serverName[] = {"WIN-773BLA1UH43", ""};

    private String nomes[] = {"Gonçalo Veloso", "André Carvalho"};
    private int numeros[] = {41482, 41839};

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

        for (int i = 0; i < nomes.length; i++) {
            ps.setString(1, nomes[i]);
            ps.setInt(2, numeros[i]);
            ps.execute();
        }

    }

    @Test
    public void selectTest() throws SQLException {
        try {
            setup();

            ResultSet rs;
            String s1 = "select * from student";

            PreparedStatement ps = con.prepareStatement(s1);
            rs = ps.executeQuery();

            for (int i = 0; rs.next(); i++) {
                assertEquals(nomes[i], rs.getString(1));
                assertEquals(numeros[i], rs.getInt(2));
            }
        } finally {
            con.close();
        }
    }

    private void clearDT() throws SQLException {
        String s1 = "delete from student";
        PreparedStatement ps = con.prepareStatement(s1);
        ps.execute();

    }

    @Test
    public void insertTest() throws SQLException {
        try {
            con = src.getConnection();

            String s1 = "insert into student(nome, numero)\n" +
                    "values\n" +
                    "(?, ?)";

            String s2 = "select * from student where nome = 'Florberto Jacinto' and numero = 42069";

            PreparedStatement ps = con.prepareStatement(s1);
            ps.setString(1, "Florberto Jacinto");
            ps.setInt(2, 42069);
            ps.execute();

            ps = con.prepareStatement(s2);
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals("Florberto Jacinto", rs.getString(1));
            assertEquals(42069, rs.getInt(2));

        } finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Test
    public void Update_Test() throws SQLException {
        try {
            setup();

            String s1 = "update student set nome = 'Gonçalo Leal' where numero = 41482";
            String s2 = "select * from student where numero = 41482";

            PreparedStatement ps = con.prepareStatement(s1);
            ps.executeUpdate();

            ps = con.prepareStatement(s2);
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals("Gonçalo Leal", rs.getString(1));

        }finally {
            if (con != null) {
                con.close();
            }
        }
    }

    @Test
    public void Delete_Test() throws SQLException {
        try {
            setup();

            String s1 = "delete from student where numero = 41839";
            String s2 = "select * from student";

            PreparedStatement ps = con.prepareStatement(s1);
            ps.execute();

            ps = con.prepareStatement(s2);
            ResultSet rs = ps.executeQuery();

            int i = 0;

            while (rs.next()) {
                i++;
            }

            assertEquals(1, i);
        }
        finally {
            if (con != null) {
                con.close();
            }
        }
    }
}