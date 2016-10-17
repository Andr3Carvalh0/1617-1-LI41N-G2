package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Template;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostTemplates extends Command {
    private final String method = "POST";
    private final String[] path = {"", "templates"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        String s1 = "insert into template(Tp_id, Tp_name, Tp_desc) values (?, ?, ?)";
        String s2 = "select max(Tp_id) from template";

        PreparedStatement ps = con.prepareStatement(s2);

        ResultSet rs = ps.executeQuery();
        rs.next();
        int id = rs.getInt(1);

        ps = con.prepareStatement(s1);
        ps.setInt(1, id+1);
        ps.setString(2, params.get("name"));
        ps.setString(3, params.get("description"));

        ps.execute();

        ps = con.prepareStatement(s2);
        rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);
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
