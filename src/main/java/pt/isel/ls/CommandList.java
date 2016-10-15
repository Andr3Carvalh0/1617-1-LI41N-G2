package pt.isel.ls;

import pt.isel.ls.Commands.*;

import java.util.LinkedList;

public class CommandList {
    private LinkedList<Command> commandList;
    public CommandList() {
        commandList = new LinkedList<>();
                                                                //              Class       Test
        commandList.add(new PostChecklist());                   //Gonçalo       Done        Done
        commandList.add(new PostChecklistCidTasks());           //Marcos
        commandList.add(new PostChecklistsCidTasksLid());       //Gonçalo
        commandList.add(new PostTemplates());                   //Andre         Done        Done
        commandList.add(new PostTemplatesTidTasks());           //Marcos
        commandList.add(new PostTemplatesTidCreate());          //Gonçalo

        commandList.add(new GetChecklists());                   //Andre         Done        Done
        commandList.add(new GetChecklistsCid());                //Marcos
        commandList.add(new GetTemplates());                    //Gonçalo
        commandList.add(new GetTemplatesTid());                 //Andre         Done
        commandList.add(new GetChecklistsClosed());             //Marcos
        commandList.add(new GetChecklistsOpenSortedDueDate());  //Gonçalo
        commandList.add(new GetChecklistsOpenSortedNoftasks()); //Andre
    }

    public LinkedList<Command> getCommandList(){
        return commandList;
    }
}
