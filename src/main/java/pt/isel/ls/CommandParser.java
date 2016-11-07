package pt.isel.ls;

import java.util.HashMap;

public class CommandParser {
    private String method;
    private String[] path;
    private HashMap<String, String> params;
    private HashMap<String, String> headers = new HashMap<>();
    private String[] headersArray;

    public CommandParser(String[] args) throws Exception {
        method = args[0];
        path = args[1].split("/");
        params = new HashMap<String, String>();
        if (args.length > 2) {
            if(args[2].contains("=") && args[2].contains(":")) throw new Exception("Não sao permitidos parâmetros com ':' ou headers com '='");

            if(args[2].contains("="))
                setParams(args,2);
            else if(args[2].contains(":"))
                setHeaders(args);

            if (args.length == 4)
                if(args[3].contains("+"))
                    setParams(args,3);
        }

    }

    private void setHeaders(String[] args) {
        headersArray = args[2].split("\\|");
        headers = new HashMap<String, String>();

        for (int i = 0; i < headersArray.length; i++) {
            headers.put(headersArray[i].split(":")[0], headersArray[i].split(":")[1]);
        }
    }

    private void setParams(String[] args, int n) {
        String[] paramsArray = args[n].split("&");
        String[] aux;
        for (int i = 0; i < paramsArray.length; i++) {
            aux = paramsArray[i].split("=");
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

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}
