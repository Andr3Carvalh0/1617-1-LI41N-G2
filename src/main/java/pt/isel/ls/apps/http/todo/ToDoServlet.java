package pt.isel.ls.apps.http.todo;

import pt.isel.ls.http.HttpResponse;
import pt.isel.ls.http.HttpStatusCode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.isel.ls.todo.ToDoRepository;

@SuppressWarnings("serial")
public class ToDoServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	doMethod(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	doMethod(req, resp);
    }
    
    private void doMethod(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	try {
        	System.out.println(
        			String.format("Received %s request for %s", req.getMethod(), req.getRequestURI()));
            uglyHandle(req, resp).send(resp);
        }catch(Throwable th) {
            // No exception should go unnoticed!
            new HttpResponse(HttpStatusCode.InternalServerError).send(resp);
            ToDoApp.trace(th.getMessage());
        }
    }       
   	    
    private HttpResponse uglyHandle(HttpServletRequest req, HttpServletResponse resp) throws IOException, URISyntaxException {
    	ToDoRepository repo = ToDoApp.getRepository();
        URI reqUri = new URI(req.getRequestURI());        
        String[] segs = reqUri.getPath().split("/");
        if(segs.length < 2 || segs.length>3 || !segs[1].equals("todos")) {
            return new HttpResponse(HttpStatusCode.NotFound);            
        }
        String method = req.getMethod();
        if(!method.equals("GET") && !method.equals("POST")) {
            return new HttpResponse(HttpStatusCode.MethodNotAllowed);            
        }        
        if(segs.length == 2) {
            return method.equals("GET") ? 
                    new ToDoController(repo).handleGetAllToDos(req)
                    : new ToDoController(repo).handlePostToDos(req);                        
        }
        try(Scanner sc = new Scanner(segs[2])){
	        if(!sc.hasNextInt()) {
	            return new HttpResponse(HttpStatusCode.NotFound);
	        }
	        int id = sc.nextInt();
	        return new ToDoController(repo).handleGetToDoById(req, id);
        }
    }
}

