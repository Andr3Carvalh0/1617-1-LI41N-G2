package pt.isel.ls;


import org.junit.Test;
import pt.isel.ls.Commands.PostTemplates;
import static junit.framework.Assert.assertEquals;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class TemplateTest {
    Connection con = null;

    private final String TEST_NAME = "SQLTest";
    private final String TEST_DESC = "DESCRIPTION";

    private int getTemplatesMaxID(Connection con) throws SQLException {
        String s0 = "select max(Tp_id) from template";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);

    }

    @Test
    public void testPostTemplates() throws SQLException {
        try {
            con = GetConnection.connect();

            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);

            int result = (int) new PostTemplates().execute(map, con);
            assertEquals(getTemplatesMaxID(con), result);

        }finally {
            if(con != null){

                int id = getTemplatesMaxID(con);

                //Delete the test entry
                String s1 = "delete from template where Tp_id = ?";
                PreparedStatement ps = con.prepareStatement(s1);
                ps.setInt(1, id);
                ps.execute();
                con.close();
            }
        }
    }

}
