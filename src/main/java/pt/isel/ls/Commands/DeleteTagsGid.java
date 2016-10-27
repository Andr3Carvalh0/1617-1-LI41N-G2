package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

public class DeleteTagsGid extends Command {
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
