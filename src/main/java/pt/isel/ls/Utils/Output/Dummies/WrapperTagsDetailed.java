package pt.isel.ls.Utils.Output.Dummies;


import pt.isel.ls.Dtos.Checklist;
import pt.isel.ls.Dtos.DtoWrapper;
import pt.isel.ls.Dtos.Tag;

import java.util.LinkedList;


public class WrapperTagsDetailed {
    private String link;
    private String message;
    private int id;
    private LinkedList<Tag> Tag;
    private LinkedList<Checklist> Checklist;


    public WrapperTagsDetailed(String link, DtoWrapper content){
        this.link = link;
        this.message = link.equals("/tags") ? "Detailed" : "";
        this.id = (((LinkedList<pt.isel.ls.Dtos.Tag>) content.getTag()).get(0)).getTg_id();
        this.Tag = (LinkedList<pt.isel.ls.Dtos.Tag>) content.getTag();
        this.Checklist = (LinkedList<pt.isel.ls.Dtos.Checklist>) content.getChecklist();
    }

}
