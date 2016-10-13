package pt.isel.ls;

public class CommandNode {
    String id;
    CommandNode[] next;

    public CommandNode(String id){
        this.id = id;
        this.next = new CommandNode[2];
    }

    public void Execute(){

    }
}
