package pt.isel.ls;

import pt.isel.ls.Commands.Command;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class Router {
    private String method;
    private String[] path;
    private HashMap<String, String> params;

    public Router(String method, String[] path, HashMap<String, String>params){
        this.method = method;
        this.path = path;
        this.params = params;
    }

    public Command Route() throws SQLException {

        LinkedList<Command> list = new CommandList().getCommandList();
        for (Command c : list) {
            if (method.equals(c.getMethod())) {
                int i = 1;
                for (; i < c.getPath().length; i++) {
                    if (!(match(path[i], c.getPath()[i]))) {
                        break;
                    }
                }
                if (path.length == i) {
                    return c;
                }
            }
        }
        return null;
    }

    private boolean match(String s, String s1) {
        if (s.equals(s1)) return true;
        else if (s1.contains("id")) {
            params.put(s1, s);
            return true;
        }
        return false;
    }

    public Object run(Command c) throws Exception {
        Connection con = EnvVars.connect(false);
        con.setAutoCommit(false);
        Object obj = null;
        try {
            obj = c.execute(params, con);
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            e.printStackTrace();
        } finally {
            try {
                if(con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
