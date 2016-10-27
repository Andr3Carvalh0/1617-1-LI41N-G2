package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class PostTags extends Command {
    private final String method = "POST";
    private final String[] path = {"", "tags"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "insert into tag(Tg_name, Tp_color) values (?, ?)";

        PreparedStatement ps = con.prepareStatement(s1, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, params.get("name"));
        ps.setString(2, params.get("color"));

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
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
