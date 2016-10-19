package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostTemplatesTidTasks extends Command {
    private final String method = "POST";
    private final String[] path = {"", "templates", "{tid}", "tasks"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        String query = "insert into template_task values (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, params.get("{tid}"));
        ps.setString(2, params.get("name"));
        ps.setString(3, params.get("description"));
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        return rs.next();
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
