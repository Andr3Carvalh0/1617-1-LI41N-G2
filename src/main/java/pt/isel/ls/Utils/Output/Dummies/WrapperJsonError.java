package pt.isel.ls.Utils.Output.Dummies;

public class WrapperJsonError {
    private String type;

    public WrapperJsonError(String query){
        String[] res = query.split("/");
        this.type = res[1].split(" ")[0];
    }
}
