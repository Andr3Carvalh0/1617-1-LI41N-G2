package pt.isel.ls;

import pt.isel.ls.Commands.*;

import java.util.LinkedList;

/*
*                  "/"
*                   |
*        ___________|____________
*        |      |        |      |
*       GET    POST   DELETE  OPTION
*        |      |        |
*       GET    POST   DELETE
*       CMDs   CMDs   CMDs
*
 */

public class Tree_Commands {

    private Command_Node root;

    public Tree_Commands(){

        LinkedList<Command_Node> GETS = new LinkedList<>();
        GETS.add(new Command_Node(null, null, new GetChecklists()));
        GETS.add(new Command_Node(null, null, new GetChecklistsCid()));
        GETS.add(new Command_Node(null, null, new GetTemplates()));
        GETS.add(new Command_Node(null, null, new GetTemplatesTid()));
        GETS.add(new Command_Node(null, null, new GetChecklistsClosed()));
        GETS.add(new Command_Node(null, null, new GetChecklistsOpenSortedDueDate()));
        GETS.add(new Command_Node(null, null, new GetChecklistsOpenSortedNoftasks()));
        GETS.add(new Command_Node(null, null, new GetTags()));
        GETS.add(new Command_Node(null, null, new PostChecklistsCidTags()));
        Command_Node gets = new Command_Node("GET", GETS, null);

        LinkedList<Command_Node> POSTS = new LinkedList<>();
        POSTS.add(new Command_Node(null, null, new PostChecklist()));
        POSTS.add(new Command_Node(null, null, new PostChecklistCidTasks()));
        POSTS.add(new Command_Node(null, null, new PostChecklistsCidTags()));
        POSTS.add(new Command_Node(null, null, new PostChecklistsCidTasksLid()));
        POSTS.add(new Command_Node(null, null, new PostTags()));
        POSTS.add(new Command_Node(null, null, new PostTemplates()));
        POSTS.add(new Command_Node(null, null, new PostTemplatesTidCreate()));
        POSTS.add(new Command_Node(null, null, new PostTemplatesTidTasks()));
        Command_Node posts = new Command_Node("POST", POSTS, null);

        LinkedList<Command_Node> DELETES = new LinkedList<>();
        DELETES.add(new Command_Node(null, null, new DeleteChecklistsCidTagsGid()));
        DELETES.add(new Command_Node(null, null, new DeleteTagsGid()));
        Command_Node deletes = new Command_Node("DELETE", DELETES, null);

        LinkedList<Command_Node> ROOT = new LinkedList<>();
        ROOT.add(gets);
        ROOT.add(posts);
        ROOT.add(deletes);
        Command_Node options = new Command_Node("OPTIONS", null, new Options());
        ROOT.add(options);
        root = new Command_Node("/", ROOT, null);

    }

    public Command_Node getTree(){
        return root;
    }
}
