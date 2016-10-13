package pt.isel.ls;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.PostChecklist;

import java.util.LinkedList;

public class CommandList {
    private LinkedList<Command> commandList;
    public CommandList() {
        commandList = new LinkedList<>();
        commandList.add(new PostChecklist());
    }

    public LinkedList<Command> getCommandList(){
        return commandList;
    }
}
