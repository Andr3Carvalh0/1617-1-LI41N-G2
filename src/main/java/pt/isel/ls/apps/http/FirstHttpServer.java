package pt.isel.ls.apps.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstHttpServer {

    /* 
     * TCP port where to listen. 
     * Standard port for HTTP is 80 but might be already in use
     */
    private static final int LISTEN_PORT = 8080;
        
   public static void main(String[] args) throws Exception {

        System.setProperty("org.slf4j.simpleLogger.levelInBrackets","true");

        Logger logger = LoggerFactory.getLogger(FirstHttpServer.class);
        logger.info("Starting main...");

        String portDef = System.getenv("PORT");
        int port = portDef != null ? Integer.valueOf(portDef) : LISTEN_PORT;
    	Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new TimeServlet()), "/*");
        server.start();
        server.join();

       logger.info("main ends.");
    }    
}
