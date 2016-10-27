package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class DeleteTagsGid extends Command {
    private final String method = "DELETE";
    private final String[] path = {"", "tags", "{gid}"};
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "delete from tag where Tg_id = ?";
        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, params.get("gid"));
        ps.executeUpdate();
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
