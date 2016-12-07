package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostTags extends Command {
    private final String[] path = {"", "tags"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        String s1 = "insert into tag values (?, ?)";
        PreparedStatement ps = con.prepareStatement(s1, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, params.get("name"));
        ps.setString(2, params.get("color"));

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();

        System.out.print( "Tag created with ID: ");
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
