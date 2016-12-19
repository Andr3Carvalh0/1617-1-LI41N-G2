package pt.isel.ls;

import java.util.HashMap;

public class CommandParser {
    private String method;
    private String[] path;
    private HashMap<String, String> params;
    private HashMap<String, String> headers = new HashMap<>();

    public CommandParser(String[] args) throws Exception {
        method = args[0];
        path = args[1].split("/");
        params = new HashMap<>();
        if (args.length > 2) {
            if (args[2].contains("=") && args[2].contains(":"))
                throw new Exception("You can't have params with ':' or headers with '='");

            if (args[2].contains("="))
                setParams(args, 2);
            else if (args[2].contains(":"))
                setHeaders(args);

            //Listen Command
            if (args[1].equals("/")) {
                params.put("port", args[2]);
            }

            if (args.length == 4)
                if (args[3].contains("+"))
                    setParams(args, 3);
        }

    }

    private void setHeaders(String[] args) {
        String[] headersArray = args[2].split("\\|");
        headers = new HashMap<>();

        for (String aHeadersArray : headersArray) {
            headers.put(aHeadersArray.split(":")[0], aHeadersArray.split(":")[1]);
        }
    }

    private void setParams(String[] args, int n) {
        String[] paramsArray = args[n].split("&");
        String[] aux;
        for (String aParamsArray : paramsArray) {
            aux = aParamsArray.split("=");
            aux[1] = aux[1].replace('+', ' ');
            params.put(aux[0], aux[1]);
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

    HashMap<String, String> getHeaders() {
        return headers;
    }
}
