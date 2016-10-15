package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Template;
import pt.isel.ls.Dtos.Template_Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;


public class GetTemplatesTid extends Command {
    private final String method = "GET";
    private final String[] path = {"", "templates", "tid"};

    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        Template template = null;
        LinkedList<Checklist> checklists = new LinkedList<>();
        LinkedList<Template_Task> tasks = new LinkedList<>();

        int id = Integer.parseInt(params.get("tid"));

        String s1 = "select * from templates where Tp_id = ?";
        String s2 = "select * from template_task where Tp_id = ?";
        String s3 = "select * from checklist where Tp_id = ?";

        PreparedStatement ps = con.prepareStatement(s1);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String name = rs.getString(2);
            String description = rs.getString(3);
            template = new Template(id, name, description);
        }

        if(template == null){
            throw new Exception("Template with id = " + id + " doesnt exist!");
        }else{
            ps = con.prepareStatement(s2);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            while (rs.next()){
                int tp_id = rs.getInt(1);
                int tp_task_id = rs.getInt(2);
                String tp_task_name = rs.getString(3);
                String tp_task_desc = rs.getString(4);
                tasks.add(new Template_Task(tp_id, tp_task_id, tp_task_name, tp_task_desc));
            }

            ps = con.prepareStatement(s3);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            while (rs.next()){
                int cl_id = rs.getInt(1);
                String nome = rs.getString(2);
                String description = rs.getString(3);
                boolean closed = rs.getBoolean(4);
                String dueDate = df.format(rs.getDate(5));
                int Tp_id = rs.getInt(6);
                checklists.add(new Checklist(cl_id, nome, description, closed, dueDate, Tp_id));
            }

            DtoWrapper result = new DtoWrapper();
            result.setTemplate(template);
            result.setChecklist(checklists);
            result.setTemplate_Task(tasks);

            return result;
        }
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
