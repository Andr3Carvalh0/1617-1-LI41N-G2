package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

public class PostChecklistsCidTags extends pt.isel.ls.Commands.Command {
    private final String method = "POST";
    private final String[] path = {"", "checklists", "{cid}", "tags"};
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "insert into tag_checklist values(?,?)";
        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, params.get("gid"));
        ps.setString(2, params.get("cid"));
        ps.executeUpdate();
        return "Success!";
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
