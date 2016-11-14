package pt.isel.ls.apps.http.todo;

import pt.isel.ls.http.FormUrlEncoded;
import pt.isel.ls.http.HttpResponse;
import pt.isel.ls.http.HttpStatusCode;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import pt.isel.ls.todo.ToDo;
import pt.isel.ls.todo.ToDoRepository;

public class ToDoController {

    private final ToDoRepository _repo;

    public ToDoController(ToDoRepository repo) {
        _repo = repo;
    }
    
    public HttpResponse handleGetAllToDos(HttpServletRequest req) {
        Iterable<ToDo> list = _repo.getAll();
        return new HttpResponse(HttpStatusCode.Ok, new ToDosView(list));
    }    
        
    public HttpResponse handleGetToDoById(HttpServletRequest req, int id) {
        ToDo td = _repo.getById(id);
        if(td == null) return new HttpResponse(HttpStatusCode.NotFound);
        return new HttpResponse(HttpStatusCode.Ok, new ToDoView(td));
    }
    
    public HttpResponse handlePostToDos(HttpServletRequest req) {        
        try {
            Map<String,String> values = FormUrlEncoded.retrieveFrom(req);
            if(!values.containsKey("description"))
                return new HttpResponse(HttpStatusCode.BadRequest);
            ToDo td = new ToDo(values.get("description"));
            _repo.add(td);            
            return new HttpResponse(HttpStatusCode.SeeOther)
                .withHeader("Location", ResolveUrl.of(td));
        } catch (IOException e) {
            return new HttpResponse(HttpStatusCode.InternalServerError);
        }                
    }    
}
