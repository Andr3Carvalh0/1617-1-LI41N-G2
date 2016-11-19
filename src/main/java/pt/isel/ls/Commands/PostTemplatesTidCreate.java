package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PostTemplatesTidCreate extends Command {
    private final String method = "POST";
    private final String[] path = {"", "templates", "{tid}", "create"};
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {

        String templateId = (params.get("{tid}")); // O id da template escolhida será utilizado múltiplas vezes.
        String s;

        // 1 - Check if template exists in database, call appropriate exception if there isn't. Template data can be reused later.
        s = "select * from template where Tp_id = " + templateId;
        PreparedStatement ps = con.prepareStatement(s);
        ResultSet rs = ps.executeQuery();
        if (!rs.isBeforeFirst()) throw new Exception("ERROR: Template " + templateId + " does not exist.");
        rs.next();
        String templateName = rs.getString(2), templateDesc = rs.getString(3);

        //2º - Criar a checklist. Alguns dos seus dados podem ser copiados da template, se não forem inseridos como parâmetros.
        s = "insert into checklist(Cl_name, Cl_desc, Cl_duedate, Tp_id) values(?, ?, CAST(? as datetime), ?)";
        ps = con.prepareStatement(s);
        if(params.containsKey("name")) ps.setString(1, params.get("name"));
            else ps.setString(1, templateName);
        if(params.containsKey("description")) ps.setString(2, params.get("description"));
            else ps.setString(2, templateDesc);
        if(params.containsKey("dueDate")) ps.setString(3, params.get("dueDate"));
            else ps.setString(3, null);
        ps.setString(4, templateId);
        ps.execute();

        //3º - Obter id da checklist criada.
        s = "select max(Cl_id) from checklist";
        ps = con.prepareStatement(s);
        rs = ps.executeQuery();
        rs.next();
        int cid = rs.getInt(1);

        //3º - Obter tasks da template e adicioná-las à checklist.
        s = "select * from template_task where Tp_id = " + templateId;
        ps = con.prepareStatement(s);
        rs = ps.executeQuery();

        int taskIndex = 1; // indice que incrementa com cada task adicionada.
        s = "insert into checklist_task(Cl_id, Cl_Task_index, Cl_Task_name, Cl_Task_desc)values(?, ?, ?, ?)";
        ps = con.prepareStatement(s);
        while(rs.next()){
            ps.setInt(1, cid);
            ps.setInt(2, taskIndex);
            ps.setString(3, rs.getString(3));
            ps.setString(4, rs.getString(4));
            ps.execute();
            taskIndex++;
        }
        return "Checklist created with ID: " + cid;
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
