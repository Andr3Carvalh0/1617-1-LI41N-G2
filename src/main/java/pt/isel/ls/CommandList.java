package pt.isel.ls;

import pt.isel.ls.Commands.*;

import java.util.LinkedList;

public class CommandList {
    private LinkedList<Command> commandList;
    public CommandList() {
        commandList = new LinkedList<>();
                                                                //              Class       Test
        commandList.add(new PostChecklist());                   //Gonçalo       Done        Done
        commandList.add(new PostChecklistCidTasks());           //Marcos*       Done        Done
        commandList.add(new PostChecklistsCidTasksLid());       //Gonçalo       Done        Done
        commandList.add(new PostTemplates());                   //Andre         Done        Done
        commandList.add(new PostTemplatesTidTasks());           //Marcos        Done        Done
        commandList.add(new PostTemplatesTidCreate());          //Gonçalo       Done        Done

        commandList.add(new GetChecklists());                   //Andre         Done        Done
        commandList.add(new GetChecklistsCid());                //Marcos        Done
        commandList.add(new GetTemplates());                    //Gonçalo       Done        Done
        commandList.add(new GetTemplatesTid());                 //Andre         Done        Done
        commandList.add(new GetChecklistsClosed());             //Marcos        Done        Done
        commandList.add(new GetChecklistsOpenSortedDueDate());  //Gonçalo*      Done        Done
        commandList.add(new GetChecklistsOpenSortedNoftasks()); //Andre         Done        Done

        commandList.add(new PostTags());                        //              Done        Done
        commandList.add(new GetTags());                         //              Done
        commandList.add(new DeleteTagsGid());                   //              Done        Done
        commandList.add(new PostChecklistsCidTags());           //              Done
        commandList.add(new DeleteChecklistsCidTagsGid());      //              Done
        commandList.add(new Options());                         //              Done

    }

    public LinkedList<Command> getCommandList(){
        return commandList;
    }
}
