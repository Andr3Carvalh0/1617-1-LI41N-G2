package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class DeleteTagsGid extends Command {
    private final String[] path = {"", "tags", "{gid}"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "select * from tag where Tg_id = ?";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, params.get("{gid}"));
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("There isnt any Tag with ID: " + params.get("{gid}"));
        } else {
            String s2 = "delete from tag_checklist where Tg_id = ?";
            ps = con.prepareStatement(s2);
            ps.setString(1, params.get("{gid}"));
            ps.executeUpdate();

            s2 = "delete from tag where Tg_id = ?";
            ps = con.prepareStatement(s2);
            ps.setString(1, params.get("{gid}"));
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
