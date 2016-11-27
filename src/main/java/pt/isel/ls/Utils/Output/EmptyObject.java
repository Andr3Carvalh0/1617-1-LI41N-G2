package pt.isel.ls.Utils.Output;

//This class is just to store the user command.
//This will be used when the user execute some query and the result is empty
class EmptyObject {
    private String query;

    EmptyObject(String query){
        this.query = query;
    }
}
