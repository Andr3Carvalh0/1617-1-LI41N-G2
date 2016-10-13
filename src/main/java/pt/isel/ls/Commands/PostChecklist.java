package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostChecklist extends Command{
    String method = "POST";
    String[] path = {"", "checklists"};

   /*public static int postChecklist(String name, String duedate, String description, Connection con) throws SQLException {
        String s1 =
        String s2 = "select max(Cl_id) from checklist";
        PreparedStatement ps = con.prepareStatement(s1);

        ps.setString(1, name);
        ps.setString(2, duedate);"insert into checklist(Cl_name, Cl_duedate, Cl_desc) values (?, CAST(? as datetime), ?)";
        ps.setString(3, description);
        ps.execute();

        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);
    }*/
   @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
        String s1;
        String s2 = "select max(Cl_id) from checklist";


        PreparedStatement ps = null;

        if(!(params.containsKey("dueDate"))){
           s1 = "insert into checklist(Cl_name, Cl_desc) values (?, ?)";
           ps = con.prepareStatement(s1);

           ps.setString(1, params.get("name"));
           ps.setString(2, params.get("description"));
        }

        else {
            s1 = "insert into checklist(Cl_name, Cl_duedate, Cl_desc) values (?, CAST(? as datetime), ?)";
            ps = con.prepareStatement(s1);

            ps.setString(1, params.get("name"));
            ps.setString(2, params.get("description"));
            ps.setString(3, params.get("dueDate"));
        }

        ps.execute();
        ps = con.prepareStatement(s2);
        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1);
    }

    public String getMethod() {
        return method;
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
