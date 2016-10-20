package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostChecklistsCidTasksLid extends Command {
    String method = "POST";
    String[] path = {"", "checklists", "{cid}", "tasks", "{lid}"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        // 1 - Change a task's isClosed status.
        String s = "update checklist_task set Cl_Task_closed = ? where Cl_id = ? and Cl_Task_id = ?";
        PreparedStatement ps = con.prepareStatement(s);
        String isClosed = "0";
        if(params.get("isClosed").equals("true")) isClosed = "1";
        ps.setString(1, isClosed);
        ps.setString(2, params.get("{cid}"));
        ps.setString(3, params.get("{lid}"));
        ps.execute();

        // 2 - Check if the corresponding checklist is complete, so that it can be closed.
        s = "select * from checklist_task where Cl_task_closed = 0";
        ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        // isBeforeFirst returns false if the cursor isn't placed before the first result, or if the result set is empty.
        // If called on a newly acquired result set, it will return false if the result set is empty.
        if (!rs.isBeforeFirst()) {
            s = "update checklist set Cl_closed = 1 where Cl_id = " + params.get("{cid}");
            ps = con.prepareStatement(s);
            ps.execute();
        }
            return null;
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
