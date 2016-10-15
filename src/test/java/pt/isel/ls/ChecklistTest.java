package pt.isel.ls;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import pt.isel.ls.Commands.GetChecklists;
import pt.isel.ls.Commands.PostChecklist;
import pt.isel.ls.Dtos.Checklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;

public class ChecklistTest {
    Connection con = null;

    private final String TEST_NAME = "SQLTest";
    private final String TEST_DESC = "DESCRIPTION";
    private final String TEST_DATE = "06-10-2016";

    private int getChecklistMaxID(Connection con) throws SQLException {
        String s0 = "select max(Cl_id) from checklist";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);

    }

    private void addChecklist(Connection con) throws SQLException {
        String s1 = "insert into checklist(Cl_name,  Cl_desc, Cl_duedate) values (?, ?, CAST(? as datetime))";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, TEST_NAME);
        ps.setString(2, TEST_DESC);
        ps.setString(3, TEST_DATE);

        ps.execute();

    }

    @Test
    public void testGetChecklist() throws SQLException {
        try {
            con = GetConnection.connect();
            addChecklist(con);

            LinkedList result = (LinkedList) new GetChecklists().execute(null, con);

            assertEquals(1, result.size());
            assertEquals(true, ((Checklist)result.get(0)).getName().equals(TEST_NAME));
            assertEquals(true, ((Checklist)result.get(0)).getDescription().equals(TEST_DESC));
            assertEquals(true, ((Checklist)result.get(0)).getDueDate().equals(TEST_DATE));

        }finally {
            if(con != null){
                int id = getChecklistMaxID(con);

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
    public void testPostChecklist() throws SQLException {
        try {

            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("dueDate", TEST_DATE);

            con = GetConnection.connect();
            int result = (int) new PostChecklist().execute(map, con);
            assertEquals(getChecklistMaxID(con), result);

        }finally {
            if(con != null){

                int id = getChecklistMaxID(con);

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
