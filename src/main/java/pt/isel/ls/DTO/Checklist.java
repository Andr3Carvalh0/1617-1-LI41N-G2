package pt.isel.ls.DTO;

public class Checklist {
    private int id, Tp_id;
    private String name, dueDate, description;
    private boolean closed;

    public Checklist(int id, String name, String description,boolean closed, String dueDate, int Tp_id){
        this.id = id;
        this.name = name;
        this.dueDate = dueDate;
        this.closed = closed;
        this.description = description;
        this.Tp_id = Tp_id;

    }

    @Override
    public String toString() {
        return "ID: "+ getId() +"\n"
                + "Name: "+ getName() +"\n"
                + "DueDate: "+ getDueDate() +"\n"
                + "Closed: "+ isClosed() +"\n"
                + "Description: "+ getDescription() +"\n"
                + "Tp_Id: " + getTp_id();
    }

    public boolean isClosed() {
        return closed;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getTp_id() {
        return Tp_id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDescription() {
        return description;
    }
}
