package pt.isel.ls.Commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class GetChecklistsCid extends Command {
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
