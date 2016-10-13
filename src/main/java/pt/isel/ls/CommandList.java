package pt.isel.ls;

import pt.isel.ls.Commands.*;

import java.util.LinkedList;

public class CommandList {
    private LinkedList<Command> commandList;
    public CommandList() {
        commandList = new LinkedList<>();
        commandList.add(new PostChecklist());
        commandList.add(new PostChecklistCidTasks());           //Marcos
        commandList.add(new PostChecklistsCidTasksLid());       //Gonçalo
        commandList.add(new PostTemplates());                   //Andre
        commandList.add(new PostTemplatesTidTasks());           //Marcos
        commandList.add(new PostTemplatesTidCreate());          //Gonçalo

        commandList.add(new GetChecklists());                   //Andre
        commandList.add(new GetChecklistsCid());                //Marcos
        commandList.add(new GetTemplates());                    //Gonçalo
        commandList.add(new GetTemplatesTid());                 //Andre
        commandList.add(new GetChecklistsClosed());             //Marcos
        commandList.add(new GetChecklistsOpenSortedDueDate());  //Gonçalo
        commandList.add(new GetChecklistsOpenSortedNoftasks()); //Andre


    }

    public LinkedList<Command> getCommandList(){
        return commandList;
    }
}
