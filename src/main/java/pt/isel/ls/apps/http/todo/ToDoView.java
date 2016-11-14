package pt.isel.ls.apps.http.todo;

import pt.isel.ls.html.HtmlPage;
import pt.isel.ls.todo.ToDo;

public class ToDoView extends HtmlPage {
    
    public ToDoView(ToDo td){
        super("To Do",
            h1(text("To Do")),
            h3(text("Description: "+td.getDescription())),
            a(ResolveUrl.ofToDoCollection(), "Collection")            
        );        
    }
}
