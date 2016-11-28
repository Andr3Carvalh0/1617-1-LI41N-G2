package pt.isel.ls.Server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.sql.Connection;

public class HTTPServer {

    private Server server;

    public HTTPServer(int port, Connection con){
        this.server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        //DONT DO IT THIS WAY.THIS WAS JUST A TEST
        handler.addServletWithMapping(new ServletHolder(new Service(con)), "/*");
    }

    public void run() throws Exception {
        server.start();
        server.join();
    }

}
