package pt.isel.ls.Commands;


import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class GetTagsGid extends Command {
    private final String[] path = {"", "tags", "{gid}"};

    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        int id = Integer.parseInt(params.get("{gid}"));
        LinkedList<Tag> tag = new LinkedList<>();
        LinkedList<Checklist> checklists = new LinkedList<>();


        String s1 = "select * from tag where Tg_id = ?";
        String s2 = "select * from tag_checklist where Tg_id = ?";
        String s3 = "select * from checklist where Cl_id = ?";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String name = rs.getString(2);
            String description = rs.getString(3);
            tag.add(new Tag(id, name, description));
        }

        if (tag.get(0) == null) {
            throw new Exception("Tag with id = " + id + " doesnt exist!");
        } else {
            ps = con.prepareStatement(s2);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                int check_id = rs.getInt(2);

                PreparedStatement ps1 = con.prepareStatement(s3);
                ps1.setInt(1, check_id);

                ResultSet rs1 = ps1.executeQuery();

                while (rs1.next()) {

                    int ck_id = rs1.getInt(1);
                    String nome = rs1.getString(2);
                    String description = rs1.getString(3);
                    boolean closed = rs1.getBoolean(4);
                    String dueDate = (rs1.getDate(5) != null) ? df.format(rs1.getDate(5)) : null;
                    int Tp_id = (rs1.getString(6) == null) ? -1 : rs1.getInt(6);
                    checklists.add(new Checklist(ck_id, nome, description, closed, dueDate, Tp_id));

                }
            }

            DtoWrapper result = new DtoWrapper();

            result.setTag(tag);
            result.setChecklist(checklists);

            return result;
        }
    }
    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
