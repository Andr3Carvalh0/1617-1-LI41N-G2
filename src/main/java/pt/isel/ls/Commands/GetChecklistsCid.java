package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class GetChecklistsCid extends Command {
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String method = "GET";
    private final String[] path = {"", "checklists", "{cid}"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        LinkedList<Checklist> cl = new LinkedList<>();
        LinkedList<Checklist_Task> ct = new LinkedList<Checklist_Task>();
        LinkedList<Template> tp = new LinkedList<>();
        LinkedList<Tag> tag = new LinkedList<>();

        //Get Checklist info
        String query = "select * from checklist where Cl_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, params.get("{cid}"));
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("Checklist with id = " + params.get("{cid}") + " doesnt exist!");
        } else {
            String dueDate = (rs.getDate(5) != null) ? df.format(rs.getDate(5)) : null;
            cl.add(new Checklist(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getBoolean(4), dueDate, rs.getInt(6)));
            //Get tasks info
            query = "select * from checklist_task where Cl_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(params.get("{cid}")));
            rs = ps.executeQuery();
            while (rs.next()) {
                int Cl_Task_id = rs.getInt(1);
                int Cl_id = rs.getInt(2);
                int Cl_Task_index = rs.getInt(3);
                boolean closed = rs.getBoolean(4);
                String nome = rs.getString(5);
                String description = rs.getString(6);
                dueDate = (rs.getDate(7) != null) ? df.format(rs.getDate(7)) : null;
                ct.add(new Checklist_Task(Cl_Task_id, Cl_id, Cl_Task_index, closed, nome, description, dueDate));
            }
            //Get template info
            query = "select * from template where Tp_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, cl.get(0).getTp_id());
            rs = ps.executeQuery();
            if (rs.next()) tp.add(new Template(rs.getInt(1), rs.getString(2), rs.getString(3)));

            LinkedList<Integer> tags_id = new LinkedList<>();
            query = "select * from tag_checklist where Cl_id = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(params.get("{cid}")));
            rs = ps.executeQuery();
            while (rs.next()) {
                tags_id.add(rs.getInt(1));
            }

            if(tags_id.size() > 0){
                query = "select * from tag where Tg_id = ?";

                for(Integer val : tags_id) {
                    ps = con.prepareStatement(query);
                    ps.setInt(1, val);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        tag.add(new Tag(rs.getInt(1), rs.getString(2), rs.getString(3)));
                    }
                }
            }

            //Wrap it up
            DtoWrapper dw = new DtoWrapper();
            dw.setChecklist(cl);
            dw.setChecklist_Task(ct);
            dw.setTemplate(tp);
            dw.setTag(tag);

            return dw;
        }
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
