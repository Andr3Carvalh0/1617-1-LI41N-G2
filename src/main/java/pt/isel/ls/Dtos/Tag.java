package pt.isel.ls.Dtos;


public class Tag implements BaseDTO{
    private int Tg_id;
    private String Tg_name, Tg_color;

    public Tag(int id, String name, String color){
        this.Tg_id = id;
        this.Tg_name = name;
        this.Tg_color = color;

    }

    @Override
    public String toString() {
        return "ID: "+ getTg_id() +"\n"
                + "Name: "+ getTg_name() +"\n"
                + "Description: "+ getTg_color() +"\n";
    }

    public int getTg_id() { return Tg_id; }

    public String getTg_color() { return Tg_color; }

    public String getTg_name() { return Tg_name; }

    @Override
    public String[] getProperties() {
        String ret[] = {"Tg_id", "Tg_name", "Tg_color"};
        return ret;
    }

    @Override
    public String[] getPropertiesValues() {
        String ret[] = {getTg_id() + "" , getTg_color(), getTg_name()};
        return ret;
    }

    @Override
    public String getDTOName() {
        return "Tag";
    }
}
