package pt.isel.ls;


import org.junit.Test;
import pt.isel.ls.Commands.GetChecklists;
import pt.isel.ls.Commands.PostChecklist;
import pt.isel.ls.Commands.PostChecklistCidTasks;
import pt.isel.ls.Commands.PostChecklistsCidTasksLid;
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

    @Test
    public void testPostChecklistsCidTasksLid() throws SQLException{
        String cid = "-1", lid = "-1";
        try{
            con = GetConnection.connect();
            cid = InsertChecklist(con);
            lid = InsertTask(cid, con);

            HashMap<String, String> map = new HashMap<>();
            map.put("isClosed", "true");
            map.put("{cid}", cid);
            map.put("{lid}", lid);

            new PostChecklistsCidTasksLid().execute(map, con);
            assertEquals(true, getTaskIsClosedById(cid, lid, con));
        }finally{
            if(con!=null) {
                //1º - Apagar task criada.
                String s = "delete from checklist_task where cl_task_id = " + lid;
                PreparedStatement ps = con.prepareStatement(s);
                ps.execute();

                //2º - Apagar checklist criada.
                s = "delete from checklist where cl_id = " + cid;
                ps = con.prepareStatement(s);
                ps.execute();

                //3º - Fechar connecção.
                con.close();
            }
        }
    }

    private String InsertTask(String cid, Connection con) throws SQLException {
        //1º - Inserir task.
        String s = "insert into checklist_task(cl_id, cl_task_name, cl_task_desc) values(" + cid + ", ?, ?)";
        PreparedStatement ps = con.prepareStatement(s);
        ps.setString(1, TEST_NAME);
        ps.setString(2, TEST_DESC);
        ps.execute();

        //2º - Obter id da task criada.
        s = "select max(cl_task_id) from checklist_task";
        ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return Integer.toString(rs.getInt(1));
    }

    private String InsertChecklist(Connection con) throws SQLException {
        //1º - Inserir checklist.
        String s = "insert into checklist(cl_name, cl_desc) values(?, ?)";
        PreparedStatement ps = con.prepareStatement(s);
        ps.setString(1, TEST_NAME);
        ps.setString(2, TEST_DESC);
        ps.execute();

        //2º - Obter id da checklist criada.
        s = "select max(cl_id) from checklist";
        ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return Integer.toString(rs.getInt(1));
    }

    private boolean getTaskIsClosedById(String cid, String lid, Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement("select Cl_Task_Closed from checklist_task where Cl_id = " + cid + " and Cl_Task_id = " + lid);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getInt(1) == 1;
    }

    @Test
    public void testPostChecklistCidTasks() throws SQLException {
        int cid = -1, tid = -1;
        try {
            HashMap<String, String> map = new HashMap<>();
            // Create test checklist first
            map.put("name", TEST_NAME);
            map.put("description", TEST_DESC);
            map.put("dueDate", TEST_DATE);

            con = GetConnection.connect();
            cid = (int) new PostChecklist().execute(map, con);

            // Then add checklist task
            map.put("name", "TEST_TASK_NAME");
            map.put("{cid}", Integer.toString(cid));
            map.put("description", "THIS IS A TASK TEST");
            map.put("dueDate", TEST_DATE);

            int result = (int) new PostChecklistCidTasks().execute(map, con);
            tid = getChecklistTaskMaxID(con);
            assertEquals(tid, result);

        }finally{
            if(con != null){

                // First delete created task
                String s1 = "delete from checklist_task where Cl_Task_id = " + tid;
                PreparedStatement ps = con.prepareStatement(s1);
                ps.execute();

                // Then delete created checklist
                s1 = "delete from checklist_task where Cl_Task_id = " + cid;
                ps = con.prepareStatement(s1);
                ps.execute();

                con.close();
            }
        }

    }

    private int getChecklistTaskMaxID(Connection con) throws SQLException {
        String s0 = "select max(Cl_Task_id) from checklist_task";
        PreparedStatement ps = con.prepareStatement(s0);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);
    }

}
