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

    private void generateChecklist_Tasks(int Cl_id, Connection con) throws SQLException {
        String s1 = "insert into checklist_task(Cl_id ,Cl_Task_index, Cl_Task_Closed, Cl_Task_name, Cl_Task_desc, Cl_Task_duedate) values (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setInt(1, Cl_id);
        ps.setInt(2, 0); //For now we leave Task_index = 0
        ps.setInt(3, 0);
        ps.setString(4, TEST_NAME);
        ps.setString(5, TEST_DESC);
        ps.setString(6, null);

        ps.setInt(3, 0);
        for (int i = 0; i < 1; i++) {
            ps.execute();
        }

    }

    private void addTemplateTask(int Tp_id, String Tp_Task_name, String Tp_Task_desc, Connection con) throws SQLException {
        String s1 = "insert into template_task(Tp_id, Tp_Task_name, Tp_Task_desc) values (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setInt(1, Tp_id);
        ps.setString(2, Tp_Task_name);
        ps.setString(3, Tp_Task_desc);

        ps.execute();
    }

    private void addChecklistFromTemplate(int Tp_id, Connection con) throws SQLException {
        String s1 = "insert into checklist(Cl_name, Cl_desc, Cl_closed, Cl_duedate, Tp_id) values (?,?,?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, "Check1");
        ps.setString(2, "Desc");
        ps.setInt(3, 0);
        ps.setString(4, null);
        ps.setInt(5, Tp_id);

        ps.execute();
    }


    @Test
    public void testPostTemplates() throws Exception {
        try {
            con = GetConnection.connect(true);
            con.setAutoCommit(false);
            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);

            int result = (int) new PostTemplates().execute(map, con);
            assertEquals(getLastInsertedTemplate(con), result);

        } finally {
            if (con != null) {
                con.rollback();
                con.close();
            }
        }
    }

    @Test
    public void testPostTemplatesTidCreate() throws Exception {
        int tid, cid;
        String s;
        PreparedStatement ps;
        ResultSet rs;
        try {
            con = GetConnection.connect(true);
            con.setAutoCommit(false);
            addTemplate("TEST_TEMPLATE", "This is a test", con);
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
                con.rollback();

                con.close();
            }
        }
    }

    @Test
    public void testGetTemplatesTid() throws Exception {
        try {
            con = GetConnection.connect(true);
            con.setAutoCommit(false);
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
            addChecklistFromTemplate(id, con);

            DtoWrapper result = (DtoWrapper) new GetTemplatesTid().execute(map, con);
            assertEquals(id, ((Template)((LinkedList) result.getTemplate()).get(0)).getTp_id());
            assertEquals(1, ((LinkedList) result.getChecklist()).size());
            assertEquals("Check1", (((Checklist)((LinkedList) result.getChecklist()).get(0)).getName()));
        } finally {
            if (con != null) {
                con.rollback();
                con.close();
            }
        }
    }

    @Test
    public void testGetTemplates() throws Exception {
        int tid1, tid2;
        try {
            // 1 - Populate templates.
            con = GetConnection.connect(true);
            con.setAutoCommit(false);
            addTemplate("TEST_TEMPLATE_1","This is the first test", con);
            tid1 = getLastInsertedTemplate(con);
            addTemplate("TEST_TEMPLATE_2", "This is the second test", con);
            tid2 = getLastInsertedTemplate(con);

            // 2 - Get template list.
            LinkedList templateLinkedList = (LinkedList) new GetTemplates().execute(null, con);

            // 3 - Find test templates in the obtained list.
            int testTemplatesFound = 0;
            for (Object t : templateLinkedList) {
                if (((Template)t).getTp_id() == tid1 || ((Template)t).getTp_id() == tid2) testTemplatesFound++;
            }
            assertEquals(2, testTemplatesFound);
        } finally {
            if (con != null) {
                con.rollback();
                con.close();
            }
        }
    }

    @Test
    public void PostTemplatesTidTasks() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        int tp_id;
        int tp_task_id;
        try {

            con = GetConnection.connect(true);
            con.setAutoCommit(false);
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
                con.rollback();
                con.close();
            }
        }
    }
}