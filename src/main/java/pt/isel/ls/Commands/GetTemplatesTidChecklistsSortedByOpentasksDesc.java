package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Checklist;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class GetTemplatesTidChecklistsSortedByOpentasksDesc extends Command {
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String[] path = {"", "templates", "{tid}", "checklists", "sorted", "by", "opentasks", "desc"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        LinkedList<Checklist> checklists = new LinkedList<>();

        String s1 = "select * from template where Tp_id = ?";
        String s2 = "select t2.Cl_id, Count(*) as nr from checklist_task\n" +
                    "inner join (select * from checklist where Tp_id = ?) as t2 on checklist_task.Cl_id = t2.Cl_id and checklist_task.Cl_Task_closed = 0\n" +
                    "group by t2.Cl_id\n" +
                    "order by count(*) DESC\n";

        String s3 = "select * from checklist where Cl_id = ?";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, params.get("{tid}"));
        ResultSet rs = ps.executeQuery();

        if(!rs.next()){
            throw new Exception("Error: there isnt any template with id: " + params.get("{tid}"));
        }

        ps = con.prepareStatement(s2);
        ps.setString(1, params.get("{tid}"));
        rs = ps.executeQuery();

        while (rs.next()){
            int checklist_id = rs.getInt(1);

            PreparedStatement ps1 = con.prepareStatement(s3);
            ps1.setInt(1, checklist_id);
            ResultSet rs1 = ps1.executeQuery();

            while(rs1.next()){
                int id = rs1.getInt(1);
                String nome = rs1.getString(2);
                String description = rs1.getString(3);
                boolean closed = rs1.getBoolean(4);
                String dueDate = (rs1.getDate(5) != null) ? df.format(rs1.getDate(5)) : null;
                int Tp_id = (rs1.getString(6) == null) ? -1 : rs1.getInt(6);
                checklists.add(new Checklist(id, nome, description, closed, dueDate, Tp_id));
            }
        }

        return checklists;
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
