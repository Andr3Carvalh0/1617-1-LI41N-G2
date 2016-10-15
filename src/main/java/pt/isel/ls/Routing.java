package pt.isel.ls;

import pt.isel.ls.Commands.Command;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class Routing {
    private String method;
    private String[] path;
    private HashMap<String, String> params;

    public Routing(String[] args) throws Exception {
        if (args.length == 2 || args.length == 3) {
            method = args[0];
            path = args[1].split("/");
            if (args.length > 2) {
                String[] paramsArray = args[2].split("&");
                params = new HashMap<String, String>();
                String[] aux;
                for (int i = 0; i < paramsArray.length; i++) {
                    aux = paramsArray[i].split("=");
                    params.put(aux[0], aux[1]);
                }
            }
        } else {
            throw new Exception("Invalid number of arguments.");
        }

    }

    public String getMethod() {
        return method;
    }

    public String[] getPath() {
        return path;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public Command Route() throws SQLException {

        LinkedList<Command> list = new CommandList().getCommandList();
        for (Command c : list) {
            if (this.getMethod().equals(c.getMethod())) {
                int i = 1;
                for (; i < c.getPath().length; i++) {
                    if (!(match(this.getPath()[i], c.getPath()[i]))) {
                        break;
                    }
                }

                if (this.getPath().length == i) {
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

    public void run(Command c) {
        Connection con = null;
        try {
            con = GetConnection.connect();
            c.execute(params, con);
        } catch (SQLException e) {
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
    }
}
