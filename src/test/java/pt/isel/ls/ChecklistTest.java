package pt.isel.ls;

import org.junit.Test;
import pt.isel.ls.Commands.*;
import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.Checklist_Task;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Utils.GetConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;

@SuppressWarnings("unchecked")
public class ChecklistTest {
    private Connection con = null;

    private final String TEST_NAME = "SQLTest";
    private final String TEST_DESC = "DESCRIPTION";
    private final String TEST_DATE = "06/10/2016";
    private final String EXPECTED_DATE = "06-10-2016";

    private int getLastInsertedChecklist(Connection con) throws SQLException {
        String s0 = "select max(Cl_id) from checklist";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);

    }

    private void addChecklist(Connection con, String name, String desc, String date) throws SQLException {
        String s1 = "insert into checklist(Cl_name,  Cl_desc, Cl_duedate) values (?, ?, CAST(? as datetime))";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, name);
        ps.setString(2, desc);
        ps.setString(3, date);

        ps.execute();

    }

    private int getLastInsertedCL_Task(Connection con) throws SQLException {
        String s0 = "select max(Cl_Task_id) from checklist_task";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);
    }

    private void generateChecklist_Tasks(int numberOfClosedTasks, int Cl_id, int numberOfTasks, Connection con) throws SQLException {
        String s1 = "insert into checklist_task(Cl_id ,Cl_Task_index, Cl_Task_Closed, Cl_Task_name, Cl_Task_desc, Cl_Task_duedate) values (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setInt(1, Cl_id);
        ps.setInt(2, 0); //For now we leave Task_index = 0
        ps.setInt(3, (numberOfClosedTasks > 0) ? 1 : 0);
        ps.setString(4, TEST_NAME);
        ps.setString(5, TEST_DESC);
        ps.setString(6, TEST_DATE);

        for (int i = 0; i < numberOfClosedTasks; i++) {
            ps.execute();
        }

        ps.setInt(3, 0);
        for (int i = 0; i < numberOfTasks - numberOfClosedTasks; i++) {
            ps.execute();
        }

    }

    @Test
    public void testGetChecklist() throws Exception {
        try {
            con = GetConnection.connect(true);
            addChecklist(con, TEST_NAME, TEST_DESC, TEST_DATE);

            LinkedList result = (LinkedList) new GetChecklists().execute(null, con);

            assertEquals(true, result.size()>=1);
            assertEquals(true, ((Checklist) result.get(result.size()-1)).getName().equals(TEST_NAME));
            assertEquals(true, ((Checklist) result.get(result.size()-1)).getDescription().equals(TEST_DESC));
            assertEquals(true, ((Checklist) result.get(result.size()-1)).getDueDate().equals(EXPECTED_DATE));

        } finally {
            if (con != null) {
                int id = getLastInsertedChecklist(con);

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
    public void testPostChecklist() throws Exception {
        try {

            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("dueDate", TEST_DATE);

            con = GetConnection.connect(true);
            int result = (int) new PostChecklist().execute(map, con);
            assertEquals(getLastInsertedChecklist(con), result);

        } finally {
            if (con != null) {

                int id = getLastInsertedChecklist(con);

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
    public void testPostChecklistsCidTasksLid() throws Exception {
        try {
            con = GetConnection.connect(true);

            addChecklist(con, TEST_NAME, TEST_DESC, TEST_DATE);
            String cid = getLastInsertedChecklist(con) + "";
            int aux = Integer.parseInt(cid);
            generateChecklist_Tasks(0, aux, 1, con);
            String lid = getLastInsertedCL_Task(con) + "";

            HashMap<String, String> map = new HashMap<>();
            map.put("isClosed", "true");
            map.put("{cid}", cid);
            map.put("{lid}", lid);

            new PostChecklistsCidTasksLid().execute(map, con);
            assertEquals(true, getTaskIsClosedById(cid, lid, con));
        } finally {
            if (con != null) {
                //2º - Apagar checklist criada.
                String s = "delete from checklist where cl_id = " + getLastInsertedChecklist(con);
                PreparedStatement ps = con.prepareStatement(s);
                ps.execute();

                //3º - Fechar connecção.
                con.close();
            }
        }
    }

    private boolean getTaskIsClosedById(String cid, String lid, Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("select Cl_Task_Closed from checklist_task where Cl_id = " + cid + " and Cl_Task_id = " + lid);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) == 1;
    }

    @Test
    public void testPostChecklistCidTasks() throws Exception {
        try {
            HashMap<String, String> map = new HashMap<>();
            // Create test checklist first
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("dueDate", TEST_DATE);
            con = GetConnection.connect(true);
            int cid = (int) new PostChecklist().execute(map, con);

            // Then add checklist task
            map.put("name", "TEST_TASK_NAME");
            map.put("{cid}", Integer.toString(cid));
            map.put("description", "THIS IS A TASK TEST");
            map.put("dueDate", TEST_DATE);

            int result = (int) new PostChecklistCidTasks().execute(map, con);
            int tid = getLastInsertedCL_Task(con);
            assertEquals(tid, result);

        } finally {
            if (con != null) {
                String s1 = "delete from checklist where Cl_id = " + getLastInsertedChecklist(con);
                PreparedStatement ps = con.prepareStatement(s1);
                ps.execute();

                con.close();
            }
        }

    }

    @Test
    public void testGetChecklistsClosed() throws Exception {
        int[] TestChecklistsIds = {-1, -1, -1};
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("dueDate", TEST_DATE);

            con = GetConnection.connect(true);
            PostChecklist pc = new PostChecklist();
            for (int i = 0; i < 3; i++) {
                TestChecklistsIds[i] = (int) pc.execute(map, con);
            }

            PreparedStatement ps = con.prepareStatement("UPDATE checklist\n" +
                    "SET Cl_closed = 1\n" +
                    "WHERE Cl_id = ?;");
            ps.setInt(1, TestChecklistsIds[1]);
            ps.executeUpdate();

            LinkedList<Checklist> list = (LinkedList<Checklist>) new GetChecklistsClosed().execute(map, con);
            for (Checklist c : list) {
                assertEquals(c.isClosed(), true);
            }
        } finally {
            if (con != null) {
                PreparedStatement dels;
                for (int i = 0; i < 3; i++) {
                    dels = con.prepareStatement("DELETE from checklist where Cl_id = ?");
                    dels.setInt(1, TestChecklistsIds[i]);
                    dels.executeUpdate();
                }
                con.close();
            }
        }
    }

    @Test
    public void testGetChecklistsOpenSortedDuedate() throws Exception {
        int[] TestChecklistsIds = {-1, -1, -1, -1};
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            String[] dates = {"2016-10-31", "2016-10-21", "2017-10-31", "2016-11-4"};
            con = GetConnection.connect(true);
            PostChecklist pc = new PostChecklist();
            for (int i = 0; i < 4; i++) {
                map.put("dueDate", dates[i]);
                TestChecklistsIds[i] = (int) pc.execute(map, con);
            }
            LinkedList<Checklist> cl = (LinkedList<Checklist>) new GetChecklistsOpenSortedDueDate().execute(map, con);
            int[] sortedIds = {1, 0, 3, 2};
            for (int i = 0; i < 4; i++) {
                assertEquals(cl.get(i).getId(), TestChecklistsIds[sortedIds[i]]);
                assertEquals(cl.get(i).isClosed(), false);
            }
            assertEquals(cl.size(), 4);
        } finally {
            if (con != null) {
                PreparedStatement dels;
                for (int i = 0; i < 4; i++) {
                    dels = con.prepareStatement("DELETE from checklist where Cl_id = ?");
                    dels.setInt(1, TestChecklistsIds[i]);
                    dels.executeUpdate();
                }
                con.close();
            }
        }
    }

    @Test
    public void testGetChecklistsCidNoTemplate() throws Exception {
        int TestChecklistId = -1;
        int[] TestTasksId = {-1, -1};
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("dueDate", TEST_DATE);
            con = GetConnection.connect(true);
            //Create Checklist
            TestChecklistId = (int) new PostChecklist().execute(map, con);
            map.put("{cid}", Integer.toString(TestChecklistId));
            //Create tasks
            PostChecklistCidTasks pcct = new PostChecklistCidTasks();
            for (int i = 0; i < 2; i++) {
                TestTasksId[i] = (int) pcct.execute(map, con);
            }
            //Retrieve values
            DtoWrapper dw = (DtoWrapper) new GetChecklistsCid().execute(map, con);
            assertEquals(new LinkedList<>(), dw.getTemplate());
            assertEquals(((Checklist)((LinkedList) dw.getChecklist()).get(0)).getId(), TestChecklistId);
            assertEquals(((LinkedList<Checklist_Task>) dw.getChecklist_Task()).get(0).getCl_Task_id(), TestTasksId[0]);
            assertEquals(((LinkedList<Checklist_Task>) dw.getChecklist_Task()).get(1).getCl_Task_id(), TestTasksId[1]);
        } finally {
            if (con != null) {
                PreparedStatement dels;

                dels = con.prepareStatement("DELETE from checklist where Cl_id = ?");
                dels.setInt(1, TestChecklistId);
                dels.executeUpdate();
                con.close();
            }
        }
    }

    @Test
    public void testChecklistsOpenSortedNofTasks() throws Exception {
        try {
            con = GetConnection.connect(true);
            addChecklist(con, "TESTE1", TEST_DESC, TEST_DATE);
            int checklist1 = getLastInsertedChecklist(con);
            addChecklist(con, "TESTE2",  TEST_DESC, TEST_DATE);
            int checklist2 = getLastInsertedChecklist(con);

            generateChecklist_Tasks(2, checklist1, 5, con);
            generateChecklist_Tasks(3, checklist2, 5, con);

            LinkedList<Checklist> checklists = (LinkedList<Checklist>) new GetChecklistsOpenSortedNoftasks().execute(null, con);

            if(checklists != null){
                assertEquals("TESTE2", checklists.get(checklists.size()-1).getName());
                assertEquals("TESTE1", checklists.get(checklists.size()-2).getName());
            }
        }finally {
            if (con != null) {
                String s1 = "DELETE from checklist where Cl_id = ?";

                PreparedStatement ps = con.prepareStatement(s1);

                for (int i = 0; i < 2; i++) {
                    ps.setString(1, getLastInsertedChecklist(con) + "");
                    ps.execute();
                }


                con.close();
            }
        }
    }
}
