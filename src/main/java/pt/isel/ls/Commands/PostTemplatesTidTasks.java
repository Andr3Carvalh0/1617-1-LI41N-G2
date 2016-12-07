package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class PostTemplatesTidTasks extends Command {
    private final String[] path = {"", "templates", "{tid}", "tasks"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "insert into template_task(Tp_id, Tp_Task_name, Tp_Task_desc) values (?, ?, ?)";
        String s2 = "select max(Tp_Task_id) from template_task";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, params.get("{tid}"));
        ps.setString(2, params.get("name"));
        ps.setString(3, params.get("description"));

        ps.execute();

        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();
        rs.next();

        System.out.print("Template Task created with ID: ");
        return rs.getInt(1);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
