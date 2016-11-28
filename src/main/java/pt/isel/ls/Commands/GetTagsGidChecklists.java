package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

public class GetTagsGidChecklists extends Command{
    private final String method = "GET";
    private final String[] path = {"", "tags", "{gid}", "checklists"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String[] getPath() {
        return new String[0];
    }
}
