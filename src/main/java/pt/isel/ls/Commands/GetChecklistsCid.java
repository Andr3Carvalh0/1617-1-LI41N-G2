package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class GetChecklistsCid extends Command {
    private static final DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    private final String method = "GET";
    private final String[] path = {"", "checklists", "{cid}"};

    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws SQLException {
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
