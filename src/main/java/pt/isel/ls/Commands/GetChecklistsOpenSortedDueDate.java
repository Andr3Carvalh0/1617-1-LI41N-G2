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

public class GetChecklistsOpenSortedDueDate extends Command {
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String method = "GET";
    private final String[] path = {"", "checklists", "open", "sorted", "duedate"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        LinkedList<Checklist> cl = new LinkedList<Checklist>();
        String query = "select * from checklist where Cl_closed = 0 order by Cl_duedate";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        rs.next();
        while(rs.next()){
            cl.add(new Checklist(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getBoolean(4),df.format(rs.getString(5)), rs.getInt(6)));
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
