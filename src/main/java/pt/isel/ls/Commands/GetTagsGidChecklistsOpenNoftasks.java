package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Checklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by MarcosAndre on 24/11/2016.
 */
public class GetTagsGidChecklistsOpenNoftasks extends Command{
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String method = "GET";
    private final String[] path = {"", "tags", "{gid}", "checklists", "open", "{noftasks}"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        LinkedList<Checklist> cl = new LinkedList<Checklist>();
        String query = "select one.Cl_id, Cl_name, Cl_desc, Cl_duedate, Tp_id, NoOfnftasks \n" +
                "from\n" +
                "(Select checklist.Cl_id, Cl_name, Cl_desc, Cl_duedate, Tp_id \n" +
                "from tag_checklist join checklist on tag_checklist.Cl_id = checklist.Cl_id\n" +
                "where Tg_id = ?) as one\n" +
                "join\n" +
                "(select checklist.Cl_id, count(Cl_Task_closed) as NoOfnftasks\n" +
                "from checklist join checklist_task on checklist.Cl_id = checklist_task.Cl_id\n" +
                "where Cl_Task_closed = 0\n" +
                "group by checklist.Cl_id) as two\n" +
                "on one.Cl_id = two.Cl_id\n" +
                "where NoOfnftasks >= ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, params.get("{gid}"));
        ps.setString(2, params.get("{noftasks}"));
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int id = rs.getInt(1);
            String nome = rs.getString(2);
            String description = rs.getString(3);
            //String dueDate = (rs.getDate(4) != null) ? df.format(rs.getDate(5)) : null;
            int Tp_id = rs.getInt(5);
           // int noftasks = rs.getInt(6);
            cl.add(new Checklist(id, nome, description, false, null, Tp_id));
        }
        return cl;
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
