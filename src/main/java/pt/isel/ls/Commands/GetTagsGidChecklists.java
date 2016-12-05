package pt.isel.ls.Commands;

import pt.isel.ls.Dtos.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

public class GetTagsGidChecklists extends Command{
    private final String method = "GET";
    private final String[] path = {"", "tags", "{gid}", "checklists"};

    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        return new GetTagsGid().execute(params, con);
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
