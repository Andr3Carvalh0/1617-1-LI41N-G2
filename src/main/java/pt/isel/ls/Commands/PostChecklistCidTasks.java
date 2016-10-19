package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostChecklistCidTasks extends Command {
    private final String method = "POST";
    private final String[] path = {"", "checklists", "{cid}", "tasks"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        String s1;
        String s2 = "select max(Cl_Task_Index) from checklist_task";

        int taskIndex;

        PreparedStatement ps = null;

        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();

        rs.next();
        taskIndex = rs.getInt(1) + 1;

        if(params.containsKey("dueDate")){
            s1 = "insert into checklist_task(Cl_id, Cl_Task_index, Cl_Task_name, Cl_Task_desc, Cl_Task_duedate)" +
                    "values (?, ?, ?, ?, CAST(? as datetime))";

            ps = con.prepareStatement(s1);

            ps.setString(1, params.get("{cid}"));
            ps.setString(2, Integer.toString(taskIndex));  //porque não usar ps.setInt(2, taskIndex) ?
            ps.setString(3, params.get("name"));
            ps.setString(4, params.get("description"));
            ps.setString(5, params.get("dueDate"));
        }

        else {
            s1 =  "insert into checklist_task(Cl_id, Cl_Task_index, Cl_Task_name, Cl_Task_desc)" +
                    "values (?, ?, ?, ?)";
            ps = con.prepareStatement(s1);

            ps.setString(1, params.get("{cid}"));
            ps.setString(2, Integer.toString(taskIndex));
            ps.setString(3, params.get("name"));
            ps.setString(4, params.get("description"));
        }

        ps.execute();

        return taskIndex;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
