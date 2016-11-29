package pt.isel.ls.Server;

import pt.isel.ls.Dtos.Checklist;

import java.util.LinkedList;

/**
 * This class is used just to make the nav bar in the checklists page(html) be cool
 */
public class WrapperChecklistView {
    private LinkedList<Checklist> Checklists;
    private LinkedList<Info> State = new LinkedList<Info>();
    private final String links[] = {"/checklists", "/checklists/closed", "/checklists/open/sorted/duedate", "/checklists/open/sorted/noftasks"};
    private final String names[] = {"All", "Closed", "By Date", "By #Tasks"};


    WrapperChecklistView(LinkedList list, int position_Active){
        Checklists = list;

        for (int i = 0; i < 4; i++) {
            State.add(new Info(i == position_Active, links[i], names[i]));
        }
    }

    class Info{
        private String Link;
        private String Name;
        private String Activated;

        Info(boolean active, String Link, String Name){
            this.Link = Link;
            this.Name = Name;
            this.Activated = active ? "active" : "";

        }

    }


}
