package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PostChecklistCidTasks extends Command {
    private final String[] path = {"", "checklists", "{cid}", "tasks"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String s1 = "insert into checklist_task(Cl_id, Cl_Task_index, Cl_Task_name, Cl_Task_desc, Cl_Task_duedate)" +
                "values (?, ?, ?, ?, CAST(? as datetime))";

        String s2 = "select max(Cl_Task_id) from checklist_task";
        String s3 = "select Cl_duedate from checklist where Cl_id = ?";


        PreparedStatement ps = con.prepareStatement(s3);
        ps.setString(1, params.get("{cid}"));
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("There isnt any checklist with ID:" + params.get("{cid}"));
        } else {

            if(validDate(params.get("dueDate"))){
                String date = rs.getString(1);

                if(date != null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-mm-yyyy");

                    Date checklist_date = sdf.parse(date);
                    Date inserted = sdf1.parse( params.get("dueDate"));

                    if(!inserted.after(checklist_date)){
                        ps = con.prepareStatement(s1);
                        ps.setString(1, params.get("{cid}"));
                        ps.setInt(2, 0);
                        ps.setString(3, params.get("name"));
                        ps.setString(4, params.get("description"));
                        ps.setString(5, formatDate(params.get("dueDate")));

                        ps.execute();

                        ps = con.prepareStatement(s2);

                        rs = ps.executeQuery();

                        rs.next();
                        System.out.print("Checklist Task created with ID: ");
                        return rs.getInt(1);
                    }
                    throw new Exception("Its not possible for the task dueDate be greater that Checklist dueDate");
                }else{
                    ps = con.prepareStatement(s1);
                    ps.setString(1, params.get("{cid}"));
                    ps.setInt(2, 0);
                    ps.setString(3, params.get("name"));
                    ps.setString(4, params.get("description"));
                    ps.setString(5, formatDate(params.get("dueDate")));

                    ps.execute();

                    ps = con.prepareStatement(s2);

                    rs = ps.executeQuery();

                    rs.next();
                    System.out.print("Checklist Task created with ID: ");
                    return rs.getInt(1);
                }
            }
        }
        throw new Exception("You can't use a past date");
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
