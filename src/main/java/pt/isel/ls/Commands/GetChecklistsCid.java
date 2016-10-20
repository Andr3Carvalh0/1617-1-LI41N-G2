package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.Checklist_Task;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Template;

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
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        Checklist cl;
        LinkedList<Checklist_Task> ct = new LinkedList<Checklist_Task>();
        Template tp;
        //Get Checklist info
        String query = "select * from checklist where Cl_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, params.get("{cid}"));
        ResultSet rs = ps.executeQuery();
        rs.next();

        String dueDate = (rs.getDate(5) != null) ? df.format(rs.getDate(5)) : null;
        cl = new Checklist(rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getBoolean(4),dueDate, rs.getInt(6));

        //Get tasks info
        query = "select * from checklist_task where Cl_id = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(params.get("{cid}")));
        rs = ps.executeQuery();
        rs.next();
        while (rs.next()){
            int Cl_Task_id = rs.getInt(1);
            int Cl_id = rs.getInt(2);
            int Cl_Task_index = rs.getInt(3);
            boolean closed = rs.getBoolean(4);
            String nome = rs.getString(5);
            String description = rs.getString(6);
            dueDate = (rs.getDate(7) != null) ? df.format(rs.getDate(7)) : null;
            ct.add(new Checklist_Task(Cl_Task_id,Cl_id,Cl_Task_index,closed,nome,description, dueDate));
        }
        //Get template info
        query = "select * from template where Tp_id = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, cl.getTp_id());
        rs = ps.executeQuery();
        rs.next();
        tp = new Template(rs.getInt(1), rs.getString(2), rs.getString(3));
        //Wrap it up
        DtoWrapper dw = new DtoWrapper();
        dw.setChecklist(cl);
        dw.setCheclist_Task(ct);
        dw.setTemplate(tp);

        return dw;
    }

    @Override
    public String getMethod() { return method; }

    @Override
    public String[] getPath() {
        return path;
    }
}
