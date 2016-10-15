package pt.isel.ls.Commands;

import java.sql.Connection;
import java.util.HashMap;

public abstract class Command {
    public abstract Object execute(HashMap<String, String> params, Connection con) throws Exception;
    public abstract String getMethod();
    public abstract String[] getPath();
}
