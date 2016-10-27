package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

public class Options extends Command {
    private final String method = "OPTIONS";
    private final String[] path = {"", "/"};
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        String commandList =
                "------------- \n"+
                "POST /checklists name={name}&description={description}&dueDate={dueDate} \n" +
                "GET /checklists \n" +
                "POST /checklists/{cid}/tasks name={name}&description={description}&dueDate={dueDate} \n" +
                "POST /checklists/{cid}/tasks/{lid} isClosed={true or false} \n" +
                "GET /checklists/{cid} \n" +
                "POST /templates name={name}&description={description} \n" +
                "POST /templates/{tid}/tasks name={name}&description={description} \n" +
                "POST /templates/{tid}/create name={name}&description={description}&dueDate={dueDate} \n" +
                "GET /templates/{tid} \n" +
                "GET /checklists/closed \n" +
                "GET /checklists/open/sorted/duedate \n" +
                "GET /checklists/open/sorted/noftasks  \n"+
                "POST /tags  name={name}&color={color} \n"+
                "GET /tags \n" +
                "DELETE /tags/{gid} \n" +
                "POST /checklists/{cid}/tags  gid={gid} \n" +
                "DELETE /checklists/{cid}/tags/{gid} \n" +
                "OPTIONS / \n" +
                "EXIT /"+
                "------------- \n";
        System.out.println(commandList);
        return null;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String[] getPath() { return path; }
}
