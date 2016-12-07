package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostTemplates extends Command {
    private final String[] path = {"", "templates"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        String s1 = "insert into template(Tp_name, Tp_desc) values (?, ?)";

        PreparedStatement ps = con.prepareStatement(s1, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, params.get("name"));
        ps.setString(2, params.get("description"));

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();

        System.out.print("Template created with ID: ");
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
