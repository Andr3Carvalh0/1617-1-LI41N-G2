package pt.isel.ls.Server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HTTPServer {

    private Server server;

    public HTTPServer(int port){
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
