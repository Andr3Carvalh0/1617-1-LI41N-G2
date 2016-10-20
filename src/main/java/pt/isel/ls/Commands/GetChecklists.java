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

public class GetChecklists extends Command{
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String method = "GET";
    private final String[] path = {"", "checklists"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        LinkedList<Checklist> list = new LinkedList<>();

        String s1 = "select * from checklist";
        PreparedStatement ps = con.prepareStatement(s1);

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
