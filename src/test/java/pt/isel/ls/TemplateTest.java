package pt.isel.ls;


import org.junit.Test;
import pt.isel.ls.Commands.GetTemplates;
import pt.isel.ls.Commands.GetTemplatesTid;
import pt.isel.ls.Commands.PostTemplates;
import pt.isel.ls.Commands.PostTemplatesTidCreate;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Template;


import static junit.framework.Assert.assertEquals;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class TemplateTest {
    private Connection con = null;

    private final String TEST_NAME = "SQLTest";
    private final String TEST_DESC = "DESCRIPTION";

    private int getTemplatesMaxID(Connection con) throws SQLException {
        String s0 = "select max(Tp_id) from template";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);

    }

    private int createTemplate() throws SQLException {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", TEST_NAME);
        map.put("description", TEST_DESC);

        return (int) new PostTemplates().execute(map, con);
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
    @Test
    public void testPostTemplatesTidCreate() throws SQLException {
        try {
            con = GetConnection.connect();

            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("{tid}", "0");

            new PostTemplatesTidCreate().execute(map, con);

        }finally {
            if(con != null){
                //TODO
                con.close();
            }
        }
    }


    @Test
    public void testGetTemplatesTid() throws Exception {
        try {
            con = GetConnection.connect();

            //Populate - Template
            int id = createTemplate();
            HashMap<String, String> map = new HashMap<>();
            map.put("{tid}", id+"");

            //Populate - Template_Task



            DtoWrapper result = (DtoWrapper) new GetTemplatesTid().execute(map, con);
            assertEquals(id, ((Template)result.getTemplate()).getTp_id());
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

    @Test
    public void testGetTemplates() throws Exception {
        int tid1 = -1, tid2 = -1;
        try {
            // 1 - Populate templates.
            con = GetConnection.connect();
            tid1 = createTemplate();
            tid2 = createTemplate();

            // 2 - Get template list.
            LinkedList<Template> templateLinkedList = (LinkedList)new GetTemplates().execute(null, con);

            // 3 - Find test templates in the obtained list.
            int testTemplatesFound = 0;
            for (Template t: templateLinkedList) {
                if (t.getTp_id() == tid1 || t.getTp_id() == tid2) testTemplatesFound++;
            }
            assertEquals(2, testTemplatesFound);
        } finally {
            {
                if(con != null) {
                    // Delete test templates from database.
                    PreparedStatement ps = con.prepareStatement("delete from template where Tp_id = ?");
                    ps.setInt(1, tid1);
                    ps.execute();
                    ps.setInt(1, tid2);
                    ps.execute();
                    con.close();
                }
            }
        }
    }

}
