package pt.isel.ls;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostChecklist {
    public static int postChecklist(String name, String duedate, String description, Connection con) throws SQLException {
        String s1 = "insert into checklist(Cl_name, Cl_duedate, Cl_desc) values (?, CAST(? as datetime), ?)";
        String s2 = "select max(Cl_id) from checklist";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, name);
        ps.setString(2, duedate);
        ps.setString(3, description);
        ps.execute();

        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();
        rs.next();

        System.out.println(rs.getInt(1));

        return rs.getInt(1);
    }

    public static int postChecklist(String name, String description, Connection con) throws SQLException {
        String s1 = "insert into checklist(Cl_name, Cl_desc) values (?, ?)";
        String s2 = "select max(Cl_id) from checklist";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, name);
        ps.setString(2, description);
        ps.execute();

        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);
    }
}
