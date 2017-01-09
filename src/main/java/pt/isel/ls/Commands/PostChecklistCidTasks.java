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
        String s4 = "update checklist set Cl_closed = 0 where Cl_id = ?";


        PreparedStatement ps = con.prepareStatement(s3);
        ps.setString(1, params.get("{cid}"));
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new Exception("There isnt any checklist with ID:" + params.get("{cid}"));
        } else {

            String taskDueDateString = params.get("dueDate");
            if(validDate(taskDueDateString)){
                String checklistDueDateString = rs.getString(1);

                if(checklistDueDateString != null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");

                    Date checklistDueDate = sdf.parse(checklistDueDateString);
                    Date taskDueDate;
                    if(taskDueDateString != null)
                        taskDueDate = sdf1.parse(taskDueDateString);
                    else
                        taskDueDate = checklistDueDate;

                    if(!taskDueDate.after(checklistDueDate)){
                        ps = con.prepareStatement(s1);
                        ps.setString(1, params.get("{cid}"));
                        ps.setInt(2, 0);
                        ps.setString(3, params.get("name"));
                        ps.setString(4, params.get("description"));
                        if(taskDueDateString != null)
                            ps.setString(5, formatDate(taskDueDateString));
                        else
                            ps.setString(5, checklistDueDateString);

                        ps.execute();

                        ps = con.prepareStatement(s2);

                        rs = ps.executeQuery();

                        rs.next();
                        System.out.print("Checklist Task created with ID: ");

                        // / Reopen the checklist when a new task is added
                        ps = con.prepareStatement(s4);
                        ps.setString(1, params.get("{cid}"));
                        ps.execute();

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

                    // / Reopen the checklist when a new task is added
                    ps = con.prepareStatement(s4);
                    ps.setString(1, params.get("{cid}"));
                    ps.execute();

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