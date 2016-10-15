package pt.isel.ls.Dtos;

public class Checklist_Task {

    private int Cl_id, Cl_Task_id, Cl_Task_index;
    private boolean Cl_isClosed;
    private String Cl_Task_name, Cl_Task_desc, Cl_Task_dueDate;


    public Checklist_Task(int Cl_id,int Cl_Task_id, int Cl_Task_index, boolean Cl_isClosed, String Cl_Task_name, String Cl_Task_desc, String Cl_Task_dueDate){
        this.Cl_id = Cl_id;
        this.Cl_Task_id = Cl_Task_id;
        this.Cl_Task_index = Cl_Task_index;
        this.Cl_isClosed = Cl_isClosed;
        this.Cl_Task_name = Cl_Task_name;
        this.Cl_Task_desc = Cl_Task_desc;
        this.Cl_Task_dueDate = Cl_Task_dueDate;
    }

    @Override
    public String toString() {
        return "Cl_ID: "+ getCl_id() +"\n"
                + "Cl_Task_ID: " + getCl_Task_id() +"\n"
                + "Cl_Task_index: " + getCl_Task_index() +"\n"
                + "Cl_isClosed: " + isCl_isClosed() +"\n"
                + "Cl_Task_Name: " + getCl_Task_name() +"\n"
                + "Cl_Task_Desc: " + getCl_Task_desc() +"\n"
                + "Cl_Task_dueDate: " + getCl_Task_dueDate() +"\n";
    }

    public int getCl_id() {
        return Cl_id;
    }

    public int getCl_Task_id() {
        return Cl_Task_id;
    }

    public int getCl_Task_index() {
        return Cl_Task_index;
    }

    public boolean isCl_isClosed() {
        return Cl_isClosed;
    }

    public String getCl_Task_name() {
        return Cl_Task_name;
    }

    public String getCl_Task_desc() {
        return Cl_Task_desc;
    }

    public String getCl_Task_dueDate() {
        return Cl_Task_dueDate;
    }
}
