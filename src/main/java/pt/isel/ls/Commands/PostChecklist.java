package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostChecklist extends Command {
    private final String[] path = {"", "checklists"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "insert into checklist(Cl_name, Cl_desc, Cl_duedate) values (? , ?, CAST(? as datetime))";
        String s2 = "select max(Cl_id) from checklist";

        if(validDate(params.get("dueDate"))){
            PreparedStatement ps = con.prepareStatement(s1);

            ps.setString(1, params.get("name"));
            ps.setString(2, params.get("description"));
            ps.setString(3, formatDate(params.get("dueDate")));

            ps.execute();
            ps = con.prepareStatement(s2);
            ResultSet rs = ps.executeQuery();
            rs.next();

            System.out.print("Checklist created with ID: ");
            return rs.getInt(1);
        }else{
            throw new Exception("You can't use a past date");
        }
    }

    public String getMethod() {
        return "POST";
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
