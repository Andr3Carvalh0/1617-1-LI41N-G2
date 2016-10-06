package pt.isel.ls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class GetChecklist {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    public static LinkedList getChecklists(Connection con) throws SQLException {
        LinkedList<Checklist> list = new LinkedList<>();

        String s1 = "select * from checklist";
        PreparedStatement ps = con.prepareStatement(s1);

        ResultSet rs = ps.executeQuery();
        rs.next();

        while (rs.next()){
            int id = rs.getInt(1);
            String nome = rs.getString(2);
            String dueDate = df.format(rs.getDate(3));
            boolean closed = rs.getBoolean(4);
            String description = rs.getString(5);
            int Tp_id = rs.getInt(6);
            list.add(new Checklist(id, nome, dueDate, closed, description, Tp_id));
        }

        return list;
    }

}
