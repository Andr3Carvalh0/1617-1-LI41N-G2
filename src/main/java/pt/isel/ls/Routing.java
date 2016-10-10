package pt.isel.ls;

import java.util.HashMap;

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
                    params.put(paramsArray[0], paramsArray[1]);
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
}
