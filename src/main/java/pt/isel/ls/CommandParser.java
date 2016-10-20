package pt.isel.ls;

import pt.isel.ls.Commands.Command;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class CommandParser {
    private String method;
    private String[] path;
    private HashMap<String, String> params;

    public CommandParser(String[] args) throws Exception {
        if (args.length == 2 || args.length == 3) {
            method = args[0];
            path = args[1].split("/");
            params = new HashMap<String, String>();
            if (args.length > 2) {
                String[] paramsArray = args[2].split("&");
                String[] aux;
                for (int i = 0; i < paramsArray.length; i++) {
                    aux = paramsArray[i].split("=");
                    aux[1] = aux[1].replace('+', ' ');
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
}
