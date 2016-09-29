package pt.isel.ls;

import org.junit.Test;
import org.junit.runner.Result;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Created by Andre on 29/09/2016.
 */
public class SQLTests {
    private static final String connectionURL = "jdbc:sqlserver://localhost;databaseName=LS;user=sa;password=zaxscdvfbgnhmj;";
    Connection con = null;

    String nomes[] = {"Gonçalo Veloso", "André Carvalho"};
    int numeros[] = {41482, 41839};

    @Test
    public void Connection_Test() throws SQLException {
        con = DriverManager.getConnection(connectionURL);
        con.close();
    }

    public void Setup() throws SQLException {
        con = DriverManager.getConnection(connectionURL);

        con.setAutoCommit(false);

        ResultSet rs;
        String s1 = "delete from student";
        String s2 = "insert into student(nome, numero)\n" +
                "values\n" +
                "(?, ?)";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.execute();

        con.commit();

        ps = con.prepareStatement(s2);

        for (int i = 0; i < nomes.length; i++) {
            ps.setString(1, nomes[i]);
            ps.setInt(2, numeros[i]);
            ps.execute();
        }

        con.commit();
        con.close();
    }

    @Test
    public void Select_Test() throws SQLException {
        Setup();

        con = DriverManager.getConnection(connectionURL);

        ResultSet rs;
        String s1 = "select * from student";

        PreparedStatement ps = con.prepareStatement(s1);
        rs = ps.executeQuery();

        for(int i = 0; rs.next();i++){
            assertEquals(nomes[i], rs.getString(1));
            assertEquals(numeros[i], rs.getInt(2));
        }
        con.close();
    }


    @Test
    public void Insert_Test() throws SQLException {
        Setup();
        con = DriverManager.getConnection(connectionURL);
        con.setAutoCommit(false);

        String s1 = "insert into student(nome, numero)\n" +
                "values\n" +
                "(?, ?)";

        String s2 = "select * from student where nome = 'Florberto Jacinto' and numero = 42069";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, "Florberto Jacinto");
        ps.setInt(2, 42069);
        ps.execute();
        con.commit();
        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();
        rs.next();
        assertEquals("Florberto Jacinto", rs.getString(1));
        assertEquals(42069, rs.getInt(2));
        con.close();
    }


    @Test
    public void Update_Test() throws SQLException {
        Setup();
        con = DriverManager.getConnection(connectionURL);
        con.setAutoCommit(false);

        String s1 = "update student set nome = 'Gonçalo Leal' where numero = 41482";
        String s2 = "select * from student where numero = 41482";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.executeUpdate();
        con.commit();

        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();
        rs.next();
        assertEquals("Gonçalo Leal", rs.getString(1));

        con.close();

    }

    @Test
    public void Delete_Test() throws SQLException {
        Setup();

        con = DriverManager.getConnection(connectionURL);
        con.setAutoCommit(false);

        String s1 = "delete from student where numero = 41839";
        String s2 = "select * from student";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.execute();
        con.commit();

        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();

        int i = 0;

        while (rs.next()){
            i++;
        }

        assertEquals(1, i);

        con.commit();
    }

}