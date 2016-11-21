package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Checklist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class GetTemplatesTidChecklistsSortedByClosedtasksAsc extends Command {
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String method = "GET";
    private final String[] path = {"", "templates", "{tid}", "checklists", "sorted", "by", "closedtasks", "asc"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        LinkedList<Checklist> list = new LinkedList<>();
        String tid = params.get("{tid}");
        String s1 = "select distinct checklist.Cl_id, Cl_name, Cl_desc, Cl_closed, Cl_duedate, Tp_id from checklist inner join(\n" +
                "\tselect Cl_id,Cl_Task_id, Cl_Task_closed from checklist_task) as t1 on Tp_id = ? AND checklist.Cl_id = t1.Cl_id";
        PreparedStatement ps = con.prepareStatement(s1);
        ps.setString(1, tid);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int id = rs.getInt(1);
            String nome = rs.getString(2);
            String description = rs.getString(3);
            boolean closed = rs.getBoolean(4);
            String dueDate = (rs.getDate(5) != null) ? df.format(rs.getDate(5)) : null;
            int Tp_id = rs.getInt(6);
            list.add(new Checklist(id, nome, description, closed, dueDate, Tp_id));
        }

        return list;
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
