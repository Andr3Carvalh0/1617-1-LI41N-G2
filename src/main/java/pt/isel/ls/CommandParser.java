package pt.isel.ls;

import java.util.HashMap;

public class CommandParser {
    private String method;
    private String[] path;
    private HashMap<String, String> params;
    private String[] headers;

    public CommandParser(String[] args) throws Exception {
            method = args[0];
            path = args[1].split("/");
            params = new HashMap<String, String>();
            if (args.length > 2) {
                String[] paramsArray = args[args.length - 1].split("&");
                String[] aux;
                for (int i = 0; i < paramsArray.length; i++) {
                    aux = paramsArray[i].split("=");
                    aux[1] = aux[1].replace('+', ' ');
                    params.put(aux[0], aux[1]);
                }
                if (args.length == 4 && method.equals("GET"))
                    headers = args[2].split("|");
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
