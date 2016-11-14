package pt.isel.ls.todo;

public class ToDo{
    
    private String _description;    
    public String getDescription() { return _description; }
    public void setDescription(String description) { _description = description;}
    
    private int _id;
    public int getId() { return _id;}
    public void setId(int id) {_id = id;}
    
    public ToDo(String description) {
        _description = description;
    }
    
    @Override
    public String toString() {        
        return String.format("%d:%s",_id, _description);
    }
}