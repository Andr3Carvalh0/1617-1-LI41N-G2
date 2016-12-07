package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class PostChecklistsCidTags extends pt.isel.ls.Commands.Command {
    private final String[] path = {"", "checklists", "{cid}", "tags"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "select * from checklist where Cl_id = ?";
        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, params.get("{cid}"));
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("There isnt any Checklist with ID:" + params.get("{cid}"));
        } else {
            String s2 = "select * from tag where Tg_id = ?";
            ps = con.prepareStatement(s2);
            ps.setString(1, params.get("gid"));
            rs = ps.executeQuery();

            if (!rs.next()) {
                throw new Exception("There isnt any Tag with ID: " + params.get("gid"));
            } else {
                String s3 = "insert into tag_checklist values(?,?)";
                ps = con.prepareStatement(s3);
                ps.setString(1, params.get("gid"));
                ps.setString(2, params.get("{cid}"));
                ps.executeUpdate();
                return "Success!";
            }
        }
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
