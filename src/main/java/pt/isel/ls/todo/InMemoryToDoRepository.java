package pt.isel.ls.todo;

import java.util.HashMap;
import java.util.Map;

public class InMemoryToDoRepository implements ToDoRepository{
    
    private int _nextId = 1;
    private final Map<Integer, ToDo> _map = new HashMap<Integer, ToDo>();
    
    public Iterable<ToDo> getAll(){
        return _map.values();
    }
    
    public ToDo getById(int id) {
        return _map.get(id);
    }
    
    public void add(ToDo t) {
        t.setId(_nextId++);
        _map.put(t.getId(), t);
    }
    
    public boolean remove(int id) {
        return _map.remove(id) != null;
    }    
}
