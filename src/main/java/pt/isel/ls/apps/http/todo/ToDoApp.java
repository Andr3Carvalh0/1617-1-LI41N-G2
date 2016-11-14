package pt.isel.ls.apps.http.todo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import pt.isel.ls.todo.InMemoryToDoRepository;
import pt.isel.ls.todo.ToDo;
import pt.isel.ls.todo.ToDoRepository;


public class ToDoApp {

    /*
     * TCP port where to listen. Standard port for HTTP is 80 but might be
     * already in use
     */
    private static final int LISTEN_PORT = 8000;

    public static void trace(String msg) {
        System.out.println(msg);
    }
    
    private static final ToDoRepository _repo = new InMemoryToDoRepository();
    public static ToDoRepository getRepository(){
    	return _repo;
    }

    public static void main(String[] args) throws Exception {
    	
    	_repo.add(new ToDo("must stop using Java"));
        _repo.add(new ToDo("must learn more about HTTP"));
    	
    	Server server = new Server(LISTEN_PORT);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(ToDoServlet.class, "/todos/*");
        server.start();
        System.out.println("Server is started");
        
        System.in.read();
        server.stop();
        System.out.println("Server is stopped, bye");        
    }
}
