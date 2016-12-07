package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class DeleteChecklistsCidTagsGid extends Command {
    private final String[] path = {"", "checklist", "{cid}", "tags", "{gid}"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "select * from tag_checklist where Cl_id = ? and Tg_id = ?";
        String s2 = "delete from tag_checklist where Tg_id = ? and Cl_id = ?";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, params.get("{cid}"));
        ps.setString(2, params.get("{gid}"));
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("There isnt any checklist with ID: " + params.get("{cid}") + " associated with Tag with ID: " + params.get("{gid}"));
        } else {
            ps = con.prepareStatement(s2);
            ps.setString(1, params.get("{gid}"));
            ps.setString(2, params.get("{cid}"));
            ps.executeUpdate();
            return "Success!";
        }
    }

    @Override
    public String getMethod() {
        return "DELETE";
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
