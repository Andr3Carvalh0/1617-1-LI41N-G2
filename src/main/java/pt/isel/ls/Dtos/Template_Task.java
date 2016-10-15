package pt.isel.ls.Dtos;

public class Template_Task {
    private int Tp_id, Tp_Task_id;
    private String Tp_Task_name, Tp_Task_desc;


    public Template_Task(int Tp_id,int Tp_Task_id, String Tp_Task_name, String Tp_Task_description){
        this.Tp_id = Tp_id;
        this.Tp_Task_id = Tp_Task_id;
        this.Tp_Task_name = Tp_Task_name;
        this.Tp_Task_desc = Tp_Task_description;
    }

    @Override
    public String toString() {
        return "TP_ID: "+ getTp_id() +"\n"
                + "TP_Task_ID: " + getTp_Task_id() +"\n"
                + "TP_Task_Name: "+ getTp_Task_name() +"\n"
                + "TP_Task_Description: "+ getTp_Task_desc() +"\n";
    }

    public int getTp_id() {
        return Tp_id;
    }

    public int getTp_Task_id() {
        return Tp_Task_id;
    }

    public String getTp_Task_name() {
        return Tp_Task_name;
    }

    public String getTp_Task_desc() {
        return Tp_Task_desc;
    }
}
