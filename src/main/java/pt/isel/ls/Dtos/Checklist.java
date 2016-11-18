package pt.isel.ls.Dtos;

public class Checklist implements BaseDTO{
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
                + "Description: "+ getDescription() +"\n"
                + "Closed: "+ isClosed() +"\n"
                + "DueDate: "+ getDueDate() +"\n"
                + "Tp_Id: " + getTp_id()+"\n\n";
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

    @Override
    public String getDTOName() {
        return "Checklist";
    }
}
