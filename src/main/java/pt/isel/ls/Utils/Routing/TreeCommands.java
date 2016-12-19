package pt.isel.ls.Utils.Routing;

import pt.isel.ls.Commands.*;
import java.util.LinkedList;

/*
*                       "/"
*                        |
*        ________________|________________
*        |      |        |       |       |
*       GET    POST    DELETE  OPTION  LISTEN
*        |      |        |
*       GET    POST    DELETE
*       CMDs   CMDs     CMDs
*
*/

public class TreeCommands {

    private CommandNode root;

    public TreeCommands(){

        LinkedList<CommandNode> GETS = new LinkedList<>();
        GETS.add(new CommandNode(null, null, new GetChecklists()));
        GETS.add(new CommandNode(null, null, new GetChecklistsCid()));
        GETS.add(new CommandNode(null, null, new GetTemplates()));
        GETS.add(new CommandNode(null, null, new GetTemplatesTid()));
        GETS.add(new CommandNode(null, null, new GetChecklistsClosed()));
        GETS.add(new CommandNode(null, null, new GetChecklistsOpenSortedDueDate()));
        GETS.add(new CommandNode(null, null, new GetChecklistsOpenSortedNoftasks()));
        GETS.add(new CommandNode(null, null, new GetTags()));
        GETS.add(new CommandNode(null, null, new GetTemplatesTidChecklistsSortedByOpentasksDesc()));
        GETS.add(new CommandNode(null, null, new GetTagsGid()));
        GETS.add(new CommandNode(null, null, new GetTagsGidChecklists()));
        CommandNode gets = new CommandNode("GET", GETS, null);

        LinkedList<CommandNode> POSTS = new LinkedList<>();
        POSTS.add(new CommandNode(null, null, new PostChecklist()));
        POSTS.add(new CommandNode(null, null, new PostChecklistCidTasks()));
        POSTS.add(new CommandNode(null, null, new PostChecklistsCidTags()));
        POSTS.add(new CommandNode(null, null, new PostChecklistsCidTasksLid()));
        POSTS.add(new CommandNode(null, null, new PostTags()));
        POSTS.add(new CommandNode(null, null, new PostTemplates()));
        POSTS.add(new CommandNode(null, null, new PostTemplatesTidCreate()));
        POSTS.add(new CommandNode(null, null, new PostTemplatesTidTasks()));
        POSTS.add(new CommandNode(null, null, new PostChecklistCidTagsCreate()));
        CommandNode posts = new CommandNode("POST", POSTS, null);

        LinkedList<CommandNode> DELETES = new LinkedList<>();
        DELETES.add(new CommandNode(null, null, new DeleteChecklistsCidTagsGid()));
        DELETES.add(new CommandNode(null, null, new DeleteTagsGid()));
        CommandNode deletes = new CommandNode("DELETE", DELETES, null);

        LinkedList<CommandNode> ROOT = new LinkedList<>();
        ROOT.add(gets);
        ROOT.add(posts);
        ROOT.add(deletes);
        CommandNode options = new CommandNode("OPTIONS", null, new Options());
        ROOT.add(options);
        CommandNode listen = new CommandNode("LISTEN", null, new Listen());
        ROOT.add(listen);

        root = new CommandNode("/", ROOT, null);

    }

    public CommandNode getTree(){
        return root;
    }
}
