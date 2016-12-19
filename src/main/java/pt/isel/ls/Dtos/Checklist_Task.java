package pt.isel.ls.Dtos;

public class Checklist_Task{

    private int Cl_id;
    private int Cl_Task_id;
    private int Cl_Task_index;
    private boolean Cl_isClosed;
    private String Cl_Task_name, Cl_Task_desc, Cl_Task_dueDate;

    public Checklist_Task(int Cl_Task_id, int Cl_id, int Cl_Task_index, boolean Cl_isClosed, String Cl_Task_name, String Cl_Task_desc, String Cl_Task_dueDate){
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
        return "Cl_ID: "+ Cl_id +"\n"
                + "Cl_Task_ID: " + Cl_Task_id +"\n"
                + "Cl_Task_index: " + Cl_Task_index +"\n"
                + "Cl_isClosed: " + Cl_isClosed +"\n"
                + "Cl_Task_Name: " + Cl_Task_name +"\n"
                + "Cl_Task_Desc: " + Cl_Task_desc +"\n"
                + "Cl_Task_dueDate: " + Cl_Task_dueDate +"\n";
    }

    public int getCl_Task_id() {
        return Cl_Task_id;
    }

}
