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
        if(args.length == 2 || args.length == 3) {
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
        }
        else {
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

    public void Route() throws SQLException {

        LinkedList<Command> list = new CommandList().getCommandList();
        for (Command c : list) {
            if(this.getMethod().equals(c.getMethod())) {
                int i = 1;
                for (; i < c.getPath().length; i++) {
                    if (!(this.getPath()[i].equals(c.getPath()[i]))) {
                        break;
                    }
                }
                if(this.getPath().length==i){
                    Connection con = GetConnection.connect();
                    System.out.println(c.execute(params, con));
                }
            }
        }
    }
}
