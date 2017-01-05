package pt.isel.ls.Commands;

import pt.isel.ls.Server.HTTPServer;

import java.sql.Connection;
import java.util.HashMap;

public class Listen extends Command {
    private final String[] path = {"LISTEN", "/", "port"};
    @Override
    public Object execute(HashMap<String, String> params, Connection con) throws Exception {
        int port = params.get("port") == null ? Integer.parseInt(System.getenv("PORT")) : Integer.parseInt(params.get("port"));

        HTTPServer server = new HTTPServer(port);
        server.run();

        return "Server initialize on port " + params.get("port");
    }

    @Override
    public String getMethod() {
        return "LISTEN";
    }

    @Override
    public String[] getPath() { return path; }
}
