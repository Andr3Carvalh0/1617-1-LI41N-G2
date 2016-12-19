package pt.isel.ls.Dtos;

public class Template{

    private int Tp_id;
    private String Tp_name, Tp_desc;

    public Template(int id, String name, String description){
        this.Tp_id = id;
        this.Tp_name = name;
        this.Tp_desc = description;

    }

    @Override
    public String toString() {
        return "ID: "+ getTp_id() +"\n"
                + "Name: "+ getTp_name() +"\n"
                + "Description: "+ getTp_desc() +"\n";
    }

    public int getTp_id() {
        return Tp_id;
    }

    public String getTp_name() {
        return Tp_name;
    }

    private String getTp_desc() {
        return Tp_desc;
    }
}
