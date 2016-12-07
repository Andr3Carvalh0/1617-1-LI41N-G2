package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

public class GetTagsGidChecklists extends Command{
    private final String[] path = {"", "tags", "{gid}", "checklists"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        return new GetTagsGid().execute(params, con);
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public String[] getPath() {
        return path;
    }
}
