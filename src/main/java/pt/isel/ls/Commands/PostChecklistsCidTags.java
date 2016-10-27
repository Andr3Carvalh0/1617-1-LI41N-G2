package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Created by MarcosAndre on 27/10/2016.
 */
public class PostChecklistsCidTags extends pt.isel.ls.Commands.Command {
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
