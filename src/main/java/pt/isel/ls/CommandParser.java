package pt.isel.ls;

import java.util.HashMap;

public class CommandParser {
    private String method;
    private String[] path;
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    private String[] headersArray;

    public CommandParser(String[] args) throws Exception {
            method = args[0];
            path = args[1].split("/");
            params = new HashMap<String, String>();
            if (args.length > 2) {
                String[] paramsArray = args[args.length - 1].split("&");
                String[] aux;
                for (int i = 0; i < paramsArray.length; i++) {
                    aux = paramsArray[i].split("=");
                    if(aux[1].contains("+")){
                        aux[1] = aux[1].replace('+', ' ');
                    }
                    params.put(aux[0], aux[1]);
                }
                if (args.length == 4 && method.equals("GET"))
                    headersArray = args[2].split("|");
                for (int i = 0; i < headersArray.length; i++) {
                    headers.put(headersArray[i].split(":")[0],headersArray[i].split(":")[1]);
                }
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

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}
