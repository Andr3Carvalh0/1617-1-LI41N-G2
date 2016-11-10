package pt.isel.ls;


import org.junit.Test;
import pt.isel.ls.Commands.GetTemplates;
import pt.isel.ls.Commands.GetTemplatesTid;
import pt.isel.ls.Commands.PostTemplates;
import pt.isel.ls.Commands.PostTemplatesTidCreate;
import pt.isel.ls.Commands.PostTemplatesTidTasks;
import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Template;
import pt.isel.ls.Dtos.Template_Task;
import pt.isel.ls.Utils.GetConnection;


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

    private int getLastInsertedTemplate(Connection con) throws SQLException {
        String s0 = "select max(Tp_id) from template";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);

    }

    private int getLastInsertedChecklist(Connection con) throws SQLException {
        String s0 = "select max(Cl_id) from checklist";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);

    }

    private void addTemplate(String name, String desc, Connection con) throws SQLException {

        String s1 = "insert into template(Tp_name, Tp_desc) values (?, ?)";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, name);
        ps.setString(2, desc);

        ps.execute();
    }

    private void addTemplateTask(int Tp_id, String Tp_Task_name, String Tp_Task_desc, Connection con) throws SQLException {
        String s1 = "insert into template_task(Tp_id, Tp_Task_name, Tp_Task_desc) values (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setInt(1, Tp_id);
        ps.setString(2, Tp_Task_name);
        ps.setString(3, Tp_Task_desc);

        ps.execute();
    }

    private void addChecklistFromTemplate(String Cl_name, String Cl_desc, int Cl_closed, String Cl_duedate, int Tp_id, Connection con) throws SQLException {
        String s1 = "insert into checklist(Cl_name, Cl_desc, Cl_closed, Cl_duedate, Tp_id) values (?,?,?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, Cl_name);
        ps.setString(2, Cl_desc);
        ps.setInt(3, Cl_closed);
        ps.setString(4, Cl_duedate);
        ps.setInt(5, Tp_id);

        ps.execute();
    }


    @Test
    public void testPostTemplates() throws Exception {
        try {
            con = GetConnection.connect(true);

            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);

            int result = (int) new PostTemplates().execute(map, con);
            assertEquals(getLastInsertedTemplate(con), result);

        } finally {
            if (con != null) {

                int id = getLastInsertedTemplate(con);

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
    public void testPostTemplatesTidCreate() throws Exception {
        int tid = -1, cid = -1;
        String s;
        PreparedStatement ps;
        ResultSet rs;
        try {
            con = GetConnection.connect(true);

            addTemplate("TEST_TEMPLATE", "This is Tree_Commands test", con);
            tid = getLastInsertedTemplate(con);
            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("{tid}", Integer.toString(tid));

            new PostTemplatesTidCreate().execute(map, con);
            cid = getLastInsertedChecklist(con);
            s = "select Tp_id from checklist where Cl_id = " + cid;
            ps = con.prepareStatement(s);
            rs = ps.executeQuery();
            rs.next();
            assertEquals(rs.getInt(1), tid);
        } finally {
            if (con != null) {
                s = "delete from template where Tp_id = " + tid;
                ps = con.prepareStatement(s);
                ps.execute();

                con.close();
            }
        }
    }

    @Test
    public void testGetTemplatesTid() throws Exception {
        try {
            con = GetConnection.connect(true);

            //Populate - Template
            addTemplate("Template1", "Desc", con);

            int id = getLastInsertedTemplate(con);
            HashMap<String, String> map = new HashMap<>();
            map.put("{tid}", id + "");

            //Populate - Template_Task
            for (int i = 0; i < 5; i++) {
                addTemplateTask(id, "Name" + i, "Desc" + i, con);
            }

            //Populate Checklist
            addChecklistFromTemplate("Check1", "Desc", 0, null, id, con);

            DtoWrapper result = (DtoWrapper) new GetTemplatesTid().execute(map, con);
            assertEquals(id, ((Template) result.getTemplate()).getTp_id());
            assertEquals(1, ((LinkedList<Checklist>) result.getChecklist()).size());
            assertEquals("Check1", ((LinkedList<Checklist>) result.getChecklist()).get(0).getName());
        } finally {
            if (con != null) {

                int id = getLastInsertedTemplate(con);

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
            con = GetConnection.connect(true);
            addTemplate("TEST_TEMPLATE_1","This is the first test", con);
            tid1 = getLastInsertedTemplate(con);
            addTemplate("TEST_TEMPLATE_2", "This is the second test", con);
            tid2 = getLastInsertedTemplate(con);

            // 2 - Get template list.
            LinkedList<Template> templateLinkedList = (LinkedList) new GetTemplates().execute(null, con);

            // 3 - Find test templates in the obtained list.
            int testTemplatesFound = 0;
            for (Template t : templateLinkedList) {
                if (t.getTp_id() == tid1 || t.getTp_id() == tid2) testTemplatesFound++;
            }
            assertEquals(2, testTemplatesFound);
        } finally {
            {
                if (con != null) {
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
    @Test
    public void PostTemplatesTidTasks() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        int tp_id = -1;
        int tp_task_id = -1;
        try {

            con = GetConnection.connect(true);
            addTemplate("template", "template", con);

            tp_id = getLastInsertedTemplate(con);

            params.put("name", "template_task");
            params.put("description", "template_task");
            params.put("{tid}", Integer.toString(tp_id));
            tp_task_id = (int) new PostTemplatesTidTasks().execute(params, con);

            Template_Task tt = new Template_Task(tp_id, tp_task_id, params.get("name"), params.get("description"));

            PreparedStatement ps = con.prepareStatement("select * from template_task where Tp_id = ? and Tp_Task_id = ?");
            ps.setInt(1, tp_id);
            ps.setInt(2, tp_task_id);
            ResultSet rs = ps.executeQuery();
            rs.next();

            assertEquals(tt.getTp_id(), rs.getInt(1));
            assertEquals(tt.getTp_Task_id(), rs.getInt(2));
            assertEquals(tt.getTp_Task_name(), rs.getString(3));
            assertEquals(tt.getTp_Task_desc(), rs.getString(4));
        } finally {
            if (con != null) {
                int id = getLastInsertedTemplate(con);
                PreparedStatement dels = con.prepareStatement("DELETE from template where Tp_id = ?");
                dels.setInt(1, id);
                dels.execute();
                con.close();
            }
        }
    }
}