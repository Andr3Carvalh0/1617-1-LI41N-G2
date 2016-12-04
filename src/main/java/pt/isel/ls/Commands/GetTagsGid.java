package pt.isel.ls.Commands;


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.util.HashMap;

public class GetTagsGid extends Command {
    private final String method = "GET";
    private final String[] path = {"", "tags", "{gid}"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        throw new NotImplementedException();
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
