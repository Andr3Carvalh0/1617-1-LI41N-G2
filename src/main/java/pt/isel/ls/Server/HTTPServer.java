package pt.isel.ls.Server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;
import java.sql.Connection;

public class HTTPServer {

    private Server server;

    public HTTPServer(int port, Connection con){
        this.server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(new ServletHolder(new Service()), "/*");
    }

    public void run() throws Exception {
        server.start();
        server.join();
    }

}
