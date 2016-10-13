package pt.isel.ls;

import pt.isel.ls.Commands.*;

import java.util.LinkedList;

public class CommandList {
    private LinkedList<Command> commandList;
    public CommandList() {
        commandList = new LinkedList<>();
        commandList.add(new PostChecklist());
        commandList.add(new PostChecklistCidTasks());
        commandList.add(new PostChecklistsCidTasksLid());
        commandList.add(new PostTemplates());
        commandList.add(new PostTemplatesTidTasks());
        commandList.add(new PostTemplatesTidCreate());
        




    }

    public LinkedList<Command> getCommandList(){
        return commandList;
    }
}
