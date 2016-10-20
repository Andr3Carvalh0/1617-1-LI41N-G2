package pt.isel.ls;


import org.junit.Test;
import pt.isel.ls.Commands.GetTemplatesTid;
import pt.isel.ls.Commands.PostTemplates;
import pt.isel.ls.Commands.PostTemplatesTidCreate;
import pt.isel.ls.Commands.PostTemplatesTidTasks;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Template;
import pt.isel.ls.Dtos.Template_Task;


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
    public void PostTemplatesTidTasks() throws SQLException {
        HashMap<String,String> params = new HashMap<>();
        int tp_id = -1;
        int tp_task_id = -1;
        try{
            params.put("name","template");
            params.put("description","template");
            con = GetConnection.connect();
            tp_id = (int) new PostTemplates().execute(params, con);
            params.put("name","template_task");
            params.put("description","template_task");
            params.put("tid", Integer.toString(tp_id));
            tp_task_id = (int) new PostTemplatesTidTasks().execute(params, con);

            Template_Task tt = new Template_Task(tp_id,tp_task_id,params.get("name"), params.get("description"));

            PreparedStatement ps = con.prepareStatement("select * from template_task where Tp_id = ? and Tp_Task_id = ?");
            ps.setInt(1, tp_id);
            ps.setInt(2, tp_task_id);
            ResultSet rs = ps.executeQuery();
            rs.next();

            assertEquals(tt.getTp_id(),rs.getInt(1));
            assertEquals(tt.getTp_Task_id(),rs.getInt(2));
            assertEquals(tt.getTp_Task_name(),rs.getString(3));
            assertEquals(tt.getTp_Task_desc(),rs.getString(4));
        }
        finally {
            if(con != null){
                if(tp_id != -1 && tp_task_id != -1){
                    PreparedStatement dels = con.prepareStatement("DELETE from template_task where Tp_id = ? and Tp_Task_id = ?");
                    dels.setInt(1,tp_id);
                    dels.setInt(2, tp_task_id);
                    dels.executeUpdate();
                    dels = con.prepareStatement("DELETE from template where Tp_id = ?");
                    dels.setInt(1,tp_id);
                }
                con.close();
            }
        }

    }

}
