package pt.isel.ls.Server.Utils;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.Tag;
import pt.isel.ls.Dtos.Template;
import pt.isel.ls.Utils.Output.Dummies.WrapperRootView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class GetRootInfo extends Command {
    private static final DateFormat df = new SimpleDateFormat("dd-mm-yyyy");

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        LinkedList<Checklist> checklists = new LinkedList<>();
        LinkedList<Template> templates = new LinkedList<>();
        LinkedList<Tag> tags = new LinkedList<>();

        String s1 = "select top(4) * from checklist";
        String s2 = "select top(4) * from template";
        String s3 = "select top(4) * from tag";
        String s4 = "select * from template where Tp_id = ?";
        PreparedStatement ps = con.prepareStatement(s1);

        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int id = rs.getInt(1);
            String nome = rs.getString(2);
            String description = rs.getString(3);
            boolean closed = rs.getBoolean(4);
            String dueDate = (rs.getDate(5) != null) ? df.format(rs.getDate(5)) : null;
            int Tp_id = (rs.getString(6) == null) ? -1 : rs.getInt(6);

            String Tp_name = null;
            if(Tp_id != -1){
                PreparedStatement ps1 = con.prepareStatement(s4);
                ps1.setInt(1, Tp_id);

                ResultSet r1 = ps1.executeQuery();
                while (r1.next()){
                    Tp_name = r1.getString(2);
                }
            }

            checklists.add(new Checklist(id, nome, description, closed, dueDate, Tp_id, Tp_name));
        }

        ps = con.prepareStatement(s2);

        rs = ps.executeQuery();

        while(rs.next()){
            templates.add(new Template(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }


        ps = con.prepareStatement(s3);

        rs = ps.executeQuery();

        while(rs.next()){
            tags.add(new Tag(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }

        return new WrapperRootView(checklists, templates, tags);
    }

    @Override
    public String getMethod() {
        return "SPECIAL";
    }

    @Override
    public String[] getPath() {
        return new String[]{""};
    }

}
